package statistics.back.averagePollution;

import java.util.Map;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * Specialized statistics result for average pollution data.
 * Provides type-safe accessors for common average statistics.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class AveragePollutionResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    private final Pollutant pollutant; // The pollutant this result is for.
    
    private Double mean; // Mean of the data for this result.
    private Double median; // Median of the data for this result.
    private Double standardDeviation; // Standard deviation of the data.
    private Double overallTrend; // Overall trend value.
    private Map<Integer, Double> yearlyMeans; // Yearly mean values.
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public AveragePollutionResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }
    
    /**
     * Set the mean value.
     * @param mean The mean value.
     */
    public void setMean(double mean) {
        this.mean = mean;
    }
    
    /**
     * Set the median value.
     * @param median The median value.
     */
    public void setMedian(double median) {
        this.median = median;
    }
    
    /**
     * Set the standard deviation.
     * @param standardDeviation The standard deviation.
     */
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
    
    /**
     * Set the yearly means.
     * @param yearlyMeans A map of year to mean value.
     */
    public void setYearlyMeans(Map<Integer, Double> yearlyMeans) {
        this.yearlyMeans = yearlyMeans;
    }
    
    /**
     * Set the overall trend value.
     * @param trend The trend value.
     */
    public void setOverallTrend(double trend) {
        this.overallTrend = trend;
    }
    
    // Simple getters:
    public double getMean() { return mean; }
    public double getMedian() { return median; }
    public double getStandardDeviation() { return standardDeviation; }
    public double getOverallTrend() { return overallTrend; }
    public Map<Integer, Double> getYearlyMeans() { return yearlyMeans; }
    
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
