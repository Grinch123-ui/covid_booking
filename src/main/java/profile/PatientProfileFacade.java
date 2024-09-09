package profile;

import com.fasterxml.jackson.databind.JsonNode;
import users.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class that provides a simpler interface on the key operations in the Profile classes. This one in particular
 * is for patient profiles.
 */
public class PatientProfileFacade extends ProfileFacade {
    private User currentUser;

    // Constructor
    public PatientProfileFacade(User currentUser) throws Exception {
        super(new PatientProfile(currentUser));
        this.currentUser = currentUser;
    }

    /**
     * Performs the operations for displaying the user's bookings. A patient can either:
     * 1. Display all valid (is on-site, non-lapsed) bookings.
     * 2. Search for a specific booking.
     * @return an arraylist containing the IDs of the valid bookings, or null if "exit" is selected.
     */
    public ArrayList<String> openProfile() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // To be used to store the list of valid booking IDs for use in the booking adjustment operations.
        ArrayList<String> validBookings = null;

        // Performing the viewing operations.
        int choice;
        boolean continueMenu = true;
        do {
            // Listing out the available options.
            System.out.println("Select how your bookings would be displayed.");
            System.out.println("1. Display all valid on-site bookings.");
            System.out.println("2. Search for a particular booking.");
            System.out.println("3. Exit");
            choice = Integer.parseInt(reader.readLine());

            // Display all valid bookings.
            if (choice == 1) {
                validBookings = userProfile.displayValidBookings();
                continueMenu = false;
            }
            // Search for a specific booking and display its data.
            else if (choice == 2) {
                JsonNode foundBooking = currentUser.searchBooking(inpBookingID());
                // executes if the booking exists.
                if (foundBooking != null) {
                    String bookingID = userProfile.viewBookingDetails(foundBooking,
                            currentUser.getUserID(),
                            currentUser.getGivenName(),
                            currentUser.getFamilyName());
                    validBookings = new ArrayList<>();
                    validBookings.add(bookingID);
                }
                continueMenu = false;
            }
            // Quit.
            else if (choice == 3) {
                continueMenu = false;
                validBookings = null;
            }
            else { System.out.println("You have inputted a out-of-range number or invalid character."); }
        } while (continueMenu);

        return validBookings;
    }

    /**
     * Performs the operations for adjusting a user's selected booking. A patient can:
     * 1. Modify the booking.
     * 2. Revert the booking to a previous version.
     * 3. Cancel the booking.
     * @param validBookings the arraylist of the valid booking IDs.
     */
    public void adjustBookings(ArrayList<String> validBookings) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice, bookingIndex = 0;

        /*
        Checking to see if there are multiple valid bookings for operation selection.
        If not, the index would default to zero as further searching is redundant.
         */
        if (validBookings.size() > 1) {
            bookingIndex = selectBookingIndex(validBookings.size());
        }

        // Performing the operations.
        boolean continueMenu = true;
        do {
            // Listing out the options that the patient can use to adjust their bookings.
            System.out.println("Select which operation to perform on your booking!");
            System.out.println("1. Modify the booking.");
            System.out.println("2. Revert the booking to its previous version. " +
                    "Note that only its start time and testing site would be reverted." );
            System.out.println("3. Cancel the booking.");
            System.out.println("4. Exit.");
            choice = Integer.parseInt(reader.readLine());

            // Modify booking.
            if (choice == 1) {
                userProfile.modifyBooking(validBookings.get(bookingIndex));
                continueMenu = false;
            }
            // Revert Booking
            else if (choice == 2) {
                userProfile.revertBooking(validBookings.get(bookingIndex));
                continueMenu = false;
            }
            // Cancel Booking
            else if (choice == 3) {
                userProfile.cancelBooking(validBookings.get(bookingIndex));
                continueMenu = false;
            }
            // Quit
            else if (choice == 4) { continueMenu = false; }
            else { System.out.println("You have inputted a out-of-range number or invalid character."); }
        } while (continueMenu);
    }
}
