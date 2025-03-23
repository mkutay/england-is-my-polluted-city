package test.statistics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.calculators.AllPollutionsCalculator;
import statistics.types.AllPollutionsResult;

/**
 * Test class for the AllPollutionCalculator class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class AllPollutionCalculatorTest {
    private final static Pollutant POLLUTANT = Pollutant.NO2;
    private final static int START_YEAR = 2018;
    private final static int END_YEAR = 2023;
    private final static double EPSILON = 1e-6; // Tolerance for double comparisons.

    private AllPollutionsCalculator calculator;
    private DataManager dataManager;
    private AllPollutionsResult result;
    
    @BeforeEach
    public void setUp() {
        calculator = new AllPollutionsCalculator();
        dataManager = DataManager.getInstance();

        result = assertDoesNotThrow(() -> {
            return (AllPollutionsResult) calculator.calculateStatisticsOverTime(POLLUTANT, START_YEAR, END_YEAR);
        });
    }

    @Test
    public void testCalculateStatisticsOverTime() {
        assertNotNull(result);
        assertEquals("Average Pollution Trends", result.getTitle());
        assertTrue(result.getDescription().contains(Integer.toString(START_YEAR)));
        assertTrue(result.getDescription().contains(Integer.toString(END_YEAR)));
    }

    @Test
    public void testCalculateStatisticsOverTime_YearlyMean() {
        Map<Integer, Double> yearlyMeans = result.getTrends().get(POLLUTANT).getYearlyMeans();
        assertTrue(yearlyMeans.containsKey(START_YEAR));

        List<DataPoint> dataPoints = dataManager.getPollutantData(START_YEAR, POLLUTANT).getData();
        double total = 0;
        double count = 0;
        for (DataPoint dataPoint : dataPoints) {
            if (dataPoint.value() > 0) {
                total += dataPoint.value();
                count++;
            }
        }
        double mean = total / count;
        assertTrue(Math.abs(mean - yearlyMeans.get(START_YEAR)) < EPSILON);

        assertTrue(yearlyMeans.containsKey(END_YEAR));
        dataPoints = dataManager.getPollutantData(END_YEAR, POLLUTANT).getData();

        total = 0;
        count = 0;
        for (DataPoint dataPoint : dataPoints) {
            if (dataPoint.value() > 0) {
                total += dataPoint.value();
                count++;
            }
        }
        mean = total / count;
        assertTrue(Math.abs(mean - yearlyMeans.get(END_YEAR)) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_Mean() {
        Map<Pollutant, Double> mean = result.getTrends()
            .values()
            .stream()
            .collect(Collectors.toMap(t -> t.getPollutant(), t -> t.getMean()));

        double avg = 0;
        for (double value : mean.values()) {
            avg += value;
        }
        avg /= mean.size();
        assertTrue(Math.abs(avg - result.getTotalMean()) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_Median() {
        Map<Pollutant, Double> median = result.getTrends()
            .values()
            .stream()
            .collect(Collectors.toMap(t -> t.getPollutant(), t -> t.getMedian()));

        double avg = 0;
        for (double value : median.values()) {
            avg += value;
        }
        avg /= median.size();
        assertTrue(Math.abs(avg - result.getTotalMedian()) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_StandardDeviation() {
        Map<Pollutant, Double> standardDeviations = result.getTrends()
            .values()
            .stream()
            .collect(Collectors.toMap(t -> t.getPollutant(), t -> t.getStandardDeviation()));

        double avg = 0;
        for (double value : standardDeviations.values()) {
            avg += value;
        }
        avg /= standardDeviations.size();
        assertTrue(Math.abs(avg - result.getTotalStandardDeviation()) < EPSILON);
    }

    @Test
    public void testCalculateStatisticsOverTime_AllPollutants() {
        for (Pollutant p : Pollutant.values()) {
            assertTrue(result.getTrends().containsKey(p));
        }
    }
}