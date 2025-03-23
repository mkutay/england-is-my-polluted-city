package statistics.back.calculators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;
import statistics.types.HistogramResult;

public class HistogramCalculator implements StatisticsCalculator {
    private static final int DEFAULT_HISTOGRAM_BINS = 10;

    private final DataManager dataManager;
    
    /**
     * Constructor.
     */
    public HistogramCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        HistogramResult result = new HistogramResult(
            "Pollution Histogram Analysis", 
            "Histogram distribution analysis for " + pollutant.getDisplayName() + " from " + startYear + " to " + endYear,
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
        
        calculateHistogram(allValues, DEFAULT_HISTOGRAM_BINS, result);
        
        return result;
    }

    /**
     * Calculate histogram data for a list of values.
     * @param values List of values.
     * @param numBins Number of bins for the histogram.
     * @param result HistogramResult object to store the information in.
     */
    private void calculateHistogram(List<Double> values, int numBins, HistogramResult result) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Values list cannot be empty.");
        }
        
        double minValue = Collections.min(values);
        double maxValue = Collections.max(values);
        double binWidth = (maxValue - minValue) / numBins;
        
        double[] binEdges = new double[numBins + 1];
        long[] binCounts = new long[numBins];
        
        // Create bin edges.
        for (int i = 0; i <= numBins; i++) {
            binEdges[i] = minValue + i * binWidth;
        }
        
        // Count values in each bin.
        for (double value : values) {
            int binIndex = (int) ((value - minValue) / binWidth);
            // Handle edge case for maximum value.
            if (binIndex == numBins) binIndex--;
            binCounts[binIndex]++;
        }
        
        long minCount = Arrays.stream(binCounts).min().orElse(Integer.MIN_VALUE);
        long maxCount = Arrays.stream(binCounts).max().orElse(Integer.MAX_VALUE);
        
        double[] binCountsLog = new double[numBins];
        
        for (int i = 0; i < numBins; i++) {
            binCountsLog[i] = Math.log10(binCounts[i] + 1);
        }
        
        result.setMinValue(minValue);
        result.setMaxValue(maxValue);
        result.setBinEdges(binEdges);
        result.setBinCounts(binCounts);
        result.setMinCount(minCount);
        result.setMaxCount(maxCount);
        result.setBinCountsLog(binCountsLog);
    }

    @Override
    public String getStatisticsName() {
        return "Histogram";
    }
}
