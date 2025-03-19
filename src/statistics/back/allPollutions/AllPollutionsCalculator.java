package statistics.back.allPollutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

public class AllPollutionsCalculator implements StatisticsCalculator {
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
     * Calculate linear regression coefficients for trend analysis.
     * @param yearlyValues Map of year to value.
     * @param startYear First year in the range.
     * @param endYear Last year in the range.
     * @return Array with [slope, intercept].
     */
    private double[] calculateLinearTrend(Map<Integer, Double> yearlyValues, int startYear, int endYear) {
        int n = endYear - startYear + 1;
        
        if (n <= 1) {
            return new double[] { 0.0, 0.0 };
        }
        
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        
        for (int year = startYear; year <= endYear; year++) {
            double x = year;
            double y = yearlyValues.getOrDefault(year, 0.0);
            
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }
        
        // Calculate slope and intercept.
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        return new double[] { slope, intercept };
    }
}
