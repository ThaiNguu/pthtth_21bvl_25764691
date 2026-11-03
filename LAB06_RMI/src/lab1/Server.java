package lab1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            HelloService service = new HelloServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("HelloService", service);

            System.out.println("Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}