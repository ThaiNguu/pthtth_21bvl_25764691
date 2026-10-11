
package bai7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageLogServer {

    private static final int PORT = 5009;

    private static final Path LOG_DIR =
            Path.of("data", "logs");

    private static final Set<String> ACTIVE_CLIENTS =
            ConcurrentHashMap.newKeySet();

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try {
            Files.createDirectories(LOG_DIR);

            try (ServerSocket server = new ServerSocket(PORT)) {
                System.out.println(
                        "MessageLogServer dang lang nghe cong " + PORT);

                while (true) {
                    Socket socket = server.accept();
                    pool.execute(() -> handleClient(socket));
                }
            }
        } catch (IOException e) {
            System.err.println(
                    "Khong khoi dong duoc Server: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String registeredId = null;

        try (
            Socket client = socket;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream(),
                            StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            client.getOutputStream(),
                            StandardCharsets.UTF_8), true)
        ) {
            // Client phai gui HELLO truoc
            String hello = in.readLine();

            if (hello == null) {
                return;
            }

            String[] parts = hello.trim().split("\\s+", 2);

            if (parts.length != 2
                    || !parts[0].equalsIgnoreCase("HELLO")
                    || !parts[1].matches("[A-Za-z0-9_-]+")) {
                out.println("ERR EXPECTED_HELLO");
                return;
            }

            String requestedId = parts[1];

            // Khong cho phep hai Client dung cung ID dong thoi
            if (!ACTIVE_CLIENTS.add(requestedId)) {
                out.println("ERR CLIENT_ID_TAKEN");
                return;
            }

            registeredId = requestedId;
            out.println("OK HELLO " + registeredId);

            System.out.println(
                    "Client " + registeredId + " da ket noi.");

            String message;

            while ((message = in.readLine()) != null) {

                if (message.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                if (message.isBlank()) {
                    out.println("ERR EMPTY_MESSAGE");
                    continue;
                }

                try {
                    saveMessage(
                            registeredId,
                            client.getRemoteSocketAddress().toString(),
                            message);

                    System.out.println(
                            "Da ghi tin nhan cua " + registeredId);

                    out.println("OK SAVED");

                } catch (IOException e) {
                    System.err.println(
                            "Loi ghi file cua " + registeredId
                            + ": " + e.getMessage());

                    out.println("ERR LOG_WRITE");
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi ket noi Client: " + e.getMessage());

        } finally {
            if (registeredId != null) {
                ACTIVE_CLIENTS.remove(registeredId);

                System.out.println(
                        "Client " + registeredId + " da ngat ket noi.");
            }
        }
    }

    private static void saveMessage(
            String clientId, String remote, String message)
            throws IOException {

        Path file = LOG_DIR.resolve(clientId + ".txt");

        String timestamp = LocalDateTime.now().format(FORMAT);

        try (var writer = Files.newBufferedWriter(
                file,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(timestamp + " | " + remote + " | " + message);
            writer.newLine();
        }
    }
}
