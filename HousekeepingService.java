package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HousekeepingService {
    public void viewAssignedTasks(int employeeID) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return;

        String query = "SELECT * FROM housekeeping_schedule WHERE housekeepers_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Task ID: " + resultSet.getInt("task_id") +
                        ", Room ID: " + resultSet.getInt("room_id") +
                        ", Schedule Date: " + resultSet.getString("schedule_date") +
                        ", Status: " + resultSet.getString("housekeeping_status"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing tasks: " + e.getMessage());
        }
    }

    public void updateTaskStatus(int taskID, String status) {
        if (!status.equalsIgnoreCase("SCHEDULED") &&
                !status.equalsIgnoreCase("COMPLETED") &&
                !status.equalsIgnoreCase("MISSED")) {
            System.out.println("Invalid status. Status must be 'SCHEDULED', 'COMPLETED', or 'MISSED'.");
            return;
        }

        Connection connection = DatabaseConnection.connect();
        if (connection == null) {
            System.out.println("Failed to connect to the database.");
            return;
        }

        String query = "UPDATE housekeeping_schedule SET housekeeping_status = ? WHERE task_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.toUpperCase());
            statement.setInt(2, taskID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Task status updated to: " + status.toUpperCase());
            } else {
                System.out.println("Task ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating task status: " + e.getMessage());
        }
    }

}

