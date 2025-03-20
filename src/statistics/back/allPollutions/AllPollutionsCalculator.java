package statistics.back.allPollutions;

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
 * Calculator for all pollutants.
 * Calculates statistics for all pollutants over a given time range.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AllPollutionsCalculator extends StatisticsCalculator {
    private final DataManager dataManager;
    
    /**
     * Constructor.
     */
    public AllPollutionsCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        AllPollutionsResult result = new AllPollutionsResult(
            "Average Pollution Trends", 
            "Trend analysis of average pollution levels from "
            + startYear + " to " + endYear
        );

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Pollutant p : Pollutant.values()) {
            final Pollutant finalPollutant = p;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                calculateForPollutant(finalPollutant, startYear, endYear, result);
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Double totalMean = result.getMean().values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
        
        result.setTotalMean(totalMean);

        Double totalMedian = result.getMedian().values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
        
        result.setTotalMedian(totalMedian);

        Double totalStandardDeviation = result.getStandardDeviation().values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        result.setTotalStandardDeviation(totalStandardDeviation);

        return result;
    }

    private void calculateForPollutant(Pollutant pollutant, int startYear, int endYear, AllPollutionsResult result) {
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

        result.setMean(pollutant, mean);

        double median = yearlyMedians
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        result.setMedian(pollutant, median);

        double standardDeviation = yearlyStandardDeviations
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        result.setStandardDeviation(pollutant, standardDeviation);

        result.setYearlyMeans(pollutant, yearlyMeans);
        result.setYearlyMedians(pollutant, yearlyMedians);
        result.setYearlyStandardDeviations(pollutant, yearlyStandardDeviations);

        double[] trendCoefficients = calculateLinearTrend(yearlyMeans, startYear, endYear);
        result.setTrendCoefficients(pollutant, trendCoefficients[0], trendCoefficients[1]);

        double startValue = yearlyMeans.get(startYear);
        double endValue = yearlyMeans.get(endYear);
        
        if (startValue > 0) {
            double percentChange = ((endValue - startValue) / startValue) * 100.0;
            result.setPercentChange(pollutant, percentChange);
        }
    }
    
    @Override
    public String getStatisticsName() {
        return "All Pollution Trends";
    }
}
