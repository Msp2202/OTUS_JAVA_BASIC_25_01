package ru.otus.chan.server;


public interface AuthenticatedProvider {
    void initialize();

    boolean authenticate(ClientHandler clientHandler, String login, String password);

    boolean registration(ClientHandler clientHandler, String login, String password, String userName);
}
