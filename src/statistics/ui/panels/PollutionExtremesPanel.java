package statistics.ui.panels;

import dataProcessing.DataPoint;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import statistics.back.pollutionExtremes.PollutionExtremesResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

import java.util.List;
import java.util.Map;

import api.PostcodeAPI;
import api.PostcodeResponse;

/**
 * Specialized panel for displaying Pollution Extremes results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class PollutionExtremesPanel extends StatisticsPanel {
    protected PollutionExtremesResult statisticsResult; // Re-cast.
    
    /**
     * Constructor.
     * @param result The PollutionExtremesResult to display.
     */
    public PollutionExtremesPanel(PollutionExtremesResult result) {
        this.statisticsResult = result;
        super(result);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void initialiseContent() {
        // Display extremes
        DataPoint maxPoint = statisticsResult.getMaxPoint();
        if (maxPoint != null) {
            VBox maxPanel = createDataPointPanel("Maximum Pollution", maxPoint);
            addToContent(maxPanel);
        }
        
        DataPoint minPoint = statisticsResult.getMinPoint();
        if (minPoint != null) {
            VBox minPanel = createDataPointPanel("Minimum Pollution", minPoint);
            addToContent(minPanel);
        }
        
        // Display hotspots if available
        displayHotspots();
        
        // Check for yearly max values for chart
        Object yearlyMaxValuesObj = statisticsResult.getValue("yearlyMaxValues");
        if (yearlyMaxValuesObj instanceof Map yearlyMaxValues && !yearlyMaxValues.isEmpty()) {
            addSeparator();
            addYearlyMaxChart(yearlyMaxValues);
            
            // Display the year with the highest value
            Integer yearWithHighestValue = (Integer) statisticsResult.getValue("yearWithHighestValue");
            Double highestOverallValue = (Double) statisticsResult.getValue("highestOverallValue");
            
            if (yearWithHighestValue != null && highestOverallValue != null) {
                addKeyValueRow("Highest Year", yearWithHighestValue + " (" + formatDouble(highestOverallValue) + ")");
            }
        }
    }
    
    /**
     * Create a panel for displaying a data point.
     * @param title Title for the panel
     * @param dataPoint The data point to display
     * @return A configured panel
     */
    private VBox createDataPointPanel(String title, DataPoint dataPoint) {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #f8f8f8;");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Label valueLabel = new Label("Value: " + formatDouble(dataPoint.value()));
        valueLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        Label locationLabel = new Label(getHotSpotLabelString(dataPoint));
        locationLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        
        panel.getChildren().addAll(titleLabel, valueLabel, locationLabel);
        return panel;
    }
    
    /**
     * Display the top hotspots.
     * @param extremesResult The result containing hotspot data.
     */
    private void displayHotspots() {
        List<DataPoint> topHotspots = statisticsResult.getTopHotspots();
        if (topHotspots == null || topHotspots.isEmpty()) return;

        VBox hotspotsContent = new VBox(5);
        hotspotsContent.setPadding(new Insets(10));
        hotspotsContent.setAlignment(Pos.TOP_LEFT);
        
        for (int i = 0; i < topHotspots.size(); i++) {
            DataPoint hotspot = topHotspots.get(i);
            Label hotspotLabel = new Label(getHotSpotLabelString(i, hotspot));
            hotspotLabel.setWrapText(true);
            hotspotsContent.getChildren().add(hotspotLabel);
        }
        
        TitledPane hotspotsPane = new TitledPane("Top Hotspots", hotspotsContent);
        hotspotsPane.setExpanded(true);
        hotspotsPane.setCollapsible(true);
        
        addToContent(hotspotsPane);
    }
    
    /**
     * Add a chart showing yearly maximum values.
     * @param yearlyMaxValues Map of year to maximum value.
     */
    private void addYearlyMaxChart(Map<Integer, Double> yearlyMaxValues) {
        // Create a line chart panel:
        LineChartPanel chartPanel = new LineChartPanel(
            "Maximum Pollution By Year",
            "Year",
            "Maximum Value of " + statisticsResult.getPollutant() + " (ppm)",
            yearlyMaxValues
        );
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        addToContent(chartPanel);
    }

    /**
     * Format a double value to 3 decimal places.
     * @param value The value to format.
     * @return The formatted value.
     */
    private String formatDouble(double value) {
        return Double.toString((int) (value * 1000) / 1000d);
    }

    /**
     * Get a new label string for a hotspot.
     * @param index The index of the hotspot.
     * @param hotspot The hotspot data point.
     * @return A label value.
     */
    private String getHotSpotLabelString(int index, DataPoint hotspot) {
        String label = getHotSpotLabelString(hotspot);

        return index + ". Value: " + hotspot.value() + ", " + label;
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
            System.out.println("Failed to fetch postcode data for hotspot: " + hotspot);
            e.printStackTrace();
            return null;
        }
    }
}