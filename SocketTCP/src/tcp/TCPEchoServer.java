package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {

    public static final int serverPort = 7;

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(serverPort);

            System.out.println("Server da duoc tao");
            System.out.println("Dang cho Client ket noi...");

            while (true) {

                try {
                    Socket s = ss.accept();

                    System.out.println("Client da ket noi: "
                            + s.getInetAddress());

                    OutputStream os = s.getOutputStream();
                    InputStream is = s.getInputStream();

                    int ch;

                    while (true) {

                        ch = is.read();

                        if (ch == -1) {
                            break;
                        }

                        System.out.println("Server nhan: "
                                + (char) ch);

                        // Gui lai cho Client
                        os.write(ch);
                        os.flush();
                    }

                    s.close();

                    System.out.println("Client da ngat ket noi.");

                } catch (IOException e1) {

                    System.out.println(
                            "Connection Error: " + e1);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Server Creation Error: " + e);
        }
    }
}