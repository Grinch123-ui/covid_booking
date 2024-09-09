package application;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The class containing the view data for the different functionalities of the system.
 */
public class WebApplicationView {
    /**
     * Displays the different operations that the user can perform after logging in.
     * @return an integer indicating the operation to be performed
     */
    public void CLIMenu() {
        // title display
        System.out.println(String.format("+%40s+", "").replace(' ', '-'));
        System.out.println(" ".repeat(2) + "Welcome to the COVID-Testing Program!" + " ".repeat(2));
        System.out.println(String.format("+%40s+", "").replace(' ', '-'));

        // printing out the user options
        System.out.println("1) Test Site Searching and Viewing");
        System.out.println("2) Booking Functionalities");
        System.out.println("3) Interview for Test Recommendation");
        System.out.println("4) View User Profile - Can display and adjust on-site and non-lapsed bookings.");
        System.out.println("5) View Admin Booking Interface - For Receptionists only. Can display and adjust non-lapsed bookings.");
        System.out.println("6) Exit");

        System.out.print("Select an operation to perform: " );
    }
}
