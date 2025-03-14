package statistics.back.simpleTrends;

import java.util.Map;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * Specialised statistics result for trend analysis data.
 * Tries to be type-safe by providing specific setters/accessors for trend information.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class SimpleTrendsResult implements StatisticsResult {
    private final String title;
    private final String description;
    private final Pollutant pollutant;
    
    private Map<Integer, Double> yearlyAverages;
    private Map<Integer, Double> yearlyTotals;
    private Double trendSlope;
    private Double trendIntercept;
    private Double percentChange;
    private Double totalPollution;
    private Double averagePollution;
    private Long dataPointCount;
    
    /**
     * Constructor.
     * @param title A short title describing these results.
     * @param description A longer description of the results.
     */
    public SimpleTrendsResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }
    
    /**
     * Set the yearly averages map.
     * @param yearlyAverages Map of year to average value.
     */
    public void setYearlyAverages(Map<Integer, Double> yearlyAverages) {
        this.yearlyAverages = yearlyAverages;
    }
    
    /**
     * Set the yearly totals map.
     * @param yearlyTotals Map of year to total value.
     */
    public void setYearlyTotals(Map<Integer, Double> yearlyTotals) {
        this.yearlyTotals = yearlyTotals;
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
     * Set the percent change from start to end.
     * @param percentChange Percentage change value.
     */
    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
    
    /**
     * Set single year snapshot values.
     * @param totalPollution Total pollution value.
     * @param averagePollution Average pollution value.
     * @param dataPointCount Number of data points.
     */
    public void setSnapshotData(double totalPollution, double averagePollution, long dataPointCount) {
        this.totalPollution = totalPollution;
        this.averagePollution = averagePollution;
        this.dataPointCount = dataPointCount;
    }
    
    // Simple getters:
    public Map<Integer, Double> getYearlyAverages() { return yearlyAverages; }
    public Map<Integer, Double> getYearlyTotals() { return yearlyTotals; }
    public double getTrendSlope() { return trendSlope; }
    public double getTrendIntercept() { return trendIntercept; }
    public double getPercentChange() { return percentChange; }
    public double getTotalPollution() { return totalPollution; }
    public double getAveragePollution() { return averagePollution; }
    public long getDataPointCount() { return dataPointCount; }
    
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