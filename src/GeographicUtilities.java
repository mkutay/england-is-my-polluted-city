import com.gluonhq.maps.MapPoint;
import net.sf.geographiclib.*;
import uk.gov.dstl.geo.osgb.EastingNorthingConversion;
import uk.gov.dstl.geo.osgb.Constants;

/**
 * Utility class for geographic calculations.
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

    /**
     * Converts a UK easting/northing value to latitude/longitude
     * @param easting easting value of point
     * @param northing northing value of point
     * @return a new MapPoint with the corresponding latitude and longitude
     */
    public static MapPoint convertEastingNorthingToLatLon(int easting, int northing) {
        double[] latitudeLongitude = EastingNorthingConversion.toLatLon(
            new double[] {easting, northing},
            Constants.ELLIPSOID_AIRY1830_MAJORAXIS,
            Constants.ELLIPSOID_AIRY1830_MINORAXIS,
            Constants.NATIONALGRID_N0,
            Constants.NATIONALGRID_E0,
            Constants.NATIONALGRID_F0,
            Constants.NATIONALGRID_LAT0,
            Constants.NATIONALGRID_LON0);

        return new MapPoint(latitudeLongitude[0], latitudeLongitude[1]);
    }
}

