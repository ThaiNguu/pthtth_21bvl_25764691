package lab5;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {

    void sendMessage(String message)
            throws RemoteException;

    void registerClient(ClientCallback client)
            throws RemoteException;
}