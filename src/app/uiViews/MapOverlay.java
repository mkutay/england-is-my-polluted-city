package app.uiViews;

import javafx.scene.layout.AnchorPane;
import utility.CustomMapView;

/**
 * Manages the map overlay, including the map view, legend, and zoom controls.
 * Ensures correct positioning of UI elements.
 * @author Chelsea Feliciano
 */
public class MapOverlay {
    private final AnchorPane overlayPane;
    private final LegendPane legend;
    private final ZoomControls zoomControls;
    private final CustomMapView mapView;

    public MapOverlay(CustomMapView mapView) {
        this.overlayPane = new AnchorPane();
        this.overlayPane.getStyleClass().add("map-overlay");
        this.mapView = mapView;
        this.legend = new LegendPane();
        this.zoomControls = new ZoomControls(mapView);

        // Ensure mapView fills the overlay
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);

        // Positioning inside MapOverlay
        AnchorPane.setBottomAnchor(legend, 20.0);
        AnchorPane.setRightAnchor(legend, 20.0);

        AnchorPane.setTopAnchor(zoomControls, 20.0);
        AnchorPane.setRightAnchor(zoomControls, 20.0);

        overlayPane.getChildren().addAll(mapView, legend, zoomControls);
    }

    public AnchorPane getOverlayPane() {return overlayPane;}
    public LegendPane getLegend() {return legend;}
    public CustomMapView getMapView() {return mapView;}
}
