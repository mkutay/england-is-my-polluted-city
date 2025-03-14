package pollutionLayer;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import dataProcessing.Pollutant;
import javafx.stage.Window;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import infoPopup.MapClickHandler;

/**
 * Handles the mouse click events on the pollution layer. Currently handles 
 * right clicks to open the info popup.
 * 
 * Refactor by Anas Ahmed, base code by Mehmet Kutay Bozkurt
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 */
public class PollutionLayerEventHandler {
    private final MapClickHandler clickHandler; // Callback interface for click events.
    private final MapView mapView;

    /**
     * Constructor.
     * @param clickHandler The click handler to notify when a polygon is clicked.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayerEventHandler(MapClickHandler clickHandler, MapView mapView) {
        this.clickHandler = clickHandler;
        this.mapView = mapView;
    }

    /**
     * Finds a polygon at the given screen coordinates.
     * @param x The x coordinate in screen space.
     * @param y The y coordinate in screen space.
     * @return A pollution polygon if found, null otherwise.
     * @author Mehmet Kutay Bozkurt
     */
    public PollutionPolygon getPolygonAtScreenCoordinates(double x, double y, PollutionPolygonManager polygonManager) {
        for (PollutionPolygon polygon : polygonManager.getPolygons()) {
            if (polygon.containsScreenPoint(x, y)) {
                return polygon;
            }
        }
        return null;
    }

    /**
     * Handles mouse click events on the canvas.
     * @param polygonManager The polygon manager class.
     * @param event The mouse event.
     * @author Mehmet Kutay Bozkurt
     */
    public void handleMouseClick(PollutionPolygonManager polygonManager, Pollutant pollutant, MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return; // Only handle right clicks.

        double x = event.getX();
        double y = event.getY();

        /*
         * Get the exact coordinates of where the mouse is being clicked.
         * (https://stackoverflow.com/questions/38612350/getting-position-in-scene-of-a-node-with-localtoscenex-y-returns-wrong-value)
         */
        double mouseX = event.getPickResult().getIntersectedNode().localToScreen(event.getX(), event.getY()).getX();
        double mouseY = event.getPickResult().getIntersectedNode().localToScreen(event.getX(), event.getY()).getY();

        // Convert screen coordinates to map coordinates:
        MapPoint mapPoint = mapView.getMapPosition(x, y);

        // Find the clicked polygon if any:
        PollutionPolygon clickedPolygon = getPolygonAtScreenCoordinates(x, y, polygonManager);
        Double pollutionValue = clickedPolygon == null ? null : clickedPolygon.getValue();

        Window window = mapView.getScene().getWindow();
        double width = window.getWidth();
        double height = window.getHeight();

        // Notify the listener:
        clickHandler.onMapClicked(mapPoint.getLatitude(), mapPoint.getLongitude(), mouseX, mouseY, width, height, pollutionValue, pollutant);
    }
}
