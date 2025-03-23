package statistics.ui.panels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import statistics.types.DistributionAnalysisResult;
import statistics.types.DistributionYearlyMetric;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.DataTablePanel;
import statistics.ui.components.LineChartPanel;

/**
 * Panel for displaying distribution analysis results using JavaFX.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.2
 */
public class DistributionAnalysisPanel extends StatisticsPanel {
    protected DistributionAnalysisResult statisticsResult; // Re-cast
    
    /**
     * Constructor.
     * @param result The DistributionAnalysisResult to display
     */
    public DistributionAnalysisPanel(DistributionAnalysisResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }
    
    @Override
    protected void initialiseContent() {
        addPercentilesChart();
        addYearlyMetricsTable();
    }

    private void addPercentilesChart() {
        if (statisticsResult.getYearlyPercentiles().isEmpty()) return;

        Map<Integer, Double> firstQuarter = statisticsResult.getYearlyPercentile(25.0);
        Map<Integer, Double> median = statisticsResult.getYearlyPercentile(50.0);
        Map<Integer, Double> thirdQuarter = statisticsResult.getYearlyPercentile(75.0);
        Map<Integer, Double> percent10 = statisticsResult.getYearlyPercentile(10.0);
        Map<Integer, Double> percent90 = statisticsResult.getYearlyPercentile(90.0);
        Map<Integer, Double> percent99 = statisticsResult.getYearlyPercentile(99.0);

        LineChartPanel chartPanel = new LineChartPanel(
            "Percentiles Over Time",
            "Year",
            "Pollution Value (ppm)",
            firstQuarter, median, thirdQuarter, percent10, percent90, percent99
        );

        chartPanel.setSeriesNames("25%", "50%", "75%", "10%", "90%", "99%");
        chartPanel.populateChart();

        chartPanel.setPadding(new Insets(10));
        VBox.setVgrow(chartPanel, Priority.ALWAYS);

        addToContent(chartPanel);
    }
    
    /**
     * Add a table of yearly distribution metrics using the DataTablePanel component.
     */
    private void addYearlyMetricsTable() {
        if (statisticsResult.getYearlySkewness().isEmpty()) return;

        // Get the data:
        Map<Integer, Map<Double, Double>> yearlyPercentiles = statisticsResult.getYearlyPercentiles();
        Map<Integer, Double> yearlySkewness = statisticsResult.getYearlySkewness();
        Map<Integer, Double> yearlyKurtosis = statisticsResult.getYearlyKurtosis();
        
        // Prepare data for the table:
        List<DistributionYearlyMetric> yearlyMetrics = new ArrayList<>();
        for (Integer year : yearlyPercentiles.keySet()) {
            Map<Double, Double> percentiles = yearlyPercentiles.get(year);
            Double median = percentiles.get(50.0);
            Double q1 = percentiles.get(25.0);
            Double q3 = percentiles.get(75.0);
            Double skewness = yearlySkewness.get(year);
            Double kurtosis = yearlyKurtosis.get(year);
            
            yearlyMetrics.add(new DistributionYearlyMetric(year, median, q1, q3, skewness, kurtosis));
        }
        
        // Sort by year:
        yearlyMetrics.sort(Comparator.comparingInt(DistributionYearlyMetric::year));
        
        // Create the table panel.
        String[] headers = {"Year", "Q1", "Median", "Q3", "IQR", "Skewness", "Kurtosis"};
        String[] meaning = {
            "The year of the data.",
            "The first quartile value.",
            "The median value.",
            "The third quartile value.",
            "The interquartile range (Q3 - Q1).",
            "A measure of skewness (either positively or negatively skewed).",
            "A measure of kurtosis (either platykurtic or leptokurtic)."
        };
        DataTablePanel<DistributionYearlyMetric> tablePanel = new DataTablePanel<>(null, headers, meaning);
        
        // Add column value functions to display the data.
        tablePanel
            .addColumnValueFunction(metric -> Integer.toString(metric.year()))
            .addColumnValueFunction(metric -> format(metric.q1()))
            .addColumnValueFunction(metric -> format(metric.median()))
            .addColumnValueFunction(metric -> format(metric.q3()))
            .addColumnValueFunction(metric -> format(metric.getIQR()))
            .addColumnValueFunction(metric -> format(metric.skewness()))
            .addColumnValueFunction(metric -> format(metric.kurtosis()));
        
        // Add column tooltips to display the data.
        tablePanel
            .addColumnTooltipFunction(metric -> "Year " + metric.year())
            .addColumnTooltipFunction(metric -> "Q1: " + format(metric.q1()))
            .addColumnTooltipFunction(metric -> "Median: " + format(metric.median()))
            .addColumnTooltipFunction(metric -> "Q3: " + format(metric.q3()))
            .addColumnTooltipFunction(metric -> "IQR: " + format(metric.getIQR()))
            .addColumnTooltipFunction(metric -> "Skewness: " + format(metric.skewness()) + " (" + metric.getSkewnessMeaning() + ")")
            .addColumnTooltipFunction(metric -> "Kurtosis: " + format(metric.kurtosis()) + " (" + metric.getKurtosisMeaning() + ")");
        
        // Set the data.
        tablePanel.setData(yearlyMetrics);
        
        // Add the table to the panel.
        addToContent(tablePanel);
    }
    
    /**
     * Format a nullable Double value for display.
     * @param value The value to format
     * @return Formatted string representation or "N/A" if null
     */
    private String format(Double value) {
        if (value == null || Double.isNaN(value)) {
            return "N/A";
        }
        return formatDouble(value);
    }
}
