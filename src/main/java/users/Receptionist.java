package users;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Receptionist extends User {
    // Creates an instance of the Receptionist
    public Receptionist(String gName, String fName, String userName, String passWord, String phoneNumber) throws Exception {
        addPartialUserData(gName, fName, userName, passWord, phoneNumber);
        distinguishUser();
        postAccount();
        setUserName(userName);
    }

    // Default empty constructor - used for logins
    public Receptionist(String userName) {
        setUserName(userName);
    }

    @Override
    public void distinguishUser() throws JSONException {
        jo.put("isCustomer", false);
        jo.put("isAdmin", true);
        jo.put("isHealthcareWorker", false);
    }
}
