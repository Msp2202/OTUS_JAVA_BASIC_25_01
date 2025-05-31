package ru.otus.chan.server;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.isNull;

public class InMemoryAuthenticatedProvider implements AuthenticatedProvider {

    private List<User> users;
    private Server server;
    public InMemoryAuthenticatedProvider(Server server) {
        this.server = server;
        this.users = new CopyOnWriteArrayList<>();
        this.users.add(new User("qwe", "qwe", "qwe1", "USER"));
        this.users.add(new User("asd", "asd", "asd1", "USER"));
        this.users.add(new User("zxc", "zxc", "zxc1", "USER"));
        this.users.add(new User("admin", "admin", "admin", "ADMIN"));
    }

    @Override
    public void initialize() {
        System.out.println("Сервис аунтентификации запущен: InMemory режим");
    }

    private String getUsernameByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (user.login.equals(login.toLowerCase()) && user.password.equals(password)) {
                return user.username;
            }
        }
        return null;
    }

    /**
     * Добавить метод для получения роли
     */
    public String getRoleByUsername(String username) {
        for (User user : users) {
            if (user.username.equalsIgnoreCase(username)) {
                return user.role;
            }
        }
        return "USER";
    }

    private boolean isLoginAlreadyExists(String login) {
        for (User user : users) {
            if (user.login.equals(login.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean isUsernameAlreadyExists(String username) {
        for (User user : users) {
            if (user.username.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {
        User user = findUserByLoginAndPassword(login, password);
        if (isNull(user)) {
            clientHandler.send("Некорректный логин/пароль");
            return false;
        }
        String authUsername = user.username;
        if (server.isUsernameBusy(authUsername)) {
            clientHandler.send("Указанная учетная запись уже занята");
            return false;
        }
        clientHandler.setUsername(authUsername);
        clientHandler.setRole(user.getRole());
        server.subscribe(clientHandler);
        clientHandler.send("/authok " + authUsername);
        return true;
    }

    @Override
    public boolean registration(ClientHandler clientHandler, String login, String password, String username) {
        if (login.length() < 3) {
            clientHandler.send("Логин должен быть 3+ символа");
            return false;
        }
        if (username.length() < 3) {
            clientHandler.send("Имя пользователя должна быть 3+ символа");
            return false;
        }
        if (password.length() < 3) {
            clientHandler.send("Пароль должен быть 3+ символа");
            return false;
        }
        if (isLoginAlreadyExists(login)) {
            clientHandler.send("Такой логин уже занят");
            return false;
        }
        if (isUsernameAlreadyExists(username)) {
            clientHandler.send("Такое имя пользователя уже занято");
            return false;
        }

        clientHandler.setUsername(username);
        users.add(new User(login, password, username, "USER")); // добавили роль по умолчанию
        server.subscribe(clientHandler);
        clientHandler.send("/regok " + username);
        return true;
    }

    private User findUserByLoginAndPassword(String login, String password) {
        for (User user : users) {
            if (user.login.equals(login.toLowerCase()) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }

    private class User {
        private String login;
        private String password;
        private String username;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        private String role;

        public User(String login, String password, String username, String role) {
            this.login = login;
            this.password = password;
            this.username = username;
            this.role = role;
        }
    }
}
