package lab4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server4 {

    public static void main(String[] args) {
        try {
            ContactService service = new ContactServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1103);

            registry.rebind("ContactService", service);

            System.out.println("Contact Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}