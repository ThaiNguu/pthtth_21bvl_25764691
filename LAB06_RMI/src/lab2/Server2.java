package lab2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server2 {

    public static void main(String[] args) {
        try {
            AddService service = new AddServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1100);

            registry.rebind("AddService", service);

            System.out.println("Add Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}