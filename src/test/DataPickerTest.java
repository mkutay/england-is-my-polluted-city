package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataPicker;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import java.time.Year;
import java.util.List;

/**
 * Test class for the DataPicker class. Tests the public methods of the class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class DataPickerTest {
    private DataPicker dataPicker;

    private static final Pollutant TEST_POLLUTANT = Pollutant.NO2;

    @BeforeEach
    public void setUp() {
        dataPicker = new DataPicker();
    }

    @Test
    public void testGetAvailableYears() {
        List<Integer> years = dataPicker.getAvailableYears(TEST_POLLUTANT);
        
        assertNotNull(years, "Available years should not be null.");
        assertFalse(years.isEmpty(), "Available years should not be empty for " + TEST_POLLUTANT);
        
        // Check if available years are reasonable (between 2000 and current year).
        for (Integer year : years) {
            assertTrue(year >= 2000 && year <= Year.now().getValue(), "Year " + year + " should be in a reasonable range.");
        }
    }

    @Test
    public void testGetAvailableYearsCache() {
        List<Integer> years1 = dataPicker.getAvailableYears(TEST_POLLUTANT);
        List<Integer> years2 = dataPicker.getAvailableYears(TEST_POLLUTANT);
        
        assertSame(years1, years2, "Second call should return cached list.");
    }

    @Test
    public void testGetPollutantData() {
        List<Integer> years = dataPicker.getAvailableYears(TEST_POLLUTANT);
        assertFalse(years.isEmpty(), "Need at least one year to test with.");
        
        int validYear = years.get(0);
        
        DataSet dataSet = dataPicker.getPollutantData(validYear, TEST_POLLUTANT);
        
        assertNotNull(dataSet, "DataSet should not be null for valid year and pollutant.");
        assertFalse(dataSet.getData().isEmpty(), "Data inside DataSet should not be empty for valid year and pollutant.");
    }

    @Test
    public void testGetPollutantDataWithInvalidYear() {
        int invalidYear = 9999;
        
        List<Integer> years = dataPicker.getAvailableYears(TEST_POLLUTANT);
        assertFalse(years.contains(invalidYear), "Year " + invalidYear + " should be invalid for testing.");
        
        assertThrows(IllegalArgumentException.class, () -> dataPicker.getPollutantData(invalidYear, TEST_POLLUTANT), "Should throw IllegalArgumentException for invalid year.");
    }
}
