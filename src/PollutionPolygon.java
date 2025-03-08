import java.util.ArrayList;
import java.util.List;

import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
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
public class PollutionPolygon{
    private final int topLeftEasting; // The easting value of the top left corner.
    private final int topLeftNorthing; // The northing value of the top left corner.
    private final int sideLength; // The side length of the square in meters.
    private Color color;

    private List<MapPoint> worldCoordinates; // The world coordinates of the polygon, stored in lat/lon.

    private double[] xPoints;
    private double[] yPoints;

    /**
     * Constructor for PollutionPolygon.
     * @param topLeftEasting The easting value of the top left corner.
     * @param topLeftNorthing The northing value of the top left corner.
     * @param color The color of the polygon.
     * @param sideLength The side length of the square in meters.
     */
    public PollutionPolygon(int topLeftEasting, int topLeftNorthing, Color color, int sideLength) {
        this.topLeftEasting = topLeftEasting;
        this.topLeftNorthing = topLeftNorthing;
        this.sideLength = sideLength;
        this.color = color;

        generatePoints();
    }

    public void setOpacity(double opacity) {
        opacity = Math.min(Math.abs(opacity), 1.0); //Sanitise to be in range 0-1
        this.color = Color.rgb(
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                opacity);
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

        xPoints = new double[4];
        yPoints = new double[4];
    }

    public void updatePoints(PollutionLayer pollutionLayer) {
        int pointIndex = 0;
        for (MapPoint worldCoordinate : worldCoordinates) {
            Point2D screenPoint = pollutionLayer.getScreenPoint(worldCoordinate.getLatitude(), worldCoordinate.getLongitude()) ;
            xPoints[pointIndex] = screenPoint.getX();
            yPoints[pointIndex] = screenPoint.getY();
            pointIndex ++;
        }
    }

    /**
     * @return The world coordinates of the polygon.
     */
    public List<MapPoint> getWorldCoordinates() {
        return worldCoordinates;
    }

    /**
     * Draw the polygon to the canvas
     * @param gc the graphics context to draw the polygon with
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, 4);
    }
}
