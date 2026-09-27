package exercise;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DateTimeUdpClient {

    public static void main(String[] args) throws Exception {

        String host = "localhost";
        int port = 5004;

        InetAddress server =
                InetAddress.getByName(host);

        Scanner scanner = new Scanner(System.in);

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(3000);

            System.out.println(
                    "Nhap DATE, TIME, DATETIME hoặc EXIT:");

            while (true) {

                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("EXIT")) {
                    break;
                }

                byte[] data =
                        command.getBytes(StandardCharsets.UTF_8);

                DatagramPacket request =
                        new DatagramPacket(
                                data,
                                data.length,
                                server,
                                port);

                socket.send(request);

                byte[] buffer = new byte[1024];

                DatagramPacket response =
                        new DatagramPacket(
                                buffer,
                                buffer.length);

                try {

                    socket.receive(response);

                    String text =
                            new String(
                                    response.getData(),
                                    response.getOffset(),
                                    response.getLength(),
                                    StandardCharsets.UTF_8);

                    System.out.println("Server: " + text);

                } catch (SocketTimeoutException e) {

                    System.out.println(
                            "Khong nhan duoc phan hoi sau 3 giay");
                }
            }
        }

        scanner.close();
    }
}