
package bai6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpBenchmarkServer {

    private static final int PORT = 5007;
    private static final int MESSAGE_SIZE = 100;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                "TCP Benchmark Server dang nghe cong " + PORT);

            while (true) {

                try (
                    Socket client = server.accept();

                    DataInputStream in = new DataInputStream(
                        client.getInputStream());

                    DataOutputStream out = new DataOutputStream(
                        client.getOutputStream())
                ) {

                    System.out.println("TCP Client da ket noi.");

                    byte[] buffer = new byte[MESSAGE_SIZE];

                    while (true) {

                        try {
                            in.readFully(buffer);
                        } catch (EOFException e) {
                            break;
                        }

                        out.write(buffer);
                        out.flush();
                    }

                    System.out.println("TCP Client da ngat.");

                } catch (IOException e) {
                    System.err.println(
                        "Loi TCP Client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println(
                "Khong khoi dong duoc TCP Server: "
                + e.getMessage());
        }
    }
}
