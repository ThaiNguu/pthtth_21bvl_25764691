package lab6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server6 {

    public static void main(String[] args) {
        try {
            BankService service = new BankServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1105);

            registry.rebind("BankService", service);

            System.out.println("Bank Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}