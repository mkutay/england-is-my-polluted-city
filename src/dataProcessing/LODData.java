package dataProcessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Store Level Of Detail data, generated from a DataSet's data.
 * 
 * @author Anas Ahmed
 * @version 1.0
 */
public class LODData {
    // The levelOfDetail reduces the amount of data stored.
    // The approximate amount of data points in an LOD is totalDataPoints / (levelOfDetail ^ 2)
    // Pollution grid size length becomes levelOfDetail km long.
    private final int levelOfDetail;
    private final List<DataPoint> LODdata;

    /**
     * Create an LOD from a dataSet.
     * @param levelOfDetail The level of detail to produce. The larger this number is, the lower resolution the LOD (1 = same as original).
     * @param dataSet The DataSet to create the LOD from.
     */
    public LODData(int levelOfDetail, DataSet dataSet) {
        this.levelOfDetail = levelOfDetail;
        this.LODdata = generateLODData(dataSet);
    }

    /**
     * Generates the LOD data from a dataset.
     * @param dataSet The DataSet to create the LOD from.
     * @return The LOD data as a list of data points.
     */
    private List<DataPoint> generateLODData(DataSet dataSet) {
        List<DataPoint> data = new ArrayList<>();

        int minEasting = dataSet.getData().stream().mapToInt(DataPoint::x).min().orElse(Integer.MAX_VALUE);
        int maxEasting = dataSet.getData().stream().mapToInt(DataPoint::x).max().orElse(Integer.MIN_VALUE);
        int minNorthing = dataSet.getData().stream().mapToInt(DataPoint::y).min().orElse(Integer.MAX_VALUE);
        int maxNorthing = dataSet.getData().stream().mapToInt(DataPoint::y).max().orElse(Integer.MIN_VALUE);

        int gridSize = 1000 * levelOfDetail;

        for (int x = minEasting; x < maxEasting; x += gridSize) {
            for (int y = minNorthing; y < maxNorthing; y += gridSize) {
                DataPoint point = dataSet.getDataPoint(x, y);
                if (point == null) continue;
                double value = getAverageValue(dataSet, gridSize, x, y);
                DataPoint updatedPoint = new DataPoint(point.gridCode(), point.x(), point.y(), value);
                data.add(updatedPoint);
            }
        }

        return data;
    }

    /**
     * Averages all data points pollution values in a certain grid size from a certain easting and northing
     * @param dataSet the DataSet of all points
     * @param gridSize the size of the grid
     * @param easting the easting of the larger grid
     * @param northing the northing of the larger grid
     * @return the average of all pollution values
     */
    private double getAverageValue(DataSet dataSet, int gridSize, int easting, int northing) {
        double value = 0; int i = 0;
        for (int x = easting; x < easting + gridSize; x+=1000) {
            for (int y = northing; y < northing + gridSize; y+=1000) {
                DataPoint dataPoint = dataSet.getDataPoint(x, y);
                if (dataPoint == null) continue;
                value += dataPoint.value();
                i++;
            }
        }
        return value/i;
    }

    // Getters:
    public int getLevelOfDetail() { return levelOfDetail; }
    public List<DataPoint> getData() { return LODdata; }
}