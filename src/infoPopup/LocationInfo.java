package infoPopup;

import com.gluonhq.maps.MapPoint;

import dataProcessing.Pollutant;
import utility.GeographicUtilities;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * A class that stores all the information about the location for the info popup.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class LocationInfo {
    private final VBox content;

    private final Label titleLabel;

    // TextFlows:
    private final TextFlow gridCodeFlow;
    private final TextFlow eastingNorthingFlow;
    private final TextFlow coordinatesFlow;
    private final TextFlow pollutionFlow;
    private final TextFlow boroughFlow;
    private final TextFlow countryFlow;
    private final TextFlow postcodeFlow;

    // Labels:
    private final Label gridCodeLabel;
    private final Label eastingNorthingLabel;
    private final Label coordinatesLabel;
    private final Label pollutionLabel;
    private final Label boroughLabel;
    private final Label postcodeLabel;
    private final Label countryLabel;

    // Information Labels:
    private final Label gridCodeInformation;
    private final Label eastingNorthingInformation;
    private final Label coordinatesInformation;
    private final Label pollutionInformation;
    private final Label boroughInformation;
    private final Label postcodeInformation;
    private final Label countryInformation;

    /**
     * Constructor -- Creates a new location info popup.
     */
    public LocationInfo(String labelStyle, String titleStyle) {
        gridCodeFlow = new TextFlow();
        eastingNorthingFlow = new TextFlow();
        coordinatesFlow = new TextFlow();
        pollutionFlow = new TextFlow();
        boroughFlow = new TextFlow();
        postcodeFlow = new TextFlow();
        countryFlow = new TextFlow();

        titleLabel = new Label("Location Information");
        titleLabel.setStyle(titleStyle);
        gridCodeLabel = new Label("Grid Code: ");
        gridCodeLabel.setStyle(labelStyle);
        eastingNorthingLabel = new Label("Easting / Northing: ");
        eastingNorthingLabel.setStyle(labelStyle);
        coordinatesLabel = new Label("Coordinates: ");
        coordinatesLabel.setStyle(labelStyle);
        pollutionLabel = new Label("Pollution Level: ");
        pollutionLabel.setStyle(labelStyle);
        boroughLabel = new Label("Borough / County: ");
        boroughLabel.setStyle(labelStyle);
        countryLabel = new Label("Country: ");
        countryLabel.setStyle(labelStyle);
        postcodeLabel = new Label("Postal Code: ");
        postcodeLabel.setStyle(labelStyle);

        gridCodeInformation = new Label();
        eastingNorthingInformation = new Label();
        coordinatesInformation = new Label();
        pollutionInformation = new Label();
        boroughInformation = new Label();
        countryInformation = new Label();
        postcodeInformation = new Label();

        gridCodeFlow.getChildren().addAll(gridCodeLabel, gridCodeInformation);
        eastingNorthingFlow.getChildren().addAll(eastingNorthingLabel, eastingNorthingInformation);
        coordinatesFlow.getChildren().addAll(coordinatesLabel, coordinatesInformation);
        pollutionFlow.getChildren().addAll(pollutionLabel, pollutionInformation);
        boroughFlow.getChildren().addAll(boroughLabel, boroughInformation);
        countryFlow.getChildren().addAll(countryLabel, countryInformation);
        postcodeFlow.getChildren().addAll(postcodeLabel, postcodeInformation);

        content = new VBox(10, titleLabel, gridCodeFlow, eastingNorthingFlow, coordinatesFlow, pollutionFlow, boroughFlow, countryFlow, postcodeFlow);
    }

    /**
     * Updates the location information in this content.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param pollutionValue The pollution value at the location, or null if unknown.
     * @param addressDetails A Map containing the address details of the queried point, or null if unknown.
     */
    public void update(double latitude, double longitude, int gridCode, Double pollutionValue, ShownLocationData addressDetails) {
        gridCodeInformation.setText("" + gridCode);

        int[] eastingNorthing = GeographicUtilities.convertLatLonToEastingNorthing(new MapPoint(latitude, longitude));
        eastingNorthingInformation.setText(String.format("%d / %d", eastingNorthing[0], eastingNorthing[1]));

        // Format coordinate display with appropriate precision:
        coordinatesInformation.setText(String.format("%.6f, %.6f", latitude, longitude));

        // Set location information if available:
        if (addressDetails != null) {
            updateLocation(addressDetails);
        } else {
            boroughInformation.setText("Not Available");
            countryInformation.setText("Not Available");
            postcodeInformation.setText("Not Available");
        }

        // Set pollution value information if available:
        if (pollutionValue != null) {
            pollutionInformation.setText(String.format("%.2f %s", pollutionValue, Pollutant.UNITS)); // Displays microgram per meter cubed.
        } else {
            pollutionInformation.setText("Not Available");
        }
    }

    /**
     * Updates the location information in the popup.
     * @param addressDetails The address details.
     */
    private void updateLocation(ShownLocationData addressDetails) {
        String boroughDetails = addressDetails.borough();
        String countryDetails = addressDetails.country();
        String postcodeDetails = addressDetails.postCode();

        if (boroughDetails != null) {
            boroughInformation.setText(boroughDetails);
        } else {
            boroughInformation.setText("Not Available");
        }

        if (countryDetails != null) {
            countryInformation.setText(countryDetails);
        } else {
            countryInformation.setText("Not Available");
        }

        if (postcodeDetails != null) {
            postcodeInformation.setText(postcodeDetails);
        } else {
            postcodeInformation.setText("Not Available");
        }
    }

    /**
     * @return The content of this location info popup.
     */
    public VBox getContent() {
        return content;
    }
}
