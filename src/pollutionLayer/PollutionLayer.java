package pollutionLayer;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import colors.*;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import infoPopup.MapClickHandler;
import utility.CustomMapView;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class for pollution rendering on the map. Handles rendering pollution data as polygons
 * on the map layer.
 *
 * @author Anas Ahmed
 * @version 2.0
 */
public class PollutionLayer extends MapLayer {
    private final CustomMapView mapView;
    private final PollutionPolygonManager pollutionPolygonManager;
    private final PollutionLayerEventHandler pollutionLayerEventHandler;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private double polygonOpacity = 0.7;
    private final ColorSchemeManager colorSchemeManager;

    private double pollutionThresholdPercentage = 0; //Stores maximum percentage value relative to max pollution value that polygons can be displayed
    private final double maxPollutionValue; //Stores the maximum pollution value of all polygons currently displayed


    /**
     * Initialises the PollutionLayer.
     *
     * @param mapView   The map view to render the pollution layer on.
     * @param dataSet   The currently used dataset.
     * @param pollutant The currently used pollutant
     */
    public PollutionLayer(CustomMapView mapView, DataSet dataSet, MapClickHandler clickHandler, Pollutant pollutant, ColorSchemeManager colorSchemeManager) {
        this.mapView = mapView;

        pollutionPolygonManager = new PollutionPolygonManager(dataSet);
        pollutionLayerEventHandler = new PollutionLayerEventHandler(clickHandler, mapView);

        this.colorSchemeManager = colorSchemeManager;

        maxPollutionValue = dataSet.getData().stream().map(DataPoint::value).max(Double::compareTo).orElse(0.0);

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // Add click event handler to the canvas. TODO refactor
        canvas.setOnMouseClicked(e -> pollutionLayerEventHandler.handleMouseClick(pollutionPolygonManager, pollutant, e));

        pollutionPolygonManager.updatePollutionPolygons(mapView);
    }

    /**
     * Re-draws all polygons every time the mapView is moved around.
     */
    @Override
    protected void layoutLayer() {
        pollutionPolygonManager.updatePollutionPolygons(mapView);
        renderPolygons();
    }

    /**
     * Draw all polygons to the canvas.
     */
    private void renderPolygons() {
        double iconSize = 1000 * pollutionPolygonManager.getCurrentLevelOfDetail() * mapView.getPixelScale();

        canvas.setWidth(mapView.getWidth());
        canvas.setHeight(mapView.getHeight());

        gc.clearRect(0, 0, mapView.getWidth(), mapView.getHeight()); // Clear canvas.

        for (PollutionPolygon polygon : pollutionPolygonManager.getPolygons()) {
            MapPoint polygonTopLeft = polygon.getWorldCoordinates().getFirst();
            Point2D polygonTopLeftScreen = getMapPoint(polygonTopLeft.getLatitude(), polygonTopLeft.getLongitude());

            if (polygonTopLeftScreen == null){
                continue; // Edge case when switching to statistics panel
            }

            if (!mapView.isPointOnScreen(polygonTopLeftScreen.getX(), polygonTopLeftScreen.getY(), iconSize)) {
                continue; //Skip if not on screen
            }

            polygon.updatePoints(this);

            if (polygon.getDataPoint().value() <= pollutionThresholdPercentage * maxPollutionValue){
                polygon.draw(gc, colorSchemeManager.getColorScheme().getNullColour(), polygonOpacity);
            } else {
                polygon.drawFromColourScheme(gc, colorSchemeManager.getColorScheme(), polygonOpacity);
            }

        }
    }

    /**
     * Wrapper for getMapPoint to use in pollutionLayer.PollutionPolygon.updatePoints().
     * @param latitude Latitude of position.
     * @param longitude Longitude of the position.
     * @return The screen position of this longitude/latitude point.
     */
    public Point2D getScreenPoint(double latitude, double longitude) {
        return getMapPoint(latitude, longitude);
    }

    public void setVisiblePolygonThreshold(double thresholdPercentage) {
        pollutionThresholdPercentage = thresholdPercentage;
        renderPolygons(); //Re-draw
    }
}
