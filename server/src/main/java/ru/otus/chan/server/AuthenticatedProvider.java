package ru.otus.chan.server;


import java.sql.Connection;

public interface AuthenticatedProvider {
    void initialize();

    boolean authenticate(ClientHandler clientHandler, String login, String password);

    boolean registration(ClientHandler clientHandler, String login, String password, String userName);

    Connection getConnection();
}
