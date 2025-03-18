package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataProcessing.DataManager;

/**
 * Test class.
 */
class DataManagerTest {
    private DataManager dataManager;

    @BeforeEach
    public void setUp() {
        dataManager = DataManager.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(dataManager);
        assertTrue(dataManager == DataManager.getInstance());
    }
}