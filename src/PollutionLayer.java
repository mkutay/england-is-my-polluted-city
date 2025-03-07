import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Rectangle;
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
            if(dataPoint.value() !=1 ){
                int easting = dataPoint.x();
                int northing = dataPoint.y();
                MapPoint mapPoint = GeographicUtilities.convertEastingNorthingToLatLon(easting, northing);

                double v = 1 - (dataPoint.value() - minValue)/(maxValue - minValue);
                int c = (int) (v * 255);

                double pollutionValue = dataPoint.value();
                // POC If statement to set the color of the circle depending on the pollution value (switch does not accept double)
                Color circleColor = Color.GRAY; //initialise the circle color

                if (pollutionValue >= 0 && pollutionValue < 2.5){ //Safe
                    circleColor = Color.YELLOW;
                }
                if (pollutionValue >= 2.5 && pollutionValue < 5.5){
                    circleColor = Color.ORANGE; //Unhealthy
                }
                if (pollutionValue >= 5.5){
                    circleColor = Color.RED; //Hazardous
                }
                Color color = circleColor;
                Circle circle = new Circle(3, color);
                Rectangle rectangle = new Rectangle(1,1,color);
                circle.setOpacity(0.5);
                points.add(new Pair<>(mapPoint, circle));
                this.getChildren().add(circle);
            }
        }
        this.markDirty();
    }


    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level
     * @return Scale factor for pixel scale
     */
    private double getPixelScale(){
        MapPoint A = mapView.getMapPosition(0, 0);
        MapPoint B = mapView.getMapPosition(mapView.getWidth(), 0);
        return  mapView.getWidth()/GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen
     * @param x the x coordinate in pixels
     * @param y the y coordinate in pixels
     * @param padding the padding inside the screen
     * @return true if the point is on the screen, false otherwise
     */
    private boolean isPointOnScreen(double x, double y, double padding) {
        return x >= padding && x <= mapView.getWidth() - padding && y >= padding && y <= mapView.getHeight() - padding;
    }

    /**
     * Method to update the layout for pollution.
     * TODO make less slow when many points are visible
     *
     * TODO EASTING AND NORTHING COVNERSION FOR EACH COORDINATE TO ACCOUNT FOR CURVATURE
     */
    @Override
    protected void layoutLayer() {
        double iconSize = 1000 * getPixelScale();
        for (Pair<MapPoint, Circle> candidate : points) {
            MapPoint point = candidate.getKey();
            Circle icon = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            if (isPointOnScreen(mapPoint.getX(), mapPoint.getY(), -iconSize)) {
                icon.setRadius(iconSize/2);
                //icon.setHeight(iconSize);
                //icon.setWidth(iconSize);
                icon.setVisible(true);
                icon.setTranslateX(mapPoint.getX());
                icon.setTranslateY(mapPoint.getY());
            } else {
                icon.setVisible(false);
            }
        }

    }
}
