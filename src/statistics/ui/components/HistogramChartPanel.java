package statistics.ui.components;

import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import statistics.types.HistogramResult;

/**
 * A JavaFX histogram chart panel for displaying distribution data with a logarithmic scale.
 * Uses LogarithmicAxis to display the data on a logarithmic scale for better visualisation.
 * Has tooltips for each bar showing the actual count value.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.3
 */
public class HistogramChartPanel extends VBox {
    private final BarChart<String, Number> barChart;
    private final double lowerBound;
    private final double upperBound;

    private HistogramResult histogramResult;
    
    /**
     * Constructor.
     * @param title Chart title.
     * @param xAxisLabel X-axis label.
     * @param yAxisLabel Y-axis label.
     * @param histogramData The histogram data to display.
     */
    public HistogramChartPanel(String title, String xAxisLabel, String yAxisLabel, HistogramResult histogramResult) {
        this.histogramResult = histogramResult;
        this.lowerBound = Math.max(1, histogramResult.getMinCount() / 2);
        this.upperBound = calculateUpperBound(lowerBound);
        
        // Create chart axes:
        CategoryAxis xAxis = new CategoryAxis();
        LogarithmicAxis yAxis = new LogarithmicAxis(lowerBound, upperBound);
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        
        // Create chart:
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        
        barChart.setBarGap(0); // No gap between bars.
        barChart.setCategoryGap(2); // Small gap between categories.
        barChart.setVerticalGridLinesVisible(false);
        
        barChart.setCategoryGap(6);
        
        // Configure chart layout.
        barChart.setPadding(new Insets(10));
        barChart.setPrefHeight(350); // Explicit height
        VBox.setVgrow(barChart, Priority.ALWAYS);
        
        populateChart();
        
        getChildren().add(barChart); // Add chart to this VBox.
        
        // Make sure this component has a minimum height
        setPrefHeight(400);
        setMinHeight(300);
        
        // Set up bindings for resizing.
        barChart.prefWidthProperty().bind(widthProperty());
        barChart.prefHeightProperty().bind(heightProperty());
    }
    
    /**
     * Populate the chart with histogram data.
     */
    private void populateChart() {
        if (histogramResult == null || histogramResult.getBinCounts().length == 0) {
            return;
        }
        
        // Clear previous data if any.
        barChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Frequency");
        
        double[] binEdges = histogramResult.getBinEdges();
        long[] binCounts = histogramResult.getBinCounts();

        // Add data points to the series.
        for (int i = 0; i < binCounts.length; i++) {
            String binLabel = formatDouble(binEdges[i]) + " - " + formatDouble(binEdges[i + 1]);
            
            // Use actual count (not logarithmic) - the logarithmic axis will handle the scaling.
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(binLabel, Math.max(lowerBound, binCounts[i]));
            dataPoint.setExtraValue(binCounts[i]); // Store the actual count for tooltips.
            series.getData().add(dataPoint);
        }
        
        // Add the series to the chart.
        barChart.getData().add(series);
        
        // Add tooltips to each bar:
        for (XYChart.Data<String, Number> item : series.getData()) {
            if (item.getNode() != null) {
                // Create tooltip with actual count value and bin range.
                long actualCount = (long) item.getExtraValue();
                String tooltipText = "Bin: " + item.getXValue() + "\nCount: " + actualCount;
                
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setShowDelay(Duration.millis(80));
                Tooltip.install(item.getNode(), tooltip);
            }
        }
    }
    
    /**
     * Update the chart with new histogram result.
     * @param newResult New result to display.
     */
    public void updateData(HistogramResult newResult) {
        this.histogramResult = newResult;
        populateChart();
    }
    
    /**
     * Format a double value for display.
     * @param value The value to format.
     * @return Formatted string representation.
     */
    private String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return "N/A";
        }
        return String.format("%.2f", value);
    }

    /**
     * Calculate an upper bound for the logarithmic axis.
     * @param lowerBound The lower bound of the axis.
     * @return The calculated upper bound.
     */
    private double calculateUpperBound(double lowerBound) {
        double ub = lowerBound;
        while (ub < histogramResult.getMaxCount()) {
            ub *= 10;
        }
        return ub;
    }
}