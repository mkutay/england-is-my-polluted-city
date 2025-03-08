import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import dataProcessing.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: MAJOR REFACTOR REQUIRED
 * Class for pollution rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final List<PollutionPolygon> polygons;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final LODManager lodManager;
    private int currentLODIndex = -1;

    /**
     * Constructor for PollutionLayer. Generates pollution data polygons from the files.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        polygons = new ArrayList<>();

        lodManager = new LODManager(DataPicker.getPollutantData(2023, "NO2"), 4);

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
        int LODIndex = lodManager.getLODIndex(getPixelScale(), mapView.getWidth(), mapView.getHeight());
        if (LODIndex == currentLODIndex) {return;} //No LOD update needed, exit
        currentLODIndex = LODIndex;

        generatePolygons(lodManager.getLODData(currentLODIndex));
        this.markDirty();
    }

    /**
     * Regenerate polygons
     * @param LODdata the LOD data to use to generate the polygons
     */
    private void generatePolygons(LODData LODdata) {
        //this.getChildren().clear(); //Reset polygons
        polygons.clear();
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : LODdata.getData()) {
            double value = dataPoint.value();
            if (value == -1) continue; // If the value is missing, skip it.
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        for (DataPoint dataPoint : LODdata.getData()) {
            if (dataPoint.value() == -1) continue; // Skip missing values.

            double v = (dataPoint.value() - minValue) / (maxValue - minValue); //TODO sophisticated colour scheme
            int c = (int) ((v) * 255);

            Color color = Color.rgb(c, c, c);
            int sideLength = 1000 * LODdata.getLevelOfDetail();

            //The easting and northing values given are the centroids of the grid, meaning we need to offset them
            int topLeftEasting = dataPoint.x() - 500; //Offset by 500m in both directions
            int topLeftNorthing = dataPoint.y() - 500;

            PollutionPolygon polygon = new PollutionPolygon(topLeftEasting, topLeftNorthing, color, sideLength);

            polygon.setOpacity(0.8);
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
        return  mapView.getWidth() / GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen.
     * @param x The x coordinate in pixels.
     * @param y The y coordinate in pixels.
     * @param padding The padding inside the screen.
     * @return True if the point is on the screen, false otherwise.
     */
    private boolean isPointOnScreen(double x, double y, double padding) {
        return x >= padding && x <= mapView.getWidth() - padding && y >= padding && y <= mapView.getHeight() - padding;
    }

    /**
     * Re-draws all polygons every time the mapView is moved around
     */
    @Override
    protected void layoutLayer() {
        updatePollutionDataPoints();
        double iconSize = 1000 * lodManager.getLODData(currentLODIndex).getLevelOfDetail() * getPixelScale();

        canvas.setWidth(mapView.getWidth());
        canvas.setHeight(mapView.getHeight());

        gc.clearRect(0, 0, mapView.getWidth(), mapView.getHeight()); //Clear canvas
        for (PollutionPolygon polygon : polygons) { // Iterate over all polygons
            MapPoint polygonTopLeft = polygon.getWorldCoordinates().getFirst(); // Get top left coordinate of the polygon
            Point2D polygonTopLeftScreen = getMapPoint(polygonTopLeft.getLatitude(), polygonTopLeft.getLongitude()); // Convert to screen coordinates

            if (!isPointOnScreen(polygonTopLeftScreen.getX(), polygonTopLeftScreen.getY(), -iconSize)) { //Cull polygons out of visible range
                continue;
            }

            polygon.updatePoints(this);
            polygon.draw(gc);
        }
    }

    /**
     * Wrapper for getMapPoint to use for PollutionPolygon
     * @param latitude latitude of position
     * @param longitude longitude
     * @return the screen position of this longitude/latitude point
     */
    public Point2D getScreenPoint(double latitude, double longitude) {
        return getMapPoint(latitude, longitude);
    }
}
