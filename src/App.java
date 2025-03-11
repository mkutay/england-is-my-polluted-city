import java.util.Iterator;
import java.util.Map;

import com.gluonhq.maps.MapPoint;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import infoPopup.InfoPopup;
import infoPopup.MapClickHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pollutionLayer.PollutionLayer;
import statistics.back.StatisticsManager;
import statistics.back.StatisticsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.StatisticsPanelFactory;
import utility.CustomMapView;

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

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage; // Store the stage reference
        
        DataManager dataManager = DataManager.getInstance();
        DataSet dataSet = dataManager.getPollutantData(2023, Pollutant.PM10);

        // Create the info popup
        infoPopup = new InfoPopup();
        
        // Create map click handler and set it as the click listener
        MapClickHandler clickHandler = new MapClickHandler(infoPopup, primaryStage);

        CustomMapView mapView = new CustomMapView();
        PollutionLayer pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
    
        mapView.addLayer(pollutionLayer);
        mapView.setZoom(10);

        stage.setTitle("England is my Polluted City");


        StackPane mapRoot = new StackPane();
        mapRoot.getChildren().add(mapView);

        //Nest the Map StackPane inside the BorderPane
        BorderPane root = new BorderPane();
        //root.setLeft(sidePanel);
        root.setCenter(mapView);


        Scene scene = new Scene(root, 900, 900);

        // showStatsPanel(stage);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        mapView.flyTo(0, startPosition, 0.01); // Instantly opens on top of London.
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