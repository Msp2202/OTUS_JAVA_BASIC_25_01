package ru.otus.chan.server;

/**
 * Класс по запуску сервера.
 * Не забываем перезапустить сервер с предварительной остановкой(Ошибка java.net.BindException:)
 */
public class ServerApplication {
    public static void main(String[] args) {
        new Server(8085).start();
    }
}