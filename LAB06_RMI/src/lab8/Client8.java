package lab8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client8 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1107);

            FlightService service =
                    (FlightService) registry.lookup("FlightService");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== FLIGHT BOOKING =====");
                System.out.println("1. Dat ve");
                System.out.println("2. Kiem tra ghe trong");
                System.out.println("3. Huy ve");
                System.out.println("0. Thoat");
                System.out.print("Nhap lua chon: ");

                choice = sc.nextInt();

                String flightNumber;
                int seats;
                boolean result;

                switch (choice) {

                    case 1:
                        System.out.print("Nhap ma chuyen bay: ");
                        flightNumber = sc.next();

                        System.out.print("Nhap so ve: ");
                        seats = sc.nextInt();

                        result = service.bookTicket(
                                flightNumber, seats);

                        if (result) {
                            System.out.println("Dat ve thanh cong!");
                        } else {
                            System.out.println(
                                    "Dat ve that bai!");
                        }
                        break;

                    case 2:
                        System.out.print("Nhap ma chuyen bay: ");
                        flightNumber = sc.next();

                        int available =
                                service.availableSeats(flightNumber);

                        if (available >= 0) {
                            System.out.println(
                                    "So ghe con trong: " + available);
                        } else {
                            System.out.println(
                                    "Khong tim thay chuyen bay!");
                        }
                        break;

                    case 3:
                        System.out.print("Nhap ma chuyen bay: ");
                        flightNumber = sc.next();

                        System.out.print("Nhap so ve huy: ");
                        seats = sc.nextInt();

                        result = service.cancelBooking(
                                flightNumber, seats);

                        if (result) {
                            System.out.println("Huy ve thanh cong!");
                        } else {
                            System.out.println(
                                    "Huy ve that bai!");
                        }
                        break;

                    case 0:
                        System.out.println(
                                "Ket thuc chuong trinh.");
                        break;

                    default:
                        System.out.println(
                                "Lua chon khong hop le!");
                }

            } while (choice != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}