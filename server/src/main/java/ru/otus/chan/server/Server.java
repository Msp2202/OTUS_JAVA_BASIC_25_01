package ru.otus.chan.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port = 8085;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            Socket socket = serverSocket.accept();
            System.out.println("Подключился клиент с портом: " + socket.getPort());

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            while (true) {
                String message = inputStream.readUTF();
                System.out.println(message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
