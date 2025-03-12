package app;

import dataProcessing.DataManager;
import dataProcessing.Pollutant;
import utility.Namer;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * Manages all UI elements that are not a part of the map, eg. the side panel.
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 */
public class UIController {
    private final VBox sidePanel;
    private final MenuBar topNavBar;
    private final DataManager dataManager;

    private ComboBox<Pollutant> pollutantDropdown;
    private ComboBox<Integer> yearDropdown;

    /**
     * Constructor for UIController.
     * @param dataManager The current data manger instance.
     * @param mapController The current map controller.
     */
    public UIController(MapController mapController) {
        this.dataManager = DataManager.getInstance();

        // Create top navigation bar:
        topNavBar = createTopNavBar();
        topNavBar.getStyleClass().add("top-nav");

        // Create a Vbox for the side panel next to the map holding the dropdown menus to select pollutant and year:
        sidePanel = createSidePanel(mapController);
        sidePanel.getStyleClass().add("side-panel");
    }

    /**
     * Initialises the UI, creating all UI elements and adding them to the side panel.
     * @return The side panel VBox containing all UI elements.
     */
    private VBox createSidePanel(MapController mapController) {
        VBox sidePanel = new VBox(10);
        // Create Label: Title for the side panel.
        Label applicationLabel = new Label(Namer.APP_NAME);
        applicationLabel.getStyleClass().add("app-title");

        // Create VBox containing dropdown menus.
        VBox pollutantDropdownBox = createPollutantDropdownBox(mapController);
        VBox yearDropdownBox = createYearDropdownBox(mapController);

        // Add dropdown menus to side panel.
        sidePanel.getChildren().addAll(applicationLabel, pollutantDropdownBox, yearDropdownBox);
        return sidePanel;
    }

    /**
     * Creates menus file and help, and then adds them to top navigation bar.
     * @return Top navigation bar.
     */
    private MenuBar createTopNavBar() {
        MenuBar topNavBar = new MenuBar();

        // File Menu:
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Help Menu:
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);
        // TODO: Add "about" functionality

        topNavBar.getMenus().addAll(fileMenu, helpMenu);
        return topNavBar;
    }

    /**
     * Sets up the pollutant dropdown:
     * @param mapController The current map controller.
     * @return The VBox containing the pollutant dropdown.
     */
    private VBox createPollutantDropdownBox(MapController mapController) {
        Label label = new Label("Pollutant:");
        pollutantDropdown = new ComboBox<>();
        pollutantDropdown.getItems().addAll(Arrays.asList(Pollutant.values()));
        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);

        pollutantDropdown.setPromptText("Select Pollutant"); // Fallback text.
        pollutantDropdown.getSelectionModel().select(0); // Set default as NO2.

        pollutantDropdown.setOnAction(e -> {
            validateYearDropdown(mapController);
            updateMapDataSet(mapController); // Update the map data set on pollutant change.
        });

        return new VBox(6, label, pollutantDropdown);
    }

    /**
     * Sets up the pollutant dropdown
     * @param mapController The current map controller.
     * @return The VBox containing the pollutant dropdown.
     */
    private VBox createYearDropdownBox(MapController mapController) {
        Label label = new Label("Year:");
        yearDropdown = new ComboBox<>();

        // Add all years that the currently selected pollutant supports data for:
        List<Integer> years = dataManager.getAvailableYears(pollutantDropdown.getValue());
        Collections.sort(years);
        yearDropdown.getItems().addAll(years);

        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        yearDropdown.setPromptText("Select Year"); // Fallback text.
        yearDropdown.getSelectionModel().select(0); // First year in the list.

        // Update the map data set on year change:
        yearDropdown.setOnAction(e -> updateMapDataSet(mapController));

        return new VBox(6, label, yearDropdown);
    }

    /**
     * Updates the map data set with the selected pollutant and year.
     * @implNote Collects the current pollutant and year and calls mapController.updateMapDataSet with these parameters.
     * @param mapController The mapController to update the working map data set for.
     */
    private void updateMapDataSet(MapController mapController) {
        Integer selectedYear = yearDropdown.getValue();
        Pollutant selectedPollutant = pollutantDropdown.getValue();
        if (selectedYear != null && selectedPollutant != null) {
            mapController.updateMapDataSet(selectedYear, selectedPollutant); // TODO: Async update so UI isnt frozen?
        }
    }

    /**
     * Updates the year dropdown menu to contain years that are available for the current dataset.
     * When changing pollutants, this needs to be called.
     */
    private void validateYearDropdown(MapController mapController) {
        int currentYear = yearDropdown.getValue();

        // Temporarily disable yearDropdown listener so we don't call listener when updating dropdown values.
        yearDropdown.setOnAction(null);

        // Update dropdown values:
        yearDropdown.getItems().clear();

        List<Integer> years = dataManager.getAvailableYears(pollutantDropdown.getValue());
        Collections.sort(years);
        yearDropdown.getItems().addAll(years);

        // If the currently selected year is no longer available, select a default value.
        if (!yearDropdown.getItems().contains(currentYear)) {
            yearDropdown.getSelectionModel().select(0); // First in the list.
        } else {
            int index = yearDropdown.getItems().indexOf(currentYear);
            yearDropdown.getSelectionModel().select(index);  // Otherwise, re-select current value.
        }

        yearDropdown.setOnAction(e -> updateMapDataSet(mapController)); // Re-enable the listener.
    }

    // Getters:
    public VBox getSidePanel() { return sidePanel; }
    public MenuBar getTopNav() { return topNavBar; }
}
