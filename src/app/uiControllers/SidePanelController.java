package app.uiControllers;

import app.uiViews.SidePanel;
import colors.ColorScheme;
import dataProcessing.Pollutant;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;

import javafx.scene.control.Label;
import java.util.List;

/**
 * Manages the side panel UI elements and coordinates between different view components.
 *
 * @author Mehmet Kutay Bozkurt and Chelsea Feliciano
 * @version 3.0
 */
public class SidePanelController {
    private final SidePanel sidePanel;
    private final BorderPane rootPane; // Root pane for swapping center content

    // UI Component Controllers:
    private final MapController mapController;
    private final DataSelectionController dataSelectionController;
    private final ColorSchemeController colorSchemeController;
    private final StatisticsController statisticsController;
    private final PollutionThresholdController pollutionThresholdController;

    // UI Navigation Elements:
    private Node mapContent;
    private boolean mapShown = true;

    private List<Label> navigationLabels;

    /**
     * Constructor for SidePanelController.
     * @param mapController The current map controller.
     * @param rootPane The root BorderPane of the application.
     */
    public SidePanelController(MapController mapController, BorderPane rootPane) {
        this.rootPane = rootPane;
        this.mapController = mapController;
        this.mapContent = mapController.getMapOverlay();

        this.statisticsController = new StatisticsController();
        this.dataSelectionController = new DataSelectionController();
        this.colorSchemeController = new ColorSchemeController();
        this.pollutionThresholdController = new PollutionThresholdController();

        // Creates a VBox with UI components of sidepanel
        this.sidePanel = new SidePanel(dataSelectionController, colorSchemeController, pollutionThresholdController);
        setupEventHandlers();
    }

    /**
     * Sets up event handlers for data selection and color scheme changes.
     */
    private void setupEventHandlers() {
        // Handle view navigation (map <-> statistics)
        sidePanel.getMapLabel().setOnMouseClicked(e -> switchView("map"));
        sidePanel.getStatsLabel().setOnMouseClicked(e -> switchView("stats"));
        sidePanel.getSwitchLabel().setOnMouseClicked(e -> switchView(mapShown ? "stats" : "map"));

        // Handle data selection changes (year, pollutant):
        dataSelectionController.setOnSelectionChanged((year, pollutant) -> {
            ColorScheme colorScheme = colorSchemeController.getSelectedColorScheme();
            mapController.updateMapDataSet(year, pollutant, colorScheme);
            
            if (!mapShown) {
                statisticsController.updateDataSetRange(year, year, pollutant);
            }
        });

        // Handle year range selection changes for statistics:
        dataSelectionController.setOnRangeSelectionChanged((startYear, endYear, pollutant) -> {
            // Only update range statistics when in statistics view.
            if (!mapShown) {
                statisticsController.updateDataSetRange(startYear, endYear, pollutant);
            }
        });

        // Handle color scheme changes:
        colorSchemeController.setOnColorSchemeChanged(colorScheme -> {
            if (mapShown) {
                Integer year = dataSelectionController.getSelectedYear();
                Pollutant pollutant = dataSelectionController.getSelectedPollutant();
                mapController.updateMapDataSet(year, pollutant, colorScheme);
            }
        });

        pollutionThresholdController.getThresholdSlider().valueProperty().addListener((e1, e2, newValue) -> {
            mapController.updatePollutionThreshold(newValue.doubleValue());
        });
    }

    /**
     * Switches between the map view and statistics view.
     */
    public void switchView(String target) {
        sidePanel.updateNavigationLabels(target);

        if ("stats".equals(target)) {
            // Switching to statistics view.
            rootPane.setCenter(statisticsController.getStatisticsPane());
            sidePanel.getSwitchLabel().setText("Return to Map");
            
            // Show end year dropdown and change year label to "Start Year".
            sidePanel.getEndYearDropdownBox().setVisible(true);
            sidePanel.getEndYearDropdownBox().setManaged(true);
            dataSelectionController.setYearLabelText("Start Year:");

            sidePanel.getColorDropdownBox().setVisible(false);
            sidePanel.getColorDropdownBox().setManaged(false);
            sidePanel.getSliderContainer().setVisible(false);
            sidePanel.getSliderContainer().setManaged(false);
            
            Integer startYear = dataSelectionController.getSelectedYear();
            Integer endYear = dataSelectionController.getSelectedEndYear();
            Pollutant pollutant = dataSelectionController.getSelectedPollutant();
            
            if (startYear != null && endYear != null && pollutant != null) {
                statisticsController.updateDataSetRange(startYear, endYear, pollutant);
            }
            
            mapShown = false;
        } else {
            // Switching to map view.
            rootPane.setCenter(mapContent);
            sidePanel.getSwitchLabel().setText("ⓘ View Pollutant Statistics");
            
            // Hide end year dropdown and change year label back to "Year".
            sidePanel.getEndYearDropdownBox().setVisible(false);
            sidePanel.getEndYearDropdownBox().setManaged(false);
            dataSelectionController.setYearLabelText("Year:");

            sidePanel.getColorDropdownBox().setVisible(true);
            sidePanel.getColorDropdownBox().setManaged(true);
            sidePanel.getSliderContainer().setVisible(true);
            sidePanel.getSliderContainer().setManaged(true);
            
            Integer year = dataSelectionController.getSelectedYear();
            Pollutant pollutant = dataSelectionController.getSelectedPollutant();
            ColorScheme colorScheme = colorSchemeController.getSelectedColorScheme();
            
            if (year != null && pollutant != null && colorScheme != null) {
                mapController.updateMapDataSet(year, pollutant, colorScheme);
            }
            
            mapShown = true;
        }
    }

    /**
     * @return The side panel VBox.
     */
    public SidePanel getSidePanel() {return sidePanel;}
}