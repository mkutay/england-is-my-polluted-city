import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import api.Api;
import dataProcessing.DataPicker;
import dataProcessing.DataSet;
import dataProcessing.LODManager;
import dataProcessing.Pollutant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main App class for the "England is my Polluted City" project. This class
 * creates a JavaFX application that can display a map of UK with pollution
 * data. It centres the map on London.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class App extends Application {
    private LODManager lodManager; // The LOD manager for the pollution data.

    @Override
    public void start(Stage stage) {
        DataSet dataSet = DataPicker.getPollutantData(2023, Pollutant.PM10);
        lodManager = new LODManager(dataSet, 4);

        MapView mapView = new MapView();
        PollutionLayer pollutionLayer = new PollutionLayer(mapView, lodManager);

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
        try {
            System.out.println(Api.fetchPostcodesByLatitudeLongitude(51.508045, -0.128217).getResult().get(0).getParliamentary_constituency());
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}