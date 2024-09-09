package testingsite;
import users.Administerer;
import users.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestingSiteFacade {
    private User currentUser;
    private TestingSite testingSite;

    // Constructor.
    public TestingSiteFacade(User currentUser) {
        this.currentUser = currentUser;
        testingSite = new TestingSite();
    }

    /**
     * Prints out the menu options for the test site search.
     */
    private void printTestSiteOptions() {
        // Listing out the search test site options.
        System.out.println("Select the test site operation you wish to perform.");
        System.out.println("1. View All Testing Sites.") ;
        System.out.println("2. Search Testing Site.");
        System.out.println("3. Create new Testing Site - Admins/Receptionists only.");
        System.out.println("4. Back.");
    }

    /**
     * Prompts the user to enter all the required information for creating a new test site.
     * @return a list with all the required fields.
     */
    private String[] enterTestSiteData() throws IOException {
        String[] testSiteData = new String[12];

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter Site Name");
        testSiteData[0] = reader.readLine();

        System.out.println("Enter Site Description");
        testSiteData[1] = reader.readLine();

        System.out.println("Enter Website URL");
        testSiteData[2] = reader.readLine();

        System.out.println("Enter Phone Number");
        testSiteData[3] = reader.readLine();

        System.out.println("Enter Longitude");
        testSiteData[4] = reader.readLine();

        System.out.println("Enter Latitude");
        testSiteData[5] = reader.readLine();

        System.out.println("Enter Unit Number");
        testSiteData[6] = reader.readLine();

        System.out.println("Enter Street");
        testSiteData[7] = reader.readLine();

        System.out.println("Enter Street2");
        testSiteData[8] = reader.readLine();

        System.out.println("Enter Suburb");
        testSiteData[9] = reader.readLine();

        System.out.println("Enter State");
        testSiteData[10] = reader.readLine();

        System.out.println("Enter Post Code");
        testSiteData[11] = reader.readLine();

        return testSiteData;
    }

    /**
     * Performs the functionality related to test sites.
     */
    public void startSearchFunc() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // The customer can choose to the following options
        while (true){
            printTestSiteOptions();
            String choice = reader.readLine();

            // View All testing site
            if (choice.equals("1")){ testingSite.viewAllTestingSite(); }
            // Search testing site based on suburb
            else if (choice.equals("2")){ testingSite.searchTestingSite(); }//
            else if (choice.equals("3") && currentUser instanceof Administerer){
                String[] testSiteData = enterTestSiteData();
                try {
                    testingSite.createTestingSite(
                            testSiteData[0],
                            testSiteData[1],
                            testSiteData[2],
                            testSiteData[3],
                            Integer.parseInt(testSiteData[4]),
                            Integer.parseInt(testSiteData[5]),
                            Integer.parseInt(testSiteData[6]),
                            testSiteData[7],
                            testSiteData[8],
                            testSiteData[9],
                            Integer.parseInt(testSiteData[10]),
                            testSiteData[11]);
                }
                catch (Exception e){ System.out.println("Wrong Input Details"); }
            }
            // Quit
            else if (choice.equals("4")){ break; }
        }
    }
}
