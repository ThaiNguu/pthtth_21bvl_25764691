
package bai8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

public class SafeFileServer {

    private static final int PORT = 5010;

    private static final long MAX_FILE_SIZE =
            100L * 1024 * 1024;

    private static final Path UPLOAD_DIR =
            Path.of("upload").toAbsolutePath().normalize();

    public static void main(String[] args) {

        try {
            Files.createDirectories(UPLOAD_DIR);

            try (ServerSocket server = new ServerSocket(PORT)) {

                System.out.println(
                        "SafeFileServer dang lang nghe cong " + PORT);

                System.out.println(
                        "Thu muc luu file: " + UPLOAD_DIR);

                while (true) {
                    Socket socket = server.accept();
                    handleClient(socket);
                }
            }

        } catch (IOException e) {
            System.err.println(
                    "Loi khoi dong Server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {

        Path tempFile = null;

        try (
            Socket client = socket;

            DataInputStream in = new DataInputStream(
                    new BufferedInputStream(
                            client.getInputStream()));

            DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(
                            client.getOutputStream()))
        ) {

            // Doc thong tin tep truoc khi nhan du lieu
            String fileName = in.readUTF();
            long fileSize = in.readLong();
            String expectedHash = in.readUTF();

            // Kiem tra ten tep
            if (!isSafeFileName(fileName)) {
                out.writeUTF("ERR INVALID_FILENAME");
                out.flush();
                return;
            }

            // Kiem tra kich thuoc
            if (fileSize < 0) {
                out.writeUTF("ERR INVALID_SIZE");
                out.flush();
                return;
            }

            if (fileSize > MAX_FILE_SIZE) {
                out.writeUTF("ERR FILE_TOO_LARGE");
                out.flush();
                return;
            }

            // SHA-256 phai co 64 ky tu he 16
            if (!expectedHash.matches("(?i)[0-9a-f]{64}")) {
                out.writeUTF("ERR INVALID_HASH");
                out.flush();
                return;
            }

            Path target = UPLOAD_DIR.resolve(fileName).normalize();

            // Dam bao tep nam truc tiep trong upload
            if (!UPLOAD_DIR.equals(target.getParent())) {
                out.writeUTF("ERR INVALID_FILENAME");
                out.flush();
                return;
            }

            // Cho phep Client bat dau gui byte
            out.writeUTF("READY");
            out.flush();

            tempFile = UPLOAD_DIR.resolve(
                    UUID.randomUUID() + ".part");

            MessageDigest digest = newSha256();
            boolean complete = true;

            // Chi doc dung so byte da khai bao
            try (OutputStream fileOut = new BufferedOutputStream(
                    Files.newOutputStream(
                            tempFile,
                            StandardOpenOption.CREATE_NEW,
                            StandardOpenOption.WRITE))) {

                byte[] buffer = new byte[8192];
                long remaining = fileSize;

                while (remaining > 0) {

                    int length = (int) Math.min(
                            buffer.length, remaining);

                    int bytesRead = in.read(
                            buffer, 0, length);

                    if (bytesRead == -1) {
                        complete = false;
                        break;
                    }

                    fileOut.write(buffer, 0, bytesRead);
                    digest.update(buffer, 0, bytesRead);

                    remaining -= bytesRead;
                }
            }

            if (!complete) {
                Files.deleteIfExists(tempFile);
                tempFile = null;

                out.writeUTF("ERR INCOMPLETE_FILE");
                out.flush();
                return;
            }

            // Tinh lai SHA-256 cua du lieu da nhan
            String actualHash =
                    HexFormat.of().formatHex(digest.digest());

            if (!actualHash.equalsIgnoreCase(expectedHash)) {

                Files.deleteIfExists(tempFile);
                tempFile = null;

                out.writeUTF("ERR HASH_MISMATCH");
                out.flush();
                return;
            }

            // Chi luu tep chinh thuc khi hash khop
            Files.move(
                    tempFile,
                    target,
                    StandardCopyOption.REPLACE_EXISTING);

            tempFile = null;

            out.writeUTF(
                    "OK SAVED " + fileName
                    + " (" + fileSize + " bytes)");

            out.flush();

            System.out.println("Da luu file: " + target);
            System.out.println("SHA-256: " + actualHash);

        } catch (IOException e) {

            System.err.println(
                    "Loi xu ly file: " + e.getMessage());

        } finally {

            // Xoa tep tam neu qua trinh that bai
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println(
                            "Khong xoa duoc file tam: "
                            + e.getMessage());
                }
            }
        }
    }

    private static boolean isSafeFileName(String name) {

        if (name == null || name.isBlank()
                || name.contains("..")
                || name.contains("/")
                || name.contains("\\")
                || name.contains(":")) {
            return false;
        }

        try {
            Path path = Path.of(name);

            return !path.isAbsolute()
                    && path.getNameCount() == 1
                    && path.getFileName().toString().equals(name);

        } catch (InvalidPathException e) {
            return false;
        }
    }

    private static MessageDigest newSha256() {

        try {
            return MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Java khong ho tro SHA-256", e);
        }
    }
}
