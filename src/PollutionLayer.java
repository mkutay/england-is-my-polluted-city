import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

/**
 * TODO: remove or refactor
 * Class for pollution Rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final Circle circle;
    private final ObservableList<Pair<MapPoint, Node>> points = FXCollections.observableArrayList();

    /**
     * Constructor for PollutionLayer.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        circle = new Circle(30, Color.RED);
        circle.setOpacity(0.5);
        this.getChildren().add(circle);
    }

    /**
     * Method to update the layout for pollution.
     */
    @Override
    protected void layoutLayer() {
        MapPoint point = new MapPoint(51.51275, -0.117278); // Point to draw circle -- Bush House
        Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
        circle.setVisible(true);
        circle.setTranslateX(mapPoint.getX());
        circle.setTranslateY(mapPoint.getY());


        // Experimenting with how to get an accurate scale value to use, converting pixel size to real world size.
        // TODO: Work on this.
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(mapView.getWidth(), mapView.getHeight());
        double currentScale = (GeographicUtilities.geodesicDistance(A, B)/mapView.getWidth());
        circle.setRadius(30 / currentScale);
        System.out.println(currentScale);
        //This currently displays the distance in meters of which 1 pixel on screen corresponds to.

    }
}
