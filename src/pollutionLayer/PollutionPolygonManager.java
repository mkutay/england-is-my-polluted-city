package pollutionLayer;

import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.LODData;
import dataProcessing.LODManager;
import utility.CustomMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Organises generating, updating and querying pollution polygons
 * Handles Level Of Detail updates
 *
 * @author Anas Ahmed
 */
public class PollutionPolygonManager {
    private final List<PollutionPolygon> polygons;

    private final LODManager lodManager;
    private int currentLODIndex = -1;
    private final static int NUMBER_OF_LODS = 4;

    public PollutionPolygonManager(DataSet dataSet) {
        polygons = new ArrayList<>();
        lodManager = new LODManager(dataSet, NUMBER_OF_LODS);
    }

    /**
     * Regenerate polygons based on the current LOD.
     * @param lodData the LOD data to use to generate the polygons.
     */
    private void generatePollutionPolygons(LODData lodData) {
        polygons.clear(); // Reset polygons

        // Find min/max values for color mapping
        double minValue = Double.POSITIVE_INFINITY;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (DataPoint dataPoint : lodData.getData()) {
            double value = dataPoint.value();
            if (value == -1) continue; // If the value is missing, skip it.
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        for (DataPoint dataPoint : lodData.getData()) {
            if (dataPoint.value() == -1) continue; // Do not generate polygons for missing values.

            // Map data value to a color using the color scheme
            double normalizedValue = (dataPoint.value() - minValue) / (maxValue - minValue);
            int sideLength = 1000 * lodManager.getLODData(currentLODIndex).getLevelOfDetail();

            PollutionPolygon polygon = PollutionPolygon.createFromDataPoint(dataPoint, sideLength, normalizedValue);
            polygons.add(polygon);
        }
    }

    /**
     * Generates pollution data polygons from the CSV files.
     * Should be called every time a LOD change is detected
     */
    public void updatePollutionPolygons(CustomMapView mapView) {
        int lodIndex = lodManager.getLODIndex(mapView.getPixelScale(), mapView.getWidth(), mapView.getHeight());
        if (lodIndex == currentLODIndex) return; // No LOD update needed, exit.

        currentLODIndex = lodIndex;
        generatePollutionPolygons(lodManager.getLODData(currentLODIndex));
    }

    public List<PollutionPolygon> getPolygons() {
        return polygons;
    }

    public int getCurrentLevelOfDetail(){
        return lodManager.getLODData(currentLODIndex).getLevelOfDetail();
    }
}
