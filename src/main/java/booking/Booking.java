package booking;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.net.http.HttpResponse;

/**
 * Abstract class for creating booking objects.
 */
public abstract class Booking {
    // URL and HTTP method setups.
    private HTMLRequestSetup html = new HTMLRequestSetup();
    private final String rootUrl = html.returnRootUrl();
    private final String bookingUrl = rootUrl + "/booking";

    // Required fields for booking creation.
    private String customerID, testingSiteID, startTime;

    /**
     * Constructor for the booking class.
     * @param inpCustomerID the ID of the customer that wishes to make a application.booking
     * @param inpTestingSiteID the ID of the testing site
     * @param inpStartTime the start time.
     */
    public Booking(String inpCustomerID, String inpTestingSiteID, String inpStartTime) {
        customerID = inpCustomerID;
        testingSiteID = inpTestingSiteID;
        startTime = inpStartTime;
    }

    /**
     * Creates the booking object depending on the location (on-site/home)
     */
    abstract void createBooking() throws Exception;

    /**
     * Creates a booking for the user based on the provided parameters.
     */
    public JSONObject compileBookingData() throws Exception {
        JSONObject bookingData = new JSONObject();
        JSONObject additionalInfo = new JSONObject();

        // Required fields: CustomerID, TestingSiteID, Starting Time, Notes, AdditionalInfo.
        bookingData.put("customerId", customerID);
        bookingData.put("testingSiteId", testingSiteID);
        bookingData.put("startTime", startTime);
        bookingData.put("notes", "N/A");
        bookingData.put("additionalInfo", additionalInfo);

        return bookingData;
    }

    /**
     * Obtains the booking ID and SMS PIN and prints them out to the console.
     * @param bookingData A converted booking object from the API.
     */
    private void printPIN_ID(ObjectNode bookingData) {
        // Obtaining the data.
        String ID = bookingData.get("id").textValue();
        String smsPIN = bookingData.get("smsPin").textValue();

        // Printing out the outputs.
        System.out.println("Your Booking ID is: " + ID);
        System.out.println("Your SMS PIN is: " + smsPIN);
    }

    /**
     * Displays the outcome of the booking: whether it succeeded or failed.
     * @param httpOperation the HTTP response from POST-ing a booking to the database.
     */
    protected void bookingStatus(HttpResponse<String> httpOperation) throws JsonProcessingException {
        ObjectNode jsonNode = new ObjectMapper().readValue(httpOperation.body(), ObjectNode.class);
        if (httpOperation.statusCode() == 201) {
            System.out.println("You have successfully made your booking!");
            printPIN_ID(jsonNode);
        }
        else { System.out.println("Failed to make your booking."); }
    }

    /**
     * Getter method for the bookingUrl attribute.
     */
    public String returnBookingURL() {
        return bookingUrl;
    }
}
