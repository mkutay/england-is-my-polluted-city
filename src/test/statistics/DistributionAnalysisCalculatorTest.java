package test.statistics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.distribution.DistributionAnalysisCalculator;
import statistics.back.distribution.DistributionAnalysisResult;

/**
 * Test class for the DistributionAnalysisCalculator class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class DistributionAnalysisCalculatorTest {
    private final static Pollutant POLLUTANT = Pollutant.NO2;
    private final static int START_YEAR = 2018;
    private final static int END_YEAR = 2023;
    private final static double EPSILON = 1e-3;

    private DistributionAnalysisCalculator calculator;
    private DistributionAnalysisResult result;
    private DataManager dataManager;
    
    @BeforeEach
    public void setUp() {
        calculator = new DistributionAnalysisCalculator();
        dataManager = DataManager.getInstance();

        result = assertDoesNotThrow(() -> {
            return (DistributionAnalysisResult) calculator.calculateStatisticsOverTime(POLLUTANT, START_YEAR, END_YEAR);
        });
    }

    @Test
    public void testCalculateStatisticsOverTime() {
        assertNotNull(result);
        assertEquals("Pollution Distribution Analysis", result.getTitle());
        assertTrue(result.getDescription().contains(POLLUTANT.getDisplayName()));
        assertTrue(result.getDescription().contains(Integer.toString(START_YEAR)));
        assertTrue(result.getDescription().contains(Integer.toString(END_YEAR)));
        assertEquals(POLLUTANT, result.getPollutant());
    }
    
    @Test
    public void testCalculateStatisticsOverTime_Percentiles() {
        Map<Double, Double> percentiles = result.getPercentiles();
        
        assertNotNull(percentiles, "Percentiles should not be null.");
        assertFalse(percentiles.isEmpty(), "Percentiles should not be empty.");
        
        // Check if all expected percentiles are present.
        double[] expectedPercentiles = { 10, 25, 50, 75, 90, 95, 99 };
        for (double percentile : expectedPercentiles) {
            assertTrue(percentiles.containsKey(percentile), 
                "Percentiles should contain " + percentile + "th percentile.");
        }
        
        // Check that percentiles are in ascending order.
        Double previousValue = null;
        for (double percentile : expectedPercentiles) {
            Double currentValue = percentiles.get(percentile);
            if (previousValue != null) {
                assertTrue(currentValue >= previousValue, 
                    "Percentile values should be in ascending order.");
            }
            previousValue = currentValue;
        }
    }
    
    @Test
    public void testCalculateStatisticsOverTime_YearlyPercentiles() {
        Map<Integer, Map<Double, Double>> yearlyPercentiles = result.getYearlyPercentiles();
        
        assertNotNull(yearlyPercentiles, "Yearly percentiles should not be null.");
        
        // Check if all years are present.
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            assertTrue(yearlyPercentiles.containsKey(year), 
                "Yearly percentiles should contain year " + year);
            
            Map<Double, Double> yearPercentiles = yearlyPercentiles.get(year);
            assertNotNull(yearPercentiles, "Percentiles for year " + year + " should not be null.");
            
            // Check if all expected percentiles are present for each year.
            double[] expectedPercentiles = { 10, 25, 50, 75, 90, 95, 99 };
            for (double percentile : expectedPercentiles) {
                assertTrue(yearPercentiles.containsKey(percentile), 
                    "Year " + year + " percentiles should contain " + percentile + "th percentile.");
            }
            
            // Check that percentiles are in ascending order for each year.
            Double previousValue = null;
            for (double percentile : expectedPercentiles) {
                Double currentValue = yearPercentiles.get(percentile);
                if (previousValue != null) {
                    assertTrue(currentValue >= previousValue, 
                        "Year " + year + " percentile values should be in ascending order");
                }
                previousValue = currentValue;
            }

            List<Double> values = dataManager.getPollutantData(year, POLLUTANT).getData().stream()
                .mapToDouble(DataPoint::value)
                .filter(val -> val >= 0) // Filter out invalid values.
                .boxed()
                .collect(Collectors.toList());
            
            Collections.sort(values);
            for (double percentile : expectedPercentiles) {
                int index = (int) Math.round(percentile / 100d * (values.size() - 1));
                double expectedValue = values.get(index);
                assertEquals(expectedValue, yearPercentiles.get(percentile), EPSILON, 
                    "Year " + year + " percentile value should match the calculated value: " + expectedValue + ", " + yearPercentiles.get(percentile));
            }
        }
    }

    @Test
    public void testCalculateStatisticsOverTime_YearlySkewness() {
        Map<Integer, Double> yearlySkewness = result.getYearlySkewness();
        
        assertNotNull(yearlySkewness, "Yearly skewness should not be null.");
        
        // Check if all years are present.
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            assertTrue(yearlySkewness.containsKey(year), 
                "Yearly skewness should contain year " + year);
            
            // We say skewness values are typically between -4 and 4 for most real-world distributions.
            double skewness = yearlySkewness.get(year);
            assertTrue(Double.isFinite(skewness), 
                "Skewness for year " + year + " should be a finite number.");
            
            assertTrue(skewness >= -4 && skewness <= 4,
                "Skewness for year " + year + " should be between -4 and 4.");
        }
    }
    
    @Test
    public void testCalculateStatisticsOverTime_YearlyKurtosis() {
        Map<Integer, Double> yearlyKurtosis = result.getYearlyKurtosis();
        
        assertNotNull(yearlyKurtosis, "Yearly kurtosis should not be null.");
        
        // Check if all years are present.
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            assertTrue(yearlyKurtosis.containsKey(year), 
                "Yearly kurtosis should contain year " + year);
            
            // Kurtosis values should be finite.
            double kurtosis = yearlyKurtosis.get(year);
            assertTrue(Double.isFinite(kurtosis), 
                "Kurtosis for year " + year + " should be a finite number");
        }
    }
    
    @Test
    public void testGetYearlyPercentile_Median() {
        double percentileToTest = 50.0; // Testing median (50th percentile).
        Map<Integer, Double> yearlyMedian = result.getYearlyPercentile(percentileToTest);
        
        assertNotNull(yearlyMedian, "Yearly median should not be null.");
        
        // Check if all years are present
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            assertTrue(yearlyMedian.containsKey(year), 
                "Yearly median should contain year " + year);
            
            // Verify the yearly percentile method returns the same value as accessing the full map.
            Double expectedMedian = result.getYearlyPercentiles().get(year).get(percentileToTest);
            assertEquals(expectedMedian, yearlyMedian.get(year), 
                "Yearly median value should match the value in the yearly percentiles map.");
        }
    }
}