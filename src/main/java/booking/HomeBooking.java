package booking;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.net.http.HttpResponse;

/**
 * Concrete class for making bookings from home.
 */
public class HomeBooking extends Booking {
    // Dummy QR and URL creator.
    private String dummyURL, dummyQR;
    private HTMLRequestSetup html = new HTMLRequestSetup();

    /**
     * Constructor for the home application.booking child class.
     * @param inpCustomerID the ID of the customer that wishes to make a application.booking
     * @param inpTestSiteID the ID of the testing site
     * @param inpStartTime the start time.
     */
    public HomeBooking(String inpCustomerID, String inpTestSiteID, String inpStartTime)  {
        super(inpCustomerID, inpTestSiteID, inpStartTime);
    }

    /**
     * Creates a application.booking (home) for the user based on the provided parameters.
     */
    @Override
    public void createBooking() throws Exception {
        JSONObject bookingToMake = compileBookingData();
        generateDummy();

        // Placing the generated data in additionalInfo.
        JSONObject additionalInfo = (JSONObject) bookingToMake.get("additionalInfo");
        additionalInfo.put("QRCode", dummyQR);
        additionalInfo.put("URL", dummyURL);

        // POST-ing the new booking to the database.
        HttpResponse<String> status = html.post(returnBookingURL(), bookingToMake.toString());

        // Printing out the outcome of the booking creation.
        bookingStatus(status);

    }

    /**
     * Generates a dummy URL and QR code to be place in additionalInfo.
     */
    private void generateDummy() {
        // Producing the URL and QR code.
        DummyGenerator dummyGenerator = new DummyGenerator();
        dummyQR = dummyGenerator.generateQR();
        dummyURL = dummyGenerator.generateRandomURL();
    }
}
