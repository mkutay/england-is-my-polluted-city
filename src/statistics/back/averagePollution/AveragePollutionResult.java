package statistics.back.averagePollution;

import java.util.HashMap;
import java.util.Map;

import statistics.back.StatisticsResult;

/**
 * Specialized statistics result for average pollution data.
 * Provides type-safe accessors for common average statistics.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AveragePollutionResult implements StatisticsResult {
    private final String title;
    private final String description;
    private final Map<String, Object> values;
    
    private double mean;
    private double median;
    private double standardDeviation;
    
    /**
     * Constructor for AveragePollutionResult.
     * 
     * @param title A short title describing these results
     * @param description A longer description of the results
     */
    public AveragePollutionResult(String title, String description) {
        this.title = title;
        this.description = description;
        this.values = new HashMap<>();
    }
    
    /**
     * Set the mean value
     * 
     * @param mean The mean value
     */
    public void setMean(double mean) {
        this.mean = mean;
        values.put("mean", mean);
    }
    
    /**
     * Set the median value
     * 
     * @param median The median value
     */
    public void setMedian(double median) {
        this.median = median;
        values.put("median", median);
    }
    
    /**
     * Set the standard deviation
     * 
     * @param standardDeviation The standard deviation
     */
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
        values.put("standardDeviation", standardDeviation);
    }
    
    /**
     * Add a year-specific mean value for trend analysis
     * 
     * @param year The year
     * @param mean The mean value for that year
     */
    public void setYearlyMean(int year, double mean) {
        values.put("mean_" + year, mean);
    }
    
    /**
     * Set the overall trend value
     * 
     * @param trend The trend value
     */
    public void setOverallTrend(double trend) {
        values.put("overallTrend", trend);
    }
    
    /**
     * Get the mean value
     * 
     * @return The mean value
     */
    public double getMean() {
        return mean;
    }
    
    /**
     * Get the median value
     * 
     * @return The median value
     */
    public double getMedian() {
        return median;
    }
    
    /**
     * Get the standard deviation
     * 
     * @return The standard deviation
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }
    
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
}
