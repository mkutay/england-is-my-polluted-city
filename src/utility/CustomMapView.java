package utility;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

/**
 * @author Anas Ahmed
 */
public class CustomMapView extends MapView {

    /**
     * Gets a scale factor to scale 1 pixel into 1 meter in the real world depending on current zoom level.
     * ie pixel size * sf = real world size
     * @return Scale factor for pixel scale.
     */
    public double getPixelScale() {
        MapPoint A = this.getMapPosition(0, 0);
        MapPoint B = this.getMapPosition(this.getWidth(), 0);
        return this.getWidth() / GeographicUtilities.geodesicDistance(A, B);
    }

    /**
     * Takes in a pixel x/y coordinate and checks if it is on the screen.
     * @param x The x coordinate in pixels.
     * @param y The y coordinate in pixels.
     * @param padding The padding inside the screen.
     * @return True if the point is on the screen, false otherwise.
     */
    public boolean isPointOnScreen(double x, double y, double padding) {
        return x >= -padding && x <= getWidth() + padding &&
                y >= -padding && y <= getHeight() + padding;
    }
}
