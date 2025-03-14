package app;

import com.gluonhq.maps.MapPoint;

import javafx.stage.Stage;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import infoPopup.MapClickHandler;
import pollutionLayer.PollutionLayer;
import utility.CustomMapView;

/**
 * Handles all the map UI elements, like the map layer, pollution rendering, and popups.
 * Receives requests to update the map data.
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 */
public class MapController {
    private final CustomMapView mapView;
    private final MapClickHandler clickHandler;

    private PollutionLayer pollutionLayer;
    private boolean pollutionLayerInitialised = false;

    /**
     * Constructor for MapController.
     * @param stage The current stage.
     */
    public MapController(Stage stage) {
        mapView = new CustomMapView();
        clickHandler = new MapClickHandler(stage);

        setupMapView();
    }

    /**
     * Initialises the pollution layer on the map with some data.
     * @param year The year to initialise with.
     * @param pollutant The pollutant to initialise with.
     */
    public void initialisePollutionLayer(int year, Pollutant pollutant) {
        updateMapDataSet(year, pollutant);
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
    public CustomMapView getMapView() throws PollutionLayerNotInitialisedException {
        if (pollutionLayerInitialised) return mapView;

        throw new PollutionLayerNotInitialisedException("Map is not initialised. Call initialisePollutionLayer first.");
    }

    /**
     * Updates the map data set with the new year and pollutant.
     * @param year The year to update to.
     * @param pollutant The pollutant to update to.
     */
    public void updateMapDataSet(int year, Pollutant pollutant) {
        if (pollutionLayer != null) mapView.removeLayer(pollutionLayer);

        DataManager dataManager = DataManager.getInstance();
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);

        pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
        mapView.addLayer(pollutionLayer); // Add back the new pollution layer.
        mapView.dirtyRefresh();
    }
}