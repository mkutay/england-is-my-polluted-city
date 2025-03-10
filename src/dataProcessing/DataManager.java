package dataProcessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javafx.util.Pair;

/**
 * Manages the loading, caching, and updating of pollution data.
 * Acts as the main entry point for data access in the application.
 *
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DataManager {
    private static DataManager instance; // Singleton instance
    
    // Cache for loaded datasets, key format as a pair: pollutant, year.
    private final Map<Pair<Pollutant, Integer>, DataSet> dataCache;
    private final DataPicker dataPicker;
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private DataManager() {
        this.dataPicker = new DataPicker();
        this.dataCache = new HashMap<>();
    }
    
    /**
     * Get the singleton instance of the DataManager.
     * @return The DataManager instance.
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    /**
     * Get pollutant data for a specific year and pollutant.
     * Returns cached data if available, otherwise loads the data.
     * @param year The year requested as an integer.
     * @param pollutant The requested pollutant.
     * @return The DataSet for the specified pollutant and year.
     */
    public DataSet getPollutantData(int year, Pollutant pollutant) {
        Pair<Pollutant, Integer> cacheKey = new Pair<>(pollutant, year);
        
        // Return cached data if available:
        if (dataCache.containsKey(cacheKey)) {
            return dataCache.get(cacheKey);
        }
        
        // Load the data if not in cache:
        DataSet dataSet = dataPicker.getPollutantData(year, pollutant);
        dataCache.put(cacheKey, dataSet);
        
        return dataSet;
    }

    /**
     * Get a list of available years in the data for a specific pollutant.
     * @param pollutant The pollutant to check for available years.
     * @return A list of available years.
     */
    public List<Integer> getAvailableYears(Pollutant pollutant) {
        return dataPicker.getAvailableYears(pollutant);
    }
    
    /**
     * Asynchronously load pollutant data for a specific year and pollutant.
     * @param year The year requested as an integer.
     * @param pollutant The requested pollutant.
     * @return A CompletableFuture that will complete with the loaded DataSet.
     */
    public CompletableFuture<DataSet> getPollutantDataAsync(int year, Pollutant pollutant) {
        return CompletableFuture.supplyAsync(() -> getPollutantData(year, pollutant));
    }
    
    /**
     * Check if data is available in the cache.
     * @param pollutant The pollutant to check.
     * @param year The year to check.
     * @return True if the data is in the cache, false otherwise.
     */
    public boolean isDataCached(Pollutant pollutant, int year) {
        return dataCache.containsKey(new Pair<>(pollutant, year));
    }
}
