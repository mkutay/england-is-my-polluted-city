import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import infoPopup.InfoPopup;
import infoPopup.MapClickHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pollutionLayer.PollutionLayer;
import utility.CustomMapView;

import java.util.Arrays;

import static com.gluonhq.maps.MapLayer.*;

/**
 * Main App class
 * This class creates a JavaFX application that can display a map of UK with pollution
 * data. It centres the map on London.
 *
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class App extends Application {
    private InfoPopup infoPopup;
    private Stage primaryStage; // Store reference to the primary stage
    private static final String PROJECT_NAME = "UK Pollution Explorer"; //The name of the project to be displayed at the top of the application

    //declare Variables related to the map and pollution display at the start for further modifications
    private static CustomMapView mapView;
    private static PollutionLayer pollutionLayer;
    private static MapClickHandler clickHandler;
    private static DataManager dataManager;
    private static MapLayer mapLayer;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage; // Store the stage reference

        dataManager = DataManager.getInstance();
        DataSet dataSet = dataManager.getPollutantData(2020, Pollutant.NO2); //Default launch

        // Create the info popup
        infoPopup = new InfoPopup();

        // Create map click handler and set it as the click listener
         clickHandler = new MapClickHandler(infoPopup, primaryStage);

        //Create a map view and PollutionLayer, adding the PollutionLayer to the map view
        mapView = new CustomMapView();
        pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
        mapLayer = new MapLayer();
        mapView.addLayer(pollutionLayer);
        mapView.addLayer(mapLayer);
        mapView.setZoom(10);

        // Create a Vbox for the side panel next to the map holding the dropdown menus to select pollutant and year
        VBox sidePanel = new VBox(10);
        sidePanel.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        //Create Label / title for the side panel
        Label applicationLabel = new Label(PROJECT_NAME);
        applicationLabel.setStyle("-fx-spacing: 10; -fx-font-weight: bold; -fx-font-size: 30px;");

        // Dropdown menu for pollutant selection wrapped in a VBox
        Label pollutantLabel = new Label("Pollutant:");
        ComboBox<Pollutant> pollutantDropdown = new ComboBox<>();

        //Add all pollutants to the dropdown
        for(Pollutant c : Pollutant.values())
            pollutantDropdown.getItems().addAll(c);

        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);
        pollutantDropdown.setPromptText("Select Pollutant"); //Fallback
        pollutantDropdown.getSelectionModel().select(0); //set default as NO2
        VBox pollutantDropdownBox = new VBox(6, pollutantLabel, pollutantDropdown);



        // Dropdown menu for year selection wrapped in a VBox
        Label yearLabel = new Label("Year:");
        ComboBox<Integer> yearDropdown = new ComboBox<>();
        for (Integer c : dataManager.getAvailableYears(pollutantDropdown.getValue())){
            yearDropdown.getItems().addAll(c);
        }


        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        yearDropdown.setPromptText("Select Year"); //Fallback
        yearDropdown.getSelectionModel().select(0); //set default as 2023
        VBox yearDropdownBox = new VBox(6, yearLabel, yearDropdown);

        //Listener to change the pollutant on the map
        pollutantDropdown.setOnAction(e -> {
            System.out.println("Selected Pollutant: " + pollutantDropdown.getValue());

//            //Refresh the year drop down for the current pollutant by removing all the current items and adding them back in
//            //TODO: could we make this more elegant and prevent the reuse of code from the main initialisation?
//            int currentYearSelected = yearDropdown.getValue();
//            yearDropdown.getItems().removeAll(yearDropdown.getItems());
//            for (Integer c : dataManager.getAvailableYears(pollutantDropdown.getValue())){
//                yearDropdown.getItems().addAll(c);
//            }
//            yearDropdown.getSelectionModel().select(0); //Set the first year from the list
            changeMapValues(yearDropdown.getValue(), pollutantDropdown.getValue());

        });


        //Listener to change the year on the map
        yearDropdown.setOnAction(e -> {
            System.out.println("Selected Year: " + yearDropdown.getValue());
            changeMapValues(yearDropdown.getValue(), pollutantDropdown.getValue());
        });


        // Add items to the side panel
        sidePanel.getChildren().addAll(applicationLabel, pollutantDropdownBox, yearDropdownBox);


        stage.setTitle("England is my Polluted City");

        //Create a StackPane to hold the mapView
        StackPane mapRoot = new StackPane();
        mapRoot.getChildren().add(mapView);
        /*
         * Create a BorderPane to hold all the elements of the GUI and nest the Map StackPane inside the BorderPane.
         * Place the side panel to the left of the map
         */
        BorderPane root = new BorderPane();
        root.setLeft(sidePanel);
        root.setCenter(mapView);


        Scene scene = new Scene(root, 900, 900);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        mapView.flyTo(0, startPosition, 0.01); // Instantly opens on top of London.


    }

    /**
     * A function that changes the
     * @param year Year as an integer from the
     * @param pollutant The Pollutant enum value (e.g NO2, PM10)
     */
    private void changeMapValues(int year, Pollutant pollutant){
        //remove the original pollutionLayer
        mapView.removeLayer(pollutionLayer);
        //Modify the pollution layer
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
        //add back the new pollution layer
        mapView.addLayer(pollutionLayer);
        mapView.dirtyRefresh();

    }

    public static void main(String[] args) {
        launch(args);
    }
}