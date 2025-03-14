package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import statistics.back.averagePollution.AveragePollutionResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

import java.util.Map;

/**
 * Specialised panel for displaying AveragePollution results.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 3.0
 */
public class AveragePollutionPanel extends StatisticsPanel {
    protected AveragePollutionResult statisticsResult; // Re-casting.
    
    /**
     * Constructor.
     * @param result The AveragePollutionResult to display.
     */
    public AveragePollutionPanel(AveragePollutionResult result) {
        this.statisticsResult = result;
        super(result);
    }
    
    @Override
    protected void initialiseContent() {        
        // Display basic statistics:
        addKeyValueRow("Overall Mean", formatDouble(statisticsResult.getMean()));
        addKeyValueRow("Overall Median", formatDouble(statisticsResult.getMedian()));
        addKeyValueRow("Overall Standard Deviation", formatDouble(statisticsResult.getStandardDeviation()));
        
        Map<Integer, Double> yearlyData = statisticsResult.getYearlyMeans();
        
        if (yearlyData == null || yearlyData.isEmpty()) return;

        // If we have yearly data, show a chart:
        addSeparator();
        addYearlyDataChart(yearlyData);
        
        // Show overall trend if available
        Double overallTrend = statisticsResult.getOverallTrend();
        if (overallTrend != null) {
            String trendDescription = overallTrend > 0.25 ? "increasing" : overallTrend < 0.25 ? "decreasing" : "stable";
            addKeyValueRow("Overall Trend", trendDescription + " (" + formatDouble(overallTrend) + ")");
        }
    }
    
    /**
     * Add a chart showing yearly data.
     * @param yearlyData Map of year to data value.
     */
    private void addYearlyDataChart(Map<Integer, Double> yearlyData) {
        // Create a line chart panel:
        LineChartPanel chartPanel = new LineChartPanel(
            "Average Pollution By Year",
            "Year",
            "Average Value of " + statisticsResult.getPollutant() + " (ppm)",
            yearlyData
        );
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);

        addToContent(chartPanel);
    }

    private String formatDouble(Double value) {
        return Double.toString((int) (value * 1000) / 1000d);
    }
}
