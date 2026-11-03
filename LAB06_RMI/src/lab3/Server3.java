package lab3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server3 {

    public static void main(String[] args) {
        try {
            PrimeService service = new PrimeServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1101);

            registry.rebind("PrimeService", service);

            System.out.println("Prime Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}