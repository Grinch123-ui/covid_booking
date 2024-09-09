package testingsite;

public interface Site {
    void createTestingSite(String name, String description, String websiteUrl, String phoneNumber, int latitude, int longitude, int unitNumber, String street, String street2, String suburb, int postcode, String state) throws Exception;
}
