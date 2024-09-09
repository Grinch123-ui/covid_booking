package testingsite;

import application.HTMLRequestSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;


public class TestingSite implements Site {
    //URL and HTML setups
    private HTMLRequestSetup html = new HTMLRequestSetup();
    private final String rootUrl = html.returnRootUrl();
    private final String testingSite = rootUrl + "/testing-site";

    private String[] ts_field = {"ID", "Name", "Description", "WebsiteUrl", "Phone Number", "Address", "CreatedAt", "UpdatedAt", "Additional Info"};
    private ObjectNode[] jsonNodes;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // This method will print out all the testing sites available in the system
    public void viewAllTestingSite() throws Exception {
        getSites();

        int counter= 1;
        System.out.println("Testing Sites List");
        System.out.println();

        for (ObjectNode node: jsonNodes){
            System.out.println("Testing Site " + counter);

            int index_1 = 0;
            counter += 1;

            for (JsonNode s: node){
                if (ts_field[index_1].equals("Address"))
                {
                    System.out.println(ts_field[index_1] + ": " + s);
                }
                else
                {
                    System.out.println(ts_field[index_1] + ": " + s.textValue());
                }
                index_1 += 1;
            }
            System.out.println();
        }
        System.out.println();
    }

    // This method will print out all the testing site based on the given suburb name
    public void searchTestingSite() throws Exception{
        getSites();

        while (true){
            System.out.println("q. Quit\n Enter Suburb Search\n");
            String search = reader.readLine();
            int counter = 1;

            for (ObjectNode node: jsonNodes){
                JsonNode address = node.get("address");
                int index_1 = 0;

                if (search.equals(address.get("suburb").textValue())){

                    System.out.println("Testing application.Site " + counter);
                    for(JsonNode node2: node){
                        if (ts_field[index_1].equals("Address"))
                        {
                            System.out.println(ts_field[index_1] + ": " + node2);
                        }
                        else
                        {
                            System.out.println(ts_field[index_1] + ": " + node2.textValue());

                        }
                        index_1 += 1;
                    }
                    System.out.println();
                    counter += 1;
                }
            }
            if (search.equals("q")) {
                break;
            }
        }
    }

    // A new testing site can be created using this method
    public void createTestingSite(String name, String description, String websiteUrl, String phoneNumber, int latitude, int longitude, int unitNumber, String street, String street2, String suburb, int postcode, String state) throws Exception {
        JSONObject additionalInfo = new JSONObject();
        JSONObject additionalInfo_2 = new JSONObject();
        JSONObject address = new JSONObject();
        JSONObject jo = new JSONObject();

        address.put("latitude", latitude);
        address.put("longitude", longitude);
        address.put("unitNumber", unitNumber);
        address.put("street", street);
        address.put("street2", street2);
        address.put("suburb", suburb);
        address.put("state", state);
        address.put("postcode", postcode);
        address.put("additionalInfo", additionalInfo);

        jo.put("name", name);
        jo.put("description", description);
        jo.put("websiteUrl", websiteUrl);
        jo.put("phoneNumber", phoneNumber);
        jo.put("address", address);
        jo.put("additionalInfo", additionalInfo_2);

        if (html.post(testingSite, jo.toString()).statusCode() == 201){
            System.out.println("Successfully created a new testing site");
        }else{
            System.out.println("Wrong Input Details");
        }
    }

    // This method will return all the testing site in JSON format present in the database
    private void getSites() throws Exception {
        HttpResponse<String> temp = html.get(testingSite, "");
        jsonNodes = new ObjectMapper().readValue(temp.body(), ObjectNode[].class);
    }
}
