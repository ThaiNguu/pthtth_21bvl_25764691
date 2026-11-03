package lab4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client4 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1103);

            ContactService service =
                    (ContactService) registry.lookup("ContactService");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== QUAN LY DANH BA =====");
                System.out.println("1. Them lien he");
                System.out.println("2. Tim lien he");
                System.out.println("3. Sua so dien thoai");
                System.out.println("4. Xoa lien he");
                System.out.println("0. Thoat");
                System.out.print("Nhap lua chon: ");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        System.out.print("Nhap ten: ");
                        String name = sc.nextLine();

                        System.out.print("Nhap so dien thoai: ");
                        String phone = sc.nextLine();

                        service.addContact(name, phone);

                        System.out.println("Them lien he thanh cong!");
                        break;

                    case 2:
                        System.out.print("Nhap ten can tim: ");
                        name = sc.nextLine();

                        phone = service.findContact(name);

                        if (phone != null) {
                            System.out.println(
                                    "So dien thoai: " + phone);
                        } else {
                            System.out.println(
                                    "Khong tim thay lien he!");
                        }
                        break;

                    case 3:
                        System.out.print("Nhap ten can sua: ");
                        name = sc.nextLine();

                        System.out.print("Nhap so dien thoai moi: ");
                        phone = sc.nextLine();

                        boolean updated =
                                service.updateContact(name, phone);

                        if (updated) {
                            System.out.println("Cap nhat thanh cong!");
                        } else {
                            System.out.println(
                                    "Khong tim thay lien he!");
                        }
                        break;

                    case 4:
                        System.out.print("Nhap ten can xoa: ");
                        name = sc.nextLine();

                        boolean deleted =
                                service.deleteContact(name);

                        if (deleted) {
                            System.out.println("Xoa thanh cong!");
                        } else {
                            System.out.println(
                                    "Khong tim thay lien he!");
                        }
                        break;

                    case 0:
                        System.out.println("Ket thuc chuong trinh.");
                        break;

                    default:
                        System.out.println("Lua chon khong hop le!");
                }

            } while (choice != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}