package ru.otus.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Objects.nonNull;

/**
 *
 */
public class Client {
    private final Scanner scanner;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public Client() throws IOException {
        scanner = new Scanner(System.in);
        socket = new Socket("localhost", 8085);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        try {
            new Thread(() -> {
                try {
                    while (true) {
                        String message = inputStream.readUTF(); // Входящее сообщение
                        if (message.startsWith("/")) {
                            if (message.equalsIgnoreCase("/exitOk")) {
                                break;
                            }
                            if (message.startsWith("/authok ")) {
                                System.out.println("Вы подключились под ником: " + message.split(" ")[1]);
                                continue;
                            }
                            if (message.startsWith("/regok ")) {
                                System.out.println("Вы успешно зарегистрировались и подключились под ником: "
                                        + message.split(" ")[1]);
                                continue;
                            }
                            if (message.equalsIgnoreCase("/kickok")) {
                                System.out.println("Вы были отключены от сервера");
                                break;
                            }
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
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

    public void disconnect() {
        try {
            if (nonNull(inputStream)) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (nonNull(outputStream)) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (nonNull(socket)) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}