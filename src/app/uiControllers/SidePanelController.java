package app.uiControllers;

import app.MapController;
import colors.ColorScheme;
import dataProcessing.Pollutant;
import app.App;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.Node;

import java.util.Arrays;
import java.util.List;

/**
 * Manages the side panel UI elements including dropdown menus and buttons.
 * 
 * Refactor and class by Mehmet Kutay Bozkurt.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 2.0
 */
public class SidePanelController {
    private final VBox sidePanel; // The side panel VBox.
    // The root BorderPane of the application that contains the side panel. Used for changing the center content.
    private final BorderPane rootPane;

    // Controllers for side panel elements:
    private final PollutionSelectorController selectorController;
    private final StatisticsController statisticsController;
    private final MapController mapController;

    private Button viewStatisticsButton;
    private Button viewMapButton;
    private Node currentCenterContent;
    private boolean mapShown = true;

    private Label homeLabel;
    private Label statsLabel;
    private List<Label> navLabels;


    /**
     * Constructor for SidePanelController.
     * @param mapController The current map controller.
     * @param statisticsController The statistics controller.
     * @param rootPane The root BorderPane of the application.
     */
    public SidePanelController(MapController mapController, StatisticsController statisticsController, BorderPane rootPane) {
        this.rootPane = rootPane;
        this.statisticsController = statisticsController;
        this.mapController = mapController;
        this.selectorController = new PollutionSelectorController();

        this.currentCenterContent = mapController.getMapOverlay();

        // Create side panel:
        sidePanel = createSidePanel();
        sidePanel.getStyleClass().add("side-panel");
        
        // Set up the data selection change listener:
        setupSelectionChangeListener();
    }

    /**
     * Sets up listener for pollutant/year selection changes.
     */
    private void setupSelectionChangeListener() {
        selectorController.setOnSelectionChanged(this::updateData);
    }

    /**
     * Updates both map and statistics data with new selections.
     * @param year Selected year.
     * @param pollutant Selected pollutant.
     * @param colorScheme Selected color scheme.
     */
    private void updateData(Integer year, Pollutant pollutant, ColorScheme colorScheme) {
        mapController.updateMapDataSet(year, pollutant, colorScheme); // Update map data.
        statisticsController.updateDataSet(year, pollutant); // Update statistics data.
        
        // Refresh the view if we're showing statistics:
        if (!mapShown) {
            showStatisticsPanel();
        }
    }

    /**
     * Initialises the UI, creating all UI elements and adding them to the side panel.
     * @return The side panel VBox containing all UI elements.
     */
    private VBox createSidePanel() {
        VBox sidePanel = new VBox();
        sidePanel.getStyleClass().add("side-panel");
        
        // Create the title label for the side panel:
        Label applicationLabel = new Label(App.APP_NAME);
        applicationLabel.getStyleClass().add("sidepanel-title");

        // Create side panel navigation
        HBox panelNav = new HBox();
        homeLabel = new Label("Home");
        statsLabel = new Label("Pollutant Statistics");
        Label signLabel = new Label(">");
        panelNav.getChildren().addAll(homeLabel, signLabel, statsLabel);
        panelNav.getStyleClass().add("side-panel-nav");
        homeLabel.getStyleClass().add("nav-label");
        statsLabel.getStyleClass().add("nav-label");

        navLabels = Arrays.asList(homeLabel, statsLabel);
        homeLabel.getStyleClass().add("active");
        // Toggle visibility when clicking the title
        homeLabel.setOnMouseClicked(e -> togglePanel("home"));
        statsLabel.setOnMouseClicked(e -> togglePanel("stats"));

        // Get dropdown containers from the selector controller:
        VBox pollutantDropdownBox = selectorController.createPollutantSelector();
        VBox yearDropdownBox = selectorController.createYearSelector();
        VBox colorDropdownBox = selectorController.createColorSelector();
        pollutantDropdownBox.getStyleClass().add("dropdown");
        yearDropdownBox.getStyleClass().add("dropdown");
        colorDropdownBox.getStyleClass().add("dropdown");

        VBox sidePanelDropdownBox = new VBox();
        sidePanelDropdownBox.getChildren().addAll(pollutantDropdownBox, yearDropdownBox, colorDropdownBox);
        sidePanelDropdownBox.getStyleClass().add("dropdown-box");

        // Add dropdown menus to side panel:
        sidePanel.getChildren().addAll(applicationLabel, panelNav, sidePanelDropdownBox);

        // Add buttons to side panel:
        addSidePanelButtons(sidePanel);

        return sidePanel;
    }

    /**
     * Adds "View Statistics" and "Go Back to Map" buttons to the side panel.
     * @param sidePanel The side panel to add buttons to.
     */
    private void addSidePanelButtons(VBox sidePanel) {
        // Create a button for statistics panel:
        viewStatisticsButton = new Button("About Current Pollutant");
        viewStatisticsButton.setMaxWidth(Double.MAX_VALUE);
        viewStatisticsButton.setOnAction(e -> showStatisticsPanel());
        viewStatisticsButton.getStyleClass().add("view-stats");

        // Create a button for going back to the map:
        viewMapButton = new Button("Go Back to Map");
        viewMapButton.setMaxWidth(Double.MAX_VALUE);
        viewMapButton.setOnAction(e -> showMapPanel());
        viewMapButton.getStyleClass().add("view-map");

        // Initially show the statistics button:
        sidePanel.getChildren().add(viewStatisticsButton);
    }

    /**
     * Shows the statistics panel and updates the UI state.
     */
    private void showStatisticsPanel() {
        // Save current center content if it's the map.
        if (mapShown) {
            currentCenterContent = rootPane.getCenter();
        }
        
        // Update button visibility:
        sidePanel.getChildren().remove(viewStatisticsButton);
        if (!sidePanel.getChildren().contains(viewMapButton)) {
            sidePanel.getChildren().add(viewMapButton);
        }
        
        // Set the center content to statistics panel:
        rootPane.setCenter(statisticsController.getStatisticsPane());
        mapShown = false;
    }

    /**
     * Toggles between Home and Statistics panel
     */
    private void togglePanel(String panel) {
        navLabels.forEach(label-> label.getStyleClass().remove("active"));
        if (panel.equals("home")) {
            homeLabel.getStyleClass().add("active");
            showMapPanel();
        } else if (panel.equals("stats")) {
            statsLabel.getStyleClass().add("active");
            showStatisticsPanel();
        }
    }

        /**
         * Shows the map panel and updates the UI state.
         */
    private void showMapPanel() {
        // Update button visibility:
        sidePanel.getChildren().remove(viewMapButton);
        if (!sidePanel.getChildren().contains(viewStatisticsButton)) {
            sidePanel.getChildren().add(viewStatisticsButton);
        }
        
        rootPane.setCenter(currentCenterContent); // Restore the map view.
        mapShown = true;
    }

    /**
     * @return The side panel VBox.
     */
    public VBox getSidePanel() {
        return sidePanel;
    }
}