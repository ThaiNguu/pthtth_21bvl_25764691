package lab3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client3 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1101);

            PrimeService service =
                    (PrimeService) registry.lookup("PrimeService");

            Scanner sc = new Scanner(System.in);

            System.out.print("Nhap n: ");
            int n = sc.nextInt();

            boolean result = service.isPrime(n);

            if (result) {
                System.out.println(n + " la so nguyen to");
            } else {
                System.out.println(n + " khong phai so nguyen to");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}