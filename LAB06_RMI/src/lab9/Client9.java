package lab9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client9 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1109);

            AuctionService service =
                    (AuctionService) registry.lookup("AuctionService");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== ONLINE AUCTION =====");
                System.out.println("1. Them san pham (Seller)");
                System.out.println("2. Xem san pham (Bidder)");
                System.out.println("3. Dat gia thau (Bidder)");
                System.out.println("0. Thoat");
                System.out.print("Nhap lua chon: ");

                choice = sc.nextInt();
                sc.nextLine();

                String name;
                double price;
                boolean result;

                switch (choice) {

                    case 1:
                        System.out.print("Nhap ten san pham: ");
                        name = sc.nextLine();

                        System.out.print("Nhap gia khoi diem: ");
                        price = sc.nextDouble();

                        result = service.addProduct(name, price);

                        if (result) {
                            System.out.println(
                                    "Them san pham thanh cong!");
                        } else {
                            System.out.println(
                                    "Them san pham that bai!");
                        }
                        break;

                    case 2:
                        System.out.println(
                                service.listProducts());
                        break;

                    case 3:
                        System.out.print("Nhap ten san pham: ");
                        name = sc.nextLine();

                        System.out.print("Nhap gia thau: ");
                        price = sc.nextDouble();

                        result = service.bid(name, price);

                        if (result) {
                            System.out.println(
                                    "Dat gia thau thanh cong!");
                        } else {
                            System.out.println(
                                    "Gia thau khong hop le!");
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