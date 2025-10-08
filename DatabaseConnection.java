package HotelManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hotelmanagementsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Veritabanına başarıyla bağlanıldı!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Veritabanına bağlanılamadı!");
            e.printStackTrace();
            return null;
        }
    }
}

