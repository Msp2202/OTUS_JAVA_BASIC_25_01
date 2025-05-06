package basic.network.client;

import basic.network.utilit.ExampleClient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Клиент запущен. Для выхода введите 'exit'");

        while (true) {
            try (Socket socket = new Socket("localhost", 8081);
                 ExampleClient client = new ExampleClient(
                         socket.getInputStream(),
                         socket.getOutputStream()
                 )) {

                /**
                 * Получаем приветственное сообщение от сервера
                 */
                System.out.println(client.getWelcomeMessage());

                /**
                 * Ввод выражения
                 */
                System.out.print("> ");
                String expression = scanner.nextLine()
                        .replace("=", "")
                        .trim();

                if (expression.equalsIgnoreCase("exit")) break;

                /**
                 * Отправка запроса и получение результата
                 */
                client.send(expression);

            } catch (IOException e) {
                System.err.println("Ошибка подключения: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }
}