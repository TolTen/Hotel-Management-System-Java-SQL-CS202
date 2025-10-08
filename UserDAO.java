package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static User getUserByEmailAndPassword(String email, String password) {
        Connection connection = DatabaseConnection.connect();
        if (connection == null) return null;

        String query = """
        SELECT u.user_ID, u.userName, u.email, u.password, e.jobRole, g.guest_ID
        FROM User u
        LEFT JOIN Employee e ON u.user_ID = e.employee_ID
        LEFT JOIN Guest g ON u.user_ID = g.user_ID
        WHERE u.email = ? AND u.password = ?;
    """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("jobRole");
                int userID = resultSet.getInt("user_ID");
                String userName = resultSet.getString("userName");

                if (role == null) { // Eğer Employee değilse, Guest kabul et
                    return new Guest(userName, email, password, userID, resultSet.getInt("guest_ID"));
                } else if (role.equalsIgnoreCase("Administrator")) {
                    return new Administrator(userName, email, password, userID, userID);
                } else if (role.equalsIgnoreCase("Receptionist")) {
                    return new Receptionist(userName, email, password, userID, userID);
                } else if (role.equalsIgnoreCase("Housekeeping")) {
                    return new Housekeeping(userName, email, password, userID, userID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

}

