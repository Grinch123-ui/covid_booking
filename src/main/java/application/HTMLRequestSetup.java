package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * A centralized class that contains methods that employ the various key endpoint calls to the API.
 */
public class HTMLRequestSetup {
    private static final String myApiKey = "Enter API key here.";
    private static final String rootUrl = "https://fit3077.com/api/v2";
    private static final String usersVerifyTokenUrl = rootUrl + "/user/verify-token";

    private HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private HttpResponse<String> response;
    private ObjectNode jsonNode;

    public HttpResponse<String> get(String url, String field) throws Exception {
        request = HttpRequest
                .newBuilder(URI.create(url + field))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> post(String url, String body) throws Exception {

        request = HttpRequest.newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;

    }

    public HttpResponse<String> verifyJWT(HttpResponse<String> jwt) throws Exception {
        jsonNode = new ObjectMapper().readValue(jwt.body(), ObjectNode.class);

        String jsonString = "{\"jwt\":\"" + jsonNode.get("jwt").textValue() + "\"}";

        request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    public HttpResponse<String> patch(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public String returnRootUrl() {
        return rootUrl;
    }
}
