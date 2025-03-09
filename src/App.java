import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    @Override
    public void start(Stage stage) {
        MapView mapView = new MapView();
        PollutionLayer pollutionLayer = new PollutionLayer(mapView);

        // DistanceTracker distanceTracker = new DistanceTracker();

        mapView.addLayer(pollutionLayer);
        // mapView.addLayer(distanceTracker);

        mapView.setZoom(14);

        stage.setTitle("England is my Polluted City");

        StackPane root = new StackPane();
        root.getChildren().add(mapView);
        Scene scene = new Scene(root, 900, 900);

        stage.setScene(scene);
        stage.show();

        // scene.setOnMouseClicked((MouseEvent event) -> {
        //     distanceTracker.addNode(mapView.getMapPosition(event.getX(), event.getY()));
        // });

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        // MapPoint startPosition = new MapPoint(60.854303762303054, -0.8863583375656637);
        mapView.flyTo(0., startPosition, 0.01); // Instantly opens on top of London.
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}