package lab9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server9 {

    public static void main(String[] args) {
        try {
            AuctionService service =
                    new AuctionServiceImpl();

            Registry registry =
                    LocateRegistry.createRegistry(1109);

            registry.rebind("AuctionService", service);

            System.out.println("Auction Server is running...");
            System.out.println("Waiting for Client...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}