package statistics.back.pollutionExtremes;

import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import javafx.util.Pair;
import statistics.back.StatisticsResult;

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
    
    private DataPoint maxPoint; // Data point with max value.
    private DataPoint minPoint; // Data point with min value.
    private List<DataPoint> topHotspots; // List of top hotspot points.
    private Pair<Double, Integer> percentile;
    private Pair<Integer, Double> highestOverallYear;
    private Map<Integer, DataPoint> yearToMaxPoints;
    
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
    }
    
    /**
     * Set the maximum data point.
     * @param maxPoint The data point with max value.
     */
    public void setMaxPoint(DataPoint maxPoint) {
        this.maxPoint = maxPoint;
    }
    
    /**
     * Set the minimum data point.
     * @param minPoint The data point with min value.
     */
    public void setMinPoint(DataPoint minPoint) {
        this.minPoint = minPoint;
    }
    
    /**
     * Set the top hotspots.
     * @param topHotspots List of top hotspot points.
     */
    public void setTopHotspots(List<DataPoint> topHotspots) {
        this.topHotspots = topHotspots;
    }
    
    /**
     * Set the percentile value for a percentile.
     * @param percentileValue The value of the percentile.
     * @param percentile The percentile value (e.g. 90 for 90th percentile).
     */
    public void setPercentile(double percentileValue, int percentile) {
        this.percentile = new Pair<>(percentileValue, percentile);
    }
    
    /**
     * Set the year with the highest overall value.
     * @param year The year with highest value.
     * @param value The highest value.
     */
    public void setHighestOverallYear(int year, double value) {
        this.highestOverallYear = new Pair<>(year, value);
    }

    /**
     * Store yearly max points.
     * @param yearToMaxPoint Map of year to max point.
     */
    public void setYearlyMaxPoints(Map<Integer, DataPoint> yearToMaxPoints) {
        this.yearToMaxPoints = yearToMaxPoints;
    }
    
    // Simple getters:
    public DataPoint getMaxPoint() { return maxPoint; }
    public DataPoint getMinPoint() { return minPoint; }
    public List<DataPoint> getTopHotspots() { return topHotspots; }
    public Pair<Double, Integer> getPercentile() { return percentile; }
    public Pair<Integer, Double> getHighestOverallYear() { return highestOverallYear; }
    public Map<Integer, DataPoint> getYearToMaxPoints() { return yearToMaxPoints; }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Pollutant getPollutant() {
        return pollutant;
    }
}