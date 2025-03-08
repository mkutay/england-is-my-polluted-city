import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import dataProcessing.DataPicker;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.LODSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * TODO: remove or refactor
 * Class for pollution rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final ObservableList<PollutionPolygon> polygons;

    /**
     * Constructor for PollutionLayer. Generates pollution data polygons from the files.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        this.polygons = FXCollections.observableArrayList();

        generatePollutionDataPoints();
    }

    /**
     * Generates pollution data polygons from the CSV files.
     */
    private void generatePollutionDataPoints() {
        //dataProcessing.DataLoader loader = new dataProcessing.DataLoader();
        //dataProcessing.DataSet dataSet = loader.loadDataFile("dataProcessing.UKAirPollutionData/NO2/mapno22023.csv");
        DataSet dataSet = DataPicker.getPollutantData(2023, "NO2");
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : dataSet.getData()) {
            double value = dataPoint.value();
            if (value == -1) continue; // If the value is missing, skip it.
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        System.out.println("Generating LOD data");
        LODSet set = new LODSet(16, dataSet);
        System.out.println("Finished generating LOD data. LOD size = " + set.getData().size() + " vs Original size " + dataSet.getData().size());

        for (DataPoint dataPoint : set.getData()) {
            if (dataPoint.value() == -1) continue; // Skip missing values.

            double v = (dataPoint.value() - minValue) / (maxValue - minValue);
            int c = (int) ((v) * 255);

            Color color = Color.rgb(c, c, c);
            PollutionPolygon polygon = new PollutionPolygon(dataPoint.x(), dataPoint.y(), color, 1000 * set.getLevelOfDetail());

            polygon.setFill(color);
            polygon.setOpacity(0.8);

            polygons.add(polygon);
            this.getChildren().add(polygon);
        }


        this.markDirty();
    }

    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level.
     * @return Scale factor for pixel scale.
     */
    private double getPixelScale() {
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(mapView.getWidth(), 0);
        return  mapView.getWidth() / GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen.
     * @param x The x coordinate in pixels.
     * @param y The y coordinate in pixels.
     * @param padding The padding inside the screen.
     * @return True if the point is on the screen, false otherwise.
     */
    private boolean isPointOnScreen(double x, double y, double padding) {
        return x >= padding && x <= mapView.getWidth() - padding && y >= padding && y <= mapView.getHeight() - padding;
    }

    /**
     * Method to update the layout for pollution.
     * TODO: Make less slow when many polygons are visible
     */
    @Override
    protected void layoutLayer() {
        double iconSize = 1000 * getPixelScale(); //TODO set to lodLevel * 1000 * pixelScale for correct sizing
        for (PollutionPolygon polygon : polygons) { // Iterate over all polygons - TODO spacial hashing (?)
            MapPoint point = polygon.getWorldCoordinates().getFirst(); // Get top left coordinate of the polygon
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude()); // Convert to screen coordinates
            
            if (!isPointOnScreen(mapPoint.getX(), mapPoint.getY(), -iconSize)) { //Cull polygons out of visible range
                polygon.setVisible(false);
                continue;
            }
            
            int pointIndex = 0;
            for (MapPoint worldCoordinate : polygon.getWorldCoordinates()) {
                Point2D screenPoint = getMapPoint(worldCoordinate.getLatitude(), worldCoordinate.getLongitude());
                polygon.getPoints().set(pointIndex, screenPoint.getX());
                polygon.getPoints().set(pointIndex + 1, screenPoint.getY());
                pointIndex += 2;
            }
            polygon.setVisible(true);
        }

    }
}
