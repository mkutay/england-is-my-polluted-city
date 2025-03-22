package statistics.ui.components;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

/**
 * A JavaFX line chart panel for displaying time series data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class LineChartPanel extends VBox {
    private List<Map<Integer, Double>> data;
    private String[] names;
    
    private final LineChart<String, Number> lineChart;
    
    /**
     * Constructor.
     * @param title Chart title.
     * @param xAxisLabel X-axis label.
     * @param yAxisLabel Y-axis label.
     * @param data Time series data (year to value mapping) for different information.
     */
    @SafeVarargs
    public LineChartPanel(String title, String xAxisLabel, String yAxisLabel, Map<Integer, Double> ...data) {
        this.data = List.of(data);
        
        // Create chart axes:
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        
        // Create chart:
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(this.data.size() != 1); // Hide legend if only one series.
        
        // Make chart fill available space:
        VBox.setVgrow(lineChart, Priority.ALWAYS);
        
        getChildren().add(lineChart);
    }
    
    /**
     * Populate the chart with data.
     */
    public void populateChart() {
        if (data == null || data.isEmpty()) {
            return;
        }
        
        // Create a separate series for each data map:
        for (int i = 0; i < data.size(); i++) {
            Map<Integer, Double> dataMap = data.get(i);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(names[i]);
            
            for (Integer key : dataMap.keySet()) {
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(key.toString(), dataMap.get(key));
                series.getData().add(dataPoint);
            }
            
            lineChart.getData().add(series);
        }

        // Add tooltip to show actual value on hover:
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tooltipText = String.format("%s: %.2f (%s)", data.getXValue(), data.getYValue(), series.getName());
                
                Tooltip tooltip = new Tooltip(tooltipText);
                tooltip.setShowDelay(Duration.millis(80));
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }
    
    /**
     * Update the chart with new data.
     * @param newData New data to display.
     */
    @SuppressWarnings("unchecked")
    public void updateData(Map<Integer, Double> ...newData) {
        this.data = List.of(newData);

        lineChart.getData().clear();
        populateChart();
    }
    
    /**
     * Set series names for the legend.
     * @param names Names for each series in the chart
     */
    public void setSeriesNames(String... names) {
        if (names == null || names.length == 0) {
            return;
        }

        this.names = names;
    }
}
