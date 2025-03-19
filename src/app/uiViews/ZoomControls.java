package app.uiViews;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
 * @version 1.0
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

        zoomInButton.setOnMouseClicked(e -> mapView.zoomIn());
        zoomOutButton.setOnMouseClicked(e -> mapView.zoomOut());
        fullScreenButton.setOnMouseClicked(e -> toggleFullscreen());

        this.setSpacing(10);
    }

    /**
     * Creates the zoom buttons and assigns icons and tooltips.
     */
    private void createZoomButtons() {
        // Create labels
        zoomInButton = new Label();
        zoomOutButton = new Label();
        fullScreenButton = new Label();

        // Apply common CSS class to all buttons
        zoomInButton.getStyleClass().add("zoom-button");
        zoomOutButton.getStyleClass().add("zoom-button");
        fullScreenButton.getStyleClass().add("zoom-button");

        // Set tooltips (hover text)
        Tooltip.install(zoomInButton, new Tooltip("Zoom In"));
        Tooltip.install(zoomOutButton, new Tooltip("Zoom Out"));
        Tooltip.install(fullScreenButton, new Tooltip("Fullscreen Mode"));

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
     * Toggles fullscreen mode for the application.
     */
    private void toggleFullscreen() {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }
}
