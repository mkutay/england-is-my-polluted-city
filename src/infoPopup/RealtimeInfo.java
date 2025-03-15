package infoPopup;

import dataProcessing.Pollutant;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

/**
 * A class that stores all the information about the live air quality readings.
 * This class is used to display the live air quality readings in the info popup
 * by giving access to the content.
 * 
 * Refactor and class by Mehmet Kutay Bozkurt
 * @author Matthias Loong
 * @version 1.0
 */
public class RealtimeInfo {
    private final VBox content;
    
    private final Label liveInfoLabel;

    // TextFlows:
    private final TextFlow liveAQIFlow;
    private final TextFlow livePollutantFlow;
    private final TextFlow liveUpdatedTimeFlow;

    // Labels:
    private final Label liveAQILabel;
    private final Label livePollutantLabel;
    private final Label liveUpdatedTimeLabel;

    // Information Labels:
    private final Label liveAQIInformation;
    private final Label livePollutantInformation;
    private final Label liveUpdatedTimeInformation;

    public RealtimeInfo(String labelStyle, String titleStyle) {
        // Initialise labels with appropriate styles:
        liveInfoLabel = new Label("Live Air Quality Readings");
        liveInfoLabel.setStyle(titleStyle);

        // Initialise TextFlows:
        liveAQIFlow = new TextFlow();
        livePollutantFlow = new TextFlow();
        liveUpdatedTimeFlow = new TextFlow();

        // Initialise Labels that will be displayed in BOLD (e.g Postal Code:, Country: )
        liveAQILabel = new Label("Live Composite AQI: ");
        liveAQILabel.setStyle(labelStyle);
        livePollutantLabel = new Label("Live Pollution Level (AQI): ");
        livePollutantLabel.setStyle(labelStyle);
        liveUpdatedTimeLabel = new Label("Last Updated: ");
        liveUpdatedTimeLabel.setStyle(labelStyle);

        // Initialise the Labels to store information about the selected MapPoint:
        liveAQIInformation = new Label();
        livePollutantInformation = new Label();
        liveUpdatedTimeInformation = new Label();

        // Add the labels and their information to their respective TextFlows:
        liveAQIFlow.getChildren().addAll(liveAQILabel, liveAQIInformation);
        livePollutantFlow.getChildren().addAll(livePollutantLabel, livePollutantInformation);
        liveUpdatedTimeFlow.getChildren().addAll(liveUpdatedTimeLabel, liveUpdatedTimeInformation);

        content = new VBox(10, liveInfoLabel, liveAQIFlow, livePollutantFlow, liveUpdatedTimeFlow);
    }

    /**
     * Updates the live air quality information in this content.
     * @param realtimeDataDetails The realtime data details.
     * @param pollutant The pollutant type.
     */
    public void update(ShownRealtimeData realtimeDataDetails, Pollutant pollutant) {
        // Set live air quality information if available:
        if (realtimeDataDetails != null) {
            updateRealtime(realtimeDataDetails, pollutant);
        } else {
            liveAQIInformation.setText("Not Available");
            livePollutantInformation.setText("Not Available");
            liveUpdatedTimeInformation.setText("Not Available");
        }
    }

    /**
     * Updates the live realtime air quality information in the popup.
     * @param realtimeDataDetails The realtime data details.
     * @param pollutant The pollutant type.
     */
    private void updateRealtime(ShownRealtimeData realtimeDataDetails, Pollutant pollutant) {
        String liveUpdatedTimeDetails = realtimeDataDetails.lastUpdated();
        String liveAQIDetails = realtimeDataDetails.aqiValue();
        String livePollutionDetails = realtimeDataDetails.pollutantValues().get(pollutant);

        if (liveUpdatedTimeDetails != null) {
            liveUpdatedTimeInformation.setText(liveUpdatedTimeDetails);
        } else {
            liveUpdatedTimeInformation.setText("Not Available");
        }

        if (liveAQIDetails != null) {
            liveAQIInformation.setText(liveAQIDetails);
        } else {
            liveAQIInformation.setText("Not Available");
        }

        if (livePollutionDetails != null) {
            livePollutantInformation.setText(livePollutionDetails);
        } else {
            livePollutantInformation.setText("Not Available");
        }
    }

    /**
     * @return The content of the popup.
     */
    public VBox getContent() {
        return content;
    }
}
