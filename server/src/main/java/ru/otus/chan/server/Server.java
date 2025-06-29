package ru.otus.chan.server;

import ru.otus.config.ServerConfig;
import ru.otus.sqlskripts.SqlQueries;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Server {

    public static final Logger log = Logger.getLogger(Server.class.getName());

    private int port = 8085;
    private List<ClientHandler> clients; // TODO заменить на бд добавив статус, пок храним в памяти. из SqlQueries.UPDATE_STATUS
    private AuthenticatedProvider authenticatedProvider;
    private InactivityMonitor inactivityMonitor;

    public Server(int port) {
        this.port = port;
        clients = new CopyOnWriteArrayList<>();
        authenticatedProvider = new JdbcAuthenticatedProvider(this);
        authenticatedProvider.initialize();
        this.inactivityMonitor = new InactivityMonitor(this);
    }

    /**
     * Ожидание и подключение клиента
     */
    public void start() {
        log.info("Запуск сервера на порту " + port);
        log.info("Конфигурация таймаутов: " +
                "INACTIVITY_TIMEOUT=" + ServerConfig.INACTIVITY_TIMEOUT + "ms, " +
                "CHECK_INTERVAL=" + ServerConfig.CHECK_INTERVAL + "ms");
        inactivityMonitor.start(); // Запускаем мониторинг неактивности
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Сервер запущен на порту: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                subscribe(new ClientHandler(socket, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inactivityMonitor.stop(); // Останавливаем монитор
        }
    }

    /**
     * Метод по добавлению новых клиентов
     * @param clientHandler
     */
    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        getActiveUsers();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        broadcastMessage("Клиент " + clientHandler.getUsername() + " вышел из чата", false, false);
        log.info("Клиент " + clientHandler.getUsername() + " вышел из чата");
        clients.remove(clientHandler);
    }

    /**
     * Рассылка сообщения всем клиентам с добавлением времени
     *
     * @param message           - исходное сообщение
     * @param addTimestamp      - добавлять ли временную метку
     * @param onlyAuthenticated - рассылать только авторизованным
     */
    public void broadcastMessage(String message, boolean addTimestamp, boolean onlyAuthenticated) {
        // TODO поиск повторного сообщения
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerMethod = stackTrace[2].getMethodName(); // [0]-getStackTrace, [1]-этот метод, [2]-вызывающий
        log.info("Broadcasting from " + callerMethod + ": " + message);

        String formattedMessage = addTimestamp
                ? formatMessageWithTime(message)
                : message;

        for (ClientHandler client : clients) {
            if (!onlyAuthenticated || client.isAuthenticated()) {
                client.send(formattedMessage);
            }
        }
    }

    /**
     * Список активных пользователей
     * @return
     */
    public String getActiveUsers(){
        if(clients.isEmpty()){
            return "Нет активных пользователей";
        }
        StringBuilder userList = new StringBuilder();
        for (ClientHandler client : clients) {
            userList.append(client.getUsername());
            userList.append(", ");
        }
        log.info("В чате активные пользователи с именами: " + userList.toString());
        return userList.toString();
    }

    /**
     * Метод поиска клиента по имени
     */
    public ClientHandler findClientByUsername(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equalsIgnoreCase(username)){
                return client;
            }
        }
        return null;
    }

    public boolean isUsernameBusy(String username) {
        for (ClientHandler c : clients) {
            if (c.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Форматирует сообщение с добавлением времени
     */
    private String formatMessageWithTime(String message) {
        return String.format("[%s] %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                message);
    }


    public AuthenticatedProvider getAuthenticatedProvider() {
        return authenticatedProvider;
    }

    /**
     * геттер для clients
     *
     * @return
     */
    public List<ClientHandler> getClients() {
        return clients;
    }

    /**
     * Бан пользователя (на 24 часа или перманентно)
     *
     * @param adminUsername  - кто банит
     * @param targetUsername - кого банят
     * @param reason         - причина
     * @return true, если успешно
     */
    public boolean banUser(String adminUsername, String targetUsername, String reason, long durationMinutes, boolean isPermanent) {
        log.info("Попытка бана: " + targetUsername + ", время: " + durationMinutes + ", причина: " + reason);
        if (Objects.isNull(targetUsername)) {
            log.info("Ошибка: Пользователь " + targetUsername + " не найден!");
            return false;
        }
        try {
            Connection connection = ((JdbcAuthenticatedProvider) getAuthenticatedProvider()).getConnection();

            int adminId = getUserIdByUsername(adminUsername, connection);
            int targetId = getUserIdByUsername(targetUsername, connection);

            if (adminId == -1 || targetId == -1) return false;

            java.sql.Timestamp bannedUntil = isPermanent
                    ? null
                    : new java.sql.Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));

            try (PreparedStatement ps = connection.prepareStatement(SqlQueries.ADD_BAN_QUERY)) {
                ps.setInt(1, targetId);
                if (bannedUntil == null) {
                    ps.setNull(2, Types.TIMESTAMP);
                } else {
                    ps.setTimestamp(2, bannedUntil);
                }
                ps.setString(3, reason);
                ps.setInt(4, adminId);

                int affectedRows = ps.executeUpdate();

                if (affectedRows > 0) {
                    ClientHandler targetClient = findClientByUsername(targetUsername);
                    if (targetClient != null) {
                        String banMessage = "Вы были забанены" +
                                (isPermanent ? " навсегда" : " на 24 часа") +
                                ". Причина: " + reason;
                        targetClient.send(banMessage);
                        unsubscribe(targetClient);
                    }
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            log.severe("Ошибка при бане пользователя: " + e.getMessage());
            return false;
        }
    }

    /**
     * Разбан пользователя
     *
     * @param username - кого разбаниваем
     * @return true, если успешно
     */
    public boolean unbanUser(String username) {
        try {
            Connection connection = getAuthenticatedProvider().getConnection();
            int userId = getUserIdByUsername(username, connection);

            if (userId == -1) {
                return false;
            }

            // Удаляем запись о бане
            try (PreparedStatement ps = connection.prepareStatement(
                    SqlQueries.REMOVE_BAN_QUERY)) {
                ps.setInt(1, userId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            log.severe("Ошибка при разбане пользователя: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получает ID пользователя по имени
     *
     * @param username   - имя пользователя
     * @param connection - соединение с БД
     * @return ID или -1, если не найден
     */
    private int getUserIdByUsername(String username, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SqlQueries.SELECT_USER_ID_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        }
    }

    /**
     * Корректно завершает работу сервера, выполняя следующие действия:
     * 1. Отправляет уведомление всем подключенным клиентам о завершении работы
     * 2. Закрывает все соединения с клиентами
     * 3. Очищает список активных клиентов
     * 4. Завершает работу серверного процесса
     *
     * @implNote Метод использует жесткое завершение через System.exit(0).
     * Для более мягкого завершения можно использовать serverSocket.close()
     * @see #broadcastMessage(String, boolean, boolean)
     * @see ClientHandler#disconnect()
     */
    public synchronized void shutdown() {

        log.info("Администратор инициировал выключение сервера");
        broadcastMessage("Сервер завершает работу. Соединение будет разорвано.", true, true);

        for (ClientHandler client : clients) {
            try {
                client.disconnect(); // Закрываем сокеты и потоки
            } catch (Exception e) {
                log.warning("Ошибка при отключении клиента " + client.getUsername() + ": " + e.getMessage());
            }
        }

        clients.clear();

        log.info("Сервер остановлен администратором");
        System.exit(0);
    }
}