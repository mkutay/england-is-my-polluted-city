package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import statistics.back.trends.TrendsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

/**
 * Specialised panel for displaying trends results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class TrendsPanel extends StatisticsPanel {
    protected TrendsResult statisticsResult; // Re-cast.

    /**
     * Constructor.
     * @param result The TrendsResult to display.
     */
    public TrendsPanel(TrendsResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        addLineChart();
        
        if (statisticsResult.getTrendSlope() != null) {
            addKeyValueRow("Trend Slope (Mean)", formatDouble(statisticsResult.getTrendSlope()) + " units per year");
        }
        
        // Add percent change if available:
        Double percentChange = statisticsResult.getPercentChange();
        if (percentChange != null) {
            String changeDirection = percentChange >= 0 ? "increase" : "decrease";
            addKeyValueRow("Percent Change (Mean)", formatDouble(Math.abs(percentChange)) + "% " + changeDirection);
        }

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
    
    /**
     * Add a line chart of yearly values.
     */
    private void addLineChart() {
        if (statisticsResult.getYearlyMeans() == null || statisticsResult.getYearlyMeans().isEmpty()) {
            return;
        }

        LineChartPanel chartPanel = new LineChartPanel(
            "Pollution Levels Over Time",
            "Year",
            "Pollution Level of " + statisticsResult.getPollutant().getDisplayName() + " (ppm)",
            statisticsResult.getYearlyMeans(),
            statisticsResult.getYearlyMedians(),
            statisticsResult.getYearlyStandardDeviations()
        );

        chartPanel.setSeriesNames("Mean", "Median", "Standard Deviation");
        chartPanel.populateChart();
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        addToContent(chartPanel);
    }
}
