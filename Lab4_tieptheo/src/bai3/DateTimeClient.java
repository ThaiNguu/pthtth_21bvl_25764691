
package bai3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class DateTimeClient {

    private static final String HOST = "localhost";
    private static final int TCP_PORT = 5003;
    private static final int UDP_PORT = 5004;

    public static void main(String[] args) {

        try (BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                    System.in, StandardCharsets.UTF_8))) {

            while (true) {

                System.out.println("\n=== DICH VU NGAY GIO ===");
                System.out.println("1. TCP");
                System.out.println("2. UDP");
                System.out.println("0. Thoat");
                System.out.print("Chon: ");

                String choice = keyboard.readLine();

                if (choice == null || choice.equals("0")) {
                    break;
                }

                if (choice.equals("1")) {
                    runTcp(keyboard);
                } else if (choice.equals("2")) {
                    runUdp(keyboard);
                } else {
                    System.out.println("Lua chon khong hop le.");
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Loi Client: " + e.getMessage());
        }
    }

    private static void runTcp(BufferedReader keyboard) {

        try (
            Socket socket = new Socket(HOST, TCP_PORT);

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

            System.out.println("Da ket noi TCP.");
            System.out.println(
                "Nhap DATE, TIME, DATETIME hoac QUIT.");

            while (true) {

                System.out.print("TCP > ");
                String command = keyboard.readLine();

                if (command == null) {
                    break;
                }

                out.println(command);

                String response = in.readLine();

                if (response == null) {
                    System.out.println(
                        "TCP Server da dong ket noi.");
                    break;
                }

                System.out.println("TCP Server: " + response);

                if (command.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Loi ket noi TCP: " + e.getMessage());
        }
    }

    private static void runUdp(BufferedReader keyboard) {

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(3000);

            System.out.println("Bat dau UDP.");
            System.out.println(
                "Nhap DATE, TIME, DATETIME.");
            System.out.println(
                "Nhap BACK de quay lai menu.");

            while (true) {

                System.out.print("UDP > ");
                String command = keyboard.readLine();

                if (command == null
                        || command.trim().equalsIgnoreCase("BACK")) {
                    break;
                }

                byte[] data =
                    command.getBytes(StandardCharsets.UTF_8);

                DatagramPacket request = new DatagramPacket(
                    data,
                    data.length,
                    InetAddress.getByName(HOST),
                    UDP_PORT);

                socket.send(request);

                byte[] buffer = new byte[1024];

                DatagramPacket response = new DatagramPacket(
                    buffer, buffer.length);

                try {
                    socket.receive(response);

                    String result = new String(
                        response.getData(),
                        response.getOffset(),
                        response.getLength(),
                        StandardCharsets.UTF_8);

                    System.out.println("UDP Server: " + result);

                } catch (SocketTimeoutException e) {
                    System.out.println(
                        "UDP timeout: Server khong phan hoi.");
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Loi ket noi UDP: " + e.getMessage());
        }
    }
}
