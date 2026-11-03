package lab2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddServiceImpl extends UnicastRemoteObject
        implements AddService {

    public AddServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}