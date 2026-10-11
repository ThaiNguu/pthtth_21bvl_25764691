package bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatServer {

    private static final int PORT = 5006;
    private static final int MAX_CLIENTS = 20;

    private static final ConcurrentHashMap<String, ClientHandler> clients
            = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println(
                    "Chat Server dang lang nghe cong " + PORT);

            while (true) {
                Socket socket = server.accept();
                pool.submit(new ClientHandler(socket));
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi khoi dong Server: " + e.getMessage());
        } finally {
            pool.shutdownNow();
        }
    }

    private static boolean validNickname(String name) {
        return name != null
                && name.trim().matches("[\\p{L}\\p{N}_-]{1,20}");
    }

    private static String getUserList() {
        return clients.values().stream()
                .map(client -> client.nickname)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(", "));
    }

    private static void broadcast(
            String message, ClientHandler sender) {

        for (ClientHandler client : clients.values()) {
            if (client != sender) {
                client.send(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket socket;

        private String nickname;
        private String key;
        private PrintWriter out;
        private boolean registered = false;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (
                Socket clientSocket = socket;

                BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream(),
                        StandardCharsets.UTF_8));

                PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(
                        clientSocket.getOutputStream(),
                        StandardCharsets.UTF_8),
                    true)
            ) {

                this.out = writer;

                String inputNickname = in.readLine();

                if (!validNickname(inputNickname)) {
                    send("ERR INVALID_NICKNAME");
                    return;
                }

                nickname = inputNickname.trim();
                key = nickname.toLowerCase(Locale.ROOT);

                if (clients.putIfAbsent(key, this) != null) {
                    send("ERR NICKNAME_TAKEN");
                    return;
                }

                registered = true;

                send("OK WELCOME " + nickname);

                System.out.println(
                        "Client da ket noi: " + nickname);

                broadcast(
                        "INFO " + nickname + " da tham gia chat.",
                        this);

                String request;

                while ((request = in.readLine()) != null) {

                    String command = request.trim();

                    if (command.isEmpty()) {
                        send("ERR EMPTY_COMMAND");
                    }

                    else if (command.equalsIgnoreCase("USERS")) {

                        send("OK USERS: " + getUserList());

                    }

                    else if (command.equalsIgnoreCase("QUIT")) {

                        send("OK BYE");
                        break;

                    }

                    else if (command.equalsIgnoreCase("MSG")
                            || command.regionMatches(
                                    true, 0, "MSG ", 0, 4)) {

                        String message = command.length() <= 3
                                ? ""
                                : command.substring(3).trim();

                        if (message.isEmpty()) {
                            send("ERR EMPTY_MESSAGE");
                        } else {
                            broadcast(
                                    "MSG " + nickname + ": " + message,
                                    this);

                            send("OK SENT");
                        }

                    }

                    else {
                        send("ERR UNKNOWN_COMMAND");
                    }
                }

            } catch (IOException e) {

                System.err.println(
                        "Loi Client "
                        + socket.getRemoteSocketAddress()
                        + ": " + e.getMessage());

            } finally {

                if (registered && clients.remove(key, this)) {

                    System.out.println(
                            "Client da roi: " + nickname);

                    broadcast(
                            "INFO " + nickname + " da roi khoi chat.",
                            this);
                }
            }
        }

        private synchronized void send(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }
}