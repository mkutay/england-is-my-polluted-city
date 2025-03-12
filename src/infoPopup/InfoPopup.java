package infoPopup;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * A popup that displays information about a location on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class InfoPopup extends Popup {
    private static final String TITLE_STYLE = "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;";
    private static final String CONTENT_STYLE = "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-radius: 4px; -fx-border-radius: 4px;";
    
    private final Label titleLabel;
    private final Label coordinatesLabel;
    private final Label pollutionLabel;

    private final Label boroughLabel;
    private final Label postcodeLabel;
    private final Label countryLabel;
    
    /**
     * Constructor -- Creates a new info popup with an enhanced visual design.
     */
    public InfoPopup() {
        // Initialize labels with appropriate styles:
        titleLabel = new Label("Location Information");
        titleLabel.setStyle(TITLE_STYLE);
        
        coordinatesLabel = new Label();

        pollutionLabel = new Label();

        // Labels associated with address:
        boroughLabel = new Label();
        countryLabel = new Label();
        postcodeLabel = new Label();

        
        // Create drop shadow effect for the popup:
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        
        // Set up the content container:
        VBox content = new VBox(10, titleLabel, coordinatesLabel, pollutionLabel, boroughLabel, countryLabel, postcodeLabel);
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
     */
    public void update(double latitude, double longitude, Double pollutionValue, Map<String, String> addressDetails) {
        // Format coordinate display with appropriate precision:
        coordinatesLabel.setText(String.format("Coordinates: %.6f, %.6f", latitude, longitude));
        String boroughDetails = addressDetails.get("borough");
        String countryDetails = addressDetails.get("country");
        String postcodeDetails = addressDetails.get("postcode");


        // Set pollution information if available:
        if (pollutionValue != null) {
            pollutionLabel.setText(String.format("Pollution level: %.2f ppm", pollutionValue));
            pollutionLabel.setVisible(true);
        } else {
            pollutionLabel.setText("Pollution level: Not available");
            pollutionLabel.setVisible(true);
        }
        
        // Set address information if available:

        if (boroughDetails != null && !boroughDetails.isEmpty()) {
            boroughLabel.setText("County / Borough: " + boroughDetails);
            boroughLabel.setVisible(true);
        } else {
            boroughLabel.setText("County / Borough: Not available");
            boroughLabel.setVisible(true);
        }

        if (countryDetails != null && !countryDetails.isEmpty()) {
            countryLabel.setText("Country: " + countryDetails);
            countryLabel.setVisible(true);
        } else {
            countryLabel.setText("Location: Not available");
            countryLabel.setVisible(true);
        }

        if (postcodeDetails != null && !postcodeDetails.isEmpty()) {
            postcodeLabel.setText("Postcode: " + postcodeDetails);
            postcodeLabel.setVisible(true);
        } else {
            postcodeLabel.setText("Postcode: Not available");
            postcodeLabel.setVisible(true);
        }
    }
}
