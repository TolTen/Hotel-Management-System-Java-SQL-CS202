package HotelManagementSystem;

import java.util.Scanner;

public class Receptionist extends Employee {

    public Receptionist(String userName, String email, String password, int userID, int employeeID) {
        super(userName, email, password, userID, employeeID, "Receptionist");
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        TaskService taskService = new TaskService();
        RoomService roomService = new RoomService();

        while (true) {
            System.out.println("\nReceptionist Menu:");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Add Task");
            System.out.println("3. Update Task Status");
            System.out.println("4. View All Tasks");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    roomService.listAvailableRooms();
                    break;

                case 2:
                    System.out.println("Enter Task Type: ");
                    String taskType = scanner.next();
                    System.out.println("Enter Room ID: ");
                    int roomID = scanner.nextInt();
                    System.out.println("Enter Employee ID: ");
                    int employeeID = scanner.nextInt();
                    System.out.println("Enter Schedule Date: ");
                    String scheduleDate = scanner.next();
                    System.out.println("Enter Status: ");
                    String status = scanner.next();

                    taskService.addTask(taskType, roomID, employeeID, scheduleDate, status);
                    break;

                case 3:
                    System.out.println("Enter Task ID: ");
                    int taskID = scanner.nextInt();
                    System.out.println("Enter New Status: ");
                    String newStatus = scanner.next();

                    taskService.updateTaskStatus(taskID, newStatus);
                    break;

                case 4:
                    taskService.viewAllTasks();
                    break;

                case 0:
                    System.out.println("Exiting Receptionist Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}


