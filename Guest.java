package HotelManagementSystem;

import java.util.Scanner;

public class Guest extends User {
    private int guestID;

    public Guest(String userName, String email, String password, int userID, int guestID) {
        super(userName, email, password, userID);
        this.guestID = guestID;
    }

    public int getGuestID() {
        return guestID;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        BookingService bookingService = new BookingService();
        Payment paymentService = new Payment();

        while (true) {
            System.out.println("\nGuest Menu:");
            System.out.println("1. Add New Booking");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Make Payment");
            System.out.println("5. View Payment Details");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Room ID: ");
                    int roomID = scanner.nextInt();
                    System.out.println("Enter Start Date (YYYY-MM-DD): ");
                    String startDate = scanner.next();
                    System.out.println("Enter End Date (YYYY-MM-DD): ");
                    String endDate = scanner.next();
                    System.out.println("Enter Number of Guests: ");
                    int numberOfGuests = scanner.nextInt();

                    bookingService.addBooking(getGuestID(), roomID, startDate, endDate, numberOfGuests);
                    break;

                case 2:
                    bookingService.viewBookings(getGuestID());
                    break;

                case 3:
                    System.out.println("Enter Booking ID to Cancel: ");
                    int bookingID = scanner.nextInt();
                    bookingService.cancelBooking(bookingID);
                    break;

                case 4:
                    System.out.println("Enter Booking ID for Payment: ");
                    int paymentBookingID = scanner.nextInt();
                    System.out.println("Enter Payment Method (CreditCard, Cash, Online, DebitCard): ");
                    String paymentMethod = scanner.next();
                    System.out.println("Enter Payment Amount: ");
                    double amount = scanner.nextDouble();

                    paymentService.processPayment(paymentBookingID, paymentMethod, amount);
                    break;

                case 5:
                    System.out.println("Enter Booking ID to View Payment Details: ");
                    int viewPaymentBookingID = scanner.nextInt();
                    paymentService.viewPaymentDetails(viewPaymentBookingID);
                    break;

                case 0:
                    System.out.println("Exiting Guest Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

