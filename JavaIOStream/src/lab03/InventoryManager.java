package lab03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static final Path INVENTORY_FILE =
            Path.of("data", "inventory.csv");

    private static final Path REPORT_FILE =
            Path.of("data", "inventory-report.txt");

    public static void main(String[] args) {

        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(
                        System.in,
                        StandardCharsets.UTF_8));

        List<Product> products = inputProducts(keyboard);

        saveProducts(products);

        List<Product> loadedProducts = loadProducts();

        if (loadedProducts.isEmpty()) {
            System.out.println("Không có sản phẩm để hiển thị.");
            return;
        }

        System.out.println("\nDANH SÁCH SẢN PHẨM");

        displayProducts(loadedProducts);

        double total = calculateTotal(loadedProducts);

        System.out.printf(
                "%nTổng giá trị tồn kho: %,.0f VND%n",
                total);

        Product maxProduct = findMaxInventoryProduct(
                loadedProducts);

        if (maxProduct != null) {
            System.out.println(
                    "Sản phẩm có giá trị tồn kho cao nhất:");
            System.out.println(maxProduct);
        }

        writeReport(
                loadedProducts,
                total,
                maxProduct);
    }

    // Nhập danh sách sản phẩm từ bàn phím
    public static List<Product> inputProducts(
            BufferedReader keyboard) {

        List<Product> products = new ArrayList<>();

        int n = 0;

        while (true) {

            try {

                System.out.print(
                        "Nhập số lượng sản phẩm: ");

                n = Integer.parseInt(
                        keyboard.readLine());

                if (n <= 0) {
                    System.out.println(
                            "Số lượng sản phẩm phải lớn hơn 0.");
                    continue;
                }

                break;

            } catch (IOException e) {

                System.err.println(
                        "Không thể đọc dữ liệu bàn phím: "
                                + e.getMessage());

            } catch (NumberFormatException e) {

                System.err.println(
                        "Vui lòng nhập một số nguyên hợp lệ.");
            }
        }

        for (int i = 0; i < n; i++) {

            while (true) {

                try {

                    System.out.println(
                            "\nNhập sản phẩm thứ " + (i + 1));

                    System.out.print("Mã sản phẩm: ");
                    String code = keyboard.readLine();

                    System.out.print("Tên sản phẩm: ");
                    String name = keyboard.readLine();

                    System.out.print("Đơn giá: ");
                    double price = Double.parseDouble(
                            keyboard.readLine());

                    System.out.print("Số lượng: ");
                    int quantity = Integer.parseInt(
                            keyboard.readLine());

                    Product product = new Product(
                            code,
                            name,
                            price,
                            quantity);

                    products.add(product);

                    break;

                } catch (NumberFormatException e) {

                    System.err.println(
                            "Đơn giá hoặc số lượng không phải số hợp lệ.");

                } catch (IllegalArgumentException e) {

                    System.err.println(
                            "Dữ liệu không hợp lệ: "
                                    + e.getMessage());

                } catch (IOException e) {

                    System.err.println(
                            "Không thể đọc dữ liệu: "
                                    + e.getMessage());
                }

                System.out.println(
                        "Vui lòng nhập lại sản phẩm này.");
            }
        }

        return products;
    }

    // Lưu danh sách sản phẩm vào inventory.csv
    public static void saveProducts(
            List<Product> products) {

        try {

            Files.createDirectories(
                    INVENTORY_FILE.getParent());

            try (BufferedWriter writer =
                    Files.newBufferedWriter(
                            INVENTORY_FILE,
                            StandardCharsets.UTF_8)) {

                writer.write(
                        "ma,ten,donGia,soLuong");

                writer.newLine();

                for (Product product : products) {

                    writer.write(
                            product.getCode()
                            + ","
                            + product.getName()
                            + ","
                            + product.getUnitPrice()
                            + ","
                            + product.getQuantity());

                    writer.newLine();
                }
            }

            System.out.println(
                    "\nĐã lưu dữ liệu vào "
                            + INVENTORY_FILE);

        } catch (IOException e) {

            System.err.println(
                    "Không ghi được tệp "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage());
        }
    }

    // Đọc lại inventory.csv
    public static List<Product> loadProducts() {

        List<Product> products =
                new ArrayList<>();

        try (BufferedReader reader =
                Files.newBufferedReader(
                        INVENTORY_FILE,
                        StandardCharsets.UTF_8)) {

            reader.readLine();

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine())
                    != null) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",", -1);

                if (parts.length != 4) {

                    System.err.println(
                            "Tệp "
                            + INVENTORY_FILE
                            + " - dòng "
                            + lineNumber
                            + " thiếu cột, bỏ qua.");

                    continue;
                }

                try {

                    String code =
                            parts[0].trim();

                    String name =
                            parts[1].trim();

                    double price =
                            Double.parseDouble(
                                    parts[2].trim());

                    int quantity =
                            Integer.parseInt(
                                    parts[3].trim());

                    Product product =
                            new Product(
                                    code,
                                    name,
                                    price,
                                    quantity);

                    products.add(product);

                } catch (NumberFormatException e) {

                    System.err.println(
                            "Tệp "
                            + INVENTORY_FILE
                            + " - dòng "
                            + lineNumber
                            + " có dữ liệu số không hợp lệ.");

                } catch (IllegalArgumentException e) {

                    System.err.println(
                            "Tệp "
                            + INVENTORY_FILE
                            + " - dòng "
                            + lineNumber
                            + " không hợp lệ: "
                            + e.getMessage());
                }
            }

        } catch (NoSuchFileException e) {

            System.err.println(
                    "Không tìm thấy tệp: "
                            + INVENTORY_FILE);

        } catch (IOException e) {

            System.err.println(
                    "Không đọc được tệp "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage());
        }

        return products;
    }

    // Hiển thị danh sách
    public static void displayProducts(
            List<Product> products) {

        for (Product product : products) {
            System.out.println(product);
        }
    }

    // Tính tổng giá trị tồn kho
    public static double calculateTotal(
            List<Product> products) {

        double total = 0;

        for (Product product : products) {
            total += product.inventoryValue();
        }

        return total;
    }

    // Tìm sản phẩm có giá trị tồn kho cao nhất
    public static Product findMaxInventoryProduct(
            List<Product> products) {

        if (products.isEmpty()) {
            return null;
        }

        Product max = products.get(0);

        for (Product product : products) {

            if (product.inventoryValue()
                    > max.inventoryValue()) {

                max = product;
            }
        }

        return max;
    }

    // Ghi báo cáo tổng hợp
    public static void writeReport(
            List<Product> products,
            double total,
            Product maxProduct) {

        try (BufferedWriter writer =
                Files.newBufferedWriter(
                        REPORT_FILE,
                        StandardCharsets.UTF_8)) {

            writer.write(
                    "BÁO CÁO TỒN KHO");

            writer.newLine();

            writer.write(
                    "Số sản phẩm: "
                            + products.size());

            writer.newLine();

            writer.write(
                    "Tổng giá trị tồn kho: %,.0f VND"
                            .formatted(total));

            writer.newLine();

            if (maxProduct != null) {

                writer.write(
                        "Sản phẩm có giá trị tồn kho cao nhất: "
                                + maxProduct.getCode()
                                + " - "
                                + maxProduct.getName()
                                + " - %,.0f VND"
                                .formatted(
                                        maxProduct.inventoryValue()));

                writer.newLine();
            }

            System.out.println(
                    "\nĐã ghi báo cáo vào "
                            + REPORT_FILE);

        } catch (IOException e) {

            System.err.println(
                    "Không ghi được tệp "
                            + REPORT_FILE
                            + ": "
                            + e.getMessage());
        }
    }
}