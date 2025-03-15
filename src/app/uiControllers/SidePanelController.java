package app.uiControllers;

import app.MapController;
import colors.ColorScheme;
import dataProcessing.Pollutant;
import app.App;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.List;

/**
 * Manages the side panel UI elements and coordinates between different view components.
 * 
 * @version 3.0
 */
public class SidePanelController {
    private final VBox sidePanel; // The side panel VBox
    private final BorderPane rootPane; // Root pane for swapping center content

    // UI Component Controllers:
    private final DataSelectionController dataSelectionController;
    private final ColorSchemeController colorSchemeController;
    private final StatisticsController statisticsController;
    private final MapController mapController;

    // UI Navigation Elements:
    private Button viewStatisticsButton;
    private Button viewMapButton;
    private Node mapContent;
    private boolean mapShown = true;

    private Label homeLabel;
    private Label statsLabel;
    private List<Label> navigationLabels;

    /**
     * Constructor for SidePanelController.
     * @param mapController The current map controller.
     * @param rootPane The root BorderPane of the application.
     */
    public SidePanelController(MapController mapController, BorderPane rootPane) {
        this.rootPane = rootPane;
        this.mapController = mapController;
        this.statisticsController = new StatisticsController();
        this.dataSelectionController = new DataSelectionController();
        this.colorSchemeController = new ColorSchemeController();

        this.mapContent = mapController.getMapOverlay();

        sidePanel = createSidePanel();
        sidePanel.getStyleClass().add("side-panel");
        
        // Set up event handlers
        setupEventHandlers();
    }

    /**
     * Sets up event handlers for data selection and color scheme changes.
     */
    private void setupEventHandlers() {
        // Handle data selection changes (year, pollutant):
        dataSelectionController.setOnSelectionChanged((year, pollutant) -> {
            ColorScheme colorScheme = colorSchemeController.getSelectedColorScheme();
            mapController.updateMapDataSet(year, pollutant, colorScheme);
            statisticsController.updateDataSet(year, pollutant);
        });
        
        // Handle color scheme changes:
        colorSchemeController.setOnColorSchemeChanged(colorScheme -> {
            Integer year = dataSelectionController.getSelectedYear();
            Pollutant pollutant = dataSelectionController.getSelectedPollutant();
            mapController.updateMapDataSet(year, pollutant, colorScheme);
        });
    }

    /**
     * Creates the side panel with all UI components.
     */
    private VBox createSidePanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("side-panel");
        
        // Create the title label for the side panel:
        Text appName = new Text(App.APP_NAME);
        TextFlow appTitle = new TextFlow(appName);
        appTitle.getStyleClass().add("app-title");

        // App logo
        Image img = new Image(getClass().getResourceAsStream("/resources/rainbow.png"));
        ImageView icon = new ImageView(img);
        icon.setFitWidth(50);  // Resizes width
        icon.setFitHeight(50); // Resizes height

        HBox appLabel = new HBox();
        appLabel.getChildren().addAll(icon, appTitle);
        appLabel.getStyleClass().add("app-label");


        // Create navigation bar:
        HBox panelNav = createNavigationBar();

        // Get data selection components:
        VBox pollutantDropdownBox = dataSelectionController.createPollutantSelector();
        VBox yearDropdownBox = dataSelectionController.createYearSelector();
        VBox colorDropdownBox = colorSchemeController.createColorSelector();
        
        pollutantDropdownBox.getStyleClass().add("dropdown");
        yearDropdownBox.getStyleClass().add("dropdown");
        colorDropdownBox.getStyleClass().add("dropdown");

        VBox selectionControls = new VBox();
        selectionControls.getChildren().addAll(pollutantDropdownBox, yearDropdownBox, colorDropdownBox);
        selectionControls.getStyleClass().add("dropdown-box");

        // Add components to side panel:
        panel.getChildren().addAll(appLabel, panelNav, selectionControls);

        // Add view switch buttons:
        addViewSwitchButtons(panel);

        return panel;
    }

    /**
     * Creates the navigation bar for the side panel.
     */
    private HBox createNavigationBar() {
        HBox panelNav = new HBox();
        homeLabel = new Label("Map View");
        statsLabel = new Label("Statistics View");
        Label signLabel = new Label(">");
        
        panelNav.getChildren().addAll(homeLabel, signLabel, statsLabel);
        panelNav.getStyleClass().add("side-panel-nav");
        
        homeLabel.getStyleClass().add("nav-label");
        statsLabel.getStyleClass().add("nav-label");
        
        navigationLabels = Arrays.asList(homeLabel, statsLabel);
        homeLabel.getStyleClass().add("active");
        
        // Set up click handlers.
        homeLabel.setOnMouseClicked(e -> switchToMapView());
        statsLabel.setOnMouseClicked(e -> switchToStatisticsView());
        
        return panelNav;
    }

    /**
     * Adds view switching buttons to the side panel.
     * @param panel The side panel VBox to add buttons to.
     */
    private void addViewSwitchButtons(VBox panel) {
        viewStatisticsButton = new Button("View Pollutant Statistics");
        viewStatisticsButton.setMaxWidth(Double.MAX_VALUE);
        viewStatisticsButton.setOnAction(e -> switchToStatisticsView());
        viewStatisticsButton.getStyleClass().add("view-stats");

        viewMapButton = new Button("Return to Map");
        viewMapButton.setMaxWidth(Double.MAX_VALUE);
        viewMapButton.setOnAction(e -> switchToMapView());
        viewMapButton.getStyleClass().add("view-map");

        // Initially show the statistics button
        panel.getChildren().add(viewStatisticsButton);
    }

    /**
     * Switches to statistics view.
     */
    private void switchToStatisticsView() {
        updateNavigationLabels("stats");
        
        // Switch buttons:
        sidePanel.getChildren().remove(viewStatisticsButton);
        if (!sidePanel.getChildren().contains(viewMapButton)) {
            sidePanel.getChildren().add(viewMapButton);
        }
        
        // Save map content.
        if (mapShown) {
            mapContent = rootPane.getCenter();
        }

        rootPane.setCenter(statisticsController.getStatisticsPane());
        mapShown = false;
    }
    
    /**
     * Switches to map view.
     */
    private void switchToMapView() {
        updateNavigationLabels("home");
        
        // Switch buttons:
        sidePanel.getChildren().remove(viewMapButton);
        if (!sidePanel.getChildren().contains(viewStatisticsButton)) {
            sidePanel.getChildren().add(viewStatisticsButton);
        }
        
        // Restore the map view.
        rootPane.setCenter(mapContent);
        mapShown = true;
    }

    /**
     * Updates the active state of navigation labels.
     */
    private void updateNavigationLabels(String active) {
        navigationLabels.forEach(label -> label.getStyleClass().remove("active"));
        
        if ("home".equals(active)) {
            homeLabel.getStyleClass().add("active");
        } else if ("stats".equals(active)) {
            statsLabel.getStyleClass().add("active");
        }
    }

    /**
     * @return The side panel VBox.
     */
    public VBox getSidePanel() {
        return sidePanel;
    }
}