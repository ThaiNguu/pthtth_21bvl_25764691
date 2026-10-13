
package kiemtrathuongki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static volatile boolean inRoom = false;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== TCP CHAT CLIENT =====");
        System.out.print("Nhap ten cua ban: ");

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            name = "Guest";
        }

        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socket.getOutputStream(), true)) {

           
            out.println("NAME|" + name);

            
            Thread receiver = new Thread(() -> {

                try {
                    String message;

                    while ((message = in.readLine()) != null) {

                        if (message.startsWith("ROOMS|")) {

                            String rooms =
                                    message.substring(6);

                            System.out.println(
                                    "\nDanh sach phong: "
                                    + rooms.replace(",", ", "));

                            System.out.println(
                                    "Nhap ten phong de tham gia.");
                        }

                        else if (message.startsWith("JOINED|")) {

                            String room =
                                    message.substring(7);

                            inRoom = true;

                            System.out.println(
                                    "\nBan da vao phong " + room);

                            System.out.println(
                                    "Hay nhap tin nhan de tro chuyen.");
                        }

                        else if (message.startsWith("LEFT|")) {

                            inRoom = false;

                            System.out.println(
                                    "\n" + message.substring(5));
                        }

                        else if (message.startsWith("MSG|")) {

                            System.out.println(
                                    "\n" + message.substring(4));
                        }

                        else if (message.startsWith("INFO|")) {

                            System.out.println(
                                    "\n[THONG BAO] "
                                    + message.substring(5));
                        }

                        else if (message.startsWith("ERROR|")) {

                            System.out.println(
                                    "\n[LOI] "
                                    + message.substring(6));
                        }
                    }

                } catch (IOException e) {

                    System.out.println(
                            "\nKet noi den server da dong.");

                } finally {
                    inRoom = false;
                }
            });

            receiver.setDaemon(true);
            receiver.start();

       
            while (true) {

                String input = scanner.nextLine().trim();

                if (input.equals("/exit")) {
                    out.println("/exit");
                    break;
                }

                if (input.equals("/quit")) {

                    if (inRoom) {
                        out.println("/quit");
                        inRoom = false;
                    } else {
                        System.out.println(
                                "Ban chua tham gia phong.");
                    }

                    continue;
                }

                if (input.isEmpty()) {
                    continue;
                }

                if (inRoom) {
                    out.println(input);
                } else {
                    out.println("JOIN|" + input);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Khong the ket noi server. "
                    + "Hay kiem tra server da chay chua.");

        } finally {
            scanner.close();
        }
    }
}
