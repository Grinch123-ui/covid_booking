package booking;

import users.Receptionist;
import users.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * A facade class to provide a simple interface to interact with the booking functionalities.
 */
public class BookingFacade {
    //User information.
    private User currentUser;
    private String userID;

    // Constructor.
    public BookingFacade(User currentUser) throws Exception {
        this.currentUser = currentUser;
        userID = currentUser.getUserID();
    }

    /**
     * Prints out the menu options for the booking functionalities.
     */
    private void printBookingOptions() {
        // Listing out the booking options.
        System.out.println("Select the booking operation you wish to perform.");
        System.out.println("1. Make an On-site booking - Admins/Receptionists only.");
        System.out.println("2. Make a Home Booking - Any user.");
        System.out.println("3. Check the status of your booking.");
    }

    /**
     * Creates a on-site booking based on the given test site. The user would be prompted to enter the start
     * time of their booking.
     * @param testingSiteID the ID of the test site to be tested in.
     */
    public void addOnSiteBooking(String testingSiteID) throws Exception {
        String startTime = inpBookingTime();
        BookingContext bookingContext = new BookingContext(new OnSiteBooking(userID, testingSiteID, startTime));
        bookingContext.addBookingToDatabase();
    }

    /**
     * Creates a home booking based on the given test site. The user would be prompted to enter the start
     * time of their booking.
     * @param testingSiteID the ID of the test site to be tested in.
     */
    public void addHomeBooking(String testingSiteID) throws Exception {
        String startTime = inpBookingTime();
        BookingContext bookingContext = new BookingContext(new HomeBooking(userID, testingSiteID, startTime));
        bookingContext.addBookingToDatabase();
    }

    /**
     * Checks the status of the user's booking based on the provided PIN code.
     */
    public void checkBookingStatus() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String pinCode = reader.readLine();
        currentUser.obtainBookingStatus(pinCode);
    }

    /**
     * Obtains the desired start time for the user's booking. The format is simplified for simplicity.
     * @return the start time of the booking (yyyy-mm-ddTHH:MM:00)
     */
    public String inpBookingTime() {
        String bookingDate, bookingTime;
        Scanner scanner = new Scanner(System.in);

        // Obtaining the inputs for the specified time.
        System.out.print("Enter Booking Date (Format - yyyy-mm-dd): ");
        bookingDate = scanner.next();
        System.out.print("Enter Booking Time (Format - HH:MM): ");
        bookingTime = scanner.next();

        //Sample Time: 2022-03-18, 15:00
        return bookingDate + "T" + bookingTime + ":00";
    }

    /**
     * Performs the various functions related to bookings within the web application.
     */
    public void startBooking() throws Exception {
        // The test site ID is hard-coded for testing purposes.
        String testingSiteID = "7fbd25ee-5b64-4720-b1f6-4f6d4731260e";
        printBookingOptions();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(reader.readLine());

        // Making an on-site booking. For testing purposes, the testing site ID is hard-coded.
        if (choice == 1 && currentUser instanceof Receptionist) { addOnSiteBooking(testingSiteID); }
        // Make a home booking.
        else if (choice == 2) { addHomeBooking(testingSiteID); }
        // Status checking.
        else if (choice == 3) { checkBookingStatus(); }
        // Invalid input handling (to a degree).
        else if ((choice == 1) && !(currentUser instanceof Receptionist)) { System.out.println("You do not have the proper permissions to make this type of booking."); }
        else { System.out.println("You have entered an invalid option."); }
    }
}
