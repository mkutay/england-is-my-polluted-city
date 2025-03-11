package statistics.back.averagePollution;

import java.util.HashMap;
import java.util.Map;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * Specialized statistics result for average pollution data.
 * Provides type-safe accessors for common average statistics.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AveragePollutionResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    private final Pollutant pollutant; // The pollutant this result is for.
    private final Map<String, Object> values; // Map of all values for this result.
    
    private double mean; // Mean of the data for this result.
    private double median; // Median of the data for this result.
    private double standardDeviation; // Standard deviation of the data.
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public AveragePollutionResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
        this.values = new HashMap<>();
    }
    
    /**
     * Set the mean value.
     * @param mean The mean value.
     */
    public void setMean(double mean) {
        this.mean = mean;
        values.put("mean", mean);
    }
    
    /**
     * Set the median value.
     * @param median The median value.
     */
    public void setMedian(double median) {
        this.median = median;
        values.put("median", median);
    }
    
    /**
     * Set the standard deviation.
     * @param standardDeviation The standard deviation.
     */
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
        values.put("standardDeviation", standardDeviation);
    }
    
    /**
     * Add a year-specific mean value for trend analysis.
     * @param year The year.
     * @param mean The mean value for that year.
     */
    public void setYearlyMean(int year, double mean) {
        values.put("mean_" + year, mean);
    }
    
    /**
     * Set the overall trend value.
     * @param trend The trend value.
     */
    public void setOverallTrend(double trend) {
        values.put("overallTrend", trend);
    }
    
    // Simple getters:
    public double getMean() { return mean; }
    public double getMedian() { return median; }
    public double getStandardDeviation() { return standardDeviation; }
    
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
