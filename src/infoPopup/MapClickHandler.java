package infoPopup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import api.aqicn.AQICNAPI;
import api.aqicn.AQICNData;
import api.postcode.PostcodeAPI;
import api.postcode.PostcodeResult;
import dataProcessing.Pollutant;
import javafx.stage.Stage;

/**
 * Handles the map click events and shows information popups.
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
    
    /**
     * Called when the map is clicked with the given information.
     * Updates the information popup and shows it.
     */
    public void onMapClicked(double latitude, double longitude, double screenX, double screenY, double width, double height, Double pollutionValue, Pollutant pollutant) {
        // Update the popup with the clicked location information:
        ShownLocationData addressDetails = getAddressFromCoordinates(latitude, longitude);
        ShownRealtimeData realtimeDataDetails = getRealtimeData(latitude, longitude);

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
     * @return The address, or default unknown values if not available.
     */
    private ShownLocationData getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<PostcodeResult> result = Objects.requireNonNull(PostcodeAPI.fetchPostcodesByLatitudeLongitude(latitude, longitude)).getResult();
            if (result == null || result.isEmpty()) return null;

            String borough = result.getFirst().getAdmin_district(); // Borough aka city/district council.
            String constituency = result.getFirst().getParliamentary_constituency(); // Constituency.
            String postCode = result.getFirst().getPostcode(); // Postcode returned (e.g WC2B 4BG).
            String country = result.getFirst().getCountry(); // Country (e.g England, Scotland, Wales, etc).

            return new ShownLocationData(borough, constituency, postCode, country);
        } catch (Exception e) {
            System.out.println("Failed to get address: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the real-time pollution data at the given coordinates.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @return The real-time pollution data, or default unknown values if not available.
     */
    private ShownRealtimeData getRealtimeData(double latitude, double longitude) {
        try {
            AQICNData AQIData = Objects.requireNonNull(AQICNAPI.getPollutionData(latitude, longitude)).getData();

            String lastUpdated = AQIData.getTimeData().getDateTimeString();
            Integer aqiValue = AQIData.getAqi();
            Map<Pollutant, String> pollutantValues = new HashMap<>();

            var no2Data = AQIData.getPollutantValues().getNo2();
            pollutantValues.put(Pollutant.NO2, no2Data != null ? String.valueOf(no2Data.getIAQIValue()) : null);

            var pm10Data = AQIData.getPollutantValues().getPm10();
            pollutantValues.put(Pollutant.PM10, pm10Data != null ? String.valueOf(pm10Data.getIAQIValue()) : null);

            var pm25Data = AQIData.getPollutantValues().getPm25();
            pollutantValues.put(Pollutant.PM2_5, pm25Data != null ? String.valueOf(pm25Data.getIAQIValue()) : null);

            return new ShownRealtimeData(lastUpdated, String.valueOf(aqiValue), pollutantValues);
        }
        catch (Exception e) {
            System.out.println("Failed to get live data: " + e.getMessage());
            return null;
        }
    }
}