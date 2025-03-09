import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Test class
 * TODO remove
 */
public class DistanceTracker extends MapLayer {
    private MapPoint A;
    private MapPoint B;

    private final Circle circleA;
    private final Circle circleB;

    public boolean lastSetA = false;

    public DistanceTracker() {
        this.A = new MapPoint(0,0);
        this.B = new MapPoint(0,0);

        circleA = new Circle(10, Color.RED);
        circleB = new Circle(10, Color.BLUE);

        this.getChildren().addAll(circleA, circleB);
    }

    public void addNode(MapPoint point) {
        if (lastSetA) {
            B = point;
        } else {
            A = point;
        }
        lastSetA = !lastSetA;
        layoutLayer();
    }


    @Override
    protected void layoutLayer() {
        Point2D pointA = getMapPoint(A.getLatitude(), A.getLongitude());
        Point2D pointB = getMapPoint(B.getLatitude(), B.getLongitude());

        circleA.setTranslateX(pointA.getX());
        circleA.setTranslateY(pointA.getY());

        circleB.setTranslateX(pointB.getX());
        circleB.setTranslateY(pointB.getY());

        System.out.println("Distance between A and B: " + GeographicUtilities.geodesicDistance(A, B)/1000 + "km");
    }
}
