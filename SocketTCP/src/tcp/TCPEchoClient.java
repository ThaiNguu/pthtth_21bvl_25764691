package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPEchoClient {

    public static final String serverIP = "127.0.0.1";
    public static final int serverPort = 7;

    public static void main(String[] args)
            throws InterruptedException, IOException {

        Socket s = null;

        try {

            // Ket noi den Server
            s = new Socket(serverIP, serverPort);

            System.out.println("Client da ket noi Server");

            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            for (int i = 0; i <= 9; i++) {

                // Gui ky tu 0 -> 9
                os.write('0' + i);
                os.flush();

                System.out.println(
                        "Client gui: " + i);

                // Nhan du lieu Server gui lai
                int ch = is.read();

                System.out.println(
                        "Client nhan: " + (char) ch);

                // Cho 2 giay
                Thread.sleep(2000);
            }

        } catch (IOException ie) {

            System.out.println(
                    "Error: Can NOT create socket");

        } finally {

            if (s != null) {
                s.close();
            }
        }
    }
}