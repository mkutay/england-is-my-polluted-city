package statistics.back.allPollutions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * Specialized statistics result for average pollution data.
 * Provides type-safe accessors for common average statistics.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class AllPollutionsResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    
    private Double totalMean; // Total mean of all pollutants.
    private Double totalMedian; // Total median of all pollutants.
    private Double totalStandardDeviation; // Total standard deviation of all pollutants.
    private Map<Pollutant, Double> mean; // Mean of the data for this result.
    private Map<Pollutant, Double> median; // Median of the data for this result.
    private Map<Pollutant, Double> standardDeviation; // Standard deviation of the data.
    private Map<Pollutant, Double> trendSlope; // Slope of the trend line.
    private Map<Pollutant, Double> trendIntercept; // Y-intercept of the trend line.
    private Map<Pollutant, Double> percentChange; // Percent change from start to end.
    private Map<Pollutant, Map<Integer, Double>> yearlyMeans; // Yearly mean values.
    private Map<Pollutant, Map<Integer, Double>> yearlyMedians; // Yearly median values.
    private Map<Pollutant, Map<Integer, Double>> yearlyStandardDeviations; // Yearly standard deviations.
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public AllPollutionsResult(String title, String description) {
        this.title = title;
        this.description = description;

        // Initialise maps with concurrent hash map:
        this.mean = new ConcurrentHashMap<>();
        this.median = new ConcurrentHashMap<>();
        this.standardDeviation = new ConcurrentHashMap<>();
        this.trendSlope = new ConcurrentHashMap<>();
        this.trendIntercept = new ConcurrentHashMap<>();
        this.percentChange = new ConcurrentHashMap<>();
        this.yearlyMeans = new ConcurrentHashMap<>();
        this.yearlyMedians = new ConcurrentHashMap<>();
        this.yearlyStandardDeviations = new ConcurrentHashMap<>();
    }

    /**
     * Set the trend linear regression coefficients.
     * @param pollutant The pollutant for which the coefficients are set.
     * @param slope Slope of the trend line.
     * @param intercept Y-intercept of the trend line.
     */
    public void setTrendCoefficients(Pollutant pollutant, double slope, double intercept) {
        this.trendSlope.put(pollutant, slope);
        this.trendIntercept.put(pollutant, intercept);
    }
    
    /**
     * Set the mean value.
     * @param pollutant The pollutant for which the mean is set.
     * @param mean The mean value.
     */
    public void setMean(Pollutant pollutant, double mean) {
        this.mean.put(pollutant, mean);
    }
    
    /**
     * Set the median value.
     * @param pollutant The pollutant for which the median is set.
     * @param median The median value.
     */
    public void setMedian(Pollutant pollutant, double median) {
        this.median.put(pollutant, median);
    }
    
    /**
     * Set the standard deviation.
     * @param pollutant The pollutant for which the standard deviation is set.
     * @param standardDeviation The standard deviation.
     */
    public void setStandardDeviation(Pollutant pollutant, double standardDeviation) {
        this.standardDeviation.put(pollutant, standardDeviation);
    }
    
    /**
     * Set the yearly means.
     * @param pollutant The pollutant for which the yearly means are set.
     * @param yearlyMeans A map of year to mean value.
     */
    public void setYearlyMeans(Pollutant pollutant, Map<Integer, Double> yearlyMeans) {
        this.yearlyMeans.put(pollutant, yearlyMeans);
    }

    /**
     * Set the yearly medians.
     * @param pollutant The pollutant for which the yearly medians are set.
     * @param yearlyMedians A map of year to median value.
     */
    public void setYearlyMedians(Pollutant pollutant, Map<Integer, Double> yearlyMedians) {
        this.yearlyMedians.put(pollutant, yearlyMedians);
    }

    /**
     * Set the yearly standard deviations.
     * @param pollutant The pollutant for which the yearly standard deviations are set.
     * @param yearlyStandardDeviations A map of year to standard deviation value.
     */
    public void setYearlyStandardDeviations(Pollutant pollutant, Map<Integer, Double> yearlyStandardDeviations) {
        this.yearlyStandardDeviations.put(pollutant, yearlyStandardDeviations);
    }
    
    /**
     * Set the percent change from start to end.
     * @param pollutant The pollutant for which the percent change is set.
     * @param percentChange Percentage change value.
     */
    public void setPercentChange(Pollutant pollutant, double percentChange) {
        this.percentChange.put(pollutant, percentChange);
    }

    /**
     * Set the total mean value.
     * @param totalMean The total mean value.
     */
    public void setTotalMean(double totalMean) {
        this.totalMean = totalMean;
    }

    /**
     * Set the total median value.
     * @param totalMedian The total median value.
     */
    public void setTotalMedian(double totalMedian) {
        this.totalMedian = totalMedian;
    }

    /**
     * Set the total standard deviation.
     * @param totalStandardDeviation The total standard deviation.
     */
    public void setTotalStandardDeviation(double totalStandardDeviation) {
        this.totalStandardDeviation = totalStandardDeviation;
    }
    
    // Simple getters:
    public Map<Pollutant, Double> getMean() { return mean; }
    public Map<Pollutant, Double> getMedian() { return median; }
    public Map<Pollutant, Double> getStandardDeviation() { return standardDeviation; }
    public Map<Pollutant, Double> getTrendSlope() { return trendSlope; }
    public Map<Pollutant, Double> getTrendIntercept() { return trendIntercept; }
    public Map<Pollutant, Double> getPercentChange() { return percentChange; }
    public Map<Pollutant, Map<Integer, Double>> getYearlyMeans() { return yearlyMeans; }
    public Map<Pollutant, Map<Integer, Double>> getYearlyMedians() { return yearlyMedians; }
    public Map<Pollutant, Map<Integer, Double>> getYearlyStandardDeviations() { return yearlyStandardDeviations; }
    public Double getTotalMean() { return totalMean; }
    public Double getTotalMedian() { return totalMedian; }
    public Double getTotalStandardDeviation() { return totalStandardDeviation; }
    
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
        return null;
    }
}
