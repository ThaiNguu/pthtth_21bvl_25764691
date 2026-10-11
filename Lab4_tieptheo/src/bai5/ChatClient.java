package bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5006;

    public static void main(String[] args) {

        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                        System.in, StandardCharsets.UTF_8));

        try (
            Socket socket = new Socket(HOST, PORT);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8));

            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                    socket.getOutputStream(),
                    StandardCharsets.UTF_8),
                true)
        ) {

            System.out.print("Nhap nickname: ");

            String nickname = keyboard.readLine();

            if (nickname == null) {
                return;
            }

            out.println(nickname);

            String welcome = in.readLine();

            if (welcome == null) {
                System.out.println("Server da dong ket noi.");
                return;
            }

            System.out.println(welcome);

            if (!welcome.startsWith("OK WELCOME ")) {
                return;
            }

            Thread receiver = new Thread(() -> {

                try {
                    String response;

                    while ((response = in.readLine()) != null) {

                        System.out.println("\n" + response);

                        if (response.equalsIgnoreCase("OK BYE")) {
                            return;
                        }
                    }

                    System.out.println(
                            "\nServer da dong ket noi.");

                } catch (IOException e) {
                    System.out.println(
                            "\nKet noi da dong.");
                }
            });

            receiver.start();

            while (true) {

                System.out.print(
                        "Nhap lenh (USERS, MSG noi_dung, QUIT): ");

                String request = keyboard.readLine();

                if (request == null) {
                    break;
                }

                out.println(request);

                if (request.trim().equalsIgnoreCase("QUIT")) {
                    receiver.join();
                    break;
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Loi ket noi: " + e.getMessage());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println("Client bi gian doan.");
        }
    }
}