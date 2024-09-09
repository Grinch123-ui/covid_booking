package users;

import org.json.JSONException;

public class Customer extends User {
    // Creates an instance of Customer - used for creation of new accounts.
    public Customer(String gName, String fName, String userName, String passWord, String phoneNumber) throws Exception {
        addPartialUserData(gName, fName, userName, passWord, phoneNumber);
        distinguishUser();
        postAccount();
        setUserName(userName);
    }

    // Default empty constructor - used for logins
    public Customer(String userName) {
        setUserName(userName);
    }

    @Override
    public void distinguishUser() throws JSONException {
        jo.put("isCustomer", true);
        jo.put("isAdmin", false);
        jo.put("isHealthcareWorker", false);
    }
}
