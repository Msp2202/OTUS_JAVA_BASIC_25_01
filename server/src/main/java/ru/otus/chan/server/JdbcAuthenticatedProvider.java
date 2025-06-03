package ru.otus.chan.server;

import java.sql.*;

/**
 * Класс для аутентификации и регистрации пользователей с использованием Jdbc
 */
public class JdbcAuthenticatedProvider implements AuthenticatedProvider {
    private final Server server;
    private Connection connection;

    public JdbcAuthenticatedProvider(Server server) {
        this.server = server;
    }


    @Override
    public void initialize() {
        try {
            String url = "jdbc:postgresql://localhost:5432/5432/chat_bd";
            String user = "postgres";
            String password = "postgres";

            connection = DriverManager.getConnection(url, user, password);
            System.out.printf("Сервис аутентификации запущен: JDBC режим");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД");
        }

    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {

        String request = "SELECT user_name , role  FROM users_chat uc WHERE login=? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(request)) {
            ps.setString(1, login);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String userName = rs.getString("user_name");
                    String role = rs.getString("role");

                    if (server.isUsernameBusy(userName)) {
                        clientHandler.send("Указанная учетная запись уже занята");
                        return false;
                    }

                    clientHandler.setUsername(userName);
                    clientHandler.setRole(role);
                    server.subscribe(clientHandler);
                    clientHandler.send("/authok " + userName);
                    return true;
                } else {
                    clientHandler.send("Некорректный логин/пароль");
                    return false;
                }
            }

        } catch (SQLException e) {
            clientHandler.send("Ошибка аутентификации");
            return false;
        }
    }

    @Override
    public boolean registration(ClientHandler clientHandler, String login, String password, String userName) {
        if (login.length() < 3) return error(clientHandler, "Логин должен быть 3+ символа");
        if (userName.length() < 3) return error(clientHandler, "Имя пользователя должно быть 3+ символа");
        if (password.length() < 3) return error(clientHandler, "Пароль должно быть 3+ символа");

        if (isValueExists("login", login)) return error(clientHandler, "Логин уже занят");

        String regRequest = "INSERT INTO users_chat (login, password, user_name, role) VALUES(?, ?, ?, 'USER')";
        try (PreparedStatement ps = connection.prepareStatement(regRequest)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, userName);
            ps.executeUpdate();

            clientHandler.setUsername(userName);
            clientHandler.setRole("USER");
            server.subscribe(clientHandler);
            clientHandler.send("/regok " + userName);
            return true;
        } catch (SQLException e) {
            return error(clientHandler, "Ошибка регистрации: " + e.getMessage());
        }
    }

    private boolean isValueExists(String column, String value) {
        String isLoginRequest = "SELECT 1 FROM users WHERE " + column + " = ?";

        try (PreparedStatement ps = connection.prepareStatement(isLoginRequest)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return true;
        }
    }

    private boolean error(ClientHandler clientHandler, String message) {
        clientHandler.send(message);
        return false;
    }
}
