package infoPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.PostcodeAPI;
import api.PostcodeResult;
import javafx.stage.Stage;

/**
 * Handles the map click events and shows information popups.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.1
 */
public class MapClickHandler { //TODO refactor and likely rename
    private static final int POPUP_OFFSET_X = 10;
    private static final int POPUP_OFFSET_Y = 10;
    
    private final InfoPopup infoPopup;
    private final Stage primaryStage;
    
    /**
     * Creates a new map click handler.
     * @param primaryStage The primary stage of the application.
     */
    public MapClickHandler( Stage primaryStage) {
        infoPopup = new InfoPopup();
        this.primaryStage = primaryStage;
    }
    
    public void onMapClicked(double latitude, double longitude, double screenX, double screenY, Double pollutionValue) {
        // Update the popup with the clicked location information:
        Map<String, String> addressDetails = getAddressFromCoordinates(latitude, longitude);
        infoPopup.update(latitude, longitude, pollutionValue, addressDetails);
        
        // Show the popup at the clicked location using the primary stage as owner:
        infoPopup.show(primaryStage, screenX + POPUP_OFFSET_X, screenY + POPUP_OFFSET_Y);
    }
    
    /**
     * Gets the address at the given coordinates.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @return The address, or null if not available.
     */
    private Map<String, String> getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<PostcodeResult> result = PostcodeAPI.fetchPostcodesByLatitudeLongitude(latitude, longitude).getResult();
            if (result == null || result.isEmpty()) {
                System.out.println("No address found for: lat=" + latitude + ", lon=" + longitude);
                return null;
            }
            /*
                Use a hashmap to store first address detail returned in a (key, value) pair
                The key is the info type (e.g postcode, borough, etc)
             */

            Map<String, String> addressDetails = new HashMap<>();
            addressDetails.put("borough", result.getFirst().getAdmin_district()); //borough aka city/district council
            addressDetails.put("constituency", result.getFirst().getParliamentary_constituency()); //constituency
            addressDetails.put("postcode", result.getFirst().getPostcode()); //Postcode returned (e.g WC2B 4BG)
            addressDetails.put("country", result.getFirst().getCountry()); //country (e.g England, Scotland, Wales, etc)
            return addressDetails;
        } catch (Exception e) {
            System.out.println("Failed to get address: " + e.getMessage());
            return null;
        }
    }
}