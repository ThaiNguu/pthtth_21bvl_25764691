package lab5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject
        implements ChatService {

    private ClientCallback client;

    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerClient(ClientCallback client)
            throws RemoteException {
        this.client = client;
    }

    @Override
    public void sendMessage(String message)
            throws RemoteException {

        System.out.println("Client: " + message);

        String response =
                "Server: Toi da nhan duoc tin nhan!";

        if (client != null) {
            client.receiveMessage(response);
        }
    }
}