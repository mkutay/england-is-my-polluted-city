import com.gluonhq.maps.MapPoint;

import dataProcessing.DataPicker;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import infoPopup.InfoPopup;
import infoPopup.MapClickHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pollutionLayer.PollutionLayer;
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
        
        DataSet dataSet = DataPicker.getPollutantData(2023, Pollutant.PM10);

        // Create the info popup
        infoPopup = new InfoPopup();
        
        // Create map click handler and set it as the click listener
        MapClickHandler clickHandler = new MapClickHandler(infoPopup, primaryStage);

        CustomMapView mapView = new CustomMapView();
        PollutionLayer pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
    

        mapView.addLayer(pollutionLayer);
        mapView.setZoom(14);

        stage.setTitle("England is my Polluted City");

        StackPane root = new StackPane();
        root.getChildren().add(mapView);
        Scene scene = new Scene(root, 900, 900);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        mapView.flyTo(0, startPosition, 0.01); // Instantly opens on top of London.
    }

    public static void main(String[] args) {
        launch(args);
    }
}