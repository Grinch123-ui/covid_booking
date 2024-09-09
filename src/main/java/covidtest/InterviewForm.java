package covidtest;

import java.util.Scanner;

/**
 * A class that provides a series of questions to the user to gauge the severity of their COVID-19 symptoms.
 */
public class InterviewForm implements TestMethods {
    private Scanner scanner = new Scanner(System.in);
    private int numYes = 0;

    /**
     * Performs input checking on the answers that were given to the questions. A counter is incremented
     * if yes is answered.
     * @param response the question response
     * @return a boolean indicating the validity of the response.
     */
    private boolean checkResponse(String response) {
        boolean validInput = false;
        if (response.equals("y")) {
            numYes += 1;
            validInput = true;
        }
        else if (response.equals("n")) {
            validInput = true;
        }
        return validInput;
    }

    /**
     * Question One. The question would be re-asked if an invalid response is provided.
     */
    private void questionOne() {
        boolean validInput;
        boolean repeatLoop = true;

        do {
            System.out.println("1) Have you exhibited any 2 of the following symptoms: " +
                    "fever, chills, shivering, sore throat?");
            String response = scanner.next().toLowerCase();
            validInput = checkResponse(response);
            if (validInput) { repeatLoop = false; }
            else { System.out.println("You have an inputted an invalid value. Please answer the question again."); }
        } while (repeatLoop);
    }

    /**
     * Question Two. The question would be re-asked if an invalid response is provided.
     */
    private void questionTwo() {
        boolean validInput;
        boolean repeatLoop = true;

        do {
            System.out.println("2) Besides the above, are you exhibiting any of the following: " +
                    "cough, loss of smell, loss of taste?");
            String response = scanner.next().toLowerCase();
            validInput = checkResponse(response);
            if (validInput) { repeatLoop = false; }
            else { System.out.println("You have an inputted an invalid value. Please answer the question again."); }
        } while (repeatLoop);
    }

    /**
     * Question Three. The question would be re-asked if an invalid response is provided.
     */
    private void questionThree() {
        boolean validInput;
        boolean repeatLoop = true;

        do {
            System.out.println("3) Have you visited any areas associated with known COVID-19 clusters?");
            String response = scanner.next().toLowerCase();
            validInput = checkResponse(response);
            if (validInput) { repeatLoop = false; }
            else { System.out.println("You have an inputted an invalid value. Please answer the question again."); }
        } while (repeatLoop);
    }

    /**
     * Question Four. The question would be re-asked if an invalid response is provided.
     */
    private void questionFour() {
        boolean validInput;
        boolean repeatLoop = true;

        do {
            System.out.println("4) Have you travelled abroad within the last 14 days?");
            String response = scanner.next().toLowerCase();
            validInput = checkResponse(response);
            if (validInput) { repeatLoop = false; }
            else { System.out.println("You have an inputted an invalid value. Please answer the question again."); }
        } while (repeatLoop);
    }

    /**
     * Question Five. The question would be re-asked if an invalid response is provided.
     */
    private void questionFive() {
        boolean validInput;
        boolean repeatLoop = true;

        do {
            System.out.println("5) Have you been in close contact with any confirmed or suspected COVID-19" +
                    " cases for the past 14 days?");
            String response = scanner.next().toLowerCase();
            validInput = checkResponse(response);
            if (validInput) { repeatLoop = false; }
            else { System.out.println("You have an inputted an invalid value. Please answer the question again."); }
        } while (repeatLoop);
    }

    /**
     * Compiles all the questions together and runs their respective methods.
     */
    public void questionForm() {
        System.out.println("You would be asked a series of questions. Please answer by inputting Y or N.");
        questionOne();
        questionTwo();
        questionThree();
        questionFour();
        questionFive();
    }

    /**
     * Returns the results from the interview form.
     * @return an integer indicating the number of "yes" responses.
     */
    public int getResults() {
        return numYes;
    }
}
