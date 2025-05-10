package ru.otus.chan.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private int port = 8085;
    private List<ClientHandler> clients;

    public Server(int port) {
        this.port = port;
        clients = new CopyOnWriteArrayList<>();
    }

    /**
     * Ожидание и подключение клиента
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                subscribe(new ClientHandler(socket, this));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод по добавлению новых клиентов
     * @param clientHandler
     */
    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void broadcastMessage(String message) throws IOException {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

}
