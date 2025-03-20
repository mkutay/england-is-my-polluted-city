package app.uiViews;

import app.App;
import app.uiControllers.*;
import utility.ImageUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.List;

/**
 * Manages the UI layout and components of the side panel.
 * This class is responsible for UI construction and delegates logic to SidePanelController.
 * 
 * Refactor and class by Chelsea Feliciano
 * @author Chelsea Feliciano, Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong
 * @version 2.0
 */
public class SidePanel extends VBox {
    private Label mapLabel;
    private Label statsLabel;
    private Label switchLabel;
    private List<Label> navigationLabels;

    // UI Component Controllers:
    private DataSelectionController dataSelectionController;
    private ColorSchemeController colorSchemeController;
    private PollutionThresholdController pollutionThresholdController;

    private VBox pollutantDropdownBox;
    private VBox yearDropdownBox;
    private VBox endYearDropdownBox;
    private VBox colorDropdownBox;
    private VBox sliderContainer;

    /**
     * Constructor initialises the UI components.
     */
    public SidePanel(DataSelectionController dataSelectionController, ColorSchemeController colorSchemeController, PollutionThresholdController pollutionThresholdController) {
        this.dataSelectionController = dataSelectionController;
        this.colorSchemeController = colorSchemeController;
        this.pollutionThresholdController = pollutionThresholdController;

        this.getStyleClass().add("side-panel");
        this.getChildren().addAll(
            createAppLabel(),
            createNavigationBar(),
            createSelectionControls(),
            createPollutionThresholdSlider(),
            createSwitchButton()
        );
    }

    /**
     * Creates the app label and icon at the top.
     */
    private HBox createAppLabel() {
        HBox appLabel = new HBox();
        appLabel.getStyleClass().add("app-label");

        Text appName = new Text(App.APP_NAME);
        TextFlow appTitle = new TextFlow(appName);
        appTitle.getStyleClass().add("app-title");

        ImageView icon = ImageUtils.createImage("/resources/icons/rainbow.png", 50);
        appLabel.getChildren().addAll(icon, appTitle);

        return appLabel;
    }

    /**
     * Creates navigation bar for switching views.
     */
    private HBox createNavigationBar() {
        HBox panelNav = new HBox();
        mapLabel = new Label("Map View");
        statsLabel = new Label("Statistics View");
        Label signLabel = new Label(">");

        panelNav.getChildren().addAll(mapLabel, signLabel, statsLabel);
        panelNav.getStyleClass().add("side-panel-nav");

        mapLabel.getStyleClass().addAll("nav-label", "active");
        statsLabel.getStyleClass().add("nav-label");

        navigationLabels = Arrays.asList(mapLabel, statsLabel);

        return panelNav;
    }

    /**
     * Creates selection controls for year, pollutant, and color scheme.
     */
    private VBox createSelectionControls() {
        VBox selectionControls = new VBox();

        pollutantDropdownBox = dataSelectionController.createPollutantSelector();
        yearDropdownBox = dataSelectionController.createYearSelector();
        endYearDropdownBox = dataSelectionController.createEndYearSelector();
        colorDropdownBox = colorSchemeController.createColorSelector();

        pollutantDropdownBox.getStyleClass().add("dropdown");
        yearDropdownBox.getStyleClass().add("dropdown");
        endYearDropdownBox.getStyleClass().add("dropdown");
        colorDropdownBox.getStyleClass().add("dropdown");

        // Initially hide end year dropdown
        endYearDropdownBox.setVisible(false);
        endYearDropdownBox.setManaged(false);

        selectionControls.getChildren().addAll(pollutantDropdownBox, yearDropdownBox, endYearDropdownBox, colorDropdownBox);
        selectionControls.getStyleClass().add("dropdown-box");

        return selectionControls;
    }

    /**
     * Creates container for the pollution threshold slider.
     */
    private VBox createPollutionThresholdSlider() {
        sliderContainer = new VBox();
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
     * Creates the switch view button.
     */
    private HBox createSwitchButton() {
        HBox switchButton = new HBox();
        switchButton.getStyleClass().add("switch-button");

        switchLabel = new Label("ⓘ View Pollutant Statistics");
        switchLabel.getStyleClass().add("switch-label");

        switchButton.getChildren().add(switchLabel);
        return switchButton;
    }

    /**
     * Updates the navigation labels when switching views.
     */
    public void updateNavigationLabels(String active) {
        navigationLabels.forEach(label -> label.getStyleClass().remove("active"));
        if ("map".equals(active)) {
            mapLabel.getStyleClass().add("active");
        } else if ("stats".equals(active)) {
            statsLabel.getStyleClass().add("active");
        }
    }

    // Getters:
    public Label getMapLabel() {return mapLabel;}
    public Label getStatsLabel() {return statsLabel;}
    public Label getSwitchLabel() {return switchLabel;}
    public VBox getEndYearDropdownBox() {return endYearDropdownBox;}
    public VBox getColorDropdownBox() {return colorDropdownBox;}
    public VBox getSliderContainer() {return sliderContainer;}
}
