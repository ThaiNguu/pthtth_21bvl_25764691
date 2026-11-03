package lab9;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionService extends Remote {

    boolean addProduct(String name, double startingPrice)
            throws RemoteException;

    String listProducts()
            throws RemoteException;

    boolean bid(String productName, double price)
            throws RemoteException;
}