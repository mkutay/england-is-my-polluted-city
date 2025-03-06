import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * TODO remove or refactor
 * Test class for pollution rendering
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final Circle circle;

    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        circle = new Circle(30, Color.RED);
        circle.setOpacity(0.5);
        this.getChildren().add(circle);
    }

    @Override
    protected void layoutLayer() {
        MapPoint point = new MapPoint(51.51275, -0.117278); //Point to draw circle
        Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
        circle.setVisible(true);
        circle.setTranslateX(mapPoint.getX());
        circle.setTranslateY(mapPoint.getY());

        circle.setRadius(30 * mapView.getZoom()/14);

        //Experimenting with how to get an accurate scale value to use, converting pixel size to real world size
        //WIP TODO
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(1, 1);
        double xScale = A.getLatitude() - B.getLatitude();
        double yScale = A.getLongitude() - B.getLongitude();
        System.out.println(xScale + " " + yScale);
    }
}
