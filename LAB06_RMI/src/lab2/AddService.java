package lab2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddService extends Remote {
    int add(int a, int b) throws RemoteException;
}