package profile;

import com.fasterxml.jackson.databind.JsonNode;
import users.User;

import java.util.ArrayList;

public class PatientProfile extends Profile {
    private User profileUser;
    // Creates an instance of the patient profile.
    public PatientProfile(User currentUser) {
        profileUser = currentUser;
    }

    /**
     * Prints out all the valid bookings of the current user. The conditions to be checked are:
     * 1. Whether a booking has lapsed.
     * 2. Whether the booking is on-site.
     * @return an ArrayList of strings, which include the IDs of the valid bookings that are displayed.
     */
    public ArrayList<String> displayValidBookings() throws Exception {
        ArrayList<String> validBookings = new ArrayList<>();
        boolean isHomeBooking;
        boolean bookingFound = false;
        int hasLapsed, bookingCount = 0;
        String bookingID;

        JsonNode userBookings = profileUser.obtainAvailableBookings();
        if (userBookings.size() > 0) {
            for (JsonNode bookings : userBookings) {
                String startTime = bookings.get("startTime").textValue();
                // Performing the condition checking for the bookings.
                isHomeBooking = checkBookingType(bookings);
                hasLapsed = compareBookingDate(startTime);
                // Printing out the booking details if valid.
                if (!isHomeBooking && hasLapsed > 0) {
                    System.out.println(String.format("%15s", "").replace(' ', '.'));
                    System.out.println("Booking #" + bookingCount);
                    System.out.println(String.format("%15s", "").replace(' ', '.'));
                    bookingID = viewBookingDetails(bookings,
                            profileUser.getUserID(),
                            profileUser.getGivenName(),
                            profileUser.getFamilyName());
                    validBookings.add(bookingID);
                    bookingFound = true;
                    bookingCount += 1;
                }
            }
            // a message is printed if there is NO valid bookings among the available list.
            if (!bookingFound) {
                System.out.println("You have no valid bookings available.");
            }
        }
        else { System.out.println("You have not made any bookings under your name."); }
        return validBookings;
    }

    /**
     * Checks if the provided booking is home or on-site.
     * This can be checked through the availability of a QR code, which is only yielded by home bookings.
     * @param booking the booking to be checked.
     * @return a boolean indicating the booking type. (Home = true, on-site = false)
     */
    public boolean checkBookingType(JsonNode booking) {
        JsonNode bookingInfo = booking.get("additionalInfo");
        return bookingInfo.has("QRCode");
    }
}
