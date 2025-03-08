package dataProcessing;

import java.util.ArrayList;
import java.util.List;

/**
 * Store Level Of Detail data, generated from a DataSet's data.
 */
public class LODData {
    // The levelOfDetail reduces the amount of data stored
    // The approximate amount of data points in an LOD is totalDataPoints/(levelOfDetail^2)
    // Pollution grid size length becomes levelOfDetail km long
    private final int levelOfDetail;
    private final List<DataPoint> LODdata;

    /**
     * Create an LOD from a dataSet
     * @param levelOfDetail the level of detail to produce. The larger this number is, the lower resolution the LOD (1 = same res as original)
     * @param dataSet the DataSet to create the LOD from
     */
    public LODData(int levelOfDetail, DataSet dataSet) {
        this.levelOfDetail = levelOfDetail;
        this.LODdata = generateLODData(dataSet);
    }

    /**
     * Generates the LOD data from a dataset
     * @param dataSet the DataSet to create the LOD from
     * @return the LOD data as a list of p
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
                if (point == null){continue;}
                //TODO get average of values in LOD range and make new data point using this average
                data.add(point);
            }
        }

        return data;
    }

    public int getLevelOfDetail() {
        return levelOfDetail;
    }

    public List<DataPoint> getData() {
        return LODdata;
    }
}