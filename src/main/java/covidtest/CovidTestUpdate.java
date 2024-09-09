package covidtest;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.net.http.HttpResponse;

/**
 * A class dedicated to the creation of COVID test objects.
 */
public class CovidTestUpdate {
    // URL and Http method setups
    private HTMLRequestSetup html = new HTMLRequestSetup();
    private final String rootUrl = html.returnRootUrl();
    private final String covidTestUrl = rootUrl + "/covid-test";

    // Required fields for COVID test creation in the database
    private Recommendations testRecommendation = new TestRecommendation(new InterviewForm());
    private String patientID;
    private String administererID;
    private String bookingID;
    private String suitedTest;

    /**
     * Class constructor
     * @param inpPatientID the ID of the patient
     * @param inpAdministererID the ID of the person administering the test.
     */
    public CovidTestUpdate(String inpPatientID, String inpAdministererID) throws Exception {
        // Initializing the instance variables
        patientID = inpPatientID;
        administererID = inpAdministererID;
        obtainBookingID();
        suitedTest = testRecommendation.chooseTestType();

        // Adding the test
        createCovidTest();
    }

    /**
     * Obtaining the booking ID of the patient. The COVID test would be added to the first booking in
     * the returned JSON list.
     */
    public void obtainBookingID() throws Exception {
        // Obtaining the list of bookings with the given userID
        String userBookingUrl = rootUrl + "/user/" + patientID;
        HttpResponse<String> bookingQuery = html.get(userBookingUrl, "?fields=bookings");

        ObjectNode userData = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);
        JsonNode bookingList = userData.get("bookings");

        // Taking the id from the first booking in the list. Method is inefficient.
        for (JsonNode bookings: bookingList) {
            bookingID = bookings.get("id").textValue();
            break;
        }
    }

    /**
     * Posts the test to the database using the data that was obtained.
     */
    public void createCovidTest() throws Exception {
        JSONObject covidTestData = new JSONObject();
        JSONObject additionalInfo = new JSONObject();

        // Required fields: CustomerID, TestingSiteID, Starting Time, Notes, AdditionalInfo.
        covidTestData.put("type", suitedTest)
                .put("patientId", patientID)
                .put("administererId", administererID)
                .put("bookingId", bookingID)
                .put("result", "PENDING")
                .put("status", "CREATED")
                .put("notes", "N/A")
                .put("additionalInfo", additionalInfo);

        if (html.post(covidTestUrl, covidTestData.toString()).statusCode() == 201){
            System.out.println("You have successfully added a COVID test to your booking! ");
            System.out.println("Your recommended test is: " + suitedTest);
        }
        else { System.out.println("Failed in adding the COVID test to your booking!"); }
    }
}
