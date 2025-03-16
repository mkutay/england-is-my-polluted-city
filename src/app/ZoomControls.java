package app;

import com.gluonhq.maps.MapPoint;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utility.CustomMapView;
import utility.ImageUtils;

/**
 * The ZoomControls class provides a simple UI for zooming in and out of the map.
 * It consists of label buttons for Zoom In, Zoom Out, and Fullscreen mode.
 *
 * @author Chelsea Feliciano
 */
public class ZoomControls extends VBox {
    private final CustomMapView mapView;
    private Label zoomInButton;
    private Label zoomOutButton;
    private Label fullScreenButton;

    /**
     * Constructs a ZoomControls instance and attaches event handlers.
     */
    public ZoomControls(CustomMapView mapView) {
        this.mapView = mapView;

        createZoomButtons();
        this.getChildren().addAll(zoomInButton, zoomOutButton, fullScreenButton);

        zoomInButton.setOnMouseClicked(e -> zoomMap(1));
        zoomOutButton.setOnMouseClicked(e -> zoomMap(-1));
        fullScreenButton.setOnMouseClicked(e -> toggleFullscreen());

        this.setSpacing(10);
    }

    private void createZoomButtons() {
        // Create labels
        zoomInButton = new Label();
        zoomOutButton = new Label();
        fullScreenButton = new Label();

        // Apply common CSS class to all buttons
        zoomInButton.getStyleClass().add("zoom-button");
        zoomOutButton.getStyleClass().add("zoom-button");
        fullScreenButton.getStyleClass().add("zoom-button");

        // Create ImageViews for icons
        ImageView zoomIn = ImageUtils.createImage("/resources/icons/add.png", 20);
        ImageView zoomOut = ImageUtils.createImage("/resources/icons/minus.png", 20);
        ImageView fullScreen = ImageUtils.createImage("/resources/icons/fullscreen.png", 20);

        // Assign icons to labels
        zoomInButton.setGraphic(zoomIn);
        zoomOutButton.setGraphic(zoomOut);
        fullScreenButton.setGraphic(fullScreen);
    }

    /**
     * Adjusts the zoom level of the map.
     *
     * @param zoomChange The amount to adjust the zoom by (+1 for in, -1 for out).
     */
    private void zoomMap(double zoomChange) {
        double newZoom = mapView.getZoom() + zoomChange;
        newZoom = Math.max(5, Math.min(newZoom, 20)); // Ensure zoom stays within limits

        MapPoint point = mapView.getMapPosition(mapView.getWidth()/2, mapView.getHeight()/2);
        mapView.setZoom(newZoom);
        mapView.setCenter(point);

        mapView.dirtyRefresh();
    }

    /**
     * Toggles fullscreen mode for the application.
     */
    private void toggleFullscreen() {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }
}
