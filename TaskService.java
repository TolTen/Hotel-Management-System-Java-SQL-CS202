package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskService {

    public int addTask(String taskType, int roomID, int employeeID, String scheduleDate, String status) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return -1;

        String query = "INSERT INTO Task (taskType, room_ID, employee_ID, scheduleDate, task_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, taskType);
            statement.setInt(2, roomID);
            statement.setInt(3, employeeID);
            statement.setString(4, scheduleDate);
            statement.setString(5, status);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
        return -1;
    }


    public String viewAllTasks() {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return "Failed to connect to the database.";

        StringBuilder tasks = new StringBuilder("Tasks:\n");
        String query = "SELECT * FROM Task";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tasks.append("Task ID: ").append(resultSet.getInt("task_ID"))
                        .append(", Type: ").append(resultSet.getString("taskType"))
                        .append(", Room ID: ").append(resultSet.getInt("room_ID"))
                        .append(", Employee ID: ").append(resultSet.getInt("employee_ID"))
                        .append(", Schedule Date: ").append(resultSet.getString("scheduleDate"))
                        .append(", Status: ").append(resultSet.getString("task_status"))
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Error fetching tasks: " + e.getMessage();
        }
        return tasks.toString();
    }


    public boolean updateTaskStatus(int taskID, String newStatus) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return false;

        String query = "UPDATE Task SET task_status = ? WHERE task_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, taskID);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating task status: " + e.getMessage());
            return false;
        }
    }


    public String viewAssignedTasks(int employeeID) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return "Failed to connect to the database.";

        StringBuilder tasks = new StringBuilder("Assigned Tasks:\n");
        String query = "SELECT * FROM Task WHERE employee_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tasks.append("Task ID: ").append(resultSet.getInt("task_ID"))
                        .append(", Type: ").append(resultSet.getString("taskType"))
                        .append(", Room ID: ").append(resultSet.getInt("room_ID"))
                        .append(", Schedule Date: ").append(resultSet.getString("scheduleDate"))
                        .append(", Status: ").append(resultSet.getString("task_status"))
                        .append("\n");
            }
        } catch (SQLException e) {
            return "Error fetching assigned tasks: " + e.getMessage();
        }
        return tasks.toString();
    }


    public boolean isTaskPendingForRoom(int roomID) {
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return true;

            String query = "SELECT COUNT(*) AS taskCount FROM Task WHERE room_ID = ? AND task_status != 'completed'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, roomID);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("taskCount") > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking task status for room: " + e.getMessage());
        }
        return true;
    }






}

