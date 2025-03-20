package test.statistics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import statistics.back.pollutionExtremes.PollutionExtremesCalculator;
import statistics.back.pollutionExtremes.PollutionExtremesResult;

/**
 * Test class for the PollutionExtremesCalculator class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class PollutionExtremesCalculatorTest {
    private final static Pollutant POLLUTANT = Pollutant.NO2;
    private final static int START_YEAR = 2018;
    private final static int END_YEAR = 2023;

    private PollutionExtremesCalculator calculator;
    private DataManager dataManager;
    private PollutionExtremesResult result;
    
    @BeforeEach
    public void setUp() {
        calculator = new PollutionExtremesCalculator();
        dataManager = DataManager.getInstance();

        result = assertDoesNotThrow(() -> {
            return (PollutionExtremesResult) calculator.calculateStatisticsOverTime(POLLUTANT, START_YEAR, END_YEAR);
        });
    }

    @Test
    public void testCalculateStatisticsOverTime() {
        assertNotNull(result);
        assertEquals("Pollution Hotspots Trends", result.getTitle());
        assertTrue(result.getDescription().contains(POLLUTANT.getDisplayName()));
        assertTrue(result.getDescription().contains(Integer.toString(START_YEAR)));
        assertTrue(result.getDescription().contains(Integer.toString(END_YEAR)));
        assertEquals(POLLUTANT, result.getPollutant());
    }

    @Test
    public void testCalculateStatisticsOverTime_YearlyMaxPoints() {
        assertTrue(result.getYearToMaxPoints().containsKey(START_YEAR));

        List<DataPoint> dataPoints = dataManager.getPollutantData(START_YEAR, POLLUTANT).getData();
        DataPoint maxPoint = null;
        for (DataPoint point : dataPoints) {
            if (point.value() >= 0) {
                if (maxPoint == null || point.value() > maxPoint.value()) {
                    maxPoint = point;
                }
            }
        }
        assertEquals(maxPoint, result.getYearToMaxPoints().get(START_YEAR));

        assertTrue(result.getYearToMaxPoints().containsKey(END_YEAR));

        dataPoints = dataManager.getPollutantData(END_YEAR, POLLUTANT).getData();
        maxPoint = null;
        for (DataPoint point : dataPoints) {
            if (point.value() >= 0) {
                if (maxPoint == null || point.value() > maxPoint.value()) {
                    maxPoint = point;
                }
            }
        }
        assertEquals(maxPoint, result.getYearToMaxPoints().get(END_YEAR));
    }

    @Test
    public void testCalculateStatisticsOverTime_YearlyMinPoints() {
        assertTrue(result.getYearToMinPoints().containsKey(START_YEAR));

        List<DataPoint> dataPoints = dataManager.getPollutantData(START_YEAR, POLLUTANT).getData();
        DataPoint minPoint = null;
        for (DataPoint point : dataPoints) {
            if (point.value() >= 0) {
                if (minPoint == null || point.value() < minPoint.value()) {
                    minPoint = point;
                }
            }
        }
        assertEquals(minPoint, result.getYearToMinPoints().get(START_YEAR));

        assertTrue(result.getYearToMinPoints().containsKey(END_YEAR));

        dataPoints = dataManager.getPollutantData(END_YEAR, POLLUTANT).getData();
        minPoint = null;
        for (DataPoint point : dataPoints) {
            if (point.value() >= 0) {
                if (minPoint == null || point.value() < minPoint.value()) {
                    minPoint = point;
                }
            }
        }
        assertEquals(minPoint, result.getYearToMinPoints().get(END_YEAR));
    }

    @Test
    public void testCalculateStatisticsOverTime_MaxYear() {
        Map<Integer, DataPoint> yearToMaxPoints = result.getYearToMaxPoints();
        Map.Entry<Integer, DataPoint> maxYear = null;
        for (Map.Entry<Integer, DataPoint> entry : yearToMaxPoints.entrySet()) {
            if (maxYear == null || entry.getValue().value() > maxYear.getValue().value()) {
                maxYear = entry;
            }
        }
        assertEquals(maxYear.getKey(), result.getMaxYear().getKey());
        assertEquals(maxYear.getValue(), result.getMaxYear().getValue());
    }
}