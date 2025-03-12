package guiComponents;

import dataProcessing.DataManager;
import dataProcessing.Pollutant;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.util.Arrays;

/**
 * a "view" Class to manage the GUI objects of the Side Panel
 */
public class SidePanel {
    //Make this class a singleton (only one SidePanel should ever be made)
    private static SidePanel uniqueInstance;

    //Define the ComboBox and VBox for the pollutant and year selectors in the GUI
    private ComboBox<Pollutant> pollutantDropdown;
    private ComboBox<Integer> yearDropdown;
    private VBox pollutantDropdownBox;
    private VBox yearDropdownBox;



    VBox sidePanel;
    private SidePanel(DataManager dataManager, String PROJECT_NAME){
        // Create a Vbox for the side panel next to the map holding the dropdown menus to select pollutant and year
        sidePanel = new VBox(10);
        sidePanel.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        //Create Label / title for the side panel
        Label applicationLabel = new Label(PROJECT_NAME);
        applicationLabel.setStyle("-fx-spacing: 10; -fx-font-weight: bold; -fx-font-size: 30px;");

        // Dropdown menu for pollutant selection wrapped in a VBox
        Label pollutantLabel = new Label("Pollutant:");
        pollutantDropdown = new ComboBox<>();

        //Add all pollutants to the dropdown
        pollutantDropdown.getItems().addAll(Arrays.asList(Pollutant.values()));

        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);
        pollutantDropdown.setPromptText("Select Pollutant"); //Fallback
        pollutantDropdown.getSelectionModel().select(0); //set default as NO2
        pollutantDropdownBox = new VBox(6, pollutantLabel, pollutantDropdown);

        //Modifies appearance of each item in dropdown menu
        pollutantDropdown.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getDisplayName()); // Shows "PM2.5"
            }
        });

        //Modifies appearance of selected item (button cell) in dropdown menu
        pollutantDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getDisplayName()); // Shows "PM2.5"
            }
        });

        // Dropdown menu for year selection wrapped in a VBox
        Label yearLabel = new Label("Year:");
        yearDropdown = new ComboBox<>();
        for (Integer c : dataManager.getAvailableYears(pollutantDropdown.getValue())){
            yearDropdown.getItems().addAll(c);
        }

        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        yearDropdown.setPromptText("Select Year"); //Fallback
        yearDropdown.getSelectionModel().select(0); //set default as 2023
        yearDropdownBox = new VBox(6, yearLabel, yearDropdown);

        //Add all items to the side panel
        sidePanel.getChildren().addAll(applicationLabel, pollutantDropdownBox, yearDropdownBox);
    }

    /**
     *
     * @return Returns the pollutant selector GUI ComboBox
     */
    public ComboBox<Pollutant> getPollutantDropdown(){
        return pollutantDropdown;
    }

    /**
     *
     * @return Returns the year selector GUI ComboBox
     */
    public ComboBox<Integer> getYearDropdown(){
        return yearDropdown;
    }

    /**
     *
     * @return Returns the VBox that holds the Year Dropdown Menu + Label
     */
    public VBox getYearDropdownBox(){
        return yearDropdownBox;
    }

    /**
     *
     * @return Returns the VBox that holds the Pollutant Dropdown Menu + Label
     */
    public VBox getPollutantDropdownBox(){
        return pollutantDropdownBox;
    }

    /**
     *A singleton getInstance() method to create only 1 side panel (if a new one is attempted to be made, it will simply return)
     *
     * @param dataManager The data manager to be passed into the unique instance
     * @param PROJECT_NAME The project name to be passed into the unique instance
     * @return The unique SidePanel object created
     */
    public static SidePanel getInstance(DataManager dataManager, String PROJECT_NAME){
        if (uniqueInstance == null){
            uniqueInstance = new SidePanel(dataManager, PROJECT_NAME);
        }
        return uniqueInstance;
    }

    /**
     *
     * @return Returns the sidePanel VBox object
     */
    public VBox getSidePanel(){
        return sidePanel;
    }
}
