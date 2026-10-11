
package bai9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class MulticastSender {

    private static final String GROUP = "239.255.0.1";
    private static final int PORT = 5011;

    public static void main(String[] args) {

        try {
            NetworkInterface networkInterface =
                    MulticastNetwork.choose(args);

            InetAddress group =
                    InetAddress.getByName(GROUP);

            try (
                MulticastSocket socket =
                        new MulticastSocket();

                BufferedReader keyboard =
                        new BufferedReader(
                                new InputStreamReader(
                                        System.in,
                                        StandardCharsets.UTF_8))
            ) {
                socket.setNetworkInterface(networkInterface);

                // Gioi han multicast trong mang cuc bo
                socket.setTimeToLive(1);

                System.out.println(
                        "Multicast Sender da san sang.");

                System.out.println("Nhom: " + GROUP);
                System.out.println("Cong: " + PORT);

                System.out.println(
                        "Nhap thong bao; nhap QUIT de ket thuc.");

                while (true) {

                    System.out.print("Thong bao: ");

                    String message = keyboard.readLine();

                    if (message == null) {
                        break;
                    }

                    byte[] data = message.getBytes(
                            StandardCharsets.UTF_8);

                    DatagramPacket packet =
                            new DatagramPacket(
                                    data,
                                    data.length,
                                    group,
                                    PORT);

                    socket.send(packet);

                    System.out.println(
                            "Da gui thong bao multicast.");

                    if (message.trim()
                            .equalsIgnoreCase("QUIT")) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi Sender: " + e.getMessage());
        }
    }
}
