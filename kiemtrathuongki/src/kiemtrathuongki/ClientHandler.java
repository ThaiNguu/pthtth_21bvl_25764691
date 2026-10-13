
package kiemtrathuongki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String name = "Guest";
    private String room = null;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getRoom() {
        return room;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void sendRoomList() {
        sendMessage(
                "ROOMS|" + String.join(",", Server.ROOMS));
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

      
            String firstMessage = in.readLine();

            if (firstMessage == null
                    || !firstMessage.startsWith("NAME|")) {
                return;
            }

            name = firstMessage.substring(5).trim();

            if (name.isEmpty()) {
                name = "Guest";
            }

            System.out.println(
                    name + " da ket noi den server.");

            sendRoomList();

            sendMessage(
                    "INFO|Nhap ten phong de tham gia.");

            String message;

            while ((message = in.readLine()) != null) {

                
                if (message.startsWith("JOIN|")) {

                    String selectedRoom =
                            message.substring(5).trim();

                    if (!Server.ROOMS.contains(selectedRoom)) {
                        sendMessage(
                                "ERROR|Phong khong ton tai.");
                        sendRoomList();
                        continue;
                    }

                  
                    if (room != null) {
                        String oldRoom = room;

                        room = null;

                        Server.broadcast(
                                oldRoom,
                                "INFO|" + name
                                + " da roi phong.");
                    }

                    room = selectedRoom;

                    sendMessage("JOINED|" + room);

                    Server.broadcast(
                            room,
                            "INFO|" + name
                            + " da tham gia phong.");

                    System.out.println(
                            name + " tham gia phong " + room);
                }

                
                else if (message.equals("/quit")) {

                    if (room != null) {
                        String oldRoom = room;

                        room = null;

                        Server.broadcast(
                                oldRoom,
                                "INFO|" + name
                                + " da roi phong.");
                    }

                    sendMessage("LEFT|Ban da roi phong.");

                    sendRoomList();

                    sendMessage(
                            "INFO|Nhap ten phong de tham gia.");
                }

           
                else if (message.equals("/exit")) {

                    sendMessage(
                            "INFO|Ngat ket noi thanh cong.");

                    break;
                }

             
                else if (room == null) {

                    sendMessage(
                            "ERROR|Ban chua tham gia phong.");
                }

                
                else if (!message.trim().isEmpty()) {

                    Server.broadcast(
                            room,
                            "MSG|" + name + ": " + message);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Client " + name + " mat ket noi.");

        } finally {

            if (room != null) {
                String oldRoom = room;
                room = null;

                Server.broadcast(
                        oldRoom,
                        "INFO|" + name + " da roi phong.");
            }

            Server.removeClient(this);

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(
                        "Loi khi dong ket noi client.");
            }

            System.out.println(
                    name + " da ngat ket noi.");
        }
    }
}
