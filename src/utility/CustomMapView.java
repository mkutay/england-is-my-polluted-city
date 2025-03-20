package utility;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.scene.input.ScrollEvent;

/**
 * The CustomMapView class extends MapView to restrict zooming and display pollution data.
 * Allows adding pollution points as markers on the map.
 * 
 * @author Anas Ahmed
 * @version 1.0
 */
public class CustomMapView extends MapView {
    private final double minZoom = 3; // Min zoom allowed
    private final double maxZoom = 20; // Max zoom allowed
    private final double zoomStep = 0.1; // Smaller step for smooth zooming

    /**
     * Constructor.
     */
    public CustomMapView() {
        setupZoomControl();
    }

    /**
     * Restricts zooming within the specified range and handles mouse scroll events.
     */
    private void setupZoomControl() {
        this.setOnScroll(this::handleScrollZoom);
    }

    /**
     * Zoom in one level.
     */
    public void zoomIn() {
        applyZoom(1);
    }

    /**
     * Zoom out one level.
     */
    public void zoomOut() {
        applyZoom(-1);
    }

    /**
     * Applies zoom changes while keeping the center fixed.
     * @param zoomChange The amount to change zoom by.
     */
    private void applyZoom(double zoomChange) {
        double newZoom = Math.max(minZoom, Math.min(this.getZoom() + zoomChange, maxZoom));

        if (newZoom != this.getZoom()) {    // Prevent unnecessary updates if zoom hasn't changed
            MapPoint point = getMapPosition(getWidth() / 2, getHeight() / 2);
            this.setCenter(point);  // Keep the center fixed while zooming
            this.setZoom(newZoom);
            this.dirtyRefresh();
        }
    }

    /**
     * Handles zooming behaviour when scrolling.
     * @param event The scroll event.
     */
    private void handleScrollZoom(ScrollEvent event) {
        double zoomChange = event.getDeltaY() > 0 ? zoomStep : -zoomStep; // Zoom in on scroll up, out on scroll down
        applyZoom(zoomChange);
        event.consume(); // Prevent default zoom behavior
    }

    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level.
     * i.e. Pixel size * sf = real world size.
     * @return Scale factor for pixel scale.
     */
    public double getPixelScale() {
        MapPoint A = this.getMapPosition(0, 0);
        MapPoint B = this.getMapPosition(this.getWidth(), 0);
        return this.getWidth() / GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen.
     * @param x The x coordinate in pixels.
     * @param y The y coordinate in pixels.
     * @param padding The padding inside the screen.
     * @return True if the point is on the screen, false otherwise.
     */
    public boolean isPointOnScreen(double x, double y, double padding) {
        return x >= -padding && x <= getWidth() + padding &&
            y >= -padding && y <= getHeight() + padding;
    }

    /**
     * Forces a map refresh when called.
     */
    public void dirtyRefresh() {
        this.markDirty();
    }
}
