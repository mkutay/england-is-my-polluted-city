package statistics.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import statistics.types.HistogramResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.components.HistogramChartPanel;

public class HistogramPanel extends StatisticsPanel {
    protected HistogramResult statisticsResult; // Re-cast

    /**
     * Constructor.
     * @param result The HistogramResult to display
     */
    public HistogramPanel(HistogramResult result) {
        super(result);
        this.statisticsResult = result;
        initialiseContent();
    }

    @Override
    protected void initialiseContent() {
        // Add histogram chart using the component.
        addHistogramChart();
    }

    /**
     * Adds a histogram chart visualization of the distribution.
     */
    private void addHistogramChart() {
        if (statisticsResult == null || statisticsResult.getBinCounts().length == 0) {
            return;
        }

        HistogramChartPanel histogramPanel = new HistogramChartPanel(
            "Pollution Value Histogram",
            "Pollution Value Range",
            "Count",
            statisticsResult
        );

        histogramPanel.setPadding(new Insets(10));
        histogramPanel.setPrefHeight(400); // Set a preferred height
        VBox.setVgrow(histogramPanel, Priority.ALWAYS);

        // Use the parent class method to add to content area
        addToContent(histogramPanel);
    }
}
