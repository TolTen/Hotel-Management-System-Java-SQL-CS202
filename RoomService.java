package HotelManagementSystem;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomService {


    public void listAvailableRooms() {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "SELECT * FROM rooms WHERE room_status = 'available'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Room ID: " + resultSet.getInt("room_id"));
                System.out.println("Room Type: " + resultSet.getString("room_type"));
                System.out.println("Capacity: " + resultSet.getInt("capacity"));
                System.out.println("Price: $" + resultSet.getDouble("price"));
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error listing available rooms: " + e.getMessage());
        }
    }

    // 2. Yeni oda ekleme (Admin yetkisi)
    public int addRoom(int hotelID, String roomName, String roomType, int capacity, double price, String status, int floorNumber) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return -1;

        String query = "INSERT INTO Room (hotel_ID, roomName, roomType, capacity, price, room_status, floorNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, hotelID);
            statement.setString(2, roomName);
            statement.setString(3, roomType);
            statement.setInt(4, capacity);
            statement.setDouble(5, price);
            statement.setString(6, status);
            statement.setInt(7, floorNumber);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Yeni oda ID'sini döndür
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }




    public String viewAllRooms() {
        StringBuilder rooms = new StringBuilder("Rooms List:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT room_ID, roomType, room_status FROM Room";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    rooms.append("Room ID: ").append(resultSet.getInt("room_ID"))
                            .append(", Type: ").append(resultSet.getString("roomType"))
                            .append(", Status: ").append(resultSet.getString("room_status"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            return "Error fetching rooms: " + e.getMessage();
        }
        return rooms.toString();
    }


    public void updateRoomStatus(int roomID, String status) {
        // Geçerli değerleri kontrol et
        if (!status.equalsIgnoreCase("available") &&
                !status.equalsIgnoreCase("occupied") &&
                !status.equalsIgnoreCase("cleaning")) {
            JOptionPane.showMessageDialog(null, "Invalid status. Must be 'available', 'occupied', or 'cleaning'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "UPDATE Room SET room_status = ? WHERE room_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.toLowerCase()); // Format kontrolü
            statement.setInt(2, roomID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Room status updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Room ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating room status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String viewAvailableRooms() {
        StringBuilder rooms = new StringBuilder("Available Rooms:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT room_ID, roomType, floorNumber, capacity, price FROM Room WHERE room_status = 'available'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    rooms.append("Room ID: ").append(resultSet.getInt("room_ID"))
                            .append(", Type: ").append(resultSet.getString("roomType"))
                            .append(", Floor: ").append(resultSet.getInt("floorNumber"))
                            .append(", Capacity: ").append(resultSet.getInt("capacity"))
                            .append(", Price: $").append(resultSet.getDouble("price"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            return "Error fetching available rooms: " + e.getMessage();
        }
        return rooms.toString();
    }



    public boolean isRoomAvailable(int roomID) {
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return false;

            String query = "SELECT room_status FROM Room WHERE room_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomID);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return "available".equalsIgnoreCase(resultSet.getString("room_status"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking room availability: " + e.getMessage());
        }
        return false;
    }



    public void updateRoomAvailabilityBasedOnTasks(int roomID) {
        TaskService taskService = new TaskService();
        boolean hasPendingTask = taskService.isTaskPendingForRoom(roomID);

        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return;

            String query = "UPDATE Room SET room_status = ? WHERE room_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, hasPendingTask ? "not_available" : "available");
                statement.setInt(2, roomID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating room availability: " + e.getMessage());
        }
    }


    public void deleteRoom(int parseInt) {
    }
}

