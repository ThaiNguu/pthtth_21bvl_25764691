
package lab08;

import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String URL = "jdbc:sqlite:nhanvien.db";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            // B1: Nap JDBC Driver
            Class.forName("org.sqlite.JDBC");

            // B2: Ket noi database
            try (Connection conn = DriverManager.getConnection(URL)) {

                System.out.println("Ket noi SQLite thanh cong!");

                // B3: Tao bang
                createTable(conn);

                // Them du lieu mau neu bang dang rong
                insertSampleData(conn);

                // Menu chuong trinh
                int choice;

                do {
                    System.out.println("\n===== QUAN LY NHAN VIEN =====");
                    System.out.println("1. Them nhan vien");
                    System.out.println("2. Hien thi danh sach");
                    System.out.println("3. Tim kiem nhan vien");
                    System.out.println("4. Cap nhat nhan vien");
                    System.out.println("5. Xoa nhan vien");
                    System.out.println("0. Thoat");
                    System.out.print("Nhap lua chon: ");

                    choice = Integer.parseInt(sc.nextLine());

                    switch (choice) {
                        case 1:
                            insertEmployee(conn);
                            break;
                        case 2:
                            displayEmployees(conn);
                            break;
                        case 3:
                            searchEmployee(conn);
                            break;
                        case 4:
                            updateEmployee(conn);
                            break;
                        case 5:
                            deleteEmployee(conn);
                            break;
                        case 0:
                            System.out.println("Da thoat chuong trinh!");
                            break;
                        default:
                            System.out.println("Lua chon khong hop le!");
                    }

                } while (choice != 0);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay SQLite JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Loi SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Vui long nhap so hop le!");
        }
    }

    // Tao bang NhanVien
    public static void createTable(Connection conn)
            throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS NhanVien ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ten TEXT NOT NULL, "
                + "chuc_vu TEXT)";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tao bang NhanVien thanh cong!");
        }
    }

    // Them 3 nhan vien mau bang Transaction
    public static void insertSampleData(Connection conn)
            throws SQLException {

        String check = "SELECT COUNT(*) FROM NhanVien";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(check)) {

            if (rs.next() && rs.getInt(1) == 0) {

                String sql = "INSERT INTO NhanVien(ten, chuc_vu) VALUES (?, ?)";

                conn.setAutoCommit(false);

                try (PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setString(1, "Nguyen Van An");
                    ps.setString(2, "Giam doc");
                    ps.executeUpdate();

                    ps.setString(1, "Tran Van Binh");
                    ps.setString(2, "Nhan vien");
                    ps.executeUpdate();

                    ps.setString(1, "Le Thi Hoa");
                    ps.setString(2, "Ke toan");
                    ps.executeUpdate();

                    conn.commit();
                    System.out.println("Them 3 nhan vien mau thanh cong!");

                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }
    }

    // Them nhan vien
    public static void insertEmployee(Connection conn)
            throws SQLException {

        System.out.print("Nhap ten: ");
        String ten = sc.nextLine();

        System.out.print("Nhap chuc vu: ");
        String chucVu = sc.nextLine();

        String sql = "INSERT INTO NhanVien(ten, chuc_vu) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setString(2, chucVu);

            ps.executeUpdate();

            System.out.println("Them nhan vien thanh cong!");
        }
    }

    // Hien thi danh sach
    public static void displayEmployees(Connection conn)
            throws SQLException {

        String sql = "SELECT * FROM NhanVien";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n===== DANH SACH NHAN VIEN =====");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Ten: " + rs.getString("ten"));
                System.out.println("Chuc vu: " + rs.getString("chuc_vu"));
                System.out.println("------------------------");
            }
        }
    }

    // Tim kiem nhan vien
    public static void searchEmployee(Connection conn)
            throws SQLException {

        System.out.print("Nhap ten can tim: ");
        String ten = sc.nextLine();

        String sql = "SELECT * FROM NhanVien WHERE ten LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ten + "%");

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Ten: " + rs.getString("ten"));
                    System.out.println("Chuc vu: " + rs.getString("chuc_vu"));
                }

                if (!found) {
                    System.out.println("Khong co du lieu!");
                }
            }
        }
    }

    // Cap nhat nhan vien
    public static void updateEmployee(Connection conn)
            throws SQLException {

        System.out.print("Nhap ID can cap nhat: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nhap ten moi: ");
        String ten = sc.nextLine();

        System.out.print("Nhap chuc vu moi: ");
        String chucVu = sc.nextLine();

        String sql = "UPDATE NhanVien SET ten = ?, chuc_vu = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.setString(2, chucVu);
            ps.setInt(3, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Cap nhat thanh cong!");
            } else {
                System.out.println("Khong tim thay nhan vien!");
            }
        }
    }

    // Xoa nhan vien
    public static void deleteEmployee(Connection conn)
            throws SQLException {

        System.out.print("Nhap ID can xoa: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM NhanVien WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Xoa thanh cong!");
            } else {
                System.out.println("Khong tim thay nhan vien!");
            }
        }
    }
}
