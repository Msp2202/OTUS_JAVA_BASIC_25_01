package ru.otus.chat.client;

import ru.otus.chat.client.utilit.ExampleClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 */
public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Клиент запущен ");

        try (Socket socket = new Socket("localhost", 8085);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            while (true) {
                String massage = scanner.nextLine();
                out.writeUTF(massage);
            }


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}