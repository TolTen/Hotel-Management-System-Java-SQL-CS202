package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CleaningHistoryService {

    public void addCleaningHistory(int roomID, int employeeID, String cleaningDate, String cleaningStatus) {
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) {
                System.out.println("Failed to connect to the database.");
                return;
            }

            String query = "INSERT INTO cleaninghistory (room_id, employee_id, cleaningdate, cleaning_status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomID);
                statement.setInt(2, employeeID);
                statement.setString(3, cleaningDate);
                statement.setString(4, cleaningStatus);

                statement.executeUpdate();
                System.out.println("Cleaning record added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding cleaning record: " + e.getMessage());
        }
    }

    public String viewCleaningHistoryByRoom(int roomID) {
        StringBuilder history = new StringBuilder("Cleaning History for Room ID: " + roomID + "\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT * FROM cleaninghistory WHERE room_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomID);

                ResultSet resultSet = statement.executeQuery();
                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    history.append("Cleaning ID: ").append(resultSet.getInt("cleaning_id"))
                            .append(", Employee ID: ").append(resultSet.getInt("employee_id"))
                            .append(", Date: ").append(resultSet.getString("cleaningdate"))
                            .append(", Status: ").append(resultSet.getString("cleaning_status"))
                            .append("\n");
                }
                if (!found) {
                    return "No cleaning history found for Room ID: " + roomID;
                }
            }
        } catch (SQLException e) {
            return "Error fetching cleaning history for room: " + e.getMessage();
        }
        return history.toString();
    }


    public String viewAllCleaningHistory() {
        StringBuilder history = new StringBuilder("All Cleaning History Records:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT * FROM cleaninghistory";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    history.append("Cleaning ID: ").append(resultSet.getInt("cleaning_id"))
                            .append(", Room ID: ").append(resultSet.getInt("room_id"))
                            .append(", Employee ID: ").append(resultSet.getInt("employee_id"))
                            .append(", Date: ").append(resultSet.getString("cleaningdate"))
                            .append(", Status: ").append(resultSet.getString("cleaning_status"))
                            .append("\n");
                }
                if (!found) {
                    return "No cleaning history records found.";
                }
            }
        } catch (SQLException e) {
            return "Error fetching all cleaning history: " + e.getMessage();
        }
        return history.toString();
    }

}


