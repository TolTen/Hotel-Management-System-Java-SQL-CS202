package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Employee extends User {
    private int employeeID;
    private String role;

    public Employee(String userName, String email, String password, int userID, int employeeID, String role) {
        super(userName, email, password, userID);
        this.employeeID = employeeID;
        this.role = role;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getRole() {
        return role;
    }

    public static String viewAllEmployees() {
        StringBuilder employees = new StringBuilder("All Employees:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT employee_ID, employeeName, jobRole FROM Employee";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    employees.append("Employee ID: ").append(resultSet.getInt("employee_ID"))
                            .append(", Name: ").append(resultSet.getString("employeeName"))
                            .append(", Role: ").append(resultSet.getString("jobRole"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            return "Error fetching employees: " + e.getMessage();
        }
        return employees.toString();
    }


    public static String viewHousekeepingEmployees() {
        StringBuilder employees = new StringBuilder("Housekeeping Employees:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT employee_ID, employeeName FROM Employee WHERE jobRole = 'Housekeeping'";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    employees.append("Employee ID: ").append(resultSet.getInt("employee_ID"))
                            .append(", Name: ").append(resultSet.getString("employeeName"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            return "Error fetching housekeeping employees: " + e.getMessage();
        }
        return employees.toString();
    }

    @Override
    public abstract void displayMenu();
}
