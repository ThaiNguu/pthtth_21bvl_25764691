
package bai7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageLogClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5009;

    public static void main(String[] args) {

        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                        System.in, StandardCharsets.UTF_8));

        try {
            System.out.print("Nhap clientId: ");
            String clientId = keyboard.readLine();

            if (clientId == null
                    || !clientId.matches("[A-Za-z0-9_-]+")) {
                System.out.println("ID khong hop le.");
                return;
            }

            try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),
                                StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),
                                StandardCharsets.UTF_8), true)
            ) {
                // Gui ID truoc khi gui tin nhan
                out.println("HELLO " + clientId);

                String response = in.readLine();

                if (response == null
                        || !response.startsWith("OK HELLO ")) {
                    System.out.println("Server: " + response);
                    return;
                }

                System.out.println("Server: " + response);

                while (true) {
                    System.out.print(
                            "Nhap tin nhan (QUIT de thoat): ");

                    String message = keyboard.readLine();

                    if (message == null) {
                        break;
                    }

                    out.println(message);

                    response = in.readLine();

                    if (response == null) {
                        System.out.println(
                                "Server da dong ket noi.");
                        break;
                    }

                    System.out.println("Server: " + response);

                    if (message.equalsIgnoreCase("QUIT")) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi Client: " + e.getMessage());
        }
    }
}
