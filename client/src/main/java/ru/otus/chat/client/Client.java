package ru.otus.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 */
public class Client {
    private Scanner scanner;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client() throws IOException {
        scanner = new Scanner(System.in);
        socket = new Socket("localhost", 8085);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        try {
            new Thread(() -> {
                while (true) {
                    try {
                        String message = inputStream.readUTF(); // Входящее сообщение
                        System.out.println(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            while (true) {
                String message = scanner.nextLine();
                outputStream.writeUTF(message); // Исходящее сообщение
                if (message.equals("/exit")) break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}