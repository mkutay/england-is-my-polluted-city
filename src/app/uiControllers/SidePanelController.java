package app.uiControllers;

import app.MapController;
import colors.ColorScheme;
import dataProcessing.Pollutant;
import app.App;
import utility.ImageUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.List;

/**
 * Manages the side panel UI elements and coordinates between different view components.
 *
 * @author Mehmet Kutay Bozkurt and Chelsea Feliciano
 * @version 3.0
 */
public class SidePanelController {
    private final VBox sidePanel; // The side panel VBox
    private final BorderPane rootPane; // Root pane for swapping center content

    // UI Component Controllers:
    private final DataSelectionController dataSelectionController;
    private final ColorSchemeController colorSchemeController;
    private final StatisticsController statisticsController;
    private final PollutionThresholdController pollutionThresholdController;
    private final MapController mapController;

    // UI Navigation Elements:
    private Node mapContent;
    private boolean mapShown = true;

    private Label homeLabel;
    private Label statsLabel;
    private Label switchLabel;
    private List<Label> navigationLabels;

    private VBox endYearDropdownBox;
    private VBox colorDropdownBox;

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
        this.pollutionThresholdController = new PollutionThresholdController();

        this.mapContent = mapController.getMapOverlay();

        sidePanel = initialiseSidePanel();
        setupEventHandlers();
    }

    /**
     * Initialises the side panel UI components.
     */
    private VBox initialiseSidePanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("side-panel");

        panel.getChildren().addAll(
            createAppLabel(),
            createNavigationBar(),
            createSelectionControls(),
            createPollutionThresholdSlider(),
            createSwitchButton()
        );

        return panel;
    }

    private HBox createAppLabel() {
        HBox appLabel = new HBox();
        appLabel.getStyleClass().add("app-label");

        // Create the title label for the side panel:
        Text appName = new Text(App.APP_NAME);
        TextFlow appTitle = new TextFlow(appName);
        appTitle.getStyleClass().add("app-title");

        // App logo
        ImageView icon = ImageUtils.createImage("/resources/icons/rainbow.png", 50);

        appLabel.getChildren().addAll(icon, appTitle);
        return appLabel;
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
        homeLabel.setOnMouseClicked(e -> switchView());
        statsLabel.setOnMouseClicked(e -> switchView());

        return panelNav;
    }

    /**
     * Creates selection controls for year, pollutant, and colour scheme.
     */
    private VBox createSelectionControls() {
        VBox selectionControls = new VBox();

        VBox pollutantDropdownBox = dataSelectionController.createPollutantSelector();
        VBox yearDropdownBox = dataSelectionController.createYearSelector();
        endYearDropdownBox = dataSelectionController.createEndYearSelector();
        colorDropdownBox = colorSchemeController.createColorSelector();

        pollutantDropdownBox.getStyleClass().add("dropdown");
        yearDropdownBox.getStyleClass().add("dropdown");
        endYearDropdownBox.getStyleClass().add("dropdown");
        colorDropdownBox.getStyleClass().add("dropdown");
        
        // Initially hide the end year dropdown since we start with map view.
        endYearDropdownBox.setVisible(false);
        endYearDropdownBox.setManaged(false);

        selectionControls.getChildren().addAll(pollutantDropdownBox, yearDropdownBox, endYearDropdownBox, colorDropdownBox);
        selectionControls.getStyleClass().add("dropdown-box");

        return selectionControls;
    }

    /**
     * Creates container for the threshold slider, along with label
     */
    private VBox createPollutionThresholdSlider() {
        VBox sliderContainer = new VBox();
        sliderContainer.setAlignment(Pos.CENTER);

        Label sliderLabel = new Label();
        Slider slider = pollutionThresholdController.getThresholdSlider();

        StringExpression labelText = Bindings.format("Pollution threshold - %.0f%%", slider.valueProperty().multiply(100));
        sliderLabel.textProperty().bind(labelText);

        slider.getStyleClass().add("thresholdSlider");
        sliderLabel.getStyleClass().add("thresholdSliderLabel");

        sliderContainer.getChildren().addAll(sliderLabel, slider);

        return sliderContainer;
    }
    /**
     * Sets up event handlers for data selection and color scheme changes.
     */
    private void setupEventHandlers() {
        // Handle data selection changes (year, pollutant):
        dataSelectionController.setOnSelectionChanged((year, pollutant) -> {
            ColorScheme colorScheme = colorSchemeController.getSelectedColorScheme();
            
            mapController.updateMapDataSet(year, pollutant, colorScheme);
            
            if (!mapShown) {
                statisticsController.updateDataSet(year, pollutant);
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

        pollutionThresholdController.getThresholdSlider().valueProperty().addListener((_, _, newValue) -> {
            mapController.updatePollutionThreshold(newValue.doubleValue());
        });
    }

    /**
     * Creates view switching button to the side panel.
     */
    private HBox createSwitchButton() {
        HBox switchButton = new HBox();
        switchButton.getStyleClass().add("switch-button");

        switchLabel = new Label("ⓘ View Pollutant Statistics");
        switchLabel.getStyleClass().add("switch-label");

        switchLabel.setOnMouseClicked(e -> switchView());
        switchButton.getChildren().add(switchLabel);
        return switchButton;
    }

    /**
     * Switches between the map view and statistics view.
     */
    private void switchView() {
        updateNavigationLabels(mapShown ? "stats" : "home");

        if (mapShown) {
            // Switching to statistics view.
            rootPane.setCenter(statisticsController.getStatisticsPane());
            switchLabel.setText("Return to Map");
            
            // Show end year dropdown and change year label to "Start Year".
            endYearDropdownBox.setVisible(true);
            endYearDropdownBox.setManaged(true);
            dataSelectionController.setYearLabelText("Start Year:");

            colorDropdownBox.setVisible(false);
            colorDropdownBox.setManaged(false);
            
            Integer startYear = dataSelectionController.getSelectedYear();
            Integer endYear = dataSelectionController.getSelectedEndYear();
            Pollutant pollutant = dataSelectionController.getSelectedPollutant();
            
            if (startYear != null && endYear != null && pollutant != null) {
                statisticsController.updateDataSetRange(startYear, endYear, pollutant);
            }
            
            mapShown = false;
        } else {
            // Switching to map view
            rootPane.setCenter(mapContent);
            switchLabel.setText("ⓘ View Pollutant Statistics");
            
            // Hide end year dropdown and change year label back to "Year".
            endYearDropdownBox.setVisible(false);
            endYearDropdownBox.setManaged(false);
            dataSelectionController.setYearLabelText("Year:");

            colorDropdownBox.setVisible(true);
            colorDropdownBox.setManaged(true);
            
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