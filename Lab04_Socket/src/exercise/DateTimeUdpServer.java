package exercise;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUdpServer {

    private static final int PORT = 5004;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MM yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH mm ss");

    public static void main(String[] args) {

        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            System.out.println("UDP DateTime Server running on port " + PORT);

            while (true) {

                DatagramPacket request =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(request);

                String command =
                        new String(
                                request.getData(),
                                request.getOffset(),
                                request.getLength(),
                                StandardCharsets.UTF_8);

                String responseText = process(command);

                byte[] data =
                        responseText.getBytes(StandardCharsets.UTF_8);

                DatagramPacket response =
                        new DatagramPacket(
                                data,
                                data.length,
                                request.getAddress(),
                                request.getPort());

                socket.send(response);
            }

        } catch (Exception e) {

            System.err.println(
                    "UDP server error: " + e.getMessage());
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

        } else {

            return "ERR UNKNOWN_COMMAND";
        }
    }
}