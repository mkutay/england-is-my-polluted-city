package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import statistics.back.trends.TrendsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

import java.util.Map;

/**
 * Specialised panel for displaying SimpleTrends results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class TrendsPanel extends StatisticsPanel {
    protected TrendsResult statisticsResult; // Re-cast.

    /**
     * Constructor.
     * @param result The SimpleTrendsResult to display.
     */
    public TrendsPanel(TrendsResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        if (statisticsResult.getYearlyMeans() != null && !statisticsResult.getYearlyMeans().isEmpty()) {
            // If we have time series data, create a chart.
            addLineChart(statisticsResult.getYearlyMeans());
            
            // Add trend statistics:
            addKeyValueRow("Trend Slope", formatDouble(statisticsResult.getTrendSlope()) + " units per year");
            
            // Add percent change if available:
            Double percentChange = statisticsResult.getPercentChange();
            if (percentChange != null) {
                String changeDirection = percentChange >= 0 ? "increase" : "decrease";
                addKeyValueRow("Percent Change", formatDouble(Math.abs(percentChange)) + "% " + changeDirection);
            }
        } else {
            // Add snapshot data if no time series:
            Double mean = statisticsResult.getMean();
            Double median = statisticsResult.getMedian();
            Double standardDeviation = statisticsResult.getStandardDeviation();
            
            if (mean != null) {
                addKeyValueRow("Mean Pollution", formatDouble(mean));
            }

            if (median != null) {
                addKeyValueRow("Median Pollution", formatDouble(median));
            }

            if (standardDeviation != null) {
                addKeyValueRow("Standard Deviation", formatDouble(standardDeviation));
            }
        }
    }
    
    /**
     * Add a line chart of yearly averages.
     * @param trendsResult The result containing yearly data.
     */
    private void addLineChart(Map<Integer, Double> yearlyMeans) {
        LineChartPanel chartPanel = new LineChartPanel(
            "Pollution Level Over Time",
            "Year",
            "Pollution Level of " + statisticsResult.getPollutant().getDisplayName() + " (ppm)",
            yearlyMeans
        );
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        addToContent(chartPanel);
    }
}
