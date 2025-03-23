package statistics.back.calculators;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.types.DistributionAnalysisResult;
import statistics.types.StatisticsResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Calculator for analysing the statistical distribution of pollution data.
 * Calculates percentiles, skewness, kurtosis, and histogram data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DistributionAnalysisCalculator implements StatisticsCalculator {
    private static final double[] PERCENTILES = {10, 25, 50, 75, 90, 95, 99};
    
    private final DataManager dataManager;
    
    /**
     * Constructor.
     */
    public DistributionAnalysisCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        DistributionAnalysisResult result = new DistributionAnalysisResult(
            "Pollution Distribution Analysis", 
            "Statistical distribution analysis for " + pollutant.getDisplayName() + " from " + startYear + " to " + endYear,
            pollutant
        );
        
        // Collect all valid data points across the specified years.
        List<Double> allValues = new ArrayList<>();
        Map<Integer, List<Double>> yearlyValues = new ConcurrentHashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (int year = startYear; year <= endYear; year++) {
            final int finalYear = year;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                DataSet dataSet = dataManager.getPollutantData(finalYear, pollutant);
                List<Double> values = dataSet.getData().stream()
                    .mapToDouble(DataPoint::value)
                    .filter(val -> val >= 0) // Filter out invalid values.
                    .boxed()
                    .collect(Collectors.toList());
                
                yearlyValues.put(finalYear, values);
            });
            futures.add(future);
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // Combine all values for overall analysis.
        for (List<Double> values : yearlyValues.values()) {
            allValues.addAll(values);
        }
        
        if (allValues.isEmpty()) {
            return result;
        }
        
        // Calculate overall percentiles.
        Map<Double, Double> percentileValues = calculatePercentiles(allValues, PERCENTILES);
        result.setPercentiles(percentileValues);
        
        // Calculate yearly skewness, kurtosis, and yearly percentiles
        Map<Integer, Double> yearlySkewness = new HashMap<>();
        Map<Integer, Double> yearlyKurtosis = new HashMap<>();
        Map<Integer, Map<Double, Double>> yearlyPercentiles = new HashMap<>();
        for (Map.Entry<Integer, List<Double>> entry : yearlyValues.entrySet()) {
            yearlySkewness.put(entry.getKey(), calculateSkewness(entry.getValue()));
            yearlyKurtosis.put(entry.getKey(), calculateKurtosis(entry.getValue()));
            yearlyPercentiles.put(entry.getKey(), calculatePercentiles(entry.getValue(), PERCENTILES));
        }
        result.setYearlySkewness(yearlySkewness);
        result.setYearlyKurtosis(yearlyKurtosis);
        result.setYearlyPercentiles(yearlyPercentiles);
        
        return result;
    }
    
    /**
     * Calculate percentiles for a list of values.
     * @param values List of values.
     * @param percentiles Array of percentiles to calculate (0 - 100).
     * @return Map of percentile to value.
     */
    private Map<Double, Double> calculatePercentiles(List<Double> values, double[] percentiles) {
        if (values.isEmpty() || percentiles.length == 0) {
            throw new IllegalArgumentException("Values list and percentiles array cannot be empty.");
        }
        
        List<Double> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        
        Map<Double, Double> result = new HashMap<>();
        for (double percentile : percentiles) {
            int index = (int) Math.round(percentile / 100d * (sortedValues.size() - 1));
            result.put(percentile, sortedValues.get(index));
        }
        
        return result;
    }
    
    /**
     * Calculate the skewness of a distribution.
     * Skewness measures the asymmetry of the probability distribution.
     * @param values List of values.
     * @return Skewness value.
     */
    private double calculateSkewness(List<Double> values) {
        if (values.size() < 3) {
            return 0.0;
        }
        
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double n = values.size();
        
        // Calculate the third moment and standard deviation.
        double sumCubed = values.stream()
            .mapToDouble(x -> Math.pow(x - mean, 3))
            .sum();
        
        double sumSquared = values.stream()
            .mapToDouble(x -> Math.pow(x - mean, 2))
            .sum();
        
        double stdDev = Math.sqrt(sumSquared / n);
        
        // Skewness formula.
        return (sumCubed / n) / Math.pow(stdDev, 3);
    }
    
    /**
     * Calculate the kurtosis of a distribution.
     * Kurtosis measures the "tailedness" of the probability distribution.
     * @param values List of values.
     * @return Kurtosis value.
     */
    private double calculateKurtosis(List<Double> values) {
        if (values.size() < 4) {
            return 0.0;
        }
        
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double n = values.size();
        
        // Calculate the fourth moment and standard deviation.
        double sumFourth = values.stream()
            .mapToDouble(x -> Math.pow(x - mean, 4))
            .sum();
        
        double sumSquared = values.stream()
            .mapToDouble(x -> Math.pow(x - mean, 2))
            .sum();
        
        double variance = sumSquared / n;
        
        // Kurtosis formula (excess kurtosis: normal distribution = 0).
        return (sumFourth / n) / Math.pow(variance, 2) - 3.0;
    }
    
    @Override
    public String getStatisticsName() {
        return "Distribution Analysis";
    }
}
