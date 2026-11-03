package lab9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class AuctionServiceImpl extends UnicastRemoteObject
        implements AuctionService {

    private HashMap<String, Double> products;

    public AuctionServiceImpl() throws RemoteException {
        super();

        products = new HashMap<>();
    }

    @Override
    public synchronized boolean addProduct(
            String name, double startingPrice)
            throws RemoteException {

        if (name == null || name.trim().isEmpty()
                || startingPrice <= 0
                || products.containsKey(name)) {
            return false;
        }

        products.put(name, startingPrice);

        return true;
    }

    @Override
    public synchronized String listProducts()
            throws RemoteException {

        if (products.isEmpty()) {
            return "Chua co san pham dau gia.";
        }

        String result = "";

        for (String name : products.keySet()) {
            result += name + " - Gia hien tai: "
                    + products.get(name) + " VND\n";
        }

        return result;
    }

    @Override
    public synchronized boolean bid(
            String productName, double price)
            throws RemoteException {

        if (!products.containsKey(productName)
                || price <= products.get(productName)) {
            return false;
        }

        products.put(productName, price);

        return true;
    }
}