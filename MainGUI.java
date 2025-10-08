package HotelManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainGUI {
    public static void main(String[] args) {
        new LoginFrame();
    }
}

class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;

    public LoginFrame() {
        setTitle("Hotel Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JLabel title = new JLabel("Welcome to Hotel Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User user = UserDAO.getUserByEmailAndPassword(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (user instanceof Guest) {
                new GuestMenu((Guest) user);
            } else if (user instanceof Administrator) {
                new AdminMenu((Administrator) user);
            } else if (user instanceof Receptionist) {
                new ReceptionistMenu((Receptionist) user);
            } else if (user instanceof Housekeeping) {
                new HousekeepingMenu((Housekeeping) user);
            }
            dispose();
        }
    }

}


// Guest Menu
class GuestMenu extends JFrame {
    private Guest guest;

    public GuestMenu(Guest guest) {
        this.guest = guest;

        setTitle("Guest Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton addBookingButton = new JButton("Add New Booking");
        JButton viewBookingButton = new JButton("View My Bookings");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton makePaymentButton = new JButton("Make Payment");
        JButton viewPaymentDetailsButton = new JButton("View Payment Details");
        JButton exitButton = new JButton("Exit");

        addBookingButton.addActionListener(e -> addBooking());
        viewBookingButton.addActionListener(e -> viewBookings());
        cancelBookingButton.addActionListener(e -> cancelBooking());
        makePaymentButton.addActionListener(e -> makePayment());
        viewPaymentDetailsButton.addActionListener(e -> viewPaymentDetails());
        exitButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(addBookingButton);
        add(viewBookingButton);
        add(cancelBookingButton);
        add(makePaymentButton);
        add(viewPaymentDetailsButton);
        add(exitButton);

        setVisible(true);
    }

    private void addBooking() {
        String roomIDInput = JOptionPane.showInputDialog("Enter Room ID:");
        String startDate = JOptionPane.showInputDialog("Enter Start Date (YYYY-MM-DD):");
        String endDate = JOptionPane.showInputDialog("Enter End Date (YYYY-MM-DD):");
        String guestCountInput = JOptionPane.showInputDialog("Enter Guest Count:");

        try {
            int roomID = Integer.parseInt(roomIDInput);
            int guestCount = Integer.parseInt(guestCountInput);

            RoomService roomService = new RoomService();
            if (!roomService.isRoomAvailable(roomID)) {
                JOptionPane.showMessageDialog(this, "The selected room is not available!", "Booking Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BookingService bookingService = new BookingService();
            bookingService.addBooking(guest.getGuestID(), roomID, startDate, endDate, guestCount);
            JOptionPane.showMessageDialog(this, "Booking successfully added!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void viewBookings() {
        BookingService bookingService = new BookingService();
        String bookings = bookingService.viewBookings(guest.getGuestID());
        JOptionPane.showMessageDialog(this, bookings, "My Bookings", JOptionPane.INFORMATION_MESSAGE);
    }


    private void cancelBooking() {
        String bookingID = JOptionPane.showInputDialog("Enter Booking ID to Cancel:");
        BookingService bookingService = new BookingService();
        bookingService.cancelBooking(Integer.parseInt(bookingID));
        JOptionPane.showMessageDialog(this, "Booking successfully canceled!");
    }

    private void makePayment() {
        String bookingIDInput = JOptionPane.showInputDialog("Enter Booking ID:");
        String paymentMethod = JOptionPane.showInputDialog("Enter Payment Method (CreditCard, Cash, Online, DebitCard):");
        String amountInput = JOptionPane.showInputDialog("Enter Payment Amount:");

        try {
            int bookingID = Integer.parseInt(bookingIDInput);
            double amount = Double.parseDouble(amountInput);

            if ("Online".equalsIgnoreCase(paymentMethod)) {
                String cardNumber = JOptionPane.showInputDialog("Enter Card Number:");
                String cvv = JOptionPane.showInputDialog("Enter CVV:");
                String cardHolderName = JOptionPane.showInputDialog("Enter Card Holder Name:");

            }

            Payment paymentService = new Payment();
            boolean success = paymentService.processPayment(bookingID, paymentMethod, amount);

            if (success) {
                JOptionPane.showMessageDialog(this, "Payment Successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Payment Failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void viewPaymentDetails() {
        String bookingIDInput = JOptionPane.showInputDialog("Enter Booking ID to View Payment Details:");
        try {
            int bookingID = Integer.parseInt(bookingIDInput);
            Payment paymentService = new Payment();
            String details = paymentService.viewPaymentDetails(bookingID);
            JOptionPane.showMessageDialog(this, details, "Payment Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Booking ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAvailableRooms() {
        RoomService roomService = new RoomService();
        String availableRooms = roomService.viewAvailableRooms();
        JOptionPane.showMessageDialog(this, availableRooms, "Available Rooms", JOptionPane.INFORMATION_MESSAGE);
    }


}




// Administrator Menu
class AdminMenu extends JFrame {
    private Administrator admin;

    public AdminMenu(Administrator admin) {
        this.admin = admin;

        setTitle("Administrator Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton addRoomButton = new JButton("Add Room");
        JButton updateRoomButton = new JButton("Update Room Status");
        JButton viewRoomsButton = new JButton("View All Rooms");
        JButton viewHotelsButton = new JButton("View All Hotels");
        JButton addHotelButton = new JButton("Add Hotel");
        JButton exitButton = new JButton("Exit");

        addRoomButton.addActionListener(e -> addRoom());
        updateRoomButton.addActionListener(e -> {

            String roomIDInput = JOptionPane.showInputDialog("Enter Room ID to Update Status:");
            String newStatusInput = JOptionPane.showInputDialog("Enter New Status (available, occupied, cleaning):");


            try {
                int roomID = Integer.parseInt(roomIDInput);
                String newStatus = newStatusInput.trim();

                RoomService roomService = new RoomService();
                roomService.updateRoomStatus(roomID, newStatus);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid Room ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewRoomsButton.addActionListener(e -> viewRooms());
        viewHotelsButton.addActionListener(e -> viewHotels());
        addHotelButton.addActionListener(e -> addHotel());
        exitButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(addRoomButton);
        add(updateRoomButton);
        add(viewRoomsButton);
        add(viewHotelsButton);
        add(addHotelButton);
        add(exitButton);

        setVisible(true);
    }

    private void addRoom() {
        String hotelID = JOptionPane.showInputDialog("Enter Hotel ID:");
        String roomName = JOptionPane.showInputDialog("Enter Room Name:");
        String roomType = JOptionPane.showInputDialog("Enter Room Type:");
        String capacity = JOptionPane.showInputDialog("Enter Capacity:");
        String price = JOptionPane.showInputDialog("Enter Price:");
        String status = JOptionPane.showInputDialog("Enter Room Status (available, occupied, cleaning):");
        String floorNumber = JOptionPane.showInputDialog("Enter Floor Number:");

        RoomService roomService = new RoomService();
        int roomID = roomService.addRoom(
                Integer.parseInt(hotelID),
                roomName,
                roomType,
                Integer.parseInt(capacity),
                Double.parseDouble(price),
                status,
                Integer.parseInt(floorNumber)
        );

        if (roomID != -1) {
            JOptionPane.showMessageDialog(this, "Room added successfully! Room ID: " + roomID);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }





    private void updateRoomStatus() {
        String roomIDInput = JOptionPane.showInputDialog("Enter Room ID to Update Status:");
        String[] statusOptions = {"available", "occupied", "not_available"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select New Status:",
                "Update Room Status", JOptionPane.QUESTION_MESSAGE, null, statusOptions, statusOptions[0]);

        try {
            int roomID = Integer.parseInt(roomIDInput);
            RoomService roomService = new RoomService();
            roomService.updateRoomStatus(roomID, newStatus);

            JOptionPane.showMessageDialog(this, "Room status updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Room ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void viewRooms() {
        RoomService roomService = new RoomService();
        String rooms = roomService.viewAllRooms();
        JOptionPane.showMessageDialog(this, rooms, "Room List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewHotels() {
        HotelService hotelService = new HotelService();
        String hotels = hotelService.viewAllHotels();
        JOptionPane.showMessageDialog(this, hotels, "Hotel List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addHotel() {
        String hotelName = JOptionPane.showInputDialog("Enter Hotel Name:");
        String address = JOptionPane.showInputDialog("Enter Address:");
        String city = JOptionPane.showInputDialog("Enter City:");
        String contact = JOptionPane.showInputDialog("Enter Contact Number:");
        String email = JOptionPane.showInputDialog("Enter Email:");

        HotelService hotelService = new HotelService();
        int hotelID = hotelService.addHotel(
                hotelName,
                address,
                city,
                contact,
                email
        );

        if (hotelID != -1) {
            JOptionPane.showMessageDialog(this, "Hotel added successfully! Hotel ID: " + hotelID);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllEmployees() {
        try {
            String employees = Employee.viewAllEmployees();
            JOptionPane.showMessageDialog(this, employees, "All Employees", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteRoom() {
        String roomIDInput = JOptionPane.showInputDialog("Enter Room ID to Delete:");
        RoomService roomService = new RoomService();
        roomService.deleteRoom(Integer.parseInt(roomIDInput));
        JOptionPane.showMessageDialog(this, "Room successfully deleted!");
    }







}




// Receptionist Menu
class ReceptionistMenu extends JFrame {
    private Receptionist receptionist;

    public ReceptionistMenu(Receptionist receptionist) {
        this.receptionist = receptionist;

        setTitle("Receptionist Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton viewRoomsButton = new JButton("View Available Rooms");
        JButton addTaskButton = new JButton("Add Task");
        JButton updateTaskButton = new JButton("Update Task Status");
        JButton viewTasksButton = new JButton("View Tasks");
        JButton exitButton = new JButton("Exit");

        viewRoomsButton.addActionListener(e -> viewRooms());
        addTaskButton.addActionListener(e -> addTask());
        updateTaskButton.addActionListener(e -> updateTaskStatus());
        viewTasksButton.addActionListener(e -> viewTasks());
        exitButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(viewRoomsButton);
        add(addTaskButton);
        add(updateTaskButton);
        add(viewTasksButton);
        add(exitButton);

        setVisible(true);
    }

    private void viewRooms() {
        RoomService roomService = new RoomService();
        String rooms = roomService.viewAvailableRooms();
        JOptionPane.showMessageDialog(this, rooms, "Available Rooms", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addTask() {
        String taskType = JOptionPane.showInputDialog("Enter Task Type:");
        String roomIDInput = JOptionPane.showInputDialog("Enter Room ID:");
        String employeeIDInput = JOptionPane.showInputDialog("Enter Employee ID:");
        String scheduleDate = JOptionPane.showInputDialog("Enter Schedule Date (YYYY-MM-DD):");
        String status = "pending";

        try {
            int roomID = Integer.parseInt(roomIDInput);
            int employeeID = Integer.parseInt(employeeIDInput);

            TaskService taskService = new TaskService();
            taskService.addTask(taskType, roomID, employeeID, scheduleDate, status);

            RoomService roomService = new RoomService();
            roomService.updateRoomAvailabilityBasedOnTasks(roomID);

            JOptionPane.showMessageDialog(this, "Task added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void updateTaskStatus() {
        String taskID = JOptionPane.showInputDialog("Enter Task ID:");
        String newStatus = JOptionPane.showInputDialog("Enter New Status (scheduled, pending, completed):");

        TaskService taskService = new TaskService();
        boolean success = taskService.updateTaskStatus(Integer.parseInt(taskID), newStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Task status updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update task status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewTasks() {
        TaskService taskService = new TaskService();
        String tasks = taskService.viewAllTasks();
        JOptionPane.showMessageDialog(this, tasks, "Task List", JOptionPane.INFORMATION_MESSAGE);
    }


    private void viewHousekeepingEmployees() {
        try {
            String employees = Employee.viewHousekeepingEmployees();
            JOptionPane.showMessageDialog(this, employees, "Housekeeping Employees", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching housekeeping employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }





}




// Housekeeping Menu
class HousekeepingMenu extends JFrame {
    private Housekeeping housekeeping;

    public HousekeepingMenu(Housekeeping housekeeping) {
        this.housekeeping = housekeeping;

        setTitle("Housekeeping Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton viewTasksButton = new JButton("View Assigned Tasks");
        JButton updateTaskButton = new JButton("Update Task Status");
        JButton viewCleaningHistoryButton = new JButton("View Cleaning History");
        JButton exitButton = new JButton("Exit");

        viewTasksButton.addActionListener(e -> viewTasks());
        updateTaskButton.addActionListener(e -> updateTaskStatus());
        viewCleaningHistoryButton.addActionListener(e -> selectCleaningHistoryOption());
        exitButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(viewTasksButton);
        add(updateTaskButton);
        add(viewCleaningHistoryButton);
        add(exitButton);

        setVisible(true);
    }

    private void viewTasks() {
        TaskService taskService = new TaskService();
        String tasks = taskService.viewAssignedTasks(housekeeping.getEmployeeID());
        JOptionPane.showMessageDialog(this, tasks, "Assigned Tasks", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTaskStatus() {
        String taskID = JOptionPane.showInputDialog("Enter Task ID:");
        String newStatus = JOptionPane.showInputDialog("Enter New Status (completed, missed):");

        TaskService taskService = new TaskService();
        boolean success = taskService.updateTaskStatus(Integer.parseInt(taskID), newStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Task status updated successfully!");
            viewTasks();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update task status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectCleaningHistoryOption() {
        String[] options = {"View Cleaning History by Room", "View All Cleaning History"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option:",
                "Cleaning History Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            viewCleaningHistoryByRoom();
        } else if (choice == 1) {
            viewAllCleaningHistory();
        }
    }

    private void viewCleaningHistoryByRoom() {
        String roomIDInput = JOptionPane.showInputDialog("Enter Room ID to View Cleaning History:");
        try {
            int roomID = Integer.parseInt(roomIDInput);
            CleaningHistoryService cleaningService = new CleaningHistoryService();
            String history = cleaningService.viewCleaningHistoryByRoom(roomID);

            JOptionPane.showMessageDialog(this, history, "Cleaning History for Room", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Room ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void viewAllCleaningHistory() {
        CleaningHistoryService cleaningService = new CleaningHistoryService();
        String history = cleaningService.viewAllCleaningHistory();

        JOptionPane.showMessageDialog(this, history, "All Cleaning History", JOptionPane.INFORMATION_MESSAGE);
    }

}








