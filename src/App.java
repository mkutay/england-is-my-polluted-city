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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pollutionLayer.PollutionLayer;
import statistics.back.StatisticsManager;
import statistics.back.StatisticsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.StatisticsPanelFactory;
import utility.CustomMapView;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

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

        //create SidePanel
        SidePanel sidePanel = SidePanel.getInstance(dataManager, PROJECT_NAME);


        //Listener to change the pollutant on the map
        sidePanel.getPollutantDropdown().setOnAction(e -> {
            System.out.println("Selected Pollutant: " + sidePanel.getPollutantDropdown().getValue());
            updateMapDataSet(sidePanel.getYearDropdown().getValue(), sidePanel.getPollutantDropdown().getValue());

        });


        //Listener to change the year on the map
        sidePanel.getYearDropdown().setOnAction(e -> {
            System.out.println("Selected Year: " + sidePanel.getYearDropdown().getValue());
            updateMapDataSet(sidePanel.getYearDropdown().getValue(), sidePanel.getPollutantDropdown().getValue());
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

        // showStatsPanel(stage);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        mapView.flyTo(0, startPosition, 0.01); // Instantly opens on top of London.


    }

    /**
     * A function that updates the map dataset
     * @param year Year as an integer from the
     * @param pollutant The Pollutant enum value (e.g NO2, PM10)
     */
    private void updateMapDataSet(int year, Pollutant pollutant){
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

    public void showStatsPanel(Stage stage) {
        StatisticsManager sm = StatisticsManager.getInstance();
        Map<String, StatisticsResult> m = sm.calculateStatisticsOverTime(Pollutant.PM10, 2018, 2023);
        Iterator<String> it = m.keySet().iterator();
        it.next();
        it.next();
        StatisticsResult sr = m.get(it.next());
        System.out.println(sr.getTitle());

        StatisticsPanel statsRoot = StatisticsPanelFactory.createPanel(sr);
        Scene scene = new Scene(statsRoot, 900, 900);
        stage.setScene(scene);
        stage.show();
    }
}