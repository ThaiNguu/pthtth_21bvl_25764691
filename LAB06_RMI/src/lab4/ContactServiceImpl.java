package lab4;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ContactServiceImpl extends UnicastRemoteObject
        implements ContactService {

    private HashMap<String, String> contacts;

    public ContactServiceImpl() throws RemoteException {
        super();
        contacts = new HashMap<>();
    }

    @Override
    public synchronized void addContact(String name, String phone)
            throws RemoteException {
        contacts.put(name, phone);
    }

    @Override
    public synchronized String findContact(String name)
            throws RemoteException {
        return contacts.get(name);
    }

    @Override
    public synchronized boolean deleteContact(String name)
            throws RemoteException {
        return contacts.remove(name) != null;
    }

    @Override
    public synchronized boolean updateContact(String name, String newPhone)
            throws RemoteException {
        if (contacts.containsKey(name)) {
            contacts.put(name, newPhone);
            return true;
        }

        return false;
    }
}