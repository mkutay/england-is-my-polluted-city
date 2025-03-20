package app.uiViews;

import javafx.scene.layout.AnchorPane;
import utility.CustomMapView;

/**
 * A UI component that overlays a map view with additional controls and manages their positioning.
 *
 * This overlay contains:
 * - A legend for map data representation
 * - Zoom controls for adjusting the map view
 * - A toggle button for showing or hiding the side panel
 *
 * It ensures proper alignment and placement of these components within the overlay.
 *
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class MapOverlay extends AnchorPane {
    private final LegendPane legend;
    private final ZoomControls zoomControls;
    private final CustomMapView mapView;
    
    private HideSidePanelButton hideSidePanelButton;

    /**
     * Constructs the map overlay with a given map view.
     * @param mapView The map view to be displayed within the overlay.
     */
    public MapOverlay(CustomMapView mapView) {
        getStyleClass().add("map-overlay");
        this.mapView = mapView;
        legend = new LegendPane();
        zoomControls = new ZoomControls(mapView);

        // Ensure the map view covers the entire overlay
        setTopAnchor(mapView, 0.0);
        setBottomAnchor(mapView, 0.0);
        setLeftAnchor(mapView, 0.0);
        setRightAnchor(mapView, 0.0);

        // Position legend and zoom controls in the overlay
        setBottomAnchor(legend, 20.0);
        setRightAnchor(legend, 20.0);
        setTopAnchor(zoomControls, 20.0);
        setRightAnchor(zoomControls, 20.0);

        getChildren().addAll(mapView, legend, zoomControls);
    }

    /**
     * Adds a button to toggle the visibility of a side panel.
     * @param sidePanel The side panel controlled by the button.
     */
    public void setSidePanel(SidePanel sidePanel) {
        hideSidePanelButton = new HideSidePanelButton(sidePanel);
        getChildren().add(hideSidePanelButton);
        setBottomAnchor(hideSidePanelButton, 50.0);
        setLeftAnchor(hideSidePanelButton, -5.0);
    }

    // Getters:
    public LegendPane getLegend() { return legend; }
    public CustomMapView getMapView() { return mapView; }
}
