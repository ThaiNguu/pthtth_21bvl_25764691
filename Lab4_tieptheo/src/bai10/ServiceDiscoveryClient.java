
package bai10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceDiscoveryClient {

    private static final int UDP_PORT = 5012;
    private static final int TIMEOUT_MS = 3000;

    private static class ServiceInfo {

        String name;
        InetAddress address;
        int tcpPort;
        String version;

        ServiceInfo(
                String name,
                InetAddress address,
                int tcpPort,
                String version) {

            this.name = name;
            this.address = address;
            this.tcpPort = tcpPort;
            this.version = version;
        }
    }

    public static void main(String[] args) {

        try {

            Map<String, ServiceInfo> services =
                    discoverServices();

            if (services.isEmpty()) {

                System.out.println(
                        "Khong tim thay dich vu.");

                System.out.println(
                        "Hay kiem tra Server, firewall "
                        + "va giao dien mang.");

                return;
            }

            System.out.println();
            System.out.println("=== DICH VU TIM THAY ===");

            int number = 1;

            for (ServiceInfo service : services.values()) {

                System.out.println(
                        number++ + ". "
                        + service.name
                        + " | IP: "
                        + service.address.getHostAddress()
                        + " | TCP: " + service.tcpPort
                        + " | Version: " + service.version);
            }

            BufferedReader keyboard =
                    new BufferedReader(
                            new InputStreamReader(
                                    System.in,
                                    StandardCharsets.UTF_8));

            System.out.print(
                    "Chon dich vu can ket noi (mac dinh 1): ");

            String choice = keyboard.readLine();

            int selectedNumber = 1;

            if (choice != null && !choice.isBlank()) {
                selectedNumber = Integer.parseInt(choice);
            }

            if (selectedNumber < 1
                    || selectedNumber > services.size()) {

                System.out.println(
                        "Lua chon khong hop le.");

                return;
            }

            ServiceInfo selected = services.values()
                    .stream()
                    .skip(selectedNumber - 1L)
                    .findFirst()
                    .orElseThrow();

            connectTcp(selected);

        } catch (IOException e) {

            System.err.println(
                    "Loi Client: " + e.getMessage());

        } catch (NumberFormatException e) {

            System.err.println(
                    "Vui long nhap so thu tu hop le.");
        }
    }

    private static Map<String, ServiceInfo> discoverServices()
            throws IOException {

        Map<String, ServiceInfo> services =
                new LinkedHashMap<>();

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setBroadcast(true);

            byte[] requestData =
                    "DISCOVER_SERVICE".getBytes(
                            StandardCharsets.UTF_8);

            // Gui yeu cau den mang broadcast
            DatagramPacket broadcast =
                    new DatagramPacket(
                            requestData,
                            requestData.length,
                            InetAddress.getByName(
                                    "255.255.255.255"),
                            UDP_PORT);

            socket.send(broadcast);

            // Cho toi da 1.5 giay de nhan cac phan hoi broadcast
            receiveResponses(socket, services, 1500);

            // Neu broadcast khong tim thay dich vu,
            // thu lai tren localhost de kiem thu cung may
            if (services.isEmpty()) {

                System.out.println(
                        "Thu tim dich vu tren localhost...");

                DatagramPacket localRequest =
                        new DatagramPacket(
                                requestData,
                                requestData.length,
                                InetAddress.getByName(
                                        "127.0.0.1"),
                                UDP_PORT);

                socket.send(localRequest);

                receiveResponses(
                        socket, services, TIMEOUT_MS);
            }
        }

        return services;
    }

    private static void receiveResponses(
            DatagramSocket socket,
            Map<String, ServiceInfo> services,
            int durationMs) throws IOException {

        long deadline =
                System.currentTimeMillis() + durationMs;

        while (System.currentTimeMillis() < deadline) {

            long remaining =
                    deadline - System.currentTimeMillis();

            socket.setSoTimeout(
                    (int) Math.max(
                            1, Math.min(400, remaining)));

            byte[] buffer = new byte[1024];

            DatagramPacket response =
                    new DatagramPacket(
                            buffer, buffer.length);

            try {

                socket.receive(response);

            } catch (SocketTimeoutException e) {

                continue;
            }

            String message = new String(
                    response.getData(),
                    response.getOffset(),
                    response.getLength(),
                    StandardCharsets.UTF_8).trim();

            String[] parts = message.split("\\s+");

            if (parts.length != 4
                    || !parts[0].equals("SERVICE")) {
                continue;
            }

            try {

                String name = parts[1];
                int tcpPort = Integer.parseInt(parts[2]);
                String version = parts[3];

                if (tcpPort < 1 || tcpPort > 65535) {
                    continue;
                }

                // Dung IP nguon cua phan hoi UDP
                InetAddress serverAddress =
                        response.getAddress();

                // Khoa giup loai bo phan hoi trung lap
                String key =
                        serverAddress.getHostAddress()
                        + "|" + tcpPort
                        + "|" + name
                        + "|" + version;

                services.putIfAbsent(
                        key,
                        new ServiceInfo(
                                name,
                                serverAddress,
                                tcpPort,
                                version));

            } catch (NumberFormatException e) {

                System.out.println(
                        "Bo qua phan hoi co cong TCP khong hop le.");
            }
        }
    }

    private static void connectTcp(ServiceInfo service)
            throws IOException {

        System.out.println();
        System.out.println(
                "Dang ket noi den "
                + service.address.getHostAddress()
                + ":" + service.tcpPort);

        try (Socket socket = new Socket()) {

            socket.connect(
                    new InetSocketAddress(
                            service.address,
                            service.tcpPort),
                    TIMEOUT_MS);

            socket.setSoTimeout(TIMEOUT_MS);

            try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream(),
                                StandardCharsets.UTF_8));

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(),
                        true,
                        StandardCharsets.UTF_8)
            ) {

                out.println("PING");

                String response = in.readLine();

                System.out.println(
                        "Phan hoi TCP: " + response);
            }
        }
    }
}
