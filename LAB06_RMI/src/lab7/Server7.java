package lab7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server7 {

    public static void main(String[] args) {
        try {
            AreaService service = new AreaServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1106);

            registry.rebind("AreaService", service);

            System.out.println("Area Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}