import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import dataProcessing.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * TODO: MAJOR REFACTOR REQUIRED
 * Class for pollution rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final ObservableList<PollutionPolygon> polygons;

    private final LODManager lodManager;
    private int currentLODIndex = -1;

    /**
     * Constructor for PollutionLayer. Generates pollution data polygons from the files.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        this.polygons = FXCollections.observableArrayList();
        lodManager = new LODManager(DataPicker.getPollutantData(2023, "NO2"), 15);
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
     * @param LODdata
     */
    private void generatePolygons(LODData LODdata) {
        this.getChildren().clear(); //Reset polygons
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
            PollutionPolygon polygon = new PollutionPolygon(dataPoint.x(), dataPoint.y(), color, sideLength);

            polygon.setOpacity(0.8);

            polygons.add(polygon);
            this.getChildren().add(polygon);
        }
    }

    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level.
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
     * Method to update the layout for pollution.
     * TODO idea for simple spacial hashing:
     * - find topleft corner of screen, and width of screen in easting/northing
     * - iterate over each "tile" in visible range and search the LODData if there exists a tile there,
     *   instead of searching all data
     * - May require LOD data including polygon data -> but very big performance upgrade
     */
    @Override
    protected void layoutLayer() {
        updatePollutionDataPoints();
        double iconSize = 1000 * lodManager.getLODData(currentLODIndex).getLevelOfDetail() * getPixelScale();
        for (PollutionPolygon polygon : polygons) { // Iterate over all polygons - TODO spacial hashing (?)
            MapPoint point = polygon.getWorldCoordinates().getFirst(); // Get top left coordinate of the polygon
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude()); // Convert to screen coordinates
            
            if (!isPointOnScreen(mapPoint.getX(), mapPoint.getY(), -iconSize)) { //Cull polygons out of visible range
                polygon.setVisible(false);
                continue;
            }
            
            int pointIndex = 0;
            for (MapPoint worldCoordinate : polygon.getWorldCoordinates()) {
                Point2D screenPoint = getMapPoint(worldCoordinate.getLatitude(), worldCoordinate.getLongitude());
                polygon.getPoints().set(pointIndex, screenPoint.getX());
                polygon.getPoints().set(pointIndex + 1, screenPoint.getY());
                pointIndex += 2;
            }
            polygon.setVisible(true);
        }

    }
}
