package ru.otus.chan.server;

import ru.otus.protocol.ProtocolConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

/**
 * Класс по обработке клиента - получение и отправка сообщений, (аналог ExampleClient из урока 22)
 * определение протокола взаимодействия
 * Вопросы с подключением на уровне сервера.
 */
public class ClientHandler {

    private static final Logger log = Logger.getLogger(ClientHandler.class.getName());
    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;


    private String username;
    private boolean authenticated;
    private String role;

    private long lastActivityTime = System.currentTimeMillis();
    private volatile boolean isConnected = true;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        username = String.valueOf(socket.getPort());
        send("Вы подключились под ником: " + username);


        new Thread(() -> {
            try {
                log.info("Подключился клиент с портом: " + socket.getPort());

                //Цикл аутентификации
                while (true) {
                    send("Перед работой с чатом необходимо выполнить " +
                            "аутентификацию '/auth login password' \n" +
                            "или регистрацию '/reg login password username'");
                    String message = inputStream.readUTF();
                    if (message.startsWith(ProtocolConstants.COMMAND_PREFIX)) {
                        if (message.equals(ProtocolConstants.EXIT)) {
                            send(ProtocolConstants.EXIT_OK);
                            break;
                        }
                        ///auth login password
                        if (message.startsWith(ProtocolConstants.AUTH)) {
                            String token[] = message.split(" ");
                            if (token.length != 3) {
                                send("Неверный формат команды /auth");
                                continue;
                            }
                            if (server.getAuthenticatedProvider()
                                    .authenticate(this, token[1], token[2])) {
                                authenticated = true;
                                break;
                            }
                        }
                        ///reg login password username
                        if (message.startsWith(ProtocolConstants.REG)) {
                            String token[] = message.split(" ");
                            if (token.length != 4) {
                                send("Неверный формат команды /reg");
                                continue;
                            }
                            if (server.getAuthenticatedProvider()
                                    .registration(this, token[1], token[2], token[3])) {
                                authenticated = true;
                                break;
                            }
                        }
                    }
                }


                // Цикл для работы с зарегистрированным пользователем,
                // прошедшим аутентификации(подтверждение пользователя) и авторизацию(с определенными провами).
                while (true) {
                    String message = inputStream.readUTF();
                    if (message.startsWith(ProtocolConstants.COMMAND_PREFIX)) { // служебное сообщение -> далее логика обработки
                        if (message.equalsIgnoreCase(ProtocolConstants.EXIT)) {
                            send(ProtocolConstants.EXIT_OK);
                            break;
                        }
                        if (message.startsWith(ProtocolConstants.PRIVATE_MSG)) { // служебное сообщение -> лисное сообщение
                            privateSend(message);
                        }
                        if (message.startsWith(ProtocolConstants.KICK)) {
                            if (!"ADMIN".equals(this.role)) {
                                send("Ошибка: Недостаточно прав!");
                                continue;
                            }
                            String[] parts = message.split(" ", 2);
                            if (parts.length != 2) {
                                send("Неверный формат: /kick username");
                                continue;
                            }
                            String targetUsername = parts[1];
                            ClientHandler target = server.findClientByUsername(targetUsername);
                            if (nonNull(target)) {
                                target.send(ProtocolConstants.KICK_OK); // Сообщение об отключении
                                target.disconnect();
                                server.broadcastMessage(targetUsername
                                        + " был кикнут администратором", true, true);
                            } else {
                                send("Пользователь не найден: " + targetUsername);
                            }
                        }
                        // ===== ДОБАВЛЯЕМ ОБРАБОТКУ БАНОВ ===== //
                        if (message.startsWith(ProtocolConstants.BAN)) {
                            if (!"ADMIN".equals(this.role)) {
                                send("Ошибка: Недостаточно прав!");
                                continue;
                            }

                            String[] parts = message.split(" ", 4);
                            if (parts.length < 4) {
                                send("Неверный формат: /ban username время_в_минутах причина");
                                continue;
                            }
                            try {
                                String targetUsername = parts[1];
                                long durationMinutes = Long.parseLong(parts[2]);
                                String reason = parts[3];

                                if (server.banUser(this.username, targetUsername, reason, durationMinutes, false)) {
                                    send("Пользователь " + targetUsername + " забанен на " + durationMinutes + " минут. Причина: " + reason);
                                    server.broadcastMessage(targetUsername + " забанен администратором", true, true);
                                } else {
                                    send("Ошибка: Не удалось забанить пользователя " + targetUsername);
                                }
                            } catch (NumberFormatException e) {
                                send("Ошибка: Некорректное время бана (должно быть числом)");
                            }
                        }
                        if (message.startsWith(ProtocolConstants.UNBAN)) {
                            if (!"ADMIN".equals(this.role)) {
                                send("Ошибка: Недостаточно прав!");
                                continue;
                            }
                            // Формат: /unban username
                            String[] parts = message.split(" ", 2);
                            if (parts.length < 2) {
                                send("Неверный формат: /unban username");
                                continue;
                            }
                            String targetUsername = parts[1];
                            if (server.unbanUser(targetUsername)) {
                                send("Пользователь " + targetUsername + " разбанен");
                            } else {
                                send("Ошибка: Не удалось разбанить пользователя " + targetUsername);
                            }
                        }
                        if (message.equalsIgnoreCase(ProtocolConstants.SHUTDOWN)) {
                            if (!"ADMIN".equals(this.role)) {
                                send("Ошибка: Недостаточно прав для выключения сервера");
                                continue;
                            }
                            server.shutdown();
                        }
                    } else {
                        server.broadcastMessage(username + ": "
                                + message, true, true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    public void privateSend(String message) {
        String[] parts = message.split(" ", 3);
        if (parts.length != 3) {
            send("Неверный формат команды. Используйте: /w ник сообщение");
            return;
        }
        String nickname = parts[1];
        String privateMsg = parts[2];

        log.info(username + " Отправил личное сообщение " + nickname);

        ClientHandler recipient = server.findClientByUsername(nickname);
        if (nonNull(recipient)) {
            recipient.send(this.username + " Личное сообщение > " + privateMsg);
        } else {
            send("Пользователь " + nickname + " не найден.\n В чате пользователи с именами: " + server.getActiveUsers());
        }
    }

    public void send(String message) {
        try {
            updateActivity();
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            log.warning("Ошибка отправки сообщения для " + username + ": " + e.getMessage());
            disconnect();
        }
    }

    public void disconnect() {
        if (!isConnected) return; // Защита от повторного вызова
        isConnected = false;

        log.info("Отключение клиента: " + username);
        server.unsubscribe(this);

        try {
            if (nonNull(outputStream)) outputStream.close();
            if (nonNull(inputStream)) inputStream.close();
            if (nonNull(socket)) socket.close();
        } catch (IOException e) {
            log.warning("Ошибка при отключении клиента " + username + ": " + e.getMessage());
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Обновляет время последней активности клиента
     *
     * @return
     */
    public void updateActivity() {
        lastActivityTime = System.currentTimeMillis();
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
