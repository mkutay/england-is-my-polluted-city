package statistics.back.calculators;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;
import statistics.types.PollutionExtremesResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Calculator for finding maximum pollution values and hotspots, i.e. the extremes.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class PollutionExtremesCalculator implements StatisticsCalculator {
    private final DataManager dataManager; // Data manager instance.
    
    /**
     * Constructor.
     */
    public PollutionExtremesCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        PollutionExtremesResult result = new PollutionExtremesResult(
            "Pollution Hotspots Trends", 
            "Trend analysis of pollution hotspots for " + pollutant.getDisplayName() + " from " + startYear + " to " + endYear,
            pollutant
        );
        
        Map<Integer, DataPoint> yearToMaxPoint = new ConcurrentHashMap<>();
        Map<Integer, DataPoint> yearToMinPoint = new ConcurrentHashMap<>();
        Map<Integer, DataPoint> yearToMedianPoint = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // Calculate for each year in parallel:
        for (int year = startYear; year <= endYear; year++) {
            final int finalYear = year;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                DataSet dataSet = dataManager.getPollutantData(finalYear, pollutant);
                List<DataPoint> dataPoints = dataSet.getData();
                
                DataPoint maxPoint = findMaxDataPoint(dataPoints);
                if (maxPoint != null) {
                    yearToMaxPoint.put(finalYear, maxPoint);
                }

                DataPoint minPoint = findMinDataPoint(dataPoints);
                if (minPoint != null) {
                    yearToMinPoint.put(finalYear, minPoint);
                }

                DataPoint medianPoint = findMedianDataPoint(dataPoints);
                if (medianPoint != null) {
                    yearToMedianPoint.put(finalYear, medianPoint);
                }
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        result.setYearlyMaxPoints(yearToMaxPoint);
        result.setYearlyMinPoints(yearToMinPoint);
        result.setYearlyMedianPoints(yearToMedianPoint);
        
        int yearWithHighestValue = yearToMaxPoint.entrySet().stream()
            .max(Map.Entry.comparingByValue(Comparator.comparingDouble(DataPoint::value)))
            .map(Map.Entry::getKey)
            .orElse(startYear);
        
        result.setMaxYear(yearWithHighestValue, yearToMaxPoint.get(yearWithHighestValue));

        int yearWithLowestValue = yearToMinPoint.entrySet().stream()
            .min(Map.Entry.comparingByValue(Comparator.comparingDouble(DataPoint::value)))
            .map(Map.Entry::getKey)
            .orElse(startYear);

        result.setMinYear(yearWithLowestValue, yearToMinPoint.get(yearWithLowestValue));

        DataPoint medianPoint = findMedianDataPoint(yearToMedianPoint.values().stream().collect(Collectors.toList()));
        if (medianPoint != null) {
            int medianYear = yearToMedianPoint.entrySet().stream()
                .filter(e -> e.getValue().equals(medianPoint))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(startYear);

            result.setMedianYear(medianYear, medianPoint);
        }
        
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
     * Find the median data point.
     * @param dataPoints List of data points.
     * @return The median data point.
     */
    private DataPoint findMedianDataPoint(List<DataPoint> dataPoints) {
        List<DataPoint> sortedDataPoints = dataPoints.stream()
            .filter(dp -> dp.value() >= 0) // Filter out invalid values.
            .sorted(Comparator.comparingDouble(DataPoint::value))
            .collect(Collectors.toList());

        int size = sortedDataPoints.size();
        if (size == 0) {
            return null;
        }

        int mid = size / 2;
        return sortedDataPoints.get(mid);
    }
}