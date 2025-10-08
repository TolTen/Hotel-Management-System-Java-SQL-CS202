package HotelManagementSystem;

import java.util.Scanner;

public class Housekeeping extends Employee {

    public Housekeeping(String userName, String email, String password, int userID, int employeeID) {
        super(userName, email, password, userID, employeeID, "Housekeeping");
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        HousekeepingService service = new HousekeepingService();

        while (true) {
            System.out.println("\nHousekeeping Menu:");
            System.out.println("1. View Assigned Tasks");
            System.out.println("2. Update Task Status");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    service.viewAssignedTasks(getEmployeeID());
                    break;

                case 2:
                    System.out.println("Enter Task ID: ");
                    int taskID = scanner.nextInt();
                    System.out.println("Enter New Status (completed/missed): ");
                    String status = scanner.next();

                    service.updateTaskStatus(taskID, status);
                    break;

                case 0:
                    System.out.println("Exiting Housekeeping Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}



