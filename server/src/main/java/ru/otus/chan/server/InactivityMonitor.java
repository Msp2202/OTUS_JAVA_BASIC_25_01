package ru.otus.chan.server;

import ru.otus.config.ServerConfig;

import java.util.List;
import java.util.logging.Logger;

import ru.otus.config.ServerConfig;

/**
 * Монитор неактивных клиентов
 */
public class InactivityMonitor {
    private static final Logger log = Logger.getLogger(InactivityMonitor.class.getName());

    private final Server server;
    private Thread checkerThread;
    private volatile boolean running;

    public InactivityMonitor(Server server) {
        this.server = server;
    }

    /**
     * Запускаем мониторинг неактивных клиентов в отдельном потоке
     */
    public void start() {
        running = true;
        checkerThread = new Thread(() -> {
            log.info("Мониторинг неактивных клиентов запущен. " +
                    "Таймаут: " + (ServerConfig.INACTIVITY_TIMEOUT / 1000 / 60) + " мин, " +
                    "Проверка каждые: " + (ServerConfig.CHECK_INTERVAL / 1000 / 60) + " мин");

            while (running) {
                try {
                    checkInactiveClients();
                    Thread.sleep(ServerConfig.CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    log.info("Мониторинг неактивных клиентов прерван");
                    break;
                }
            }
        });

        checkerThread.start();
    }

    /**
     * Останавливаем мониторинг неактивных клиентов
     */
    public void stop() {
        running = false;
        if (checkerThread != null) {
            checkerThread.interrupt();
        }
    }

    /**
     * Проверяем и отключает неактивных клиентов
     */
    private void checkInactiveClients() {
        long currentTime = System.currentTimeMillis();
        int inactiveCount = 0;

        log.info("=== Начало проверки неактивных клиентов ===");
        log.info("Текущее время: " + System.currentTimeMillis());
        log.info("Количество подключенных клиентов: " + server.getClients().size());

        server.getClients().forEach(client ->
                log.info("Клиент " + client.getUsername() +
                        " | Последняя активность: " + client.getLastActivityTime() +
                        " | Неактивен (мс): " + (System.currentTimeMillis() - client.getLastActivityTime()))
        );

        List<ClientHandler> clients = server.getClients();
        for (ClientHandler client : clients) {
            if ((currentTime - client.getLastActivityTime()) > ServerConfig.INACTIVITY_TIMEOUT) {
                try {
                    log.info("Отключаем неактивного клиента: " + client.getUsername());
                    client.send("Вы были отключены за неактивность");
                    client.disconnect();
                    inactiveCount++;
                } catch (Exception e) {
                    log.warning("Ошибка при отключении клиента: " + e.getMessage());
                }
            }
        }

        if (inactiveCount > 0) {
            log.info("Отключено неактивных клиентов: " + inactiveCount);
            server.broadcastMessage("Обновлён список пользователей: " +
                    server.getActiveUsers(), true, true);
        }
    }
}