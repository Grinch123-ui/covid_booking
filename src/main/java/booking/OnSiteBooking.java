package booking;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import javax.print.attribute.standard.JobKOctets;
import java.net.http.HttpResponse;
import java.util.Locale;

/**
 * Concrete class for making on-site bookings.
 */
public class OnSiteBooking extends Booking {
    private HTMLRequestSetup html = new HTMLRequestSetup();

    /**
     * Constructor for the on-site application.booking child class.
     * @param inpCustomerID the ID of the customer that wishes to make a application.booking
     * @param inpTestSiteID the ID of the testing site
     * @param inpStartTime the start time.
     */
    public OnSiteBooking(String inpCustomerID, String inpTestSiteID, String inpStartTime) {
        super(inpCustomerID, inpTestSiteID, inpStartTime);
    }

    /**
     * Creates a application.booking (on-site) for the user based on the provided parameters.
     */
    @Override
    public void createBooking() throws Exception {
        JSONObject bookingToMake = compileBookingData();

        // POST-ing the new booking to the database.
        HttpResponse<String> status = html.post(returnBookingURL(), bookingToMake.toString());

        // Printing out the outcome of the booking creation.
        bookingStatus(status);
    }
}