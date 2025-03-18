package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Test class for the DataManager class. Tests the public methods of the class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
class DataManagerTest {
    private DataManager dataManager;
    
    private static final int TEST_YEAR = 2020;
    private static final Pollutant TEST_POLLUTANT = Pollutant.NO2;

    @BeforeEach
    public void setUp() {
        dataManager = DataManager.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(dataManager, "Instance should not be null.");
        assertTrue(dataManager == DataManager.getInstance(), "Should return the same instance.");
    }

    @Test
    public void testGetPollutantData() {
        DataSet dataSet1 = dataManager.getPollutantData(TEST_YEAR, TEST_POLLUTANT);
        assertNotNull(dataSet1, "Should return a non-null dataset.");
        
        DataSet dataSet2 = dataManager.getPollutantData(TEST_YEAR, TEST_POLLUTANT);
        assertSame(dataSet1, dataSet2, "Should return the same cached data object.");
    }

    @Test
    public void testGetAvailableYears() {
        List<Integer> years = dataManager.getAvailableYears(TEST_POLLUTANT);
        assertNotNull(years, "Should return a non-null list of years.");
    }

    @Test
    public void testGetPollutantDataAsync() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<DataSet> future = dataManager.getPollutantDataAsync(TEST_YEAR, TEST_POLLUTANT);
        DataSet dataSet = future.get(5, TimeUnit.SECONDS); // Wait up to 5 seconds.
        
        assertNotNull(dataSet, "Async data loading should return a non-null dataset.");
        assertTrue(dataManager.isDataCached(TEST_POLLUTANT, TEST_YEAR), "Data should be cached after async loading.");
    }

    @Test
    public void testIsDataCached() {
        assertFalse(dataManager.isDataCached(TEST_POLLUTANT, TEST_YEAR), "Data should not be cached initially.");
        
        dataManager.getPollutantData(TEST_YEAR, TEST_POLLUTANT);
        assertTrue(dataManager.isDataCached(TEST_POLLUTANT, TEST_YEAR), "Data should be cached after loading.");
    }

    @Test
    public void testCachingDifferentYears() {
        DataSet dataSet2020 = dataManager.getPollutantData(TEST_YEAR, TEST_POLLUTANT);
        DataSet dataSet2021 = dataManager.getPollutantData(2021, TEST_POLLUTANT);
        
        assertNotSame(dataSet2020, dataSet2021, "Different years should return different datasets.");
        
        assertTrue(dataManager.isDataCached(TEST_POLLUTANT, TEST_YEAR), "2020 data should be cached.");
        assertTrue(dataManager.isDataCached(TEST_POLLUTANT, 2021), "2021 data should be cached.");
    }

    @Test
    public void testCachingDifferentPollutants() {
        Pollutant anotherPollutant = Pollutant.PM10;
        
        DataSet dataSetNO2 = dataManager.getPollutantData(TEST_YEAR, TEST_POLLUTANT);
        DataSet dataSetPM10 = dataManager.getPollutantData(TEST_YEAR, anotherPollutant);
        
        assertNotSame(dataSetNO2, dataSetPM10, "Different pollutants should return different datasets.");
        
        assertTrue(dataManager.isDataCached(TEST_POLLUTANT, TEST_YEAR), "NO2 data should be cached.");
        assertTrue(dataManager.isDataCached(anotherPollutant, TEST_YEAR), "PM10 data should be cached.");
    }

    /**
     * Clear the cache after each test.
     */
    @AfterEach
    public void tearDown() {
        dataManager.clearCache();
    }
}