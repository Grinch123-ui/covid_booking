package login;

import users.User;
import users.UserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Login {
    // Variables for user creation and login
    private String givenName, firstName, userName, password, phoneNum;
    private UserFactory userFactory;
    private User currentUser;

    /**
     * Prints out the menu options for the booking functionalities.
     */
    private void printLoginOptions() {
        // Listing out the login options.
        System.out.println("1. Login as Customer.");
        System.out.println("2. Login as Administrator or Receptionist.");
        System.out.println("3. Login as Administerer.");
        System.out.println("4. Login as Patient.");
        System.out.println("5. Create a new account.");
        System.out.println("6. Quit.");
    }

    /**
     * Performs the functionality for logins/account creation
     */
    public void performLogin() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            // Printing out the user roles.
            printLoginOptions();
            String userChoice = reader.readLine();
            int choice = 1000;

            try{
                choice = Integer.parseInt(userChoice);
            }
            catch (Exception e){
                System.out.println("Please Enter Valid Choice");
            }

            // USER LOGIN
            if (choice > 0 && choice < 5) {
                //The system will verify the entered username and password
                System.out.println("Enter Username");
                userName = reader.readLine();
                System.out.println("Enter Password");
                password = reader.readLine();

                // Initializing the user factory for logins.
                userFactory = new UserFactory();
                // Customer login
                if (choice == 1) { currentUser = userFactory.createExistingUser(5, userName); }
                // Admin/Receptionist login
                else if (choice == 2) { currentUser = userFactory.createExistingUser(6, userName); }
                // Administerer login
                else if (choice == 3) { currentUser = userFactory.createExistingUser(7, userName); }
                // Patient login
                else if (choice == 4) { currentUser = userFactory.createExistingUser(8, userName); }

                //Once the verification process if completed, the customer will be brought to the new customer page
                try {
                    currentUser.login(userName, password);
                    break;
                }
                catch (Exception e){ System.out.println("Wrong Username or Password"); }
            }
            // Creation of new accounts
            else if (choice == 5){
                try {
                    // Message printing and obtaining user information
                    System.out.println("Select the role that your account would take:");
                    System.out.println("1. Customer \n2. Administrator or Receptionist \n3. Administerer \n4. Patient");
                    int choice2 = Integer.parseInt(reader.readLine());
                    obtainUserInfo();
                    userFactory = new UserFactory(givenName, firstName, userName, password, phoneNum);

                    // Customer creation
                    if (choice2 == 1) { currentUser = userFactory.createUser(1); }
                    // Admin/Receptionist creation
                    else if (choice2 == 2) { currentUser = userFactory.createUser(2); }
                    // Administerer creation
                    else if (choice2 == 3) { currentUser = userFactory.createUser(3); }
                    // Patient creation
                    else if (choice2 == 4) { currentUser = userFactory.createUser(4); }
                }
                catch (Exception e){
                    System.out.println("Failed to create new account");
                }
            }
            // Loop Termination
            else if (choice == 6) { break; }
        }
    }

    /**
     * Obtains the user's information.
     */
    public void obtainUserInfo() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" ~~ Please enter your information ~~ ");

        System.out.println("Enter Given Name:");
        givenName = reader.readLine();

        System.out.println("Enter Family Name:");
        firstName = reader.readLine();

        System.out.println("Enter Username:");
        userName = reader.readLine();

        System.out.println("Enter Password:");
        password = reader.readLine();

        System.out.println("Enter Phone Number:");
        phoneNum = reader.readLine();
    }

    /**
     * Returns the user to be used for the different operations in the database.
     * @return the user object to perform operations on.
     */
     public User returnCurrentUser() {
        return currentUser;
    }
}
