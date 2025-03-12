package statistics.ui.components;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * A JavaFX line chart panel for displaying time series data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class LineChartPanel extends VBox {
    private final Map<Integer, Double> data;
    private final LineChart<String, Number> lineChart;
    
    /**
     * Constructor.
     * @param title Chart title
     * @param xAxisLabel X-axis label
     * @param yAxisLabel Y-axis label
     * @param data Time series data (year to value mapping)
     */
    public LineChartPanel(String title, String xAxisLabel, String yAxisLabel, Map<Integer, Double> data) {
        this.data = data;
        
        // Create chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        
        // Create chart:
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);
        
        // Make chart fill available space:
        VBox.setVgrow(lineChart, Priority.ALWAYS);
        
        populateChart();
        
        getChildren().add(lineChart);
    }
    
    /**
     * Populate the chart with data.
     */
    private void populateChart() {
        if (data == null || data.isEmpty()) {
            return;
        }
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        for (Integer key : data.keySet()) {
            series.getData().add(new XYChart.Data<>(key.toString(), data.get(key)));
        }
        
        lineChart.getData().add(series);
    }
    
    /**
     * Update the chart with new data.
     * @param newData New data to display.
     */
    public void updateData(Map<Integer, Double> newData) {
        lineChart.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        for (Integer key : newData.keySet()) {
            series.getData().add(new XYChart.Data<>(key.toString(), newData.get(key)));
        }
        
        lineChart.getData().add(series);
    }
}
