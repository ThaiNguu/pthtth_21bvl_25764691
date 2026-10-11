
package bai2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5002;

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

            System.out.println("Da ket noi den Server.");
            System.out.println(
                "Nhap chu so tu 0 den 9.");
            System.out.println(
                "Nhap QUIT de ket thuc.");

            String request;

            while (true) {

                System.out.print("Nhap: ");
                request = keyboard.readLine();

                if (request == null) {
                    break;
                }

                out.println(request);

                String response = in.readLine();

                if (response == null) {
                    System.out.println(
                        "Server da dong ket noi.");
                    break;
                }

                System.out.println(
                    "Gui [" + request
                    + "] -> Server: " + response);

                if (request.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Loi ket noi den Server: " + e.getMessage());
        }
    }
}
