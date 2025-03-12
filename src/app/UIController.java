package app;

import dataProcessing.DataManager;
import dataProcessing.Pollutant;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;

/**
 * Manages all UI elements that are not a part of the map - the side panel
 */
public class UIController {
    private final VBox sidePanel;
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

        // Create a Vbox for the side panel next to the map holding the dropdown menus to select pollutant and year
        sidePanel = new VBox(10);
        sidePanel.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        initialiseUI(mapController);
    }

    /**
     * Initialises the UI, creating all UI elements and adding them to the side panel.
     */
    private void initialiseUI(MapController mapController) {
        //Create Label / title for the side panel
        Label applicationLabel = new Label("UK Pollution Explorer");
        applicationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;");

        //Create VBox containing dropdown menus
        VBox pollutantDropdownBox = createPollutantDropdownBox(mapController);
        VBox yearDropdownBox = createYearDropdownBox(mapController);

        //Add dropdown menus to side panel
        sidePanel.getChildren().addAll(applicationLabel, pollutantDropdownBox, yearDropdownBox);
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
}
