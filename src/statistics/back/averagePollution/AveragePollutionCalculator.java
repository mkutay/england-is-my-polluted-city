package statistics.back.averagePollution;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public StatisticsResult calculateStatistics(Pollutant pollutant, int year) {
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        
        AveragePollutionResult result = new AveragePollutionResult(
            "Average Pollution Levels", 
            "Statistical analysis of average pollution levels for " + 
            dataSet.getPollutant() + " in " + dataSet.getYear(),
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
            pollutant + " from " + startYear + " to " + endYear,
            pollutant
        );
        
        double[] yearlyAverages = new double[endYear - startYear + 1];
        double meanOverall = 0.0;
        double medianOverall = 0.0;
        double standardDeviationOverall = 0.0;

        Map<Integer, Double> yearlyMeans = new HashMap<>();
        
        for (int year = startYear; year <= endYear; year++) {
            DataSet dataSet = dataManager.getPollutantData(year, pollutant);
            List<DataPoint> dataPoints = dataSet.getData();
            
            double mean = calculateMean(dataPoints);
            yearlyAverages[year - startYear] = mean;
            yearlyMeans.put(year, mean);

            meanOverall += mean;
            medianOverall += calculateMedian(dataPoints);
            standardDeviationOverall += calculateStandardDeviation(dataPoints, mean);
        }

        result.setMean(meanOverall / yearlyAverages.length);
        result.setMedian(medianOverall / yearlyAverages.length);
        result.setStandardDeviation(standardDeviationOverall / yearlyAverages.length);
        result.setYearlyMeans(yearlyMeans);
        
        // Calculate trend:
        double trend = yearlyAverages[yearlyAverages.length - 1] - yearlyAverages[0];
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
