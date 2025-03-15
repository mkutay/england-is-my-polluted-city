package app;

import colors.ColorScheme;
import colors.ColorSchemeManager;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import infoPopup.MapClickHandler;
import pollutionLayer.PollutionLayer;
import utility.CustomMapView;

/**
 * Handles all the map UI elements, like the map layer, pollution rendering, point selection and popups.
 * Receives requests to update the map data.
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 */
public class MapController {
    private final StackPane mapOverlay;
    private final CustomMapView mapView;

    private final MapClickHandler clickHandler;
    private final ColorSchemeManager colorSchemeManager;
    private final LegendPane legend;

    private int currentYear;
    private Pollutant currentPollutant;
    private ColorScheme currentColourScheme;

    private PollutionLayer pollutionLayer;
    private PollutionPolygonSelector pollutionPolygonSelector;
    private boolean pollutionLayerInitialised = false;

    /**
     * Constructor for MapController.
     * @param stage The current stage.
     */
    public MapController(Stage stage, ColorSchemeManager colorSchemeManager) {
        this.colorSchemeManager = colorSchemeManager;

        mapView = new CustomMapView();
        clickHandler = new MapClickHandler(stage);

        //Map overlay contains all map elements and is what is added to the root
        mapOverlay = new StackPane();
        mapOverlay.getStyleClass().add("map-overlay");

        legend = new LegendPane();
        StackPane.setAlignment(legend, Pos.BOTTOM_RIGHT);// Position legend near the top right of map
        mapOverlay.getChildren().addAll(mapView, legend); // Add elements to map overlay

        //pollutionPolygonSelector = new PollutionPolygonSelector(mapView, mapOverlay);

        setupMapView();
    }

    /**
     * Initialises the pollution layer on the map with some data.
     * @param year The year to initialise with.
     * @param pollutant The pollutant to initialise with.
     */
    public void initialisePollutionLayer(int year, Pollutant pollutant) {
        updateMapDataSet(year, pollutant, colorSchemeManager.getColorScheme());
        pollutionLayerInitialised = true;
    }

    /**
     * Sets the initial position of the MapView onto London, and initialises the default zoom.
     */
    private void setupMapView() {
        MapPoint startLoc = new MapPoint(51.508045, -0.128217); // London coordinates.
        mapView.setCenter(startLoc); // Instantly centers the map
        mapView.setZoom(10);
    }

    /**
     * @return The map view.
     * @throws PollutionLayerNotInitialisedException If pollution layer is not initialised.
     */
    public StackPane getMapOverlay() throws PollutionLayerNotInitialisedException {
        if (pollutionLayerInitialised) return mapOverlay;

        throw new PollutionLayerNotInitialisedException("Map is not initialised. Call initialisePollutionLayer first.");
    }

    /**
     * Updates the map data set with the new year and pollutant.
     * Updates the colour scheme data accordingly
     * @param year The year to update to.
     * @param pollutant The pollutant to update to.
     */
    public void updateMapDataSet(int year, Pollutant pollutant, ColorScheme colorScheme) {
        System.out.println("Updating map data set to year: " + year + ", pollutant: " + pollutant + ", color scheme: " + colorScheme);

        if (year == currentYear && pollutant == currentPollutant && colorScheme.toString().equals(currentColourScheme.toString())) {
            return; // No need to update if the data is the same.
        }

        currentYear = year;
        currentPollutant = pollutant;
        currentColourScheme = colorScheme;

        if (pollutionLayer != null) mapView.removeLayer(pollutionLayer);
        colorSchemeManager.updateColorScheme(colorScheme);

        DataManager dataManager = DataManager.getInstance();
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);

        legend.updateLegend(colorSchemeManager, dataSet.getMaxPollutionValue());

        pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler, pollutant, colorSchemeManager);
        mapView.addLayer(pollutionLayer); // Add back the new pollution layer.
        mapView.dirtyRefresh();
    }
}