package ru.otus.chan.server;

import java.sql.*;

/**
 * Класс для аутентификации и регистрации пользователей с использованием Jdbc
 */
public class JdbcAuthenticatedProvider implements AuthenticatedProvider {
    private final Server server;
    private Connection connection;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/chat_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public JdbcAuthenticatedProvider(Server server) {
        this.server = server;
    }


    @Override
    public void initialize() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.print("Сервис аутентификации запущен: JDBC режим");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД", e);
        }

    }

    @Override
    public boolean authenticate(ClientHandler clientHandler, String login, String password) {

        try (PreparedStatement ps = connection.prepareStatement(SqlQueries.AUTH_QUERY)) {
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
        if (login.length() < 3) {
            return error(clientHandler, "Логин должен быть 3+ символа");
        }
        if (userName.length() < 3) {
            return error(clientHandler, "Имя пользователя должно быть 3+ символа");
        }
        if (password.length() < 3) {
            return error(clientHandler, "Пароль должно быть 3+ символа");
        }

        if (isValueExists("login", login)) {
            return error(clientHandler, "Логин уже занят");
        }

        try (PreparedStatement ps = connection.prepareStatement(SqlQueries.REGISTER_QUERY)) {
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
        if(!SqlQueries.ALLOWED_COLUMNS.contains(column)){
            throw new IllegalArgumentException("Не правильно указана колонка: " + column);
        }
        // Форматирование безопасного запроса
        String isLoginRequest = String.format(SqlQueries.VALUE_EXISTS_TEMPLATE, column);

        try (PreparedStatement ps = connection.prepareStatement(isLoginRequest)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Проверка провалена: " + column, e);
        }
    }

    private boolean error(ClientHandler clientHandler, String message) {
        clientHandler.send(message);
        return false;
    }
}
