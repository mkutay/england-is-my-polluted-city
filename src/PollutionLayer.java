import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * TODO: remove or refactor
 * Class for pollution rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final ObservableList<Pair<MapPoint, PollutionPolygon>> points;

    /**
     * Constructor for PollutionLayer. Generates pollution data points from the files.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        this.points = FXCollections.observableArrayList();

        generatePollutionDataPoints();
    }

    /**
     * Generates pollution data points from the CSV files.
     */
    private void generatePollutionDataPoints() {
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");

        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : dataSet.getData()) {
            double value = dataPoint.value();
            if (value == -1) continue; // If the value is missing, skip it.
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }


        for (DataPoint dataPoint : dataSet.getData()) {
            if (dataPoint.value() == -1) continue; // Skip missing values.
            int easting = dataPoint.x();
            int northing = dataPoint.y();
            MapPoint mapPoint = GeographicUtilities.convertEastingNorthingToLatLon(easting, northing);
            double v = (dataPoint.value() - minValue) / (maxValue - minValue);
            int c = (int) ((1 - v) * 255);

            Color color = Color.rgb(c, c, c);
            PollutionPolygon polygon = new PollutionPolygon(easting, northing, color, 1000);
            polygon.setFill(color);
            polygon.setOpacity(0.5);

            points.add(new Pair<>(mapPoint, polygon));
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
     * TODO: Make less slow when many points are visible
     */
    @Override
    protected void layoutLayer() {
        double iconSize = 1000 * getPixelScale();
        for (Pair<MapPoint, PollutionPolygon> candidate : points) {
            MapPoint point = candidate.getKey();
            PollutionPolygon icon = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            if (!isPointOnScreen(mapPoint.getX(), mapPoint.getY(), -iconSize)) {
                icon.setVisible(false);
                continue;
            }
            int i = 0;
            for (MapPoint worldCoordinate : icon.getWorldCoordinates()) {
                Point2D screenPoint = getMapPoint(worldCoordinate.getLatitude(), worldCoordinate.getLongitude());
                icon.getPoints().set(i, screenPoint.getX());
                i++;
                icon.getPoints().set(i, screenPoint.getY());
                i++;
            }
            icon.setVisible(true);
        }

    }
}
