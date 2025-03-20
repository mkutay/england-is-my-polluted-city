package statistics.back.trends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

/**
 * Calculator for trends analysis.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class TrendsCalculator extends StatisticsCalculator {
    private final DataManager dataManager;
    
    /**
     * Constructor.
     */
    public TrendsCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        TrendsResult result = new TrendsResult(
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
            .orElse(0.0);

        result.setMean(mean);

        double median = yearlyMedians
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        result.setMedian(median);

        double standardDeviation = yearlyStandardDeviations
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        result.setStandardDeviation(standardDeviation);

        result.setYearlyMeans(yearlyMeans);
        result.setYearlyMedians(yearlyMedians);
        result.setYearlyStandardDeviations(yearlyStandardDeviations);

        double[] trendCoefficients = calculateLinearTrend(yearlyMeans, startYear, endYear);
        result.setTrendCoefficients(trendCoefficients[0], trendCoefficients[1]);

        double startValue = yearlyMeans.get(startYear);
        double endValue = yearlyMeans.get(endYear);
        
        if (startValue > 0) {
            double percentChange = ((endValue - startValue) / startValue) * 100.0;
            result.setPercentChange(percentChange);
        }
        
        return result;
    }
    
    @Override
    public String getStatisticsName() {
        return "Trends";
    }
}
