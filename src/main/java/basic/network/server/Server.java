package basic.network.server;

import basic.network.Calculator;
import basic.network.client.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8081;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Сервер подключен к порту :" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            try (
                    DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())

            ) {
                System.out.println("Клиент с портом " + clientSocket.getPort() + "подключен к серверу.\n Для выхода введите 'exit'");

                /**
                 * Информационное сообщение для клиента о списке доступных операций
                 */
                outputStream.writeUTF("Доступные операции: +, -, *, /\nВведите выражение в формате:" +
                        " число операция число");

                /**
                 * Получен запрос на вычисления
                 */
                String requst = inputStream.readUTF();
                System.out.println("Получен запрос" + requst);

                /**
                 * Условия для выхода
                 */
                if (requst.equalsIgnoreCase("exist")) {
                    clientSocket.close();
                    continue;
                }

                /**
                 * Обработка запроса
                 *
                 * доп инф. использование \\s+ вместо простого пробела или
                 * \\s (без +) позволяет обрабатывать случаи с несколькими пробелами между элементами
                 */
                String response = null;
                try {
                    String[] parts = requst.trim().split("\\s+");
                    if (parts.length != 3) {
                        throw new IllegalArgumentException("Неверный формат запроса");
                    }

                    double num_1 = Double.parseDouble(parts[0]);
                    String operation = parts[1];
                    double num_2 = Double.parseDouble(parts[2]);

                    response = "Результат вычисления: " + Calculator.calculate(num_1, operation, num_2);

                } catch (Exception e) {
                    System.err.printf("Ошибка при обработке запроса: " + e.getMessage());
                }

                /**
                 * Отправляем ответ
                 */
                if (nonNull(response)) {
                    outputStream.writeUTF(response);
                }

            } finally {
                clientSocket.close();
            }
        }
    }
}
