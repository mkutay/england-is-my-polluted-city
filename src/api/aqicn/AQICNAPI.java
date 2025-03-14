package api.aqicn;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class to manage API Calls to AQICN to get real-time data values.
 * Additional specific information can be get from https://api.waqi.info/feed/geo:51.508045;-0.128217/?token=9cb6e336b29393762ddc877abb3e43a60b805a5c
 * 
 * Adapted from code written by Kutay
 * @author Matthias Loong
 * @version 1.0
 */
public class AQICNAPI {
    private static final String AQICN_API_BASE_URL = "https://api.waqi.info/";
    // API key is hardcoded for this project specifically to avoid fiddling with environment variables.
    private static final String AQICN_API_KEY = "9cb6e336b29393762ddc877abb3e43a60b805a5c";
    
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    /**
     * Fetches pollution data from AQICN using latitude and longitude coordinates.
     * @param latitude The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @return AQIResponse containing all pollution data.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static AQIResponse getPollutionData(double latitude, double longitude) throws IOException, InterruptedException {
        String url = AQICN_API_BASE_URL + "feed/geo:" + latitude + ";" + longitude + "/?token=" + AQICN_API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), AQIResponse.class);
        }

        return null;
    }
}
