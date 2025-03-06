import com.gluonhq.maps.MapPoint;
import net.sf.geographiclib.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Utility class for geographic calculations
 */
public class GeographicUtilities{
    /**
     * Distance between two latitude and longitude positions on earth using vincenty formula
     * @param point1 MapPoint of the first world position
     * @param point2 MapPoint of the second world position
     * @return Returns the distance between point1 and point2 in meters
     */
    public static double geodesicDistance(MapPoint point1, MapPoint point2) {
        double lat1 = point1.getLatitude();
        double lon1 = point1.getLongitude();
        double lat2 = point2.getLatitude();
        double lon2 = point2.getLongitude();

        GeodesicData g = Geodesic.WGS84.Inverse(lat1, lon1, lat2, lon2);
        return g.s12;
    }
}

