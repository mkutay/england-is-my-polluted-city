package pollutionLayer;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import colors.ColorScheme;
import colors.DefaultColorScheme;
import dataProcessing.*;
import infoPopup.MapClickHandler;
import utility.CustomMapView;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

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

    private final Canvas canvas;
    private final GraphicsContext gc;
    
    // Color configuration:
    private final ColorScheme colorScheme;
    private double polygonOpacity = 0.7;

    // Callback interface for click events:
    private final MapClickHandler clickHandler;

    /**
     * TODO: Dynamic dataset updating -- important.
     * Initialises the PollutionLayer.
     * @param mapView The map view to render the pollution layer on.
     * @param dataSet The currently used dataset.
     */
    public PollutionLayer(CustomMapView mapView, DataSet dataSet, MapClickHandler clickHandler) {
        this.mapView = mapView;

        pollutionPolygonManager = new PollutionPolygonManager(dataSet);

        this.clickHandler = clickHandler; // TODO: Move away from this class into another.
        this.colorScheme = new DefaultColorScheme();

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // Add click event handler to the canvas
        canvas.setOnMouseClicked(this::handleMouseClick); // TODO: Probably should not be here and or on the canvas.

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
     * Finds a polygon at the given screen coordinates.
     * @param x The x coordinate in screen space.
     * @param y The y coordinate in screen space.
     * @return A pollution polygon if found, null otherwise.
     */
    public PollutionPolygon getPolygonAtScreenCoordinates(double x, double y) {
        for (PollutionPolygon polygon : pollutionPolygonManager.getPolygons()) {
            if (polygon.containsScreenPoint(x, y)) {
                return polygon;
            }
        }
        return null;
    }

    /**
     * TODO: This should (probably) NOT be here - we need an eventHandler class to control these things.
     * Handles mouse click events on the canvas.
     * @param event The mouse event.
     */
    private void handleMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return; // Only handle right clicks.

        //absolute values on the map
        double x = event.getX();
        double y = event.getY();

        /*
         *Get the exact coordinates of where the mouse is being clicked
         * (https://stackoverflow.com/questions/38612350/getting-position-in-scene-of-a-node-with-localtoscenex-y-returns-wrong-value)
         * TODO: add bounds for the right side of the window to prevent drawing outside of the application
         */
        double mouseX = event.getPickResult().getIntersectedNode().localToScreen(event.getX(), event.getY()).getX();
        double mouseY = event.getPickResult().getIntersectedNode().localToScreen(event.getX(), event.getY()).getY();


        
        // Convert screen coordinates to map coordinates:
        MapPoint mapPoint = mapView.getMapPosition(x, y);


        // Find the clicked polygon if any:
        PollutionPolygon clickedPolygon = getPolygonAtScreenCoordinates(x, y);
        Double pollutionValue = clickedPolygon == null ? null : clickedPolygon.getValue();
        
        // Notify the listener:
        clickHandler.onMapClicked(mapPoint.getLatitude(), mapPoint.getLongitude(), mouseX, mouseY, pollutionValue);
    }

    /**
     * Draw all polygons to the canvas
     */
    private void renderPolygons() {
        double iconSize = 1000 * pollutionPolygonManager.getCurrentLevelOfDetail() * mapView.getPixelScale();

        canvas.setWidth(mapView.getWidth());
        canvas.setHeight(mapView.getHeight());

        gc.clearRect(0, 0, mapView.getWidth(), mapView.getHeight()); // Clear canvas.

        for (PollutionPolygon polygon : pollutionPolygonManager.getPolygons()) {
            MapPoint polygonTopLeft = polygon.getWorldCoordinates().getFirst();
            Point2D polygonTopLeftScreen = getMapPoint(polygonTopLeft.getLatitude(), polygonTopLeft.getLongitude());

            if (!mapView.isPointOnScreen(polygonTopLeftScreen.getX(), polygonTopLeftScreen.getY(), iconSize)) {
                continue;
            }

            polygon.updatePoints(this);
            polygon.draw(gc, colorScheme, polygonOpacity);
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
}
