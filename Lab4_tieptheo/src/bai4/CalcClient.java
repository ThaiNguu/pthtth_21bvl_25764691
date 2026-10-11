package bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5005;

    public static void main(String[] args) {

        try (
            Socket socket = new Socket(HOST, PORT);

            BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                    System.in, StandardCharsets.UTF_8));

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

            System.out.println("Da ket noi Calculator Server.");
            System.out.println(
                "Nhap: CALC toan_tu so1 so2");
            System.out.println(
                "Vi du: CALC + 100 200");
            System.out.println("Nhap QUIT de thoat.");

            while (true) {

                System.out.print("Nhap phep tinh: ");
                String request = keyboard.readLine();

                if (request == null) {
                    break;
                }

                out.println(request);

                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server da dong ket noi.");
                    break;
                }

                System.out.println("Ket qua: " + response);

                if (request.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Loi ket noi Server: " + e.getMessage());
        }
    }
}