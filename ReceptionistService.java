package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceptionistService {

    public void viewAvailableRooms() {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "SELECT * FROM rooms WHERE room_status = 'available'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Available Rooms:");
            while (resultSet.next()) {
                System.out.println("Room ID: " + resultSet.getInt("room_id") +
                        ", Room Type: " + resultSet.getString("room_type") +
                        ", Price: $" + resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing available rooms: " + e.getMessage());
        }
    }


    public void updateBookingStatus(int bookingID, String status) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "UPDATE bookings SET booking_status = ? WHERE booking_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setInt(2, bookingID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking updated to: " + status);
            } else {
                System.out.println("Booking ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating booking status: " + e.getMessage());
        }
    }


    public void assignHousekeepingTask(int roomID, int employeeID, String scheduleDate) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "INSERT INTO housekeeping_schedule (room_id, housekeepers_id, schedule_date, housekeeping_status) VALUES (?, ?, ?, 'scheduled')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomID);
            statement.setInt(2, employeeID);
            statement.setString(3, scheduleDate);

            statement.executeUpdate();
            System.out.println("Housekeeping task has been scheduled successfully.");
        } catch (SQLException e) {
            System.out.println("Error assigning housekeeping task: " + e.getMessage());
        }
    }
}

