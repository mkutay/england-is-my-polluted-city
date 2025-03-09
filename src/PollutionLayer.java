import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import colors.ColorScheme;
import colors.DefaultColorScheme;
import dataProcessing.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for pollution rendering on the map.
 * Handles rendering pollution data as polygons on the map layer.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 2.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final List<PollutionPolygon> polygons;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final LODManager lodManager;
    private int currentLODIndex = -1;
    
    // Color configuration
    private ColorScheme colorScheme;
    private double polygonOpacity = 0.7;

    /**
     * Constructor for PollutionLayer. Generates pollution data polygons from the files.
     * @param mapView The map view to render the pollution layer on.
     * @param lodManager The level of detail manager.
     */
    public PollutionLayer(MapView mapView, LODManager lodManager) {
        this.mapView = mapView;
        this.lodManager = lodManager;
        this.polygons = new ArrayList<>();
        this.colorScheme = new DefaultColorScheme();

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        updatePollutionDataPoints();
    }

    /**
     * Generates pollution data polygons from the CSV files.
     * Called every time a LOD change is detected
     */
    private void updatePollutionDataPoints() {
        int lodIndex = lodManager.getLODIndex(getPixelScale(), mapView.getWidth(), mapView.getHeight());
        if (lodIndex == currentLODIndex) return; // No LOD update needed, exit.
        currentLODIndex = lodIndex;

        generatePolygons(lodManager.getLODData(currentLODIndex));
        this.markDirty();
    }

    /**
     * Regenerate polygons.
     * @param lodData the LOD data to use to generate the polygons.
     */
    private void generatePolygons(LODData lodData) {
        polygons.clear(); // Reset polygons
        
        // Find min/max values for color mapping
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : lodData.getData()) {
            double value = dataPoint.value();
            if (value == -1) continue; // If the value is missing, skip it.
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        for (DataPoint dataPoint : lodData.getData()) {
            if (dataPoint.value() == -1) continue; // Skip missing values.

            // Map data value to a color using the color scheme
            double normalizedValue = (dataPoint.value() - minValue) / (maxValue - minValue);
            Color color = colorScheme.getColor(normalizedValue);
            
            int sideLength = 1000 * lodData.getLevelOfDetail();

            // The easting and northing values given are the centroids of the grid, meaning we need to offset them.
            // We offset by 500m in both directions.
            int topLeftEasting = dataPoint.x() - 500;
            int topLeftNorthing = dataPoint.y() - 500;

            PollutionPolygon polygon = new PollutionPolygon(topLeftEasting, topLeftNorthing, color, sideLength);
            polygon.setOpacity(polygonOpacity);
            polygons.add(polygon);
        }
    }

    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level.
     * ie pixel size * sf = real world size
     * @return Scale factor for pixel scale.
     */
    private double getPixelScale() {
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(mapView.getWidth(), 0);
        return mapView.getWidth() / GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen.
     * @param x The x coordinate in pixels.
     * @param y The y coordinate in pixels.
     * @param padding The padding inside the screen.
     * @return True if the point is on the screen, false otherwise.
     */
    private boolean isPointOnScreen(double x, double y, double padding) {
        return x >= -padding && x <= mapView.getWidth() + padding && 
            y >= -padding && y <= mapView.getHeight() + padding;
    }

    /**
     * Re-draws all polygons every time the mapView is moved around.
     */
    @Override
    protected void layoutLayer() {
        updatePollutionDataPoints();
        double iconSize = 1000 * lodManager.getLODData(currentLODIndex).getLevelOfDetail() * getPixelScale();

        canvas.setWidth(mapView.getWidth());
        canvas.setHeight(mapView.getHeight());

        gc.clearRect(0, 0, mapView.getWidth(), mapView.getHeight()); // Clear canvas.
        
        for (PollutionPolygon polygon : polygons) {
            MapPoint polygonTopLeft = polygon.getWorldCoordinates().getFirst();
            Point2D polygonTopLeftScreen = getMapPoint(polygonTopLeft.getLatitude(), polygonTopLeft.getLongitude());

            if (!isPointOnScreen(polygonTopLeftScreen.getX(), polygonTopLeftScreen.getY(), iconSize)) {
                continue;
            }

            polygon.updatePoints(this);
            polygon.draw(gc);
        }
    }

    /**
     * Wrapper for getMapPoint to use for PollutionPolygon.
     * @param latitude Latitude of position.
     * @param longitude Longitude of the position.
     * @return The screen position of this longitude/latitude point.
     */
    public Point2D getScreenPoint(double latitude, double longitude) {
        return getMapPoint(latitude, longitude);
    }
}
