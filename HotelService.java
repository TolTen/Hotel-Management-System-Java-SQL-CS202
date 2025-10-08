package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelService {

    public void listHotels() {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "SELECT * FROM hotels";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Hotel ID: " + resultSet.getInt("hotel_id"));
                System.out.println("Hotel Name: " + resultSet.getString("hotel_name"));
                System.out.println("City: " + resultSet.getString("city"));
                System.out.println("Contact: " + resultSet.getString("contact_number"));
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error listing hotels: " + e.getMessage());
        }
    }

    public int addHotel(String hotelName, String address, String city, String contact, String email) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return -1;

        String query = "INSERT INTO Hotel (hotelName, address, city, contactNumber, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hotelName);
            statement.setString(2, address);
            statement.setString(3, city);
            statement.setString(4, contact);
            statement.setString(5, email);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding hotel: " + e.getMessage());
        }
        return -1;
    }



    public String viewAllHotels() {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return "Failed to connect to the database.";

        StringBuilder hotels = new StringBuilder("Available Hotels:\n");
        String query = "SELECT * FROM Hotel";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotels.append("Hotel ID: ").append(resultSet.getInt("hotel_ID"))
                        .append(", Name: ").append(resultSet.getString("hotelName"))
                        .append(", City: ").append(resultSet.getString("city"))
                        .append(", Contact: ").append(resultSet.getString("contactNumber"))
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Error fetching hotels: " + e.getMessage();
        }
        return hotels.toString();
    }
}

