package covidtest;

/**
 * Provides a recommendation on the type of test to take depending on the results obtained from the interview
 * form.
 */
public class TestRecommendation extends Recommendations {
    private String recommendedTestType;
    private TestMethods testMethod;

    /**
     * A constructor for the class. The interview is conducted upon creation.
     */
    public TestRecommendation(TestMethods providedTestMethod) {
        super(providedTestMethod);
        testMethod = retTestMethods();
    }

    /**
     * Obtains the results from the test method used.
     */
    @Override
    public void obtainResults() {
        testMethod.questionForm();
        setResults(testMethod.getResults());
    }

    /**
     * Sets the recommended test types based on the number of "yes" answers provided in the interview.
     * @return String of the test type.
     */
    @Override
    public String chooseTestType() {
        // Running the interview form and obtaining the results.
        testMethod.questionForm();
        int results = testMethod.getResults();

        // If the user displays many COVID-19 symptoms (3+), they would be recommended to take a PCR test.
        if (results >= 3) { recommendedTestType = TestTypes.PCR.toString(); }
        else { recommendedTestType = TestTypes.RAT.toString(); }
        return recommendedTestType;
    }
}
