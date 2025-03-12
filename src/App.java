import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import guiComponents.SidePanel;
import infoPopup.InfoPopup;
import infoPopup.MapClickHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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

        //Create the SidePanel
        SidePanel sidePanel = SidePanel.getInstance(dataManager, PROJECT_NAME);

        //Listener to change the pollutant on the map
        sidePanel.getPollutantDropdown().setOnAction(e -> {
            System.out.println("Selected Pollutant: " + sidePanel.getPollutantDropdown().getValue());

//            //Refresh the year drop down for the current pollutant by removing all the current items and adding them back in
//            //TODO: could we make this more elegant and prevent the reuse of code from the main initialisation?
//            int currentYearSelected = yearDropdown.getValue();
//            yearDropdown.getItems().removeAll(yearDropdown.getItems());
//            for (Integer c : dataManager.getAvailableYears(pollutantDropdown.getValue())){
//                yearDropdown.getItems().addAll(c);
//            }
//            yearDropdown.getSelectionModel().select(0); //Set the first year from the list
            changeMapValues(sidePanel.getYearDropdown().getValue(), sidePanel.getPollutantDropdown().getValue());

        });


        //Listener to change the year on the map
        sidePanel.getYearDropdown().setOnAction(e -> {
            System.out.println("Selected Year: " + sidePanel.getYearDropdown().getValue());
            changeMapValues(sidePanel.getYearDropdown().getValue(), sidePanel.getPollutantDropdown().getValue());
        });


        stage.setTitle("England is my Polluted City");

        //Create a StackPane to hold the mapView
        StackPane mapRoot = new StackPane();
        mapRoot.getChildren().add(mapView);
        /*
         * Create a BorderPane to hold all the elements of the GUI and nest the Map StackPane inside the BorderPane.
         * Place the side panel to the left of the map
         */
        BorderPane root = new BorderPane();
        root.setLeft(sidePanel.getSidePanel());
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