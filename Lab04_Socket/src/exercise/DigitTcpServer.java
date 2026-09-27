package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitTcpServer {

    private static final int PORT = 5002;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("Digit TCP Server running on port " + PORT);

            while (true) {

                try (Socket socket = server.accept()) {

                    System.out.println("Client connected");

                    BufferedReader in =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream(),
                                            StandardCharsets.UTF_8));

                    PrintWriter out =
                            new PrintWriter(
                                    new OutputStreamWriter(
                                            socket.getOutputStream(),
                                            StandardCharsets.UTF_8),
                                    true);

                    String request;

                    while ((request = in.readLine()) != null) {

                        if (request.equalsIgnoreCase("QUIT")) {
                            out.println("OK BYE");
                            break;
                        }

                        out.println(convertDigit(request));
                    }

                    System.out.println("Client disconnected");

                } catch (IOException e) {
                    System.err.println(
                            "Client error: " + e.getMessage());
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Server error: " + e.getMessage());
        }
    }

    static String convertDigit(String input) {

        if (input.length() != 1
                || input.charAt(0) < '0'
                || input.charAt(0) > '9') {

            return "ERR INVALID_DIGIT";
        }

        switch (input.charAt(0)) {

            case '0': return "không";
            case '1': return "một";
            case '2': return "hai";
            case '3': return "ba";
            case '4': return "bốn";
            case '5': return "năm";
            case '6': return "sáu";
            case '7': return "bảy";
            case '8': return "tám";
            case '9': return "chín";

            default:
                return "ERR INVALID_DIGIT";
        }
    }
}