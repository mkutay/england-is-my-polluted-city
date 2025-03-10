package statistics.back.averagePollution;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

import java.util.List;
import java.util.OptionalDouble;
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
    public StatisticsResult calculateStatistics(DataSet dataSet) {
        AveragePollutionResult result = new AveragePollutionResult(
            "Average Pollution Levels", 
            "Statistical analysis of average pollution levels for " + 
            dataSet.getPollutant() + " in " + dataSet.getYear()
        );
        
        List<DataPoint> dataPoints = dataSet.getData();
        
        // Calculate mean:
        double mean = calculateMean(dataPoints);
        result.setMean(mean);
        
        // Calculate median:
        double median = calculateMedian(dataPoints);
        result.setMedian(median);
        
        // Calculate standard deviation:
        double stdDev = calculateStandardDeviation(dataPoints, mean);
        result.setStandardDeviation(stdDev);
        
        return result;
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        AveragePollutionResult result = new AveragePollutionResult(
            "Average Pollution Trends", 
            "Trend analysis of average pollution levels for " + 
            pollutant + " from " + startYear + " to " + endYear
        );
        
        double[] yearlyAverages = new double[endYear - startYear + 1];
        
        for (int year = startYear; year <= endYear; year++) {
            DataSet dataSet = dataManager.getPollutantData(year, pollutant);
            List<DataPoint> dataPoints = dataSet.getData();
            
            double mean = calculateMean(dataPoints);
            yearlyAverages[year - startYear] = mean;
            result.setYearlyMean(year, mean);
        }
        
        // Calculate trend:
        double trend = calculateTrend(yearlyAverages);
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
    
    /**
     * Calculate the trend from an array of yearly values.
     * @param yearlyValues Array of values by year.
     * @return Trend value (positive means increasing, negative means decreasing).
     */
    private double calculateTrend(double[] yearlyValues) {
        if (yearlyValues.length <= 1) {
            return 0.0;
        }
        
        // Simple trend calculation: last value minus first value.
        return yearlyValues[yearlyValues.length - 1] - yearlyValues[0];
    }
}
