import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PollutionPolygonCollection {
    private final HashMap<String, PollutionPolygon> pollutionPolygons;

    public PollutionPolygonCollection() {
        pollutionPolygons = new HashMap<>();
    }

    public void clear(){
        pollutionPolygons.clear();
    }

    public void add(PollutionPolygon pollutionPolygon){
        pollutionPolygons.put(pollutionPolygon.getTopLeftEastingNorthing(), pollutionPolygon);
    }

    /**
     * Get the polygon from a specified top left easting/northing coordinate
     * @param topLeftEasting the easting of the top left
     * @param topLeftNorthing the northing of the top left
     * @return the polygon with the specified top left coordinate
     */
    public PollutionPolygon get(int topLeftEasting, int topLeftNorthing){
        String key = topLeftEasting + "," + topLeftNorthing;
        return pollutionPolygons.get(key);
    }

    public List<PollutionPolygon> values(){
        return new ArrayList<>(pollutionPolygons.values());
    }
}
