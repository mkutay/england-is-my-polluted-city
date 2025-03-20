package test;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import lod.LODData;
import lod.LODManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LODManager and LODData classes.
 *
 * @author Anas Ahmed
 * @version 1.0
 */
class LODSystemsTest {
    private LODManager lodManager;
    private DataSet dataSet;
    private final static int NUM_LODS = 32; // Large number to stress-test.
    private final static double RANGE_OF_ERROR = 0.15; // +-% Error for approximate LOD size.

    @BeforeEach
    public void setUp(){
        // Get arbitrary test data - "shape" of data is important to test so using real data is necessary.
        DataManager dataManager = DataManager.getInstance();
        dataSet = dataManager.getPollutantData(2018, Pollutant.NO2);
        lodManager = new LODManager(dataSet, NUM_LODS);
    }

    @Test
    public void testLODManager() {
        assertNotNull(lodManager);
        for (int i = 0; i < NUM_LODS - 1; i++) {
            LODData lodData = lodManager.getLODData(i);
            assertNotNull(lodData);
        }
    }

    @Test
    public void testLODData() {
        for (int i = 0; i < NUM_LODS - 1; i++) {
            LODData lodData = lodManager.getLODData(i);
            assertEquals(i+1, lodData.getLevelOfDetail(), "LOD level should be initialized correctly");

            // Asserting number of LODs = +-RANGE_OF_ERROR% totalDataPoints / (levelOfDetail ^ 2).
            int expectedSize = dataSet.getData().size() / (lodData.getLevelOfDetail() * lodData.getLevelOfDetail());
            int actualSize = lodData.getData().size();
            double percentDifference =  Math.abs(1 - ((double) actualSize / expectedSize));
            assertTrue(percentDifference <= RANGE_OF_ERROR , "LOD data size should be within " + RANGE_OF_ERROR*100 + "% expected size. Current: " + percentDifference);

        }
    }
}
