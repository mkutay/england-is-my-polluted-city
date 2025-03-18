package statistics.back.trends;

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
public class TrendsResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    private final Pollutant pollutant; // The pollutant this result is for.
    
    private Double mean; // Mean of the data for this result.
    private Double median; // Median of the data for this result.
    private Double standardDeviation; // Standard deviation of the data.
    private Double trendSlope; // Slope of the trend line.
    private Double trendIntercept; // Y-intercept of the trend line.
    private Double percentChange; // Percent change from start to end.
    private Map<Integer, Double> yearlyMeans; // Yearly mean values.
    private Map<Integer, Double> yearlyMedians; // Yearly median values.
    private Map<Integer, Double> yearlyStandardDeviations; // Yearly standard deviations.
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public TrendsResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }

    /**
     * Set the trend linear regression coefficients.
     * @param slope Slope of the trend line.
     * @param intercept Y-intercept of the trend line.
     */
    public void setTrendCoefficients(double slope, double intercept) {
        this.trendSlope = slope;
        this.trendIntercept = intercept;
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
     * Set the yearly medians.
     * @param yearlyMedians A map of year to median value.
     */
    public void setYearlyMedians(Map<Integer, Double> yearlyMedians) {
        this.yearlyMedians = yearlyMedians;
    }

    /**
     * Set the yearly standard deviations.
     * @param yearlyStandardDeviations A map of year to standard deviation value.
     */
    public void setYearlyStandardDeviations(Map<Integer, Double> yearlyStandardDeviations) {
        this.yearlyStandardDeviations = yearlyStandardDeviations;
    }
    
    /**
     * Set the percent change from start to end.
     * @param percentChange Percentage change value.
     */
    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
    
    // Simple getters:
    public Double getMean() { return mean; }
    public Double getMedian() { return median; }
    public Double getStandardDeviation() { return standardDeviation; }
    public Double getTrendSlope() { return trendSlope; }
    public Double getTrendIntercept() { return trendIntercept; }
    public Double getPercentChange() { return percentChange; }
    public Map<Integer, Double> getYearlyMeans() { return yearlyMeans; }
    public Map<Integer, Double> getYearlyMedians() { return yearlyMedians; }
    public Map<Integer, Double> getYearlyStandardDeviations() { return yearlyStandardDeviations; }
    
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
