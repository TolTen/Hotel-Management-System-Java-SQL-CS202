package HotelManagementSystem;

import java.util.Scanner;

public class Administrator extends Employee {

    public Administrator(String userName, String email, String password, int userID, int employeeID) {
        super(userName, email, password, userID, employeeID, "Administrator");
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        RoomService roomService = new RoomService();
        HotelService hotelService = new HotelService();

        while (true) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1. Add Room");
            System.out.println("2. Update Room Status");
            System.out.println("3. View All Hotels");
            System.out.println("4. Add Hotel");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Hotel ID: ");
                    int hotelID = scanner.nextInt();
                    System.out.println("Enter Room Name: ");
                    String roomName = scanner.next();
                    System.out.println("Enter Room Type: ");
                    String roomType = scanner.next();
                    System.out.println("Enter Capacity: ");
                    int capacity = scanner.nextInt();
                    System.out.println("Enter Price: ");
                    double price = scanner.nextDouble();
                    System.out.println("Enter Room Status: ");
                    String status = scanner.next();
                    System.out.println("Enter Floor Number: ");
                    int floorNumber = scanner.nextInt();

                    roomService.addRoom(hotelID, roomName, roomType, capacity, price, status, floorNumber);
                    break;

                case 2:
                    System.out.println("Enter Room ID to Update Status: ");
                    int roomID = scanner.nextInt();
                    System.out.println("Enter New Status: ");
                    String newStatus = scanner.next();

                    roomService.updateRoomStatus(roomID, newStatus);
                    break;

                case 3:
                    hotelService.listHotels();
                    break;

                case 4:
                    System.out.println("Enter Hotel Name: ");
                    String hotelName = scanner.next();
                    System.out.println("Enter Address: ");
                    String address = scanner.next();
                    System.out.println("Enter City: ");
                    String city = scanner.next();
                    System.out.println("Enter Contact Number: ");
                    String contact = scanner.next();
                    System.out.println("Enter Email: ");
                    String email = scanner.next();

                    hotelService.addHotel(hotelName, address, city, contact, email);
                    break;

                case 0:
                    System.out.println("Exiting Administrator Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
