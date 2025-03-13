package infoPopup;

import dataProcessing.Pollutant;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
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
    private static final String LABEL_STYLE = "-fx-font-weight: bold";


    //Declare TextFlow to store the labels of the popup
    private final TextFlow coordinatesFlow;
    private final TextFlow pollutionFlow;
    private final TextFlow boroughFlow;
    private final TextFlow postcodeFlow;
    private final TextFlow countryFlow;


    private final Label titleLabel;
    private final Label coordinatesLabel;
    private final Label pollutionLabel;
    private final Label boroughLabel;
    private final Label postcodeLabel;
    private final Label countryLabel;

    private final Label coordinatesInformation;
    private final Label pollutionInformation;
    private final Label boroughInformation;
    private final Label postcodeInformation;
    private final Label countryInformation;

    private String boroughDetails;
    private String countryDetails;
    private String postcodeDetails;
    
    /**
     * Constructor -- Creates a new info popup with an enhanced visual design.
     */
    public InfoPopup() {
        // Initialize labels with appropriate styles:
        titleLabel = new Label("Location Information");
        titleLabel.setStyle(TITLE_STYLE);

        //initialise TextFlows
        coordinatesFlow = new TextFlow();
        pollutionFlow = new TextFlow();
        boroughFlow = new TextFlow();
        postcodeFlow = new TextFlow();
        countryFlow = new TextFlow();

        //initialise Labels that will be displayed in BOLD (e.g Postal Code:, Country: )
        coordinatesLabel = new Label("Coordinates: ");
        coordinatesLabel.setStyle(LABEL_STYLE);
        pollutionLabel = new Label("Pollution Level: ");
        pollutionLabel.setStyle(LABEL_STYLE);
        boroughLabel = new Label("Borough / County: ");
        boroughLabel.setStyle(LABEL_STYLE);
        countryLabel = new Label("Country: ");
        countryLabel.setStyle(LABEL_STYLE);
        postcodeLabel = new Label("Postal Code: ");
        postcodeLabel.setStyle(LABEL_STYLE);

        //initialise the Labels to store information about the selected MapPoint
        coordinatesInformation = new Label();
        pollutionInformation = new Label();
        boroughInformation = new Label();
        countryInformation = new Label();
        postcodeInformation = new Label();

        //Add the labels and their information to their respective TextFlows
        coordinatesFlow.getChildren().addAll(coordinatesLabel, coordinatesInformation);
        pollutionFlow.getChildren().addAll(pollutionLabel, pollutionInformation);
        boroughFlow.getChildren().addAll(boroughLabel, boroughInformation);
        countryFlow.getChildren().addAll(countryLabel, countryInformation);
        postcodeFlow.getChildren().addAll(postcodeLabel, postcodeInformation);




        // Create drop shadow effect for the popup:
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        
        // Set up the content container:
        VBox content = new VBox(10, titleLabel, coordinatesFlow, pollutionFlow , boroughFlow, countryFlow, postcodeFlow);
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
        coordinatesInformation.setText(String.format("%.6f, %.6f", latitude, longitude));
        if (addressDetails !=null){
            boroughDetails = addressDetails.get("borough");
            countryDetails = addressDetails.get("country");
            postcodeDetails = addressDetails.get("postcode");
        }

        // Set pollution information if available:
        if (pollutionValue != null) {
            pollutionInformation.setText(String.format("%.2f" + Pollutant.UNITS, pollutionValue)); //displays micrograms per meter cubed
        } else {
            pollutionInformation.setText("Not available");
        }
        
        // Set address information if available:
        if (addressDetails != null && boroughDetails != null && !boroughDetails.isEmpty()) {
            boroughInformation.setText(boroughDetails);
        } else {
            boroughInformation.setText("Not available");
        }

        if (addressDetails != null && countryDetails != null && !countryDetails.isEmpty()) {
            countryInformation.setText(countryDetails);

        } else {
            countryInformation.setText("Not available");
        }

        if (addressDetails != null && postcodeDetails != null && !postcodeDetails.isEmpty()) {
            postcodeInformation.setText(postcodeDetails);
        } else {
            postcodeInformation.setText("Not available");
        }
        //Show all flows
        coordinatesFlow.setVisible(true);
        pollutionFlow.setVisible(true);
        boroughFlow.setVisible(true);
        countryFlow.setVisible(true);
        postcodeFlow.setVisible(true);

    }
}
