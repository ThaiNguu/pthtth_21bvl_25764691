
package bai8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class SafeFileClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5010;

    public static void main(String[] args) {

        try (BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                        System.in, StandardCharsets.UTF_8))) {

            System.out.print("Nhap duong dan file can gui: ");
            String pathText = keyboard.readLine();

            if (pathText == null || pathText.isBlank()) {
                System.out.println("Duong dan khong hop le.");
                return;
            }

            Path source;

            try {
                source = Path.of(pathText).toAbsolutePath().normalize();

            } catch (InvalidPathException e) {
                System.out.println("Duong dan khong hop le.");
                return;
            }

            if (!Files.isRegularFile(source)
                    || !Files.isReadable(source)) {
                System.out.println(
                        "File khong ton tai hoac khong doc duoc.");
                return;
            }

            long fileSize = Files.size(source);
            String hash = sha256(source);

            System.out.println(
                    "Ten file goc: " + source.getFileName());

            System.out.print(
                    "Ten file luu tren Server "
                    + "(Enter de dung ten goc): ");

            String remoteName = keyboard.readLine();

            if (remoteName == null || remoteName.isBlank()) {
                remoteName = source.getFileName().toString();
            }

            System.out.println("Kich thuoc: " + fileSize + " bytes");
            System.out.println("SHA-256: " + hash);

            try (
                Socket socket = new Socket(HOST, PORT);

                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(
                                socket.getInputStream()));

                DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(
                                socket.getOutputStream()))
            ) {

                // Gui metadata truoc phan du lieu
                out.writeUTF(remoteName);
                out.writeLong(fileSize);
                out.writeUTF(hash);
                out.flush();

                // Cho Server xac nhan truoc khi gui byte
                String response = in.readUTF();

                if (!response.equals("READY")) {
                    System.out.println("Server: " + response);
                    return;
                }

                System.out.println(
                        "Server da chap nhan, bat dau gui file.");

                // Gui dung so byte da khai bao
                try (InputStream fileIn = new BufferedInputStream(
                        Files.newInputStream(source))) {

                    byte[] buffer = new byte[8192];
                    long remaining = fileSize;

                    while (remaining > 0) {

                        int length = (int) Math.min(
                                buffer.length, remaining);

                        int bytesRead = fileIn.read(
                                buffer, 0, length);

                        if (bytesRead == -1) {
                            throw new IOException(
                                    "File da thay doi hoac bi rut ngan.");
                        }

                        out.write(buffer, 0, bytesRead);
                        remaining -= bytesRead;
                    }
                }

                out.flush();

                // Nhan ket qua kiem tra SHA-256
                System.out.println(
                        "Ket qua Server: " + in.readUTF());
            }

        } catch (IOException e) {

            System.err.println(
                    "Loi Client: " + e.getMessage());
        }
    }

    private static String sha256(Path file) throws IOException {

        MessageDigest digest = newSha256();

        try (InputStream in = new BufferedInputStream(
                Files.newInputStream(file))) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        return HexFormat.of().formatHex(digest.digest());
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
