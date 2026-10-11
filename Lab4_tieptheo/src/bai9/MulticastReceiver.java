
package bai9;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class MulticastReceiver {

    private static final String GROUP = "239.255.0.1";
    private static final int PORT = 5011;

    public static void main(String[] args) {

        boolean joined = false;

        try {
            NetworkInterface networkInterface =
                    MulticastNetwork.choose(args);

            InetAddress group =
                    InetAddress.getByName(GROUP);

            InetSocketAddress groupAddress =
                    new InetSocketAddress(group, PORT);

            // Tao socket chua bind de co the dat reuse address
            MulticastSocket socket =
                    new MulticastSocket(null);

            try {
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(PORT));

                socket.setNetworkInterface(networkInterface);

                // Tham gia nhom bang giao dien da chon
                socket.joinGroup(
                        groupAddress, networkInterface);

                joined = true;

                System.out.println(
                        "Receiver dang nghe nhom " + GROUP);

                System.out.println("Cong: " + PORT);
                System.out.println(
                        "Giao dien: "
                        + networkInterface.getDisplayName());

                System.out.println(
                        "Dang cho thong bao...");

                try {
                    while (true) {

                        byte[] buffer = new byte[2048];

                        DatagramPacket packet =
                                new DatagramPacket(
                                        buffer, buffer.length);

                        socket.receive(packet);

                        String message = new String(
                                packet.getData(),
                                packet.getOffset(),
                                packet.getLength(),
                                StandardCharsets.UTF_8);

                        System.out.println(
                                "Nhan tu "
                                + packet.getAddress()
                                        .getHostAddress()
                                + ": " + message);

                        if (message.trim()
                                .equalsIgnoreCase("QUIT")) {
                            break;
                        }
                    }

                } finally {
                    // Roi nhom truoc khi dong socket
                    if (joined) {
                        socket.leaveGroup(
                                groupAddress, networkInterface);

                        joined = false;

                        System.out.println(
                                "Da roi nhom multicast.");
                    }
                }

            } finally {
                // Dong socket sau khi roi nhom
                if (joined) {
                    socket.leaveGroup(
                            groupAddress, networkInterface);
                }

                socket.close();
                System.out.println("Socket da dong.");
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi Receiver: " + e.getMessage());
        }
    }
}
