
package kiemtrathuongki;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static final int PORT = 5000;

    public static final List<String> ROOMS =
            Arrays.asList("Study", "Game", "TraiNghiem");

    public static final List<ClientHandler> clients =
            new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("===== TCP CHAT SERVER =====");

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            System.out.println(
                    "Server dang chay tai cong " + PORT);

            System.out.println(
                    "Dang cho client ket noi...");

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "Co client moi ket noi: "
                        + socket.getRemoteSocketAddress());

                ClientHandler handler =
                        new ClientHandler(socket);

                synchronized (clients) {
                    clients.add(handler);
                }

                handler.start();
            }

        } catch (IOException e) {
            System.out.println(
                    "Loi server: " + e.getMessage());
        }
    }

    public static void broadcast(
            String room, String message) {

        synchronized (clients) {
            for (ClientHandler client : clients) {

                if (room.equals(client.getRoom())) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void removeClient(
            ClientHandler client) {

        synchronized (clients) {
            clients.remove(client);
        }
    }
}
