package lab6;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankServiceImpl extends UnicastRemoteObject
        implements BankService {

    private double balance = 1000000;

    public BankServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized double getBalance()
            throws RemoteException {
        return balance;
    }

    @Override
    public synchronized void deposit(double amount)
            throws RemoteException {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public synchronized boolean withdraw(double amount)
            throws RemoteException {

        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }

        return false;
    }
}