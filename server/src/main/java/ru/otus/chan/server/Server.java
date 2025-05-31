package ru.otus.chan.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private int port = 8085;
    private List<ClientHandler> clients;
    private AuthenticatedProvider authenticatedProvider;

    public Server(int port) {
        this.port = port;
        clients = new CopyOnWriteArrayList<>();
        authenticatedProvider = new InMemoryAuthenticatedProvider(this);
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
            e.printStackTrace();
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
        broadcastMessage("Клиент " + clientHandler.getUsername() + " вышел из чата");
        System.out.println("Клиент " + clientHandler.getUsername() + " вышел из чата");
        clients.remove(clientHandler);
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
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
        System.out.println("В чате активные пользователи с именами: " + userList.toString());
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

    public AuthenticatedProvider getAuthenticatedProvider() {
        return authenticatedProvider;
    }
}
