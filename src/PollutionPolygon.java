import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class PollutionPolygon extends Polygon {
    private final int topLeftEasting;
    private final int topLeftNorthing;

    private final int offset;

    private MapPoint[] worldCoordinates;

    public PollutionPolygon(int topLeftEasting, int topLeftNorthing, Color color, int offset) {
        this.setFill(color);

        this.topLeftEasting = topLeftEasting;
        this.topLeftNorthing = topLeftNorthing;
        this.offset = offset;

        generatePoints();
    }

    private void generatePoints() {
        worldCoordinates = new MapPoint[] {
                GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing),
                GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + offset, topLeftNorthing),
                GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + offset, topLeftNorthing + offset),
                GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing + offset)
        };

        for (int i = 0; i < 8; i++) {
            getPoints().add(0.0);
        }
    }

    public MapPoint[] getWorldCoordinates() {
        return worldCoordinates;
    }
}
