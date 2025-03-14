package infoPopup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import api.*;
import dataProcessing.Pollutant;
import javafx.stage.Stage;

/**
 * Handles the map click events and shows information popups.
 * TODO: refactor and likely rename.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class MapClickHandler {
    private static final int POPUP_OFFSET_X = 10;
    private static final int POPUP_OFFSET_Y = 10;
    
    private final InfoPopup infoPopup;
    private final Stage primaryStage;
    
    /**
     * Creates a new map click handler.
     * @param primaryStage The primary stage of the application.
     */
    public MapClickHandler(Stage primaryStage) {
        infoPopup = new InfoPopup();
        this.primaryStage = primaryStage;
    }
    
    public void onMapClicked(double latitude, double longitude, double screenX, double screenY, double width, double height, Double pollutionValue, Pollutant pollutant) {
        // Update the popup with the clicked location information:
        Map<String, String> addressDetails = getAddressFromCoordinates(latitude, longitude);
        Map<String, Object> realtimeDataDetails = getRealtimeData(latitude, longitude);


        infoPopup.update(latitude, longitude, pollutionValue, addressDetails, realtimeDataDetails, pollutant);
        
        if (screenX + infoPopup.getWidth() > width) {
            screenX -= infoPopup.getWidth() + POPUP_OFFSET_X;
        } else {
            screenX += POPUP_OFFSET_X;
        }
        
        if (screenY + infoPopup.getHeight() > height) {
            screenY -= infoPopup.getHeight() + POPUP_OFFSET_Y;
        } else {
            screenY += POPUP_OFFSET_Y;
        }
        // Show the popup at the clicked location using the primary stage as owner:
        infoPopup.show(primaryStage, screenX, screenY);
    }
    
    /**
     * Gets the address at the given coordinates.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @return The address, or null if not available.
     */
    private Map<String, String> getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<PostcodeResult> result = Objects.requireNonNull(PostcodeAPI.fetchPostcodesByLatitudeLongitude(latitude, longitude)).getResult();
            if (result == null || result.isEmpty()) {
                return getDefaultAddress();
            }


            /**
             * Use a hashmap to store first address detail returned in a (key, value) pair.
             * The key is the info type (e.g postcode, borough, etc).
             */
            Map<String, String> addressDetails = new HashMap<>();
            addressDetails.put("borough", result.getFirst().getAdmin_district()); // Borough aka city/district council.
            addressDetails.put("constituency", result.getFirst().getParliamentary_constituency()); // Constituency.
            addressDetails.put("postcode", result.getFirst().getPostcode()); // Postcode returned (e.g WC2B 4BG).
            addressDetails.put("country", result.getFirst().getCountry()); // Country (e.g England, Scotland, Wales, etc).
            return addressDetails;
        } catch (Exception e) {
            System.out.println("Failed to get address: " + e.getMessage());
            return getDefaultAddress();
        }
    }

    private Map<String, Object> getRealtimeData(double latitude, double longitude) {
        try {
            AQICNData AQIData = Objects.requireNonNull(AQICNAPI.getPollutionData(latitude, longitude)).getData();
            Map<String, Object> realTimeData = new HashMap<>();
            realTimeData.put("last_updated", AQIData.getTimeData().getDateTimeString());
            realTimeData.put("aqi_value", AQIData.getAqi());
            realTimeData.put("live_no2", AQIData.getPollutantValues().getNo2().getIAQIValue());
            realTimeData.put("live_pm25", AQIData.getPollutantValues().getPm25().getIAQIValue());
            realTimeData.put("live_pm10", AQIData.getPollutantValues().getPm10().getIAQIValue());
            return realTimeData;
        }
        catch (Exception e) {
            System.out.println("Failed to get live data: " + e.getMessage());
            return getDefaultLiveData();
        }
    }

    private Map<String, String> getDefaultAddress() {
        return Map.of(
                "borough", "Unknown",
                "postcode", "Unknown",
                "country", "Unknown"
        );
    }

    private Map<String, Object> getDefaultLiveData() {
        return Map.of(
                "last_updated", "Unknown",
                "aqi_value", "Unknown",
                "live_no2", "Unknown",
                "live_pm25","Unknown",
                "live_pm10","Unknown"
        );
    }
}