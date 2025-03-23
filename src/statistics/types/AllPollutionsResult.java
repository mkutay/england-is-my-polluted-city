package statistics.types;

import java.util.Map;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * Specialized statistics result for all pollutants.
 * Provides type-safe accessors for statistics.
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
    private Map<Pollutant, TrendsResult> trends; // Trends for each pollutant.
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public AllPollutionsResult(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Set the trends for each pollutant.
     * @param trends The trends for each pollutant.
     */
    public void setTrends(Map<Pollutant, TrendsResult> trends) {
        this.trends = trends;
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
    public Map<Pollutant, TrendsResult> getTrends() { return trends; }
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
