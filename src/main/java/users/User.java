package users;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public abstract class User {
    // URL and HTTP method setups
    private final HTMLRequestSetup html = new HTMLRequestSetup();
    private final String rootUrl = html.returnRootUrl();
    private final String usersUrl = rootUrl + "/user";
    private final String loginVerify = rootUrl + "/user/login" + "?jwt=true";

    private String userName;
    //Partial JSONObject - will be used to distinguish between user types.
    JSONObject jo = new JSONObject();

    /**
     * Sets certain parameters in the JSON object that differentiates between types of users.
     */
    abstract protected void distinguishUser() throws Exception;

    /**
     * Partially fills up the required fields for the user depending on the entered parameters.
     */
    public void addPartialUserData(String gName, String fName, String userName, String passWord, String phoneNumber) throws JSONException {
        JSONObject additionalInfo = new JSONObject();
        jo.put("givenName", gName);
        jo.put("familyName", fName);
        jo.put("userName", userName);
        jo.put("password", passWord);
        jo.put("phoneNumber", phoneNumber);
        jo.put("additionalInfo", additionalInfo);
    }

    /**
     * Inputs the user account into the web database.
     */
    public void postAccount() throws Exception {
        if (html.post(usersUrl, jo.toString()).statusCode() == 201) {
            System.out.println("Successfully created new account");
            System.out.println();
        } else {
            System.out.println("Failed to create new account");
            System.out.println();
        }
    }

    /**
     * Sets the userName variable to allow for various functions to be performed on the API.
     */
    protected void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Obtaining the given name of the user.
     * @return the given name of the user.
     */
    public String getGivenName() throws Exception {
        HttpResponse<String> users = getAllUser();

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(users.body(), ObjectNode[].class);

        for (ObjectNode node : jsonNodes) {
            if (node.get("userName").textValue().equals(userName)) {
                return node.get("givenName").textValue();
            }
        }
        return null;
    }

    /**
     * Obtaining the given name of the user.
     * @return the user's family name.
     */
    public String getFamilyName() throws Exception {
        HttpResponse<String> users = getAllUser();

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(users.body(), ObjectNode[].class);

        for (ObjectNode node : jsonNodes) {
            if (node.get("userName").textValue().equals(userName)) {
                return node.get("familyName").textValue();
            }
        }
        return null;
    }


    /**
     * Returns the userID of the current user object.
     */
    public String getUserID() throws Exception {
        HttpResponse<String> users = getAllUser();

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(users.body(), ObjectNode[].class);

        for (ObjectNode node : jsonNodes) {
            if (node.get("userName").textValue().equals(userName)) {
                return node.get("id").textValue();
            }
        }
        return null;
    }

    /**
     * Returns all the users present in the database
     */
    private HttpResponse<String> getAllUser() throws Exception {
        return html.get(usersUrl, "");
    }

    /**
     * Attempts to log in to the system and verify the given username and password
     */
    public HttpResponse<String> login(String userName, String passWord) throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("userName", userName);
        jo.put("password", passWord);

        return html.verifyJWT(html.post(loginVerify, jo.toString()));
    }

    /**
     * Obtains all the available bookings for the user with the provided userName.
     * @return a JsonNode list of booking objects.
     */
    public JsonNode obtainAvailableBookings() throws Exception {
        String userBookingURL = usersUrl + "/" + getUserID();
        HttpResponse<String> bookingQuery = html.get(userBookingURL, "?fields=bookings");

        ObjectNode userData = new ObjectMapper().readValue(bookingQuery.body(), ObjectNode.class);
        return userData.get("bookings");
    }

    /**
     * With a given PIN code, searches through the available list of bookings of the current user
     * to check the status of the matching application.booking.
     * @param givenPINCode The PIN code of the application.booking the user wishes to check the status for.
     */
    public void obtainBookingStatus(String givenPINCode) throws Exception {
       JsonNode bookingList = obtainAvailableBookings();

        // Searches through the given user's bookings to find the one with a matching PIN.
        boolean notFound = true;
        for (JsonNode bookings : bookingList) {
            String bookingPin = bookings.get("smsPin").textValue();
            if (bookingPin.equals(givenPINCode)) {
                notFound = false;
                System.out.println("Your booking status with the given PIN is: "
                        + bookings.get("status").textValue());
                break;
            }
        }
        // Returns the status or a message depending on the results.
        if (notFound) {
            System.out.println("There is no booking for the PIN code you have provided.");
        }
    }

    /**
     * Obtains the booking that matches with the given booking ID.
     * @param searchID the ID of the booking to be searched.
     * @return the booking that matches with the given ID.
     */
    public JsonNode searchBooking(String searchID) throws Exception {
        JsonNode bookingList = obtainAvailableBookings();

        // Searches through the given user's bookings to find the one with a matching booking ID.
        for (JsonNode bookings : bookingList) {
            String bookingID = bookings.get("id").textValue();
            if (bookingID.equals(searchID)) {
                return bookings;
            }
        }
        System.out.println("There is no booking with the ID you have provided.");
        return null;
    }
}



