package application;

import booking.*;
import covidtest.CovidTestUpdate;
import login.Login;
import profile.AdminProfileFacade;
import profile.PatientProfileFacade;
import profile.ProfileFacade;
import testingsite.TestingSite;
import testingsite.TestingSiteFacade;
import users.Receptionist;
import users.Administerer;
import users.User;
import users.UserFactory;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The class consisting of the functions for the CLI within the program.
 */
public class WebApplicationController {
    private User currentUser;
    private Login loginOps = new Login();
    private WebApplicationView webApplicationView;

    /**
     * Creating the various facade classes to perform the different functionalities on.
     */
    private TestingSiteFacade testSiteOps;
    private BookingFacade bookingOps;
    private ProfileFacade patientProfileOps;
    private ProfileFacade adminProfileOps;

    /**
     * Variables for covid tests. The administerer ID is hard-coded to ease testing.
     * The ID refers to Mary Brown, which is also a health care worker.
     */
    private String administererID = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";
    private CovidTestUpdate covidTesting;

    //-------------------------------------------------------------------------------------------------
    // Constructor.
    public WebApplicationController(WebApplicationView view) throws Exception {
        // Login title display
        System.out.println(String.format("+%30s+", "").replace(' ', '-'));
        System.out.println(" ".repeat(4) + "Login/Account Creation" + " ".repeat(2));
        System.out.println(String.format("+%30s+", "").replace(' ', '-'));

        // Performing the login and initializing the current user.
        loginOps.performLogin();
        currentUser = loginOps.returnCurrentUser();

        // Initializing the different facade classes.
        testSiteOps = new TestingSiteFacade(currentUser);
        bookingOps = new BookingFacade(currentUser);
        patientProfileOps = new PatientProfileFacade(currentUser);
        adminProfileOps = new AdminProfileFacade(currentUser);

        // Initializing the View object.
        webApplicationView = view;
    }

    /**
     * Executes the program. This comprises of all the functionalities defined in the spec.
     */
    public void runProgram() throws Exception {
        new BufferedReader(new InputStreamReader(System.in));

        // Shows a menu indicating the available options and executes it.
        int menuOption;
        boolean continueMenu = true;
        do {
            webApplicationView.CLIMenu();
            menuOption = processChoice();
            // userName and the currentUser would be properly initialized after login.
            if (menuOption == 1) {
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                System.out.println(" ".repeat(2) + "Test Site Search & Viewing" + " ".repeat(2));
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                testSiteOps.startSearchFunc();
            }
            else if (menuOption == 2) {
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                System.out.println(" ".repeat(2) + "Booking Functionalities" + " ".repeat(2));
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                bookingOps.startBooking();
            }
            else if (menuOption == 3) {
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                System.out.println(" ".repeat(2) + "Test Recommendation Interview" + " ".repeat(2));
                System.out.println(String.format("+%30s+", "").replace(' ', '-'));
                covidTesting = new CovidTestUpdate(currentUser.getUserID(), administererID); }
            else if (menuOption == 4) {
                System.out.println(String.format("+%15s+", "").replace(' ', '-'));
                System.out.println(" ".repeat(2) + "User Profile" + " ".repeat(2));
                System.out.println(String.format("+%15s+", "").replace(' ', '-'));
                patientProfileOps.startProfileOp();
            }
            else if (menuOption == 5) {
                System.out.println(String.format("+%15s+", "").replace(' ', '-'));
                System.out.println(" ".repeat(2) + "Admin Profile" + " ".repeat(2));
                System.out.println(String.format("+%15s+", "").replace(' ', '-'));
                if (currentUser instanceof Receptionist) {
                    adminProfileOps.startProfileOp();
                }
                else { System.out.println("You don't have the proper permissions to view this profile."); }
            }
            else if (menuOption == 6) {
                System.out.println("THANK YOU!");
                continueMenu = false; }
            else { System.out.println("You have inputted a out-of-range number or invalid character."); }
        } while (continueMenu);
    }

    /**
     * Obtains an integer input from the user to be processed into which functionality to run.
     * @return an integer indicating the user's choice.
     */
    public int processChoice() {
        int choice;
        Scanner scanner = new Scanner(System.in);
        try { choice = scanner.nextInt(); }
        catch(InputMismatchException e) { choice = -1; }
        return choice;
    }

}
