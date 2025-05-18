package ru.otus.chan.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.util.Objects.nonNull;

/**
 * Класс по обработке клиента - получение и отправка сообщений, (аналог ExampleClient из урока 22)
 * определение протокола взаимодействия
 * Вопросы с подключением на уровне сервера.
 */
public class ClientHandler {

    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private String username;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        username = String.valueOf(socket.getPort());
        send("Вы подключились под ником: " + username);


        new Thread(() -> {
            try {
                System.out.println("Подключился клиент с портом: " + socket.getPort());

                while (true) {
                    String message = inputStream.readUTF();
                    if (message.startsWith("/")) { // служебное сообщение -> далее логика обработки
                        if (message.equalsIgnoreCase("/exit")) {
                            send("exitOk");
                            break;
                        }
                        if (message.startsWith("/w")) { // служебное сообщение -> лисное сообщение
                            privateSend(message);
                        }

                    } else {
                        server.broadcastMessage(username + ": " + message);
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

        System.out.println(username + " Отправил личное сообщение " + nickname);

        ClientHandler recipient = server.findClientByUsername(nickname);
        if (nonNull(recipient)) {
            recipient.send(this.username + " Личное сообщение > " + privateMsg);
        } else {
            send("Пользователь " + nickname + " не найден.\n В чате пользователи с именами: " + server.getActiveUsers());
        }
    }

    public void send(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        server.unsubscribe(this);
        server.getActiveUsers();
        try {
            if (nonNull(inputStream)) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (nonNull(outputStream)) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (nonNull(socket)) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = String.valueOf(username);
    }
}
