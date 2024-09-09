package profile;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import users.User;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class AdminProfile extends Profile {
    // URL and HTTP method setups.
    private HTMLRequestSetup html = new HTMLRequestSetup();
    private final String bookingUrl = html.returnRootUrl() + "/booking";

    /**
     * Prints out all the valid bookings of the current user. The conditions to be checked are:
     * 1. Whether a booking has lapsed.
     * @return an ArrayList of strings, which include the IDs of the valid bookings that are displayed.
     */
    public ArrayList<String> displayValidBookings() throws Exception {
        ArrayList<String> validBookings = new ArrayList<>();

        boolean bookingFound = false;
        int hasLapsed, bookingCount = 0;
        String bookingID;

        HttpResponse<String> allBookingQuery = html.get(bookingUrl, "");
        ObjectNode[] bookingList = new ObjectMapper().readValue(allBookingQuery.body(), ObjectNode[].class);

        if (bookingList.length > 0) {
            for (ObjectNode bookings : bookingList) {
                String startTime = bookings.get("startTime").textValue();
                // Performing the condition checking for the bookings.
                hasLapsed = compareBookingDate(startTime);

                // Printing out the booking details if valid. The ID of the valid booking is added to a list.
                if (hasLapsed > 0) {
                    System.out.println(String.format("%15s", "").replace(' ', '.'));
                    System.out.println("Booking #" + bookingCount);
                    System.out.println(String.format("%15s", "").replace(' ', '.'));
                    bookingID = viewBookingDetails(bookings,
                            bookings.get("customer").get("id").textValue(),
                            bookings.get("customer").get("givenName").textValue(),
                            bookings.get("customer").get("familyName").textValue()
                    );
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
     * With a given booking ID, the admin would attempt to verify the booking that corresponds to that ID.
     * @param bookingID The booking ID given to the by the user to the receptionist for modification
     */
    public void verifyBooking(String bookingID) throws Exception {
        String bookingID_URL = bookingUrl + "/" + bookingID;
        HttpResponse<String> bookingInfo = html.get(bookingID_URL, "?fields=covidTests");

        // Obtaining the covid test in the booking object.
        ObjectNode booking = new ObjectMapper().readValue(bookingInfo.body(), ObjectNode.class);
        JsonNode covidTest = booking.get("covidTests");

        // It is assumed that each booking can only one covid test (as stated in Ed).
        // There's two status a covid test can take: CREATED and COMPLETED.
        for (JsonNode tests : covidTest) {
            String testStatus = tests.get("status").textValue();
            if (!testStatus.equals("COMPLETE")) {
                System.out.println("The booking that corresponds to the given ID is valid.");
            } else {
                System.out.println("The booking that corresponds to the given ID is invalid. The test has already been taken.");
            }
            break;
        }
    }

    /**
     * Deletes a booking from the database.
     * @param bookingID the ID of the booking to be deleted.
     */
    public void deleteBooking(String bookingID) throws IOException, InterruptedException {
        String delUrl = bookingUrl + "/" + bookingID;
        html.delete(delUrl);
    }
}
