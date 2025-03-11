package pollutionLayer;

//TODO

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import infoPopup.MapClickHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * @author Anas Ahmed and Mehmet Kutay Bozkurt
 * Refactor by Anas Ahmed, base code by
 */
public class PollutionLayerEventHandler {
    private final MapClickHandler clickHandler;  // Callback interface for click events
    private final MapView mapView;

    public PollutionLayerEventHandler(MapClickHandler clickHandler, MapView mapView) {
        this.clickHandler = clickHandler;
        this.mapView = mapView;
    }

    /**
     * Method by Mehmet Kutay Bozkurt
     * Finds a polygon at the given screen coordinates.
     * @param x The x coordinate in screen space.
     * @param y The y coordinate in screen space.
     * @return A pollution polygon if found, null otherwise.
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
     * Method by Mehmet Kutay Bozkurt
     * @param polygonManager The polygon manager class.
     * @param event The mouse event.
     */
    public void handleMouseClick(PollutionPolygonManager polygonManager, MouseEvent event) {
        if (event.getButton() != MouseButton.SECONDARY) return; // Only handle right clicks.

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
        PollutionPolygon clickedPolygon = getPolygonAtScreenCoordinates(x, y, polygonManager);
        Double pollutionValue = clickedPolygon == null ? null : clickedPolygon.getValue();

        // Notify the listener:
        clickHandler.onMapClicked(mapPoint.getLatitude(), mapPoint.getLongitude(), mouseX, mouseY, pollutionValue);
    }
}
