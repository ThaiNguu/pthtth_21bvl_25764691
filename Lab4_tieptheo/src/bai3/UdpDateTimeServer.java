
package bai3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpDateTimeServer {

    private static final int PORT = 5004;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            System.out.println(
                "UDP Server dang lang nghe cong " + PORT);

            while (true) {

                byte[] buffer = new byte[1024];

                DatagramPacket request = new DatagramPacket(
                    buffer, buffer.length);

                socket.receive(request);

                String command = new String(
                    request.getData(),
                    request.getOffset(),
                    request.getLength(),
                    StandardCharsets.UTF_8);

                String response = DateTimeService.process(command);

                byte[] responseData =
                    response.getBytes(StandardCharsets.UTF_8);

                DatagramPacket reply = new DatagramPacket(
                    responseData,
                    responseData.length,
                    request.getAddress(),
                    request.getPort());

                socket.send(reply);

                System.out.println(
                    "Nhan: " + command
                    + " -> " + response);
            }

        } catch (IOException e) {
            System.err.println(
                "Loi UDP Server: " + e.getMessage());
        }
    }
}
