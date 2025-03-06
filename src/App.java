import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

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
    @Override
    public void start(Stage stage) {
        MapView mapView = new MapView();
        mapView.addLayer(new PollutionLayer(mapView));
        mapView.setZoom(14);

        stage.setTitle("England is my Polluted City");

        StackPane root = new StackPane();
        root.getChildren().add(mapView);
        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); // These are the coordinates for Trafalgar Square.
        mapView.flyTo(0., startPosition, 0.1); // Instantly opens on top London.
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}