package test.statistics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.histogram.HistogramCalculator;
import statistics.back.histogram.HistogramResult;

/**
 * Test class for the HistogramCalculator class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class HistogramCalculatorTest {
    private final static Pollutant POLLUTANT = Pollutant.NO2;
    private final static int START_YEAR = 2018;
    private final static int END_YEAR = 2023;
    private final static double EPSILON = 1e-3;

    private HistogramCalculator calculator;
    private HistogramResult result;
    private DataManager dataManager;
    
    @BeforeEach
    public void setUp() {
        calculator = new HistogramCalculator();
        dataManager = DataManager.getInstance();

        result = assertDoesNotThrow(() -> {
            return (HistogramResult) calculator.calculateStatisticsOverTime(POLLUTANT, START_YEAR, END_YEAR);
        });
    }

    @Test
    public void testCalculateStatisticsOverTime() {
        assertNotNull(result);
        assertEquals("Pollution Histogram Analysis", result.getTitle());
        assertTrue(result.getDescription().contains(POLLUTANT.getDisplayName()));
        assertTrue(result.getDescription().contains(Integer.toString(START_YEAR)));
        assertTrue(result.getDescription().contains(Integer.toString(END_YEAR)));
        assertEquals(POLLUTANT, result.getPollutant());
    }
    
    @Test
    public void testBinEdges() {
        double[] binEdges = result.getBinEdges();
        assertNotNull(binEdges, "Bin edges should not be null.");
        assertTrue(binEdges.length > 1, "There should be at least 2 bin edges.");
        
        for (int i = 1; i < binEdges.length; i++) {
            assertTrue(binEdges[i] > binEdges[i - 1], "Bin edges should be in ascending order.");
        }
        
        assertEquals(result.getMinValue(), binEdges[0], EPSILON, "First bin edge should be the minimum value.");
        assertEquals(result.getMaxValue(), binEdges[binEdges.length - 1], EPSILON, "Last bin edge should be the maximum value.");
    }
    
    @Test
    public void testBinCounts() {
        long[] binCounts = result.getBinCounts();
        assertNotNull(binCounts, "Bin counts should not be null.");
        
        assertEquals(result.getBinEdges().length - 1, binCounts.length, "Number of bin counts should be one less than number of bin edges.");
        
        for (long count : binCounts) {
            assertTrue(count >= 0, "Bin count should be non-negative");
        }
        
        int totalDataPoints = 0;
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            totalDataPoints += dataManager.getPollutantData(year, POLLUTANT).getData().stream()
                .filter(point -> point.value() >= 0)
                .count();
        }
        
        assertEquals(totalDataPoints, Arrays.stream(binCounts).sum(), "Sum of bin counts should equal total number of data points.");
    }
    
    @Test
    public void testMinMaxValues() {
        double minValue = result.getMinValue();
        double maxValue = result.getMaxValue();
        
        assertTrue(minValue <= maxValue, "Min value should be less than or equal to max value.");
        
        // Calculate actual min and max from the raw data.
        double actualMin = Double.MAX_VALUE;
        double actualMax = Double.MIN_VALUE;
        
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            List<Double> values = dataManager.getPollutantData(year, POLLUTANT).getData().stream()
                .mapToDouble(DataPoint::value)
                .filter(val -> val >= 0)
                .boxed()
                .collect(Collectors.toList());
            
            if (!values.isEmpty()) {
                double yearMin = values.stream().min(Double::compare).get();
                double yearMax = values.stream().max(Double::compare).get();
                
                if (yearMin < actualMin) actualMin = yearMin;
                if (yearMax > actualMax) actualMax = yearMax;
            }
        }
        
        assertEquals(actualMin, minValue, EPSILON, "Minimum value should match the calculated minimum.");
        assertEquals(actualMax, maxValue, EPSILON, "Maximum value should match the calculated maximum.");
    }
    
    @Test
    public void testMinMaxCounts() {
        long minCount = result.getMinCount();
        long maxCount = result.getMaxCount();
        
        assertTrue(minCount <= maxCount, "Min count should be less than or equal to max count.");
        
        long[] binCounts = result.getBinCounts();
        
        long actualMin = Arrays.stream(binCounts).min().getAsLong();
        long actualMax = Arrays.stream(binCounts).max().getAsLong();
        
        assertEquals(actualMin, minCount, "Minimum count should match the actual minimum bin count.");
        assertEquals(actualMax, maxCount, "Maximum count should match the actual maximum bin count.");
    }
    
    @Test
    public void testBinCountsLog() {
        double[] binCountsLog = result.getBinCountsLog();
        long[] binCounts = result.getBinCounts();
        
        assertNotNull(binCountsLog, "Bin counts log should not be null.");
        assertEquals(binCounts.length, binCountsLog.length, "Log counts array should have same length as counts array.");
        
        for (int i = 0; i < binCounts.length; i++) {
            double expectedLog = Math.log10(binCounts[i] + 1);
            assertEquals(expectedLog, binCountsLog[i], EPSILON, 
                "Log transformation at index " + i + " should be log10(count + 1).");
        }
    }
    
    @Test
    public void testBinWidth() {
        double[] binEdges = result.getBinEdges();
        double expectedBinWidth = (result.getMaxValue() - result.getMinValue()) / (binEdges.length - 1);
        
        // Check that all bins have roughly the same width.
        for (int i = 1; i < binEdges.length; i++) {
            double binWidth = binEdges[i] - binEdges[i - 1];
            assertEquals(expectedBinWidth, binWidth, EPSILON, 
                "Bin width should be consistent between all bins.");
        }
    }
    
    @Test
    public void testDataInBins() {
        double[] binEdges = result.getBinEdges();
        long[] binCounts = result.getBinCounts();
        
        List<Double> allValues = new ArrayList<>();
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            List<Double> values = dataManager.getPollutantData(year, POLLUTANT).getData().stream()
                .mapToDouble(DataPoint::value)
                .filter(val -> val >= 0)
                .boxed()
                .collect(Collectors.toList());
            allValues.addAll(values);
        }
        
        // Count data points in each bin manually and compare with the calculated bin counts.
        long[] manualBinCounts = new long[binEdges.length - 1];
        double binWidth = (result.getMaxValue() - result.getMinValue()) / (binEdges.length - 1);
        
        for (double value : allValues) {
            int binIndex = (int) ((value - result.getMinValue()) / binWidth);
            // Handle edge case for maximum value.
            if (binIndex == binEdges.length - 1) binIndex--;
            manualBinCounts[binIndex]++;
        }
        
        assertArrayEquals(manualBinCounts, binCounts, 
            "Manually counted bin counts should match the calculated bin counts.");
    }
}
