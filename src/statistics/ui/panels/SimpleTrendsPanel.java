package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import statistics.back.simpleTrends.SimpleTrendsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

import java.util.Map;

/**
 * Specialised panel for displaying SimpleTrends results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class SimpleTrendsPanel extends StatisticsPanel {
    protected SimpleTrendsResult statisticsResult; // Re-cast.

    /**
     * Constructor.
     * @param result The SimpleTrendsResult to display.
     */
    public SimpleTrendsPanel(SimpleTrendsResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        // Add trend information:
        if (statisticsResult.getYearlyAverages() != null && !statisticsResult.getYearlyAverages().isEmpty()) {
            // If we have time series data, create a chart.
            addLineChart();
            
            addSeparator();
            
            // Add trend statistics:
            addKeyValueRow("Trend Slope", formatDouble(statisticsResult.getTrendSlope()) + " units per year");
            
            // Add percent change if available:
            Double percentChange = statisticsResult.getPercentChange();
            if (percentChange != null) {
                String changeDirection = percentChange >= 0 ? "increase" : "decrease";
                addKeyValueRow("Percent Change", formatDouble(Math.abs(percentChange)) + "% " + changeDirection);
            }
        } else {
            // Add snapshot data if no time series
            Double totalPollution = statisticsResult.getTotalPollution();
            Double avgPollution = statisticsResult.getAveragePollution();
            Long dataPointCount = statisticsResult.getDataPointCount();
            
            if (totalPollution != null) {
                addKeyValueRow("Total Pollution", formatDouble(totalPollution));
            }
            
            if (avgPollution != null) {
                addKeyValueRow("Average Pollution", formatDouble(avgPollution));
            }
            
            if (dataPointCount != null) {
                addKeyValueRow("Number of Data Points", dataPointCount.toString());
            }
        }
    }
    
    /**
     * Add a line chart of yearly averages.
     * @param trendsResult The result containing yearly data.
     */
    private void addLineChart() {
        Map<Integer, Double> yearlyAverages = statisticsResult.getYearlyAverages();
        if (yearlyAverages == null || yearlyAverages.isEmpty()) return;
        
        LineChartPanel chartPanel = new LineChartPanel(
            "Pollution Level Over Time",
            "Year",
            "Pollution Level of " + statisticsResult.getPollutant() + " (ppm)",
            yearlyAverages
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
}
