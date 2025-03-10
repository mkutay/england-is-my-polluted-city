package infoPopup;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * A popup that displays information about a location on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.1
 */
public class InfoPopup extends Popup {
    private static final String TITLE_STYLE = "-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333333;";
    private static final String CONTENT_STYLE = "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-background-radius: 4px; -fx-border-radius: 4px;";
    
    private final Label titleLabel;
    private final Label coordinatesLabel;
    private final Label pollutionLabel;
    private final Label addressLabel;
    
    /**
     * Constructor -- Creates a new info popup with an enhanced visual design.
     */
    public InfoPopup() {
        // Initialize labels with appropriate styles
        titleLabel = new Label("Location Information");
        titleLabel.setStyle(TITLE_STYLE);
        
        coordinatesLabel = new Label();
        pollutionLabel = new Label();
        addressLabel = new Label();
        
        // Create drop shadow effect for the popup
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        
        // Set up the content container
        VBox content = new VBox(10, titleLabel, coordinatesLabel, pollutionLabel, addressLabel);
        content.setPadding(new Insets(12));
        content.setStyle(CONTENT_STYLE);
        content.setEffect(dropShadow);
        
        this.getContent().add(content);

        this.setAutoHide(true);
    }
    
    /**
     * Updates the popup with information about a location.
     * 
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param pollutionValue The pollution value at the location, or null if unknown.
     * @param address The address of the location, or null if unknown.
     */
    public void update(double latitude, double longitude, Double pollutionValue, String address) {
        // Format coordinate display with appropriate precision
        coordinatesLabel.setText(String.format("Coordinates: %.6f, %.6f", latitude, longitude));
        
        // Set pollution information if available
        if (pollutionValue != null) {
            pollutionLabel.setText(String.format("Pollution level: %.2f ppm", pollutionValue));
            pollutionLabel.setVisible(true);
        } else {
            pollutionLabel.setText("Pollution level: Not available");
            pollutionLabel.setVisible(true);
        }
        
        // Set address information if available
        if (address != null && !address.isEmpty()) {
            addressLabel.setText("Location: " + address);
            addressLabel.setVisible(true);
        } else {
            addressLabel.setText("Location: Not available");
            addressLabel.setVisible(true);
        }
    }
}
