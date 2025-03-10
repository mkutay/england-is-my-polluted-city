package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import com.google.gson.Gson;

/**
 * Postcode API manager class
 * Used to fetch postcode data from world coordinates
 *
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class PostcodeAPI {
    private static final String POSTCODES_API_BASE_URL = "https://api.postcodes.io/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    
    /**
     * Fetches address data from postcodes.io using latitude and longitude coordinates.
     * @param latitude The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @return PostcodeResponse containing all address data.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static PostcodeResponse fetchPostcodesByLatitudeLongitude(double latitude, double longitude) throws IOException, InterruptedException {
        String url = POSTCODES_API_BASE_URL + "postcodes?lon=" + longitude + "&lat=" + latitude;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), PostcodeResponse.class);
        }
        
        return null;
    }
}
