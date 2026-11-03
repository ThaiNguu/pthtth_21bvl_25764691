package lab5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server5 {

    public static void main(String[] args) {
        try {
            ChatService service = new ChatServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1104);

            registry.rebind("ChatService", service);

            System.out.println("Chat Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}