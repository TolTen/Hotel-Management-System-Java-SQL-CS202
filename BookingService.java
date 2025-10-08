package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingService {
    public void addBooking(int guestID, int roomID, String startDate, String endDate, int numberOfGuests) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "INSERT INTO bookings (guest_id, room_id, start_date, end_date, number_of_guests, status) VALUES (?, ?, ?, ?, ?, 'pending')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, guestID);
            statement.setInt(2, roomID);
            statement.setString(3, startDate);
            statement.setString(4, endDate);
            statement.setInt(5, numberOfGuests);
            statement.executeUpdate();

            System.out.println("Booking request has been submitted successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding booking: " + e.getMessage());
        }
    }

    public String viewBookings(int guestID) {
        StringBuilder bookings = new StringBuilder("My Bookings:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT b.booking_ID, r.roomType, b.startDate, b.endDate, b.booking_status " +
                    "FROM Booking b JOIN Room r ON b.room_ID = r.room_ID WHERE b.user_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, guestID);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    bookings.append("Booking ID: ").append(resultSet.getInt("booking_ID"))
                            .append(", Room Type: ").append(resultSet.getString("roomType"))
                            .append(", Start Date: ").append(resultSet.getString("startDate"))
                            .append(", End Date: ").append(resultSet.getString("endDate"))
                            .append(", Status: ").append(resultSet.getString("booking_status"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            return "Error fetching bookings: " + e.getMessage();
        }
        return bookings.toString();
    }



    public void cancelBooking(int bookingID) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "UPDATE bookings SET status = 'cancelled' WHERE booking_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingID);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Booking has been cancelled successfully.");
            } else {
                System.out.println("Booking ID not found or already cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }
}

