package lab8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server8 {

    public static void main(String[] args) {
        try {
            FlightService service =
                    new FlightServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1107);

            registry.rebind("FlightService", service);

            System.out.println("Flight Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}