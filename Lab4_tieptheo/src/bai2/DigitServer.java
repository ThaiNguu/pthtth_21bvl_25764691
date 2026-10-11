
package bai2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitServer {

    private static final int PORT = 5002;

    private static final String[] WORDS = {
        "không", "một", "hai", "ba", "bốn",
        "năm", "sáu", "bảy", "tám", "chín"
    };

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                "TCP Server dang lang nghe cong " + PORT);

            while (true) {

                try (
                    Socket socket = server.accept();

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

                    System.out.println(
                        "Client ket noi: "
                        + socket.getRemoteSocketAddress());

                    String request;

                    while ((request = in.readLine()) != null) {

                        System.out.println(
                            "Nhan: [" + request + "]");

                        String response;

                        if (request.equalsIgnoreCase("QUIT")) {
                            out.println("OK BYE");
                            System.out.println("Gui: OK BYE");
                            break;
                        }

                        if (request.length() == 1
                                && request.charAt(0) >= '0'
                                && request.charAt(0) <= '9') {

                            int number = request.charAt(0) - '0';
                            response = WORDS[number];

                        } else {
                            response = "ERR INVALID_DIGIT";
                        }

                        out.println(response);
                        System.out.println("Gui: " + response);
                    }

                    System.out.println("Client da ngat ket noi.");

                } catch (IOException e) {
                    System.err.println(
                        "Loi xu ly Client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Khong khoi dong duoc Server tren cong "
                + PORT + ": " + e.getMessage());
        }
    }
}
