package ru.otus.chan.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/**
 * Класс по обработке клиента - получение и отправка сообщений, (аналог ExampleClient из урока 22)
 * определение протокола взаимодействия
 * Вопросы с подключением на уровне сервера.
 */
public class ClientHandler {

    private Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                System.out.println("Подключился клиент с портом: " + socket.getPort());

                while (true) {
                    String message = inputStream.readUTF();
                    System.out.println(message);
                    server.broadcastMessage(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void send(String message) throws IOException {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
