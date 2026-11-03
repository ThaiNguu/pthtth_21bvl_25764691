package lab8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class FlightServiceImpl extends UnicastRemoteObject
        implements FlightService {

    private HashMap<String, Integer> flights;
    private HashMap<String, Integer> capacity;

    public FlightServiceImpl() throws RemoteException {
        super();

        flights = new HashMap<>();
        capacity = new HashMap<>();

        flights.put("VN001", 100);
        flights.put("VN002", 80);
        flights.put("VN003", 50);

        capacity.put("VN001", 100);
        capacity.put("VN002", 80);
        capacity.put("VN003", 50);
    }

    @Override
    public synchronized boolean bookTicket(
            String flightNumber, int seats)
            throws RemoteException {

        if (!flights.containsKey(flightNumber)
                || seats <= 0
                || seats > flights.get(flightNumber)) {
            return false;
        }

        flights.put(flightNumber,
                flights.get(flightNumber) - seats);

        return true;
    }

    @Override
    public synchronized int availableSeats(String flightNumber)
            throws RemoteException {

        if (!flights.containsKey(flightNumber)) {
            return -1;
        }

        return flights.get(flightNumber);
    }

    @Override
    public synchronized boolean cancelBooking(
            String flightNumber, int seats)
            throws RemoteException {

        if (!flights.containsKey(flightNumber)
                || seats <= 0
                || seats > capacity.get(flightNumber)
                        - flights.get(flightNumber)) {
            return false;
        }

        flights.put(flightNumber,
                flights.get(flightNumber) + seats);

        return true;
    }
}