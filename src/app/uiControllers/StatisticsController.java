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
 * Handles the statistics display and navigation between different statistics panels.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 3.0
 */
public class StatisticsController {
    private final StatisticsPanelFactory statisticsPanelFactory;
    private final StatisticsManager statisticsManager;

    private BorderPane statisticsPane;
    private Button prevButton;
    private Button nextButton;

    private Integer currentYear;
    private Integer currentEndYear;
    private Pollutant currentPollutant;
    private Map<String, StatisticsResult> statistics;
    private List<String> statisticsKeys;
    private int currentKeyIndex;

    /**
     * Constructor for StatisticsController.
     */
    public StatisticsController() {
        statisticsPane = new BorderPane();
        this.statisticsPanelFactory = StatisticsPanelFactory.getInstance();
        this.statisticsManager = StatisticsManager.getInstance();
        
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
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setSpacing(20);
        
        // Create spacer to push buttons apart:
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        buttonBar.getChildren().addAll(prevButton, spacer, nextButton);
        statisticsPane.setBottom(buttonBar);
        
        // Initially disable buttons until we have statistics:
        updateButtonStates();
    }
    
    /**
     * Shows the previous statistic in the collection.
     */
    private void showPreviousStatistic() {
        if (statisticsKeys != null && !statisticsKeys.isEmpty() && currentKeyIndex > 0) {
            currentKeyIndex--;
            updateStatisticsDisplay();
        }
    }
    
    /**
     * Shows the next statistic in the collection.
     */
    private void showNextStatistic() {
        if (statisticsKeys != null && !statisticsKeys.isEmpty() && currentKeyIndex < statisticsKeys.size() - 1) {
            currentKeyIndex++;
            updateStatisticsDisplay();
        }
    }
    
    /**
     * Updates the button states and labels based on current position in statistics collection.
     */
    private void updateButtonStates() {
        if (statisticsKeys == null || statisticsKeys.isEmpty()) {
            prevButton.setDisable(true);
            nextButton.setDisable(true);
            prevButton.setText("Previous");
            nextButton.setText("Next");
            return;
        }
        
        // Update previous button:
        prevButton.setDisable(currentKeyIndex <= 0);
        prevButton.setText(currentKeyIndex > 0 ? 
            "Previous: " + statisticsKeys.get(currentKeyIndex - 1) : "Previous");

        // Update next button:
        nextButton.setDisable(currentKeyIndex >= statisticsKeys.size() - 1);
        nextButton.setText(currentKeyIndex < statisticsKeys.size() - 1 ? 
            "Next: " + statisticsKeys.get(currentKeyIndex + 1) : "Next");
    }
    
    /**
     * Updates the displayed statistics based on current key.
     */
    private void updateStatisticsDisplay() {
        if (statisticsKeys == null || statisticsKeys.isEmpty()) {
            return;
        }

        String key = statisticsKeys.get(currentKeyIndex);
        StatisticsResult sr = statistics.get(key);
        
        StatisticsPanel statisticsPanel = statisticsPanelFactory.createPanel(sr);
        statisticsPane.setCenter(statisticsPanel);
        
        updateButtonStates();
    }

    /**
     * Updates the statistics for a range of years.
     */
    public void updateDataSetRange(Integer startYear, Integer endYear, Pollutant pollutant) {
        if (startYear == null || endYear == null || pollutant == null) {
            return;
        }
        
        if (endYear < startYear) {
            return;
        }
        
        // Only update if the selection has actually changed.
        if (currentYear != null && currentYear.equals(startYear) && 
            currentEndYear != null && currentEndYear.equals(endYear) &&
            currentPollutant != null && currentPollutant.equals(pollutant) &&
            statistics != null) {
            return;
        }
        
        currentYear = startYear;
        currentEndYear = endYear;
        currentPollutant = pollutant;
        
        statistics = statisticsManager.calculateStatisticsOverTime(pollutant, startYear, endYear);
        statisticsKeys = new ArrayList<>(statistics.keySet());
        updateStatisticsDisplay();
    }

    /**
     * @return The statistics panel for displaying.
     */
    public BorderPane getStatisticsPane() {
        return statisticsPane;
    }
}
