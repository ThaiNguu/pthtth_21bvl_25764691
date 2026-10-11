
package bai3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpDateTimeServer {

    private static final int PORT = 5003;

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

                    System.out.println("Client da ket noi.");

                    String request;

                    while ((request = in.readLine()) != null) {

                        String command = request.trim();
                        String response;

                        if (command.equalsIgnoreCase("QUIT")) {
                            response = "OK BYE";
                            out.println(response);

                            System.out.println(
                                "Nhan: " + request
                                + " -> " + response);
                            break;
                        }

                        response = DateTimeService.process(request);
                        out.println(response);

                        System.out.println(
                            "Nhan: " + request
                            + " -> " + response);
                    }

                    System.out.println(
                        "Phien TCP da ket thuc.");

                } catch (IOException e) {
                    System.err.println(
                        "Loi phien TCP: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Khong khoi dong duoc TCP Server: "
                + e.getMessage());
        }
    }
}
