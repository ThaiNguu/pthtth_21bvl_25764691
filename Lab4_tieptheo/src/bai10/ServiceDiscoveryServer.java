
package bai10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceDiscoveryServer {

    private static final int UDP_PORT = 5012;
    private static final int TCP_PORT = 5013;

    private static final String SERVICE_NAME = "EchoService";
    private static final String VERSION = "1.0";

    public static void main(String[] args) {

        ExecutorService pool =
                Executors.newFixedThreadPool(10);

        try (
            ServerSocket tcpServer =
                    new ServerSocket(TCP_PORT);

            DatagramSocket udpServer =
                    new DatagramSocket(UDP_PORT)
        ) {

            System.out.println(
                    "TCP Service dang lang nghe cong "
                    + TCP_PORT);

            System.out.println(
                    "UDP Discovery dang lang nghe cong "
                    + UDP_PORT);

            // Chay TCP Server trong thread rieng
            Thread tcpThread = new Thread(() -> {
                while (!tcpServer.isClosed()) {
                    try {
                        Socket client = tcpServer.accept();

                        pool.execute(
                                () -> handleClient(client));

                    } catch (IOException e) {
                        if (!tcpServer.isClosed()) {
                            System.err.println(
                                    "Loi TCP: " + e.getMessage());
                        }
                    }
                }
            });

            tcpThread.setDaemon(true);
            tcpThread.start();

            // Nhan yeu cau kham pha dich vu bang UDP
            while (true) {

                byte[] buffer = new byte[1024];

                DatagramPacket request =
                        new DatagramPacket(
                                buffer, buffer.length);

                udpServer.receive(request);

                String message = new String(
                        request.getData(),
                        request.getOffset(),
                        request.getLength(),
                        StandardCharsets.UTF_8).trim();

                if (message.equals("DISCOVER_SERVICE")) {

                    String responseText =
                            "SERVICE " + SERVICE_NAME
                            + " " + TCP_PORT
                            + " " + VERSION;

                    byte[] responseData =
                            responseText.getBytes(
                                    StandardCharsets.UTF_8);

                    DatagramPacket response =
                            new DatagramPacket(
                                    responseData,
                                    responseData.length,
                                    request.getAddress(),
                                    request.getPort());

                    udpServer.send(response);

                    System.out.println(
                            "Da phan hoi yeu cau tu "
                            + request.getAddress()
                                    .getHostAddress());
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Loi khoi dong Server: " + e.getMessage());

        } finally {
            pool.shutdownNow();
        }
    }

    private static void handleClient(Socket client) {

        try (
            Socket socket = client;

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(),
                    true,
                    StandardCharsets.UTF_8)
        ) {

            String request = in.readLine();

            if (request == null) {
                return;
            }

            if (request.trim().equalsIgnoreCase("PING")) {

                out.println(
                        "PONG " + SERVICE_NAME + " " + VERSION);

                System.out.println(
                        "Da phuc vu TCP Client: "
                        + socket.getRemoteSocketAddress());

            } else if (request.trim().equalsIgnoreCase("QUIT")) {

                out.println("OK BYE");

            } else {

                out.println("ERR UNKNOWN_COMMAND");
            }

        } catch (IOException e) {

            System.err.println(
                    "Loi xu ly TCP Client: " + e.getMessage());
        }
    }
}
