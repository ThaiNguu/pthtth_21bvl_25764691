
package bai6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpBenchmarkServer {

    private static final int PORT = 5008;
    private static final int MESSAGE_SIZE = 100;

    public static void main(String[] args) {

        try (DatagramSocket server = new DatagramSocket(PORT)) {

            System.out.println(
                "UDP Benchmark Server dang nghe cong " + PORT);

            while (true) {

                byte[] buffer = new byte[MESSAGE_SIZE];

                DatagramPacket request = new DatagramPacket(
                    buffer, buffer.length);

                server.receive(request);

                if (request.getLength() != MESSAGE_SIZE) {
                    System.out.println(
                        "Bo qua datagram khong dung 100 byte.");
                    continue;
                }

                DatagramPacket response = new DatagramPacket(
                    request.getData(),
                    request.getOffset(),
                    request.getLength(),
                    request.getAddress(),
                    request.getPort());

                server.send(response);
            }

        } catch (IOException e) {
            System.err.println(
                "Loi UDP Server: " + e.getMessage());
        }
    }
}
