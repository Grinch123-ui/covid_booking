package profile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provide a set methods to interact with the key functionality in the Profile set of classes.
 */
public abstract class ProfileFacade {
    protected Profile userProfile;

    // Constructor
    public ProfileFacade(Profile userProfile) { this.userProfile = userProfile; }

    /**
     * An abstract method for displaying the booking data in the profile.
     * The available operations differ depending on the user type.
     * @return an arraylist containing the IDs of the valid bookings.
     */
    public abstract ArrayList<String> openProfile() throws Exception;

    /**
     * An abstract method for the booking adjust operations.
     * The available operations differ depending on the user type.
     * @param validBookings the arraylist of the valid booking IDs.
     */
    public abstract void adjustBookings(ArrayList<String> validBookings) throws Exception;

    /**
     * Combines the openProfile and adjustBooking functionalities.
     */
    public void startProfileOp() throws Exception {
        ArrayList<String> results = openProfile();
        if (results != null) {
            adjustBookings(results);
        }
    }

    /**
     * Prompts the user to select which booking in validBookings they wish to perform operations on.
     * @param bookingSize the size of validBookings, which indicate the number of valid bookings.
     * @return the index of the booking to be selected in validBookings.
     */
    protected int selectBookingIndex(int bookingSize) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int bookingIndex;

        // Requesting the user to select a booking for the index.
        boolean continueMenu = true;
        do {
            System.out.println(String.format("+%20s+", "").replace(' ', '-'));
            System.out.println(" ".repeat(2) + "Booking Selection" + " ".repeat(2));
            System.out.println(String.format("+%20s+", "").replace(' ', '-'));
            System.out.println("Select the number of the booking you wish to perform adjustments on: ");
            bookingIndex = Integer.parseInt(reader.readLine());

            // if a valid integer for the index is inputted, the number can directly be returned.
            if (bookingIndex >= 0 && bookingIndex < bookingSize) { continueMenu = false; }
            else { System.out.println("You have entered an invalid number or character."); }
        } while (continueMenu);

        return bookingIndex;
    }

    /**
     * Prompts the user to input the ID of the booking they wish to search for.
     * @return a string indicating the ID of the booking.
     */
    protected String inpBookingID() {
        String bookingID;
        Scanner scanner = new Scanner(System.in);

        // Obtaining the input for the booking ID to be searched.
        System.out.print("Enter the ID of the booking you wish to search for: ");
        bookingID = scanner.next();

        return bookingID;
    }
}
