package app;

import com.gluonhq.maps.MapPoint;
import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import infoPopup.InfoPopup;
import infoPopup.MapClickHandler;
import javafx.stage.Stage;
import pollutionLayer.PollutionLayer;
import utility.CustomMapView;

import java.util.concurrent.CompletableFuture;

/**
 * Handles all the map UI elements, like the map layer, pollution rendering, popups
 * Receives requests to update the map data
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 */
public class MapController {
    private final CustomMapView mapView;
    private final MapClickHandler clickHandler;
    private PollutionLayer pollutionLayer;

    private boolean pollutionLayerInitialized = false;

    public MapController(Stage stage) {
        mapView = new CustomMapView();
        clickHandler = new MapClickHandler(stage);

        setupMapView();
    }


    public void initialisePollutionLayer(int year, Pollutant pollutant, DataManager dataManager) {
        updateMapDataSet(year, pollutant, dataManager);
        pollutionLayerInitialized = true;
    }

    /**
     * Sets the initial position of the MapView onto London, and initialises the default zoom
     */
    private void setupMapView() {
        mapView.flyTo(0, new MapPoint(51.508045, -0.128217), 0.01);
        mapView.setZoom(10);
    }

    /**
     * Gets the map view
     * @throws PollutionLayerNotInitialisedException If pollution layer is not initialised
     */
    public CustomMapView getMapView() throws PollutionLayerNotInitialisedException {
        if (!pollutionLayerInitialized) {
            throw new PollutionLayerNotInitialisedException("Map is not initialised. Call initialisePollutionLayer first.");
        }
        return mapView;
    }


    public void updateMapDataSet(int year, Pollutant pollutant, DataManager dataManager) {
        if (pollutionLayer != null) {mapView.removeLayer(pollutionLayer);}

        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        pollutionLayer = new PollutionLayer(mapView, dataSet, clickHandler);
        mapView.addLayer(pollutionLayer); //add back the new pollution layer
        mapView.dirtyRefresh();
    }
}