import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

/**
 * TODO: remove or refactor
 * Class for pollution Rendering on the map.
 * 
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionLayer extends MapLayer {
    private final MapView mapView;
    private final ObservableList<Pair<MapPoint, Circle>> points = FXCollections.observableArrayList();

    /**
     * Constructor for PollutionLayer.
     * @param mapView The map view to render the pollution layer on.
     */
    public PollutionLayer(MapView mapView) {
        this.mapView = mapView;
        generatePollutionDataPoints();
    }

    private void generatePollutionDataPoints() {
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile("UKAirPollutionData/NO2/mapno22023.csv");

        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : dataSet.getData()) {
            double value = dataPoint.value();
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        for (DataPoint dataPoint : dataSet.getData()){
            int easting = dataPoint.x();
            int northing = dataPoint.y();
            MapPoint mapPoint = GeographicUtilities.convertEastingNorthingToLatLon(easting, northing);

            double v = 1 - (dataPoint.value() - minValue)/(maxValue - minValue);
            int c = (int) (v * 255);

            Color color = Color.rgb(c, c, c);
            Circle circle = new Circle(3, color);
            circle.setOpacity(0.5);
            points.add(new Pair<>(mapPoint, circle));
            this.getChildren().add(circle);
        }
        this.markDirty();
    }

    private double getPixelScale(){
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(mapView.getWidth(), mapView.getHeight());
        return  GeographicUtilities.geodesicDistance(A, B)/mapView.getWidth();
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen
     * @param x the x coordinate in pixels
     * @param y the y coordinate in pixels
     * @return true if the point is on the screen, false otherwise
     */
    private boolean isPointOnScreen(double x, double y) {
        return x >= 0 && x <= mapView.getWidth() && y >= 0 && y <= mapView.getHeight();
    }

    /**
     * Method to update the layout for pollution.
     * TODO make less slow when many points are visible
     */
    @Override
    protected void layoutLayer() {
        for (Pair<MapPoint, Circle> candidate : points) {
            MapPoint point = candidate.getKey();
            Circle circle = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            if (isPointOnScreen(mapPoint.getX(), mapPoint.getY())) {
                circle.setRadius(800/getPixelScale());
                circle.setVisible(true);
                circle.setTranslateX(mapPoint.getX());
                circle.setTranslateY(mapPoint.getY());
            } else {
                circle.setVisible(false);
            }
        }

    }
}
