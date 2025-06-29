package ru.otus.chat.client;

import com.sun.tools.javac.Main;
import ru.otus.protocol.ProtocolConstants;
import ru.otus.utill.NetworkConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

/**
 *
 */
public class Client {

    private static final Logger log = Logger.getLogger(Client.class.getName());
    private final Scanner scanner;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public Client() throws IOException {

        scanner = new Scanner(System.in);
        socket = new Socket(NetworkConstants.SERVER_HOST, NetworkConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        try {
            new Thread(() -> {
                try {
                    while (true) {
                        String message = inputStream.readUTF(); // Входящее сообщение
                        if (message.startsWith(ProtocolConstants.COMMAND_PREFIX)) {
                            if (message.equalsIgnoreCase(ProtocolConstants.EXIT_OK)) {
                                break;
                            }
                            if (message.startsWith(ProtocolConstants.AUTH_OK)) {
                                log.info("Вы подключились под ником: " + message.split(" ")[1]);
                                continue;
                            }
                            if (message.startsWith(ProtocolConstants.REG_OK)) {
                                log.info("Вы успешно зарегистрировались и подключились под ником: "
                                        + message.split(" ")[1]);
                                continue;
                            }
                            if (message.equalsIgnoreCase(ProtocolConstants.KICK_OK)) {
                                log.info("Вы были отключены от сервера");
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
                if (message.equals(ProtocolConstants.EXIT)) break;
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