package users;

/**
 * Factory builder for the user objects.
 */
public class UserFactory {
    //Common User Data
    private String gName, fName, userName, passWord, phoneNumber;

    /**
     * Constructor for the UserFactory class.
     * @param gName the user's given name
     * @param fName the user's first name
     * @param userName the username the user wishes to use in the web application
     * @param passWord the password the user wishes to use in the web application
     * @param phoneNumber the user's phone number.
     */
    public UserFactory(String gName, String fName, String userName, String passWord, String phoneNumber) {
        this.gName = gName;
        this.fName = fName;
        this.userName = userName;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Empty constructor for the UserFactory class. Is used for logins rather than account creation.
     */
    public UserFactory() {}

    /**
     * Creates a different user depending on the value passed as the parameter.
     * @param i the value used in the selection condition to determine the objects created
     * @return the user that is created
     */
    public User createUser(int i) throws Exception {
        // Account creation for new account
        if (i == 1) { return new Customer(gName, fName, userName, passWord, phoneNumber); }
        else if (i == 2) { return new Receptionist(gName, fName, userName, passWord, phoneNumber); }
        else if (i == 3) { return new Administerer(gName, fName, userName, passWord, phoneNumber); }
        else if (i == 4) { return new Patient(gName, fName, userName, passWord, phoneNumber); }
        return null;
    }

    public User createExistingUser(int i, String oldUserName){
        // Account login for existing customer
        if (i == 5) { return new Customer(oldUserName); }
        else if (i == 6) { return new Receptionist(oldUserName); }
        else if (i == 7) { return new Administerer(oldUserName); }
        else if (i == 8) { return new Patient(oldUserName); }
        return null;
    }
}
