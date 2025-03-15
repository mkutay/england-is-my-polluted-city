package statistics.back.averagePollution;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Calculator for average pollution statistics.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class AveragePollutionCalculator implements StatisticsCalculator {
    private final DataManager dataManager;
    
    /**
     * Constructor
     */
    public AveragePollutionCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatistics(Pollutant pollutant, int year) {
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        
        AveragePollutionResult result = new AveragePollutionResult(
            "Average Pollution Levels", 
            "Statistical analysis of average pollution levels for " + 
            pollutant.getDisplayName() + " in " + year,
            pollutant
        );
        
        List<DataPoint> dataPoints = dataSet.getData();
        
        // Calculate mean:
        double mean = calculateMean(dataPoints);
        result.setMean(mean);
        
        // Calculate median:
        double median = calculateMedian(dataPoints);
        result.setMedian(median);
        
        // Calculate standard deviation:
        double standardDeviation = calculateStandardDeviation(dataPoints, mean);
        result.setStandardDeviation(standardDeviation);
        
        return result;
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        AveragePollutionResult result = new AveragePollutionResult(
            "Average Pollution Trends", 
            "Trend analysis of average pollution levels for " + 
            pollutant.getDisplayName() + " from " + startYear + " to " + endYear,
            pollutant
        );

        Map<Integer, Double> yearlyMeans = new ConcurrentHashMap<>();
        Map<Integer, Double> yearlyMedians = new ConcurrentHashMap<>();
        Map<Integer, Double> yearlyStandardDeviations = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // Asynchoronously calculate yearly means, medians, and standard deviations:
        for (int year = startYear; year <= endYear; year++) {
            final int finalYear = year;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                DataSet dataSet = dataManager.getPollutantData(finalYear, pollutant);
                List<DataPoint> dataPoints = dataSet.getData();
                
                double mean = calculateMean(dataPoints);
                yearlyMeans.put(finalYear, mean);
                yearlyMedians.put(finalYear, calculateMedian(dataPoints));
                yearlyStandardDeviations.put(finalYear, calculateStandardDeviation(dataPoints, mean));
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        double mean = yearlyMeans
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0) / yearlyMeans.size();

        result.setMean(mean);

        double median = yearlyMedians
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0) / yearlyMedians.size();

        result.setMedian(median);

        double standardDeviation = yearlyStandardDeviations
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0) / yearlyStandardDeviations.size();

        result.setStandardDeviation(standardDeviation);

        result.setYearlyMeans(yearlyMeans);
        
        // Calculate trend:
        double trend = yearlyMeans.get(endYear) - yearlyMeans.get(startYear);
        result.setOverallTrend(trend);
        
        return result;
    }
    
    @Override
    public String getStatisticsName() {
        return "Average Pollution";
    }
    
    /**
     * Calculate the mean value from a list of data points.
     * @param dataPoints List of data points.
     * @return The mean value.
     */
    private double calculateMean(List<DataPoint> dataPoints) {
        OptionalDouble mean = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .average();
        
        return mean.orElse(0.0);
    }
    
    /**
     * Calculate the median value from a list of data points.
     * @param dataPoints List of data points.
     * @return The median value.
     */
    private double calculateMedian(List<DataPoint> dataPoints) {
        List<Double> sortedValues = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .sorted()
            .boxed()
            .collect(Collectors.toList());
        
        if (sortedValues.isEmpty()) {
            return 0.0;
        }
        
        int middle = sortedValues.size() / 2;
        if (sortedValues.size() % 2 == 1) {
            return sortedValues.get(middle);
        } else {
            return (sortedValues.get(middle - 1) + sortedValues.get(middle)) / 2.0;
        }
    }
    
    /**
     * Calculate the standard deviation from a list of data points.
     * @param dataPoints List of data points.
     * @param mean Precalculated mean value.
     * @return The standard deviation.
     */
    private double calculateStandardDeviation(List<DataPoint> dataPoints, double mean) {
        double sumSquaredDiffs = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .map(value -> Math.pow(value - mean, 2))
            .sum();
        
        int validCount = (int) dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0)
            .count();
        
        return Math.sqrt(sumSquaredDiffs / validCount);
    }
}
