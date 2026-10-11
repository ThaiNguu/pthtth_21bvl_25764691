package bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcServer {

    private static final int PORT = 5005;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                "Calculator Server dang lang nghe cong " + PORT);

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

                    System.out.println("Client da ket noi.");

                    String request;

                    while ((request = in.readLine()) != null) {

                        String response;

                        if (request.trim().equalsIgnoreCase("QUIT")) {
                            response = "OK BYE";
                            out.println(response);

                            System.out.println(
                                request + " -> " + response);
                            break;
                        }

                        response = CalculatorService.calculate(request);
                        out.println(response);

                        System.out.println(
                            request + " -> " + response);
                    }

                } catch (IOException e) {
                    System.err.println(
                        "Loi xu ly Client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Khong khoi dong duoc Server: " + e.getMessage());
        }
    }
}