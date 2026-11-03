package lab6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client6 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1105);

            BankService service =
                    (BankService) registry.lookup("BankService");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== BANKING SYSTEM =====");
                System.out.println("1. Kiem tra so du");
                System.out.println("2. Gui tien");
                System.out.println("3. Rut tien");
                System.out.println("0. Thoat");
                System.out.print("Nhap lua chon: ");

                choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        System.out.println(
                                "So du: " + service.getBalance());
                        break;

                    case 2:
                        System.out.print("Nhap so tien gui: ");
                        double amount = sc.nextDouble();

                        service.deposit(amount);

                        if (amount > 0) {
                            System.out.println("Gui tien thanh cong!");
                        } else {
                            System.out.println(
                                    "So tien khong hop le!");
                        }

                        break;

                    case 3:
                        System.out.print("Nhap so tien rut: ");
                        amount = sc.nextDouble();

                        boolean result =
                                service.withdraw(amount);

                        if (result) {
                            System.out.println("Rut tien thanh cong!");
                        } else {
                            System.out.println(
                                    "Rut tien that bai! So du khong du.");
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