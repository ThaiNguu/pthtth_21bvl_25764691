package lab2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client2 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1100);

            AddService service =
                    (AddService) registry.lookup("AddService");

            Scanner sc = new Scanner(System.in);

            System.out.print("Nhap a: ");
            int a = sc.nextInt();

            System.out.print("Nhap b: ");
            int b = sc.nextInt();

            int result = service.add(a, b);

            System.out.println("Tong a + b = " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}