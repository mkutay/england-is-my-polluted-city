package infoPopup;

import java.util.List;

import api.PostcodeAPI;
import api.PostcodeResult;
import javafx.stage.Stage;

/**
 * Handles the map click events and shows information popups.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.1
 */
public class MapClickHandler {
    private static final int POPUP_OFFSET_X = 10;
    private static final int POPUP_OFFSET_Y = 10;
    
    private final InfoPopup infoPopup;
    private final Stage primaryStage;
    
    /**
     * Creates a new map click handler.
     * @param infoPopup The popup to show information in.
     * @param primaryStage The primary stage of the application.
     */
    public MapClickHandler(InfoPopup infoPopup, Stage primaryStage) {
        this.infoPopup = infoPopup;
        this.primaryStage = primaryStage;
    }
    
    public void onMapClicked(double latitude, double longitude, double screenX, double screenY, Double pollutionValue) {
        // Update the popup with the clicked location information:
        String address = getAddressFromCoordinates(latitude, longitude);
        infoPopup.update(latitude, longitude, pollutionValue, address);
        
        // Show the popup at the clicked location using the primary stage as owner:
        infoPopup.show(primaryStage, screenX + POPUP_OFFSET_X, screenY + POPUP_OFFSET_Y);
    }
    
    /**
     * Gets the address at the given coordinates.
     * @param latitude The latitude.
     * @param longitude The longitude.
     * @return The address, or null if not available.
     */
    private String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<PostcodeResult> result = PostcodeAPI.fetchPostcodesByLatitudeLongitude(latitude, longitude).getResult();
            if (result == null || result.isEmpty()) {
                System.out.println("No address found for: lat=" + latitude + ", lon=" + longitude);
                return null;
            }
            return result.getFirst().getParliamentary_constituency();
        } catch (Exception e) {
            System.out.println("Failed to get address: " + e.getMessage());
            return null;
        }
    }
}