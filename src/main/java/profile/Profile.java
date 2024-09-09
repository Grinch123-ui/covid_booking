package profile;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import users.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Parent class for user profiles. It contains the functionality related to booking modification and restoration.
 */
public abstract class Profile {
    private final HTMLRequestSetup html = new HTMLRequestSetup();
    private final String bookingUrl = html.returnRootUrl() + "/booking";

    /**
     * Prints out all the valid bookings of the current user.
     * @return an ArrayList of strings, which include the IDs of the valid bookings that are displayed.
     */
    abstract public ArrayList<String> displayValidBookings() throws Exception;

    /**
     * Check whether the particular booking is lapsed when compared to today's/current day and time.
     * @param bookingDateTime the start time of the booking to be compared
     * @return An integer indicating the comparison results (0 = equal, <0 = less/lapsed, >0 = more/not lapsed)
     */
    public int compareBookingDate(String bookingDateTime) throws Exception {
        // The startTime string is in the format of yyyy-MM-dd'T'HH:mm:ss.SSS'Z'.
        // This would be separated into the date (yyyy-MM-dd) and time (HH:mm:ss)
        String bookingDate = bookingDateTime.substring(0, 10);
        String bookingTime = bookingDateTime.substring(11, 19);
        String bookingDateTimeFormatted = bookingDate + "-" + bookingTime;

        // The newly formatted string is parsed to fit the format given by the Date class.
        SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date formattedBookingDateTime = formatterDateTime.parse(bookingDateTimeFormatted);

        // The booking date and time are compared with today's day and time.
        Date systemDateTime = new Date(System.currentTimeMillis());
        return formattedBookingDateTime.compareTo(systemDateTime);
    }

    /**
     * Obtains and prints the essential details of a given booking for viewing
     * @param booking the booking object to obtain the data from
     * @return ID of the viewed booking.
     */
    public String viewBookingDetails(JsonNode booking, String userID, String givenName, String familyName) {
        String siteID, siteName, startTime, bookingID, bookingStatus, bookingPIN;
        JsonNode testSiteData = booking.get("testingSite");

        // Obtaining the required data from the booking object.
        siteID = testSiteData.get("id").textValue();
        siteName = testSiteData.get("name").textValue();
        bookingID = booking.get("id").textValue();
        startTime = booking.get("startTime").textValue();
        bookingStatus = booking.get("status").textValue();
        bookingPIN = booking.get("smsPin").textValue();

        // Printing out the results.
        System.out.println("Customer ID: " + userID);
        System.out.println("Customer Name: " + givenName + " " + familyName);
        System.out.println("Testing Site ID: " + siteID);
        System.out.println("Testing Site Name: " + siteName);
        System.out.println("Booking ID:  " + bookingID);
        System.out.println("Booking Start Time: " + startTime);
        System.out.println("Booking Status: " + bookingStatus);
        System.out.println("Booking PIN: " + bookingPIN + "\n");

        return bookingID;
    }

    /**
     * Changes the status of the given booking to CANCELLED by using the PATCH endpoint.
     * The booking will still remain in the system.
     * @param bookingID The ID of the booking to be cancelled.
     */
    public void cancelBooking(String bookingID) throws Exception {
        String patchUrl = bookingUrl + "/" + bookingID;
        HttpResponse<String> bookingQuery = html.get(patchUrl, "");
        ObjectNode booking = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);

        String currentStatus = booking.get("status").textValue();

        // the booking is PATCHED only if its status isn't cancelled.
        if (currentStatus.equals("CANCELLED")) {
            System.out.println("Your booking has already been cancelled.");
        } else {
            // Creating the object with the required fields for patching.
            JSONObject patchObject = createPatchObject(booking.get("customer").get("id").textValue(),
                    booking.get("testingSite").get("id").textValue(),
                    booking.get("startTime").textValue(),
                    "CANCELLED",
                    booking.get("additionalInfo"));

            html.patch(patchUrl, patchObject.toString());
            System.out.println("The status of the booking has been changed to - CANCELLED.");
        }
    }

    /**
     * Modifies the booking that corresponds with the given ID based on these parameters:
     * 1. Test site only.
     * 2. Start time only.
     * 3. Test site and start time.
     * @param bookingID the ID of the booking to be modified.
     */
    public void modifyBooking(String bookingID) throws Exception {
        // Obtaining the booking object that corresponds to the give ID for data access.
        String url = bookingUrl + "/" + bookingID;
        HttpResponse<String> bookingQuery = html.get(url, "?fields=covidTests");
        ObjectNode booking = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);

        // Printing out the available options for test modification.
        System.out.println("Select a modification to make for the booking: \n1. Venue Only \n2. Time Only \n3. Venue and Time");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choice = reader.readLine();

        JSONObject patchObject;
        // A copy of the booking before modification is added to modificationHistory before the PATCH.
        JsonNode updatedAddInfo = saveHistory(bookingID, booking.get("startTime").textValue(), booking.get("testingSite").get("id").textValue());

        while (true) {
            int checkInpTime;
            // Modify test site only
            if (choice.equals("1")) {
                System.out.println("Please enter the new venue ID: ");
                String newVenueID = reader.readLine();
                patchObject = createPatchObject(
                        booking.get("customer").get("id").textValue(),
                        newVenueID,
                        booking.get("startTime").textValue(),
                        booking.get("status").textValue(),
                        updatedAddInfo);
                break;
            }
            // Modify time only.
            else if (choice.equals("2")) {
                String newStartTime = inputBookingTime();
                checkInpTime = compareBookingDate(newStartTime);
                if (checkInpTime > 0) {
                    patchObject = createPatchObject(
                            booking.get("customer").get("id").textValue(),
                            booking.get("testingSite").get("id").textValue(),
                            newStartTime,
                            booking.get("status").textValue(),
                            updatedAddInfo);
                    break;
                }
                else { System.out.println("You have inputted a date and time that has already passed. Please reenter some values past the current date and time."); }
            }
            // Modify test site and start time.
            else if (choice.equals("3")) {
                System.out.println("Please enter the new venue ID: ");
                String newVenueID = reader.readLine();
                String newStartTime = inputBookingTime();
                checkInpTime = compareBookingDate(newStartTime);
                if (checkInpTime > 0) {
                    patchObject = createPatchObject(
                            booking.get("customer").get("id").textValue(),
                            newVenueID,
                            newStartTime,
                            booking.get("status").textValue(),
                            updatedAddInfo);
                    break;
                }
                else { System.out.println("You have inputted a time that has already passed. Please reenter some values past the current date and time."); }
            } else {
                System.out.println("You have entered an invalid choice");
            }
        }

        // Patching the booking and printing out the status.
        if (html.patch(url, patchObject.toString()).statusCode() == 200) {
            System.out.println("Your booking has been successfully modified based on the given parameters!");
        }
        else {
            System.out.println("You failed to modify your given booking.");
        }
    }

    /**
     * Reverts the booking with the given ID to a previous one. The following data would be reverted:
     * 1. Testing site ID.
     * 2. Start Time.
     * Note that only a past booking with a still-valid start time can be reverted to.
     * @param bookingID the ID of the booking to be reverted.
     */
    public void revertBooking(String bookingID) throws Exception {
        String url = bookingUrl + "/" + bookingID;
        HttpResponse<String> bookingQuery = html.get(url, "?fields=covidTests");

        ObjectNode booking = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);

        JSONObject patchObject;
        // The history in additionalInfo is displayed to the user for viewing (if available)
        ArrayList<Integer> validBookingList = showHistory(bookingID);

        if (validBookingList.size() > 0) {
            System.out.println("Input the number of the booking you wish to revert to: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            int choice = Integer.parseInt(reader.readLine());
            JsonNode bookingHistory = booking.get("additionalInfo").get("bookingHistory");
            while (true) {
                if (validBookingList.contains(choice)) {
                    JsonNode chosenRevert = bookingHistory.get(choice);
                    patchObject = createPatchObject(
                            booking.get("customer").get("id").textValue(),
                            chosenRevert.get("testingSiteId").textValue(),
                            chosenRevert.get("startTime").textValue(),
                            booking.get("status").textValue(),
                            booking.get("additionalInfo"));
                    break;
                } else {
                    System.out.println("You have inputted an invalid booking number. Please enter again.");
                }
            }

            // Patching the booking and printing out the status.
            if (html.patch(url, patchObject.toString()).statusCode() == 200) {
                System.out.println("Your booking has been reverted to the data of a previous booking!");
            }
            else {
                System.out.println("Your given booking has failed to revert.");
            }
        }
        else { System.out.println("There are no valid modifications for you to revert to!"); }
    }

    /**
     * Adds a copy of the booking's test site and start time before modification in the same booking's
     * additionalInfo as an array. The last 3 changes are saved.
     * Only the startTime and testSiteID are changed as those are the values that can be modified.
     * @param bookingID the ID of the booking to be saved.
     * @param startTime the startTime of the booking to be saved.
     * @param testSiteID the ID of the test site to be saved.
     */
    public JsonNode saveHistory(String bookingID, String startTime, String testSiteID) throws Exception {
        // 1. Obtaining additionalInfo from the given booking.
        JsonNode additionalInfo = obtainAdditionalInfo(bookingID);

        /*
        2. Creating an ArrayNode to store the booking history in additionalInfo.
        For simplicity, a new array for the bookingHistory is created each time whenever modification occurs.
        The booking history data would be added to this array.
        */
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<JSONObject> bookingHistory = new ArrayList<>();
        ArrayNode array = mapper.valueToTree(bookingHistory);

        /*
        3. Checking to see if a modifications has been made yet.
        The bookingHistory field would not exist if NO modifications have been made to the current booking AT ALL.
        */
        boolean hasModifications = additionalInfo.has("bookingHistory");

        // 4. Adding the most latest change of the given booking to bookingHistory.
        ObjectNode pastVersion = array.addObject();
        pastVersion.put("testingSiteId", testSiteID);
        pastVersion.put("startTime", startTime);

        /*
        5. If bookingHistory exists, there would be at least 1 modification in the list.
        We would then loop through the original bookingHistory array to add the next (up to) two latest
        booking information to our new array.
        */
        if (hasModifications) {
            int versionCount = 1;
            JsonNode historyField = additionalInfo.get("bookingHistory");
            for (JsonNode versions: historyField) {
                pastVersion = array.addObject();
                pastVersion.put("testingSiteId", versions.get("testingSiteId").textValue());
                pastVersion.put("startTime", versions.get("startTime").textValue());
                versionCount += 1;
                // if the count reaches 3, then we don't need to look through the next booking as the limit is reached.
                if (versionCount == 3) { break; }
            }
        }

        // 6. Updating additionalInfo with the new bookingHistory array.
        ((ObjectNode) additionalInfo).putArray("bookingHistory").addAll(array);
        return additionalInfo;
    }

    /**
     * Shows the data of the bookings stored in modificationHistory.
     */
    public ArrayList<Integer> showHistory(String bookingID) throws Exception {
        // 1. Obtaining additionalInfo from the given booking.
        JsonNode additionalInfo = obtainAdditionalInfo(bookingID);

        ArrayList<Integer> indexList = new ArrayList<>();

        // 2. Checking to see if a modifications has been made yet. There would be at least one entry if it does.
        boolean hasModifications = additionalInfo.has("bookingHistory");

        // 3. Printing out the output if bookingHistory exists.
        if (hasModifications) {
            int isValid, bookingCount = 0;
            JsonNode historyField = additionalInfo.get("bookingHistory");
            for (JsonNode versions: historyField) {
                System.out.println(String.format("+%20s+", "").replace(' ', '.'));
                System.out.println("Past Booking #" + bookingCount);
                System.out.println(String.format("+%20s+", "").replace(' ', '.'));

                String startTime = versions.get("startTime").textValue();
                isValid = compareBookingDate(startTime);

                if (isValid > 0) {
                    System.out.println("Testing Site ID: " + versions.get("testingSiteId").textValue());
                    System.out.println("Start Time: " + startTime);
                    indexList.add(bookingCount);
                }
                else { System.out.println("This booking has lapsed, so it cannot be reverted to."); }
                bookingCount += 1;
            }
        }
        else { System.out.println("The booking you have chosen does not have any versions to revert to!"); }
        return indexList;
    }

    /**
     * Obtains the additionalInfo object of the given userID.
     * @param bookingID the ID of the booking to obtain additionalInfo from.
     */
    public JsonNode obtainAdditionalInfo(String bookingID) throws Exception {
        String url = bookingUrl + "/" + bookingID;
        HttpResponse<String> bookingQuery = html.get(url, "");
        ObjectNode booking = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);

        return booking.get("additionalInfo");
    }

    /**
     * Obtains the desired start time for the user's booking. The format is simplified for simplicity.
     * @return the start time of the booking (yyyy-mm-ddTHH:MM:00)
     */
    public String inputBookingTime() {
        String bookingDate, bookingTime;
        Scanner scanner = new Scanner(System.in);

        // Obtaining the inputs for the specified time.
        System.out.print("Enter Booking Date (Format - yyyy-mm-dd): ");
        bookingDate = scanner.next();
        System.out.print("Enter Booking Time (Format - HH:MM): ");
        bookingTime = scanner.next();

        //Sample Time: 2024-03-18, 15:00
        return bookingDate + "T" + bookingTime + ":00";
    }

    /**
     * Creates the JSONObject required for the PATCH endpoint.
     * @param customerID     the ID of the customer.
     * @param testSiteID     the ID of the test site.
     * @param startTime      the booking's start time.
     * @param status         the status of the booking.
     * @param additionalData the additional data related to the booking.
     * @return a JSONObject with the required data.
     */
    public JSONObject createPatchObject(String customerID, String testSiteID, String startTime,
                                        String status, JsonNode additionalData) throws Exception {

        JSONObject updateData = new JSONObject();

        // Converting additionalData to the JSONObject type.
        // The PATCH endpoint doesn't accept JsonNode objects for its additionalInfo field.
        ObjectMapper mapper = new ObjectMapper();
        JSONObject convertAddInfo = new JSONObject(mapper.writeValueAsString(additionalData));

        // Updating the fields based on the given data.
        updateData.put("customerId", customerID);
        updateData.put("testingSiteId", testSiteID);
        updateData.put("startTime", startTime);
        updateData.put("status", status);
        updateData.put("notes", "N/A");
        updateData.put("additionalInfo", convertAddInfo);

        return updateData;
    }
}
