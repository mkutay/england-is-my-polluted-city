package infoPopup;

import dataProcessing.Pollutant;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * A popup that displays information about a location on the map.
 * 
 * Refactor by Mehmet Kutay Bozkurt
 * @author Mehmet Kutay Bozkurt and Matthias Loong
 * @version 4.0
 */
public class InfoPopup extends Popup {
    private static final String TITLE_STYLE = "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;";
    private static final String CONTENT_STYLE = "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-radius: 4px; -fx-border-radius: 4px;";
    private static final String LABEL_STYLE = "-fx-font-weight: bold";

    private final LocationInfo locationInfo;
    private final RealtimeInfo realtimeInfo;
    
    /**
     * Constructor -- Creates a new info popup.
     */
    public InfoPopup() {
        // Create drop shadow effect for the popup:
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);

        this.locationInfo = new LocationInfo(LABEL_STYLE, TITLE_STYLE);
        this.realtimeInfo = new RealtimeInfo(LABEL_STYLE, TITLE_STYLE);
        
        // Set up the content container:
        VBox content = new VBox(10, locationInfo.getContent(), realtimeInfo.getContent());
        content.setPadding(new Insets(12));
        content.setStyle(CONTENT_STYLE);
        content.setEffect(dropShadow);
        
        this.getContent().add(content);

        this.setAutoHide(true);
    }
    
    /**
     * Updates the popup with information about a location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param pollutionValue The pollution value at the location, or null if unknown.
     * @param addressDetails A Map containing the address details of the queried point, or null if unknown.
     * @param pollutant The pollutant type.
     */
    public void update(double latitude, double longitude, Double pollutionValue, ShownLocationData addressDetails, ShownRealtimeData realtimeDataDetails, Pollutant pollutant) {
        locationInfo.update(latitude, longitude, pollutionValue, addressDetails);
        realtimeInfo.update(realtimeDataDetails, pollutant);
    }
}