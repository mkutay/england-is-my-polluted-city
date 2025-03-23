package statistics.ui.panels;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import dataProcessing.Pollutant;
import statistics.types.AllPollutionsResult;
import statistics.types.TrendsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.LineChartPanel;

/**
 * Specialised panel for displaying all pollutant results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AllPollutionsPanel extends StatisticsPanel {
    protected AllPollutionsResult statisticsResult; // Re-cast.

    /**
     * Constructor.
     * @param result The AllPollutionsResult to display.
     */
    public AllPollutionsPanel(AllPollutionsResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        addLineChart();

        Double mean = statisticsResult.getTotalMean();
        Double median = statisticsResult.getTotalMedian();
        Double standardDeviation = statisticsResult.getTotalStandardDeviation();
        
        if (mean != null) {
            addKeyValueRow("Total Mean Pollution", formatDouble(mean));
        }

        if (median != null) {
            addKeyValueRow("Total Median Pollution", formatDouble(median));
        }

        if (standardDeviation != null) {
            addKeyValueRow("Total Standard Deviation", formatDouble(standardDeviation));
        }
    }
    
    /**
     * Add a line chart of yearly values.
     */
    private void addLineChart() {
        if (statisticsResult.getTrends() == null || statisticsResult.getTrends().isEmpty()) {
            return;
        }

        Map<Pollutant, TrendsResult> trends = statisticsResult.getTrends();

        LineChartPanel chartPanel = new LineChartPanel(
            "Pollution Levels Over Time",
            "Year",
            "Pollution Level in ppm",
            trends.get(Pollutant.NO2).getYearlyMeans(),
            trends.get(Pollutant.PM10).getYearlyMeans(),
            trends.get(Pollutant.PM2_5).getYearlyMeans()
        );

        chartPanel.setSeriesNames(Pollutant.NO2.getDisplayName(), Pollutant.PM10.getDisplayName(), Pollutant.PM2_5.getDisplayName());
        chartPanel.populateChart();
        
        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        addToContent(chartPanel);
    }
}
