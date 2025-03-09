import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class used for rendering a single pollution "value" on the map. These values are represented
 * as polygons on the map, with the color of the polygon representing the pollution scale.
 * This polygon is, in reality, a close approximation of a square. The polygon has an area of 1 km^2.
 * We use these approximate squares to account for curvature of the earth, making a seamless grid mapped onto the earth
 *
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.1
 */
public class PollutionPolygon {
    private final int topLeftEasting; // The easting value of the top left corner.
    private final int topLeftNorthing; // The northing value of the top left corner.
    private final int sideLength; // The side length of the square in meters.
    private Color color;
    private final double value; // Store the pollution value

    private final List<MapPoint> worldCoordinates; // The world coordinates of the polygon, stored in lat/lon.

    private double[] xPoints;
    private double[] yPoints;

    /**
     * Constructor for PollutionPolygon.
     * @param topLeftEasting The easting value of the top left corner.
     * @param topLeftNorthing The northing value of the top left corner.
     * @param color The color of the polygon.
     * @param sideLength The side length of the square in meters.
     * @param value The pollution value this polygon represents.
     */
    public PollutionPolygon(int topLeftEasting, int topLeftNorthing, Color color, int sideLength, double value) {
        this.topLeftEasting = topLeftEasting;
        this.topLeftNorthing = topLeftNorthing;
        this.sideLength = sideLength;
        this.color = color;
        this.value = value;
        
        this.worldCoordinates = new ArrayList<>(4); // Pre-size for efficiency
        this.xPoints = new double[4];
        this.yPoints = new double[4];

        generatePoints();
    }

    /**
     * Set the opacity of the polygon.
     * @param opacity The opacity of the polygon, clamped to range [0.0, 1.0].
     */
    public void setOpacity(double opacity) {
        opacity = Math.min(Math.max(0.0, opacity), 1.0); // Sanitise to be in range 0 - 1.
        this.color = Color.rgb(
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255),
            opacity
        );
    }

    /**
     * @return The pollution value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Generates the world coordinates of the polygon, converting the easting and northings into longitude and latitude.
     */
    private void generatePoints() {
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + sideLength, topLeftNorthing));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting + sideLength, topLeftNorthing + sideLength));
        worldCoordinates.add(GeographicUtilities.convertEastingNorthingToLatLon(topLeftEasting, topLeftNorthing + sideLength));
    }

    /**
     * Updates the screen coordinates based on the current map projection.
     * @param pollutionLayer The layer containing the map projection.
     */
    public void updatePoints(PollutionLayer pollutionLayer) {
        int pointIndex = 0;
        for (MapPoint worldCoordinate : worldCoordinates) {
            Point2D screenPoint = pollutionLayer.getScreenPoint(worldCoordinate.getLatitude(), worldCoordinate.getLongitude());
            xPoints[pointIndex] = screenPoint.getX();
            yPoints[pointIndex] = screenPoint.getY();
            pointIndex++;
        }
    }

    /**
     * Gets the world coordinates of the polygon.
     * @return An unmodifiable view of the world coordinates list.
     */
    public List<MapPoint> getWorldCoordinates() {
        return Collections.unmodifiableList(worldCoordinates);
    }

    /**
     * Draw the polygon to the canvas.
     * @param gc The graphics context to draw the polygon onto.
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillPolygon(xPoints, yPoints, 4);
    }

    /**
     * Checks if the given point is inside this polygon.
     * This method is taken from this StackOverflow answer:
     * https://stackoverflow.com/a/2922778/28383027 on 2025-03-09.
     * @param x The x coordinate in screen space.
     * @param y The y coordinate in screen space.
     * @return True if the point is inside the polygon, false otherwise.
     */
    public boolean containsScreenPoint(double x, double y) {
        boolean contains = false;
        int n = xPoints.length;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (((yPoints[i] > y) != (yPoints[j] > y)) && (x < (xPoints[j] - xPoints[i]) * (y - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i])) {
                contains = !contains;
            }
        }
        return contains;
    }
}
