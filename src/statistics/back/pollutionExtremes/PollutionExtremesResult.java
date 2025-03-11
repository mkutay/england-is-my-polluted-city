package statistics.back.pollutionExtremes;

import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Specialised statistics result for pollution extremes data.
 * Provides type-safe setters/accessors for hotspots and extremes information.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class PollutionExtremesResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    private final Pollutant pollutant;
    private final Map<String, Object> values; // Key-value pairs for the result, including specific values and objects.
    
    private DataPoint maxPoint; // Data point with max value.
    private DataPoint minPoint; // Data point with min value.
    private List<DataPoint> topHotspots; // List of top hotspot points.
    
    /**
     * Constructor for PollutionExtremesResult.
     * 
     * @param title A short title describing these results
     * @param description A longer description of the results
     */
    public PollutionExtremesResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
        this.values = new HashMap<>();
    }
    
    /**
     * Set the maximum data point.
     * @param maxPoint The data point with max value.
     */
    public void setMaxPoint(DataPoint maxPoint) {
        this.maxPoint = maxPoint;
        if (maxPoint != null) {
            values.put("maxValue", maxPoint.value());
            String locationString = String.format("Grid %d (Easting: %d, Northing: %d)", 
                maxPoint.gridCode(), maxPoint.x(), maxPoint.y());
            values.put("maxValueLocation", locationString);
        }
    }
    
    /**
     * Set the minimum data point.
     * @param minPoint The data point with min value.
     */
    public void setMinPoint(DataPoint minPoint) {
        this.minPoint = minPoint;
        if (minPoint != null) {
            values.put("minValue", minPoint.value());
            String locationString = String.format("Grid %d (Easting: %d, Northing: %d)", 
                minPoint.gridCode(), minPoint.x(), minPoint.y());
            values.put("minValueLocation", locationString);
        }
    }
    
    /**
     * Set the top hotspots.
     * @param topHotspots List of top hotspot points.
     */
    public void setTopHotspots(List<DataPoint> topHotspots) {
        this.topHotspots = topHotspots;
        values.put("topHotspots", topHotspots);
    }
    
    /**
     * Set the percentile value for a percentile.
     * @param percentileValue The value of the percentile.
     * @param percentile The percentile value (e.g. 90 for 90th percentile).
     */
    public void setPercentile(double percentileValue, int percentile) {
        values.put("percentileValue_" + percentile, percentileValue);
    }
    
    /**
     * Set yearly maximum data.
     * @param year The year.
     * @param maxValue The maximum value for that year.
     */
    public void setYearlyMaxValue(int year, double maxValue) {
        values.put("maxValue_" + year, maxValue);
    }
    
    /**
     * Set the year with the highest overall value.
     * @param year The year with highest value.
     * @param value The highest value.
     */
    public void setHighestOverallYear(int year, double value) {
        values.put("yearWithHighestValue", year);
        values.put("highestOverallValue", value);
    }

    /**
     * Store yearly max points.
     * @param yearToMaxPoint Map of year to max point.
     */
    public void setYearlyMaxPoints(Map<Integer, DataPoint> yearToMaxPoint) {
        values.put("yearlyMaxPoints", yearToMaxPoint);
    }
    
    /**
     * Store yearly max values.
     * @param yearToMaxValue Map of year to max value.
     */
    public void setYearlyMaxValues(Map<Integer, Double> yearToMaxValue) {
        values.put("yearlyMaxValues", yearToMaxValue);
    }
    
    // Simple getters:
    public DataPoint getMaxPoint() { return maxPoint; }
    public DataPoint getMinPoint() { return minPoint; }
    public List<DataPoint> getTopHotspots() { return topHotspots; }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public Object getValue(String key) {
        return values.get(key);
    }
    
    @Override
    public Map<String, Object> getAllValues() {
        return new HashMap<>(values);
    }

    @Override
    public Pollutant getPollutant() {
        return pollutant;
    }
}