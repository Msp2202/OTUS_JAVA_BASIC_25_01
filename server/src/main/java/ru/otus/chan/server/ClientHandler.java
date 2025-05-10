package ru.otus.chan.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Класс по обработке клиента - получение и отправка сообщений,
 * определение протокола взаимодействия
 * Вопросы с подключением на уровне сервера.
 */
public class ClientHandler {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }
}
