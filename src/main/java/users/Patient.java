package users;

import org.json.JSONException;

public class Patient extends User {
    // Creates an instance of the Patient
    public Patient(String gName, String fName, String userName, String passWord, String phoneNumber) throws Exception {
        addPartialUserData(gName, fName, userName, passWord, phoneNumber);
        distinguishUser();
        postAccount();
        setUserName(userName);
    }

    // Default empty constructor - used for logins
    public Patient(String userName) {
        setUserName(userName);
    }

    @Override
    public void distinguishUser() throws JSONException {
        jo.put("isCustomer", true);
        jo.put("isAdmin", false);
        jo.put("isHealthcareWorker", false);
    }
}
