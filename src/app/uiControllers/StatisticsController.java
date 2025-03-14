package app.uiControllers;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import dataProcessing.Pollutant;
import statistics.back.StatisticsManager;
import statistics.back.StatisticsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.StatisticsPanelFactory;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all statistics UI elements and changes in the panel.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class StatisticsController {
    private final StatisticsPanelFactory statisticsPanelFactory;

    private BorderPane statisticsPane;
    private Button prevButton;
    private Button nextButton;

    private Integer currentYear;
    private Pollutant currentPollutant;
    private Map<String, StatisticsResult> statistics;
    private List<String> statisticsKeys;
    private int currentKeyIndex;

    /**
     * Constructor.
     */
    public StatisticsController() {
        statisticsPane = new BorderPane();
        this.statisticsPanelFactory = StatisticsPanelFactory.getInstance();
        initialiseNavigationButtons();
    }
    
    /**
     * Initialises the navigation buttons for cycling through statistics results.
     */
    private void initialiseNavigationButtons() {
        prevButton = new Button("Previous");
        nextButton = new Button("Next");
        
        prevButton.setOnAction(e -> showPreviousStatistic());
        nextButton.setOnAction(e -> showNextStatistic());

        Insets buttonBarPadding = new Insets(12);
        
        HBox buttonBar = new HBox();
        buttonBar.setPadding(buttonBarPadding);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        
        // Create spacer to push buttons apart:
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Add buttons with spacer between them.
        buttonBar.getChildren().addAll(prevButton, spacer, nextButton);
        statisticsPane.setBottom(buttonBar);
        
        // Initially disable buttons until we have statistics.
        updateButtonStates();
    }
    
    /**
     * Shows the previous statistic in the collection.
     */
    private void showPreviousStatistic() {
        if (statisticsKeys != null && !statisticsKeys.isEmpty() && currentKeyIndex > 0) {
            currentKeyIndex--;
            updateStatisticsDisplay();
            updateButtonStates();
        }
    }
    
    /**
     * Shows the next statistic in the collection.
     */
    private void showNextStatistic() {
        if (statisticsKeys != null && !statisticsKeys.isEmpty() && currentKeyIndex < statisticsKeys.size() - 1) {
            currentKeyIndex++;
            updateStatisticsDisplay();
            updateButtonStates();
        }
    }
    
    /**
     * Updates the button states and texts based on current position in statistics collection.
     */
    private void updateButtonStates() {
        if (statisticsKeys == null || statisticsKeys.isEmpty()) {
            prevButton.setDisable(true);
            nextButton.setDisable(true);
            prevButton.setText("Previous");
            nextButton.setText("Next");
        } else {
            if (currentKeyIndex == 0) {
                prevButton.setText("Previous");
            } else {
                prevButton.setText("Previous (" + statisticsKeys.get(currentKeyIndex - 1) + ")");
            }

            if (currentKeyIndex == statisticsKeys.size() - 1) {
                nextButton.setText("Next");
            } else {
                nextButton.setText("Next (" + statisticsKeys.get(currentKeyIndex + 1) + ")");
            }

            prevButton.setDisable(currentKeyIndex <= 0);
            nextButton.setDisable(currentKeyIndex >= statisticsKeys.size() - 1);
        }
    }
    
    /**
     * Updates the displayed statistics based on current key.
     */
    private void updateStatisticsDisplay() {
        String key = statisticsKeys.get(currentKeyIndex);
        if (key == null || statistics == null) return;

        StatisticsResult sr = statistics.get(key);
        StatisticsPanel statisticsPanel = statisticsPanelFactory.createPanel(sr);
        statisticsPane.setCenter(statisticsPanel);
    }

    /**
     * Updates the statistics dataset with the specified year and pollutant.
     * @param year The year to show statistics for.
     * @param pollutant The pollutant to show statistics for.
     */
    public void updateDataSet(Integer year, Pollutant pollutant) {
        // Only update if the selection has actually changed:
        if (year.equals(currentYear) && pollutant.equals(currentPollutant)) return;
        
        currentYear = year;
        currentPollutant = pollutant;
        
        StatisticsManager statisticsManager = StatisticsManager.getInstance();
        statistics = statisticsManager.calculateStatistics(pollutant, year);
        
        // Convert the keyset to a list for indexed access.
        statisticsKeys = new ArrayList<>(statistics.keySet());

        updateStatisticsDisplay();

        updateButtonStates();
    }

    /**
     * @return The statistics panel for displaying purposes.
     */
    public BorderPane getStatisticsPane() {
        return statisticsPane;
    }
}
