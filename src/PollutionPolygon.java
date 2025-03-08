import java.util.ArrayList;
import java.util.List;

import com.gluonhq.maps.MapPoint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Class used for rendering a single pollution "value" on the map. These values are represented
 * as polygons on the map, with the color of the polygon representing the pollution scale.
 * This polygon is, in reality, a close approximation of a square. The polygon has an area of 1 km^2.
 * We use these approximate squares to account for curvature of the earth, making a seamless grid mapped onto the earth
 *
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionPolygon extends Polygon {
    private final int topLeftEasting; // The easting value of the top left corner.
    private final int topLeftNorthing; // The northing value of the top left corner.
    private final int sideLength; // The side length of the square in meters.

    private List<MapPoint> worldCoordinates; // The world coordinates of the polygon, stored in lat/lon.

    /**
     * Constructor for PollutionPolygon.
     * @param topLeftEasting The easting value of the top left corner.
     * @param topLeftNorthing The northing value of the top left corner.
     * @param color The color of the polygon.
     * @param sideLength The side length of the square in meters.
     */
    public PollutionPolygon(int topLeftEasting, int topLeftNorthing, Color color, int sideLength) {
        this.setFill(color);

        this.topLeftEasting = topLeftEasting;
        this.topLeftNorthing = topLeftNorthing;
        this.sideLength = sideLength;

        generatePoints();
    }

    /**
     * Generates the world coordinates of the polygon, converting the easting and northings into longitude and latitude
     */
    private void generatePoints() {
        worldCoordinates = new ArrayList<>();
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + sideLength, topLeftNorthing));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + sideLength, topLeftNorthing + sideLength));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing + sideLength));

        for (int i = 0; i < 8; i++) { //Initialise the polygon with coordinates
            getPoints().add(0.0);
        }
    }

    /**
     * @return The world coordinates of the polygon.
     */
    public List<MapPoint> getWorldCoordinates() {
        return worldCoordinates;
    }

    public String getTopLeftEastingNorthing() {
        return topLeftEasting + "," + topLeftNorthing;
    }
}
