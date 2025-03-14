package api;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class to manage API Calls to AQICN to get real-time data
 * Get info from = https://api.waqi.info/feed/geo:51.508045;-0.128217/?token=9cb6e336b29393762ddc877abb3e43a60b805a5c
 * Adapted from code written by Kutay
 * @author Matthias Loong
 */
public class AQICNAPI {
    private static final String AQICN_API_BASE_URL = "https://api.waqi.info/";
    private static final String AQICN_API_KEY = "9cb6e336b29393762ddc877abb3e43a60b805a5c"; //Key hardcoded for this project specifically to avoid fiddling with env variables
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static AQIResponse getPollutionData(double lat, double lon) throws IOException, InterruptedException{
        String url = AQICN_API_BASE_URL + "feed/geo:" + lat + ";" + lon + "/?token="+AQICN_API_KEY;

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
