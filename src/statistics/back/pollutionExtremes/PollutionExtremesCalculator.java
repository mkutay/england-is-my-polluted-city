package statistics.back.pollutionExtremes;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calculator for finding maximum pollution values and hotspots, i.e. the extremes.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class PollutionExtremesCalculator implements StatisticsCalculator {
    private final DataManager dataManager; // Data manager instance.
    private static final int TOP_HOTSPOTS_COUNT = 5; // Number of top hotspots to find.
    private static final int PERCENTILE = 90; // Percentile to calculate.
    
    /**
     * Constructor.
     */
    public PollutionExtremesCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatistics(Pollutant pollutant, int year) {
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        
        PollutionExtremesResult result = new PollutionExtremesResult(
            "Pollution Hotspots", 
            "Analysis of highest pollution areas for " + 
            dataSet.getPollutant() + " in " + dataSet.getYear()
        );
        
        List<DataPoint> dataPoints = dataSet.getData();
        
        // Find maximum value:
        DataPoint maxPoint = findMaxDataPoint(dataPoints);
        result.setMaxPoint(maxPoint);
        
        // Find minimum value:
        DataPoint minPoint = findMinDataPoint(dataPoints);
        result.setMinPoint(minPoint);
        
        // Find top hotspots:
        List<DataPoint> topHotspots = findTopHotspots(dataPoints, TOP_HOTSPOTS_COUNT);
        result.setTopHotspots(topHotspots);
        
        // Calculate some percentile:
        double percentileValue = calculatePercentile(dataPoints, PERCENTILE);
        result.setPercentile(percentileValue, PERCENTILE);
        
        return result;
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        PollutionExtremesResult result = new PollutionExtremesResult(
            "Pollution Hotspots Trends", 
            "Trend analysis of pollution hotspots for " + pollutant + " from " + startYear + " to " + endYear
        );
        
        Map<Integer, DataPoint> yearToMaxPoint = new HashMap<>();
        Map<Integer, Double> yearToMaxValue = new HashMap<>();
        
        for (int year = startYear; year <= endYear; year++) {
            DataSet dataSet = dataManager.getPollutantData(year, pollutant);
            List<DataPoint> dataPoints = dataSet.getData();
            
            DataPoint maxPoint = findMaxDataPoint(dataPoints);
            if (maxPoint != null) {
                yearToMaxPoint.put(year, maxPoint);
                yearToMaxValue.put(year, maxPoint.value());
                result.setYearlyMaxValue(year, maxPoint.value());
            }
        }
        
        result.setYearlyMaxPoints(yearToMaxPoint);
        result.setYearlyMaxValues(yearToMaxValue);
        
        // Find year with highest value:
        int yearWithHighestValue = yearToMaxValue.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(startYear);
        
        result.setHighestOverallYear(yearWithHighestValue, yearToMaxValue.get(yearWithHighestValue));
        
        return result;
    }
    
    @Override
    public String getStatisticsName() {
        return "Pollution Extremes";
    }
    
    /**
     * Find the data point with the maximum value.
     * @param dataPoints List of data points.
     * @return The data point with the highest value.
     */
    private DataPoint findMaxDataPoint(List<DataPoint> dataPoints) {
        return dataPoints.stream()
            .filter(dp -> dp.value() >= 0) // Filter out invalid values.
            .max(Comparator.comparingDouble(DataPoint::value))
            .orElse(null);
    }
    
    /**
     * Find the data point with the minimum value.
     * @param dataPoints List of data points.
     * @return The data point with the lowest value.
     */
    private DataPoint findMinDataPoint(List<DataPoint> dataPoints) {
        return dataPoints.stream()
            .filter(dp -> dp.value() >= 0) // Filter out invalid values.
            .min(Comparator.comparingDouble(DataPoint::value))
            .orElse(null);
    }
    
    /**
     * Find the top N data points with highest values.
     * @param dataPoints List of data points.
     * @param count Number of top points to find.
     * @return List of the top N data points.
     */
    private List<DataPoint> findTopHotspots(List<DataPoint> dataPoints, int count) {
        return dataPoints.stream()
            .filter(dp -> dp.value() >= 0) // Filter out missing values.
            .sorted(Comparator.comparingDouble(DataPoint::value).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate a percentile value from the data points.
     * @param dataPoints List of data points.
     * @param percentile The percentile to calculate (0 - 100).
     * @return The calculated percentile value.
     */
    private double calculatePercentile(List<DataPoint> dataPoints, int percentile) {
        List<Double> sortedValues = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out missing values.
            .sorted()
            .boxed()
            .collect(Collectors.toList());
        
        if (sortedValues.isEmpty()) return 0.0;
        
        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size()) - 1;
        index = Math.max(0, Math.min(index, sortedValues.size() - 1));
        return sortedValues.get(index);
    }
}