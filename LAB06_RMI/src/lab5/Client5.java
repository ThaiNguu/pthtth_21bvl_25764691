package lab5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client5 extends UnicastRemoteObject
        implements ClientCallback {

    public Client5() throws RemoteException {
        super();
    }

    @Override
    public void receiveMessage(String message)
            throws RemoteException {
        System.out.println(message);
    }

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1104);

            ChatService service =
                    (ChatService) registry.lookup("ChatService");

            Client5 client = new Client5();

            service.registerClient(client);

            Scanner sc = new Scanner(System.in);

            System.out.println("===== CHAT CLIENT =====");

            while (true) {
                System.out.print("Client: ");
                String message = sc.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Ket thuc chat.");
                    break;
                }

                service.sendMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}