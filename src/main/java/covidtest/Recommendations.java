package covidtest;

/**
 * Parent class for the recommendations. This is used in case different methods aside from PCR/RAT are used
 * in any extensions.
 */
public abstract class Recommendations {
    private TestMethods testMethod;
    private int results;

    public Recommendations(TestMethods providedTestMethod) {
        testMethod = providedTestMethod;
    }

    abstract public void obtainResults();

    abstract public String chooseTestType();

    public TestMethods retTestMethods() {
        return testMethod;
    }

    public void setResults(int value) {
        results = value;
    }



}
