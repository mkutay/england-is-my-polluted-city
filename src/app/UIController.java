package app;

import dataProcessing.DataManager;
import dataProcessing.Pollutant;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.Arrays;

/**
 * Manages all UI elements that are not a part of the map - the side panel
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors
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
    public UIController(DataManager dataManager, MapController mapController) {
        this.dataManager = dataManager;

        // Create top navigation bar - currently has file and help menu
        topNavBar = createTopNavBar();
        topNavBar.getStyleClass().add("top-nav");

        // Create a Vbox for the side panel next to the map holding the dropdown menus to select pollutant and year
        sidePanel = createSidePanel(mapController);
        sidePanel.getStyleClass().add("side-panel");
    }

    /**
     * Initialises the UI, creating all UI elements and adding them to the side panel.
     * @return side panel
     */
    private VBox createSidePanel(MapController mapController) {
        VBox sidePanel = new VBox(10);
        //Create Label / title for the side panel
        Label applicationLabel = new Label("UK Pollution Explorer");
        applicationLabel.getStyleClass().add("app-title");

        //Create VBox containing dropdown menus
        VBox pollutantDropdownBox = createPollutantDropdownBox(mapController);
        VBox yearDropdownBox = createYearDropdownBox(mapController);

        //Add dropdown menus to side panel
        sidePanel.getChildren().addAll(applicationLabel, pollutantDropdownBox, yearDropdownBox);
        return sidePanel;
    }

    /**
     * Creates menus file and help, then adds to top navigation bar.
     * @return top navigation bar
     */
    private MenuBar createTopNavBar() {
        MenuBar topNavBar = new MenuBar();

        //File Menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        //Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        //aboutItem.setOnAction(e -> );
        helpMenu.getItems().add(aboutItem);

        topNavBar.getMenus().addAll(fileMenu, helpMenu);
        return topNavBar;
    }

    /**
     * Sets up the pollutant dropdown
     * @param mapController The current map controller.
     * @return The VBox containing the pollutant dropdown
     */
    private VBox createPollutantDropdownBox(MapController mapController) {
        Label label = new Label("Pollutant:");
        pollutantDropdown = new ComboBox<>();
        pollutantDropdown.getItems().addAll(Arrays.asList(Pollutant.values()));
        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);

        pollutantDropdown.setPromptText("Select Pollutant"); //Fallback
        pollutantDropdown.getSelectionModel().select(0); //set default as NO2

        pollutantDropdown.setOnAction(e -> {
            validateYearDropdown(mapController);
            updateMapDataSet(mapController); //Update the map data set on pollutant change
        });

        return new VBox(6, label, pollutantDropdown);
    }

    /**
     * Sets up the pollutant dropdown
     * @param mapController The current map controller.
     * @return The VBox containing the pollutant dropdown
     */
    private VBox createYearDropdownBox(MapController mapController) {
        Label label = new Label("Year:");
        yearDropdown = new ComboBox<>();

        //Add all years that the currently selected pollutant supports data for
        yearDropdown.getItems().addAll(dataManager.getAvailableYears(pollutantDropdown.getValue()));

        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        yearDropdown.setPromptText("Select Year"); //Fallback
        yearDropdown.getSelectionModel().select(0);

        //Update the map data set on year change
        yearDropdown.setOnAction(e -> updateMapDataSet(mapController));

        return new VBox(6, label, yearDropdown);
    }

    /**
     * Collects the current pollutant and year and calls mapController.updateMapDataSet with these parameters
     * @param mapController the mapController to update the working map data set for
     */
    private void updateMapDataSet(MapController mapController) {
        Integer selectedYear = yearDropdown.getValue();
        Pollutant selectedPollutant = pollutantDropdown.getValue();
        if (selectedYear != null && selectedPollutant != null) {
            mapController.updateMapDataSet(selectedYear, selectedPollutant, dataManager); //TODO async update so UI isnt frozen?
        }
    }

    /**
     * Updates the year dropdown menu to contain years that are available for the current dataset
     * When changing pollutants, this needs to be called
     */
    private void validateYearDropdown(MapController mapController) {
        int currentYear = yearDropdown.getValue();

        yearDropdown.setOnAction(null); //Temporarily disable yearDropdown listener so we don't call listener when updating

        //Update dropdown values
        yearDropdown.getItems().clear();
        yearDropdown.getItems().addAll(dataManager.getAvailableYears(pollutantDropdown.getValue()));

        //If the currently selected year is no longer available, select a default value
        if (!yearDropdown.getItems().contains(currentYear)) {
            yearDropdown.getSelectionModel().select(0);
        } else {
            int index = yearDropdown.getItems().indexOf(currentYear);
            yearDropdown.getSelectionModel().select(index);  //Otherwise, re-select current value
        }

        yearDropdown.setOnAction(e -> updateMapDataSet(mapController)); //Re-enable the listener
    }

    public VBox getSidePanel() {
        return sidePanel;
    }
    public MenuBar getTopNav() { return topNavBar; }
}
