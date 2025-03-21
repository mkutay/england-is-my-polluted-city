package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.stream.Collectors;

import statistics.back.pollutionExtremes.PollutionExtremesResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;
import dataProcessing.DataPoint;
import api.postcode.PostcodeAPI;
import api.postcode.PostcodeResponse;

/**
 * Specialised panel for displaying Pollution Extremes results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 3.0
 */
public class PollutionExtremesPanel extends StatisticsPanel {
    protected PollutionExtremesResult statisticsResult; // Re-cast.
    
    /**
     * Constructor.
     * @param result The PollutionExtremesResult to display.
     */
    public PollutionExtremesPanel(PollutionExtremesResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        addLineChart();

        if (statisticsResult.getMaxYear() != null) {
            addKeyValueRow("Year with Highest Pollution", statisticsResult.getMaxYear().getKey() + " - " + formatDouble(statisticsResult.getMaxYear().getValue().value()) + " ppm, " + getHotSpotLabelString(statisticsResult.getMaxYear().getValue()));
        }

        if (statisticsResult.getMedianYear() != null) {
            addKeyValueRow("Year with Median Pollution", statisticsResult.getMedianYear().getKey() + " - " + formatDouble(statisticsResult.getMedianYear().getValue().value()) + " ppm, " + getHotSpotLabelString(statisticsResult.getMedianYear().getValue()));
        }

        if (statisticsResult.getMinYear() != null) {
            addKeyValueRow("Year with Lowest Pollution", statisticsResult.getMinYear().getKey() + " - " + formatDouble(statisticsResult.getMinYear().getValue().value()) + " ppm, " + getHotSpotLabelString(statisticsResult.getMinYear().getValue()));
        }
    }

    /**
     * Add a line chart of yearly values.
     */
    private void addLineChart() {
        if (statisticsResult.getYearToMaxPoints() == null || statisticsResult.getYearToMaxPoints().isEmpty()) {
            return;
        }

        Map<Integer, Double> yearlyMaxValues = statisticsResult.getYearToMaxPoints().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().value()));
        
        Map<Integer, Double> yearlyMinValues = statisticsResult.getYearToMinPoints().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().value()));

        Map<Integer, Double> yearlyMedianValues = statisticsResult.getYearToMedianPoints().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().value()));

        LineChartPanel chartPanel = new LineChartPanel(
            "Pollution Extremes Over Time",
            "Year",
            "Pollution Level of " + statisticsResult.getPollutant().getDisplayName() + " (ppm)",
            yearlyMaxValues,
            yearlyMedianValues,
            yearlyMinValues
        );

        chartPanel.setSeriesNames("Max", "Median", "Min");
        chartPanel.populateChart();
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        addToContent(chartPanel);
    }

    /**
     * Get a new label string value for a hotspot.
     * @param index The index of the hotspot.
     * @param hotspot The hotspot data point.
     * @return A new label value.
     */
    private String getHotSpotLabelString(DataPoint hotspot) {
        try {
            PostcodeResponse response = PostcodeAPI.fetchPostcodesByEastingNorthing(hotspot.x(), hotspot.y());

            String label = String.format("Grid: %d (Easting: %d, Northing: %d)",
                hotspot.gridCode(), hotspot.x(), hotspot.y());

            if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
                label += " - " + response.getResult().get(0).getParliamentary_constituency();
            }

            return label;
        } catch (Exception e) {
            System.err.println("Failed to fetch postcode data for hotspot: " + hotspot);
            e.printStackTrace();
            return null;
        }
    }
}