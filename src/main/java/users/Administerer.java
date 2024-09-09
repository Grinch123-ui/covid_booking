package users;

import org.json.JSONException;

public class Administerer extends User {
    // Creates an instance of the Administerer
    public Administerer(String gName, String fName, String userName, String passWord, String phoneNumber) throws Exception {
        addPartialUserData(gName, fName, userName, passWord, phoneNumber);
        distinguishUser();
        postAccount();
        setUserName(userName);
    }

    // Default empty constructor - used for logins
    public Administerer(String userName) {
        setUserName(userName);
    }

    @Override
    public void distinguishUser() throws JSONException {
        jo.put("isCustomer", false);
        jo.put("isAdmin", false);
        jo.put("isHealthcareWorker", true);
    }

}
