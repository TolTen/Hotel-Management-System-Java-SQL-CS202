package HotelManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Payment {


    public boolean processPayment(int bookingID, String paymentMethod, double amount) {
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return false;

            String query = "INSERT INTO Payment (booking_ID, paymentMethod, amount, payment_status) VALUES (?, ?, ?, 'completed')";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookingID);
                statement.setString(2, paymentMethod);
                statement.setDouble(3, amount);

                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error processing payment: " + e.getMessage());
            return false;
        }
    }



    public String viewPaymentDetails(int bookingID) {
        StringBuilder details = new StringBuilder("Payment Details:\n");
        try (Connection connection = DatabaseConnection.connect()) {
            if (connection == null) return "Failed to connect to the database.";

            String query = "SELECT paymentMethod, amount, payment_status FROM Payment WHERE booking_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookingID);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    details.append("Payment Method: ").append(resultSet.getString("paymentMethod"))
                            .append(", Amount: $").append(resultSet.getDouble("amount"))
                            .append(", Status: ").append(resultSet.getString("payment_status"))
                            .append("\n");
                } else {
                    return "No payment details found for this booking.";
                }
            }
        } catch (SQLException e) {
            return "Error fetching payment details: " + e.getMessage();
        }
        return details.toString();
    }



    private boolean isValidPaymentMethod(String method) {
        return method.equalsIgnoreCase("CreditCard") ||
                method.equalsIgnoreCase("Cash") ||
                method.equalsIgnoreCase("Online") ||
                method.equalsIgnoreCase("DebitCard");
    }
}


