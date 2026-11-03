package lab7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client7 {

    public static void main(String[] args) {
        try {
            Registry registry =
                    LocateRegistry.getRegistry("localhost", 1106);

            AreaService service =
                    (AreaService) registry.lookup("AreaService");

            Scanner sc = new Scanner(System.in);

            int choice;

            do {
                System.out.println("\n===== AREA CALCULATOR =====");
                System.out.println("1. Hinh chu nhat");
                System.out.println("2. Hinh tron");
                System.out.println("3. Hinh tam giac");
                System.out.println("0. Thoat");
                System.out.print("Nhap lua chon: ");

                choice = sc.nextInt();

                double result;

                switch (choice) {

                    case 1:
                        System.out.print("Nhap chieu rong: ");
                        double width = sc.nextDouble();

                        System.out.print("Nhap chieu cao: ");
                        double height = sc.nextDouble();

                        result = service.rectangleArea(width, height);

                        System.out.println(
                                "Dien tich hinh chu nhat: " + result);
                        break;

                    case 2:
                        System.out.print("Nhap ban kinh: ");
                        double radius = sc.nextDouble();

                        result = service.circleArea(radius);

                        System.out.println(
                                "Dien tich hinh tron: " + result);
                        break;

                    case 3:
                        System.out.print("Nhap canh day: ");
                        double base = sc.nextDouble();

                        System.out.print("Nhap chieu cao: ");
                        height = sc.nextDouble();

                        result = service.triangleArea(base, height);

                        System.out.println(
                                "Dien tich hinh tam giac: " + result);
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