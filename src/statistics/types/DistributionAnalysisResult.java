package statistics.types;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Result class for distribution analysis containing percentiles, skewness, kurtosis, and histogram data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DistributionAnalysisResult implements StatisticsResult {
    private final String title;
    private final String description;
    private final Pollutant pollutant;
    
    private Map<Double, Double> percentiles;
    private Map<Integer, Map<Double, Double>> yearlyPercentiles;
    private Map<Integer, Double> yearlySkewness;
    private Map<Integer, Double> yearlyKurtosis;
    
    /**
     * Constructor.
     * @param title Title of the result
     * @param description Description of the result
     * @param pollutant The pollutant this result is for
     */
    public DistributionAnalysisResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }
    
    // Setters:
    public void setPercentiles(Map<Double, Double> percentiles) {
        this.percentiles = percentiles;
    }
    
    public void setYearlyPercentiles(Map<Integer, Map<Double, Double>> yearlyPercentiles) {
        this.yearlyPercentiles = yearlyPercentiles;
    }
    
    public void setYearlySkewness(Map<Integer, Double> yearlySkewness) {
        this.yearlySkewness = yearlySkewness;
    }
    
    public void setYearlyKurtosis(Map<Integer, Double> yearlyKurtosis) {
        this.yearlyKurtosis = yearlyKurtosis;
    }
    
    // Getters
    public Map<Double, Double> getPercentiles() { return percentiles; }
    public Map<Integer, Map<Double, Double>> getYearlyPercentiles() { return yearlyPercentiles; }
    public Map<Integer, Double> getYearlySkewness() { return yearlySkewness; }
    public Map<Integer, Double> getYearlyKurtosis() { return yearlyKurtosis; }

    /**
     * Get the yearly percentile values for a given percentile.
     * @param percentile The percentile to get values for.
     * @return A map of year to percentile value.
     */
    public Map<Integer, Double> getYearlyPercentile(double percentile) {
        return yearlyPercentiles.entrySet().stream()
            .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue().get(percentile)));
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
    public Pollutant getPollutant() {
        return pollutant;
    }
}
