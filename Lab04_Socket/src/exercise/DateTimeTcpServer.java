package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTcpServer {

    private static final int PORT = 5003;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MM yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH mm ss");

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("TCP DateTime Server running on port " + PORT);

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

                        String response = process(request);

                        out.println(response);

                        if (request.equalsIgnoreCase("QUIT")) {
                            break;
                        }
                    }

                    System.out.println("Client disconnected");
                }

            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    static String process(String request) {

        if (request.equalsIgnoreCase("DATE")) {

            return LocalDate.now().format(DATE_FORMAT);

        } else if (request.equalsIgnoreCase("TIME")) {

            return LocalTime.now().format(TIME_FORMAT);

        } else if (request.equalsIgnoreCase("DATETIME")) {

            LocalDateTime now = LocalDateTime.now();

            return now.toLocalDate().format(DATE_FORMAT)
                    + " "
                    + now.toLocalTime().format(TIME_FORMAT);

        } else if (request.equalsIgnoreCase("QUIT")) {

            return "OK BYE";

        } else {

            return "ERR UNKNOWN_COMMAND";
        }
    }
}