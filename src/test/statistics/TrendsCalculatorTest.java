package test.statistics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.trends.TrendsCalculator;
import statistics.back.trends.TrendsResult;

/**
 * Test class for the TrendsCalculator class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class TrendsCalculatorTest {
    private final static Pollutant POLLUTANT = Pollutant.NO2;
    private final static int START_YEAR = 2018;
    private final static int END_YEAR = 2023;
    private final static double EPSILON = 1e-6; // Tolerance for double comparisons.

    private TrendsCalculator calculator;
    private DataManager dataManager;
    private TrendsResult result;
    
    @BeforeEach
    public void setUp() {
        calculator = new TrendsCalculator();
        dataManager = DataManager.getInstance();

        result = assertDoesNotThrow(() -> {
            return (TrendsResult) calculator.calculateStatisticsOverTime(POLLUTANT, START_YEAR, END_YEAR);
        });
    }

    @Test
    public void testCalculateStatisticsOverTime() {
        assertNotNull(result);
        assertEquals("Average Pollution Trends", result.getTitle());
        assertTrue(result.getDescription().contains(POLLUTANT.getDisplayName()));
        assertTrue(result.getDescription().contains(Integer.toString(START_YEAR)));
        assertTrue(result.getDescription().contains(Integer.toString(END_YEAR)));
        assertEquals(POLLUTANT, result.getPollutant());
    }

    @Test
    public void testCalculateStatisticsOverTime_YearlyMean() {
        assertTrue(result.getYearlyMeans().containsKey(START_YEAR));

        List<DataPoint> dataPoints = dataManager.getPollutantData(START_YEAR, POLLUTANT).getData();
        double mean = dataPoints.stream().mapToDouble(DataPoint::value).filter(val -> val >= 0).average().orElse(0.0);
        assertEquals(mean, result.getYearlyMeans().get(START_YEAR));

        assertTrue(result.getYearlyMeans().containsKey(END_YEAR));

        dataPoints = dataManager.getPollutantData(END_YEAR, POLLUTANT).getData();
        mean = dataPoints.stream().mapToDouble(DataPoint::value).filter(val -> val >= 0).average().orElse(0.0);
        assertEquals(mean, result.getYearlyMeans().get(END_YEAR));
    }

    @Test
    public void testCalculateStatisticsOverTime_Mean() {
        Map<Integer, Double> yearlyMeans = result.getYearlyMeans();

        double avg = 0;
        for (double value : yearlyMeans.values()) {
            avg += value;
        }
        avg /= yearlyMeans.size();
        assertTrue(Math.abs(avg - result.getMean()) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_Median() {
        Map<Integer, Double> yearlyMedians = result.getYearlyMedians();

        double avg = 0;
        for (double value : yearlyMedians.values()) {
            avg += value;
        }
        avg /= yearlyMedians.size();
        assertTrue(Math.abs(avg - result.getMedian()) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_StandardDeviation() {
        Map<Integer, Double> yearlyStandardDeviations = result.getYearlyStandardDeviations();

        double avg = 0;
        for (double value : yearlyStandardDeviations.values()) {
            avg += value;
        }
        avg /= yearlyStandardDeviations.size();
        assertTrue(Math.abs(avg - result.getStandardDeviation()) < EPSILON);
    }
}