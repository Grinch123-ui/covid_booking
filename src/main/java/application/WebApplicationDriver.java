package application;

/**
 * Main class for running the program.
 */
public class WebApplicationDriver {
    public static void main(String[] args) throws Exception {
        WebApplicationView newView = new WebApplicationView();
        WebApplicationController testApplication = new WebApplicationController(newView);
        testApplication.runProgram();
    }
}
