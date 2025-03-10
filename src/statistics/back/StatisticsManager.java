package statistics.back;

import dataProcessing.DataManager;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.averagePollution.AveragePollutionCalculator;
import statistics.back.pollutionExtremes.PollutionExtremesCalculator;
import statistics.back.simpleTrends.SimpleTrendsCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for statistics calculations.
 * Follows the singleton pattern and serves as the main entry point for the statistics backend.
 * Similar to the DataManager class, but for statistics calculations.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class StatisticsManager {
    private static StatisticsManager instance; // Singelton instance.
    
    private final List<StatisticsCalculator> calculators;
    private final Map<String, StatisticsResult> resultCache;
    private final DataManager dataManager;
    
    /**
     * Private constructor for singleton pattern.
     */
    private StatisticsManager() {
        calculators = new ArrayList<>();
        resultCache = new HashMap<>();
        dataManager = DataManager.getInstance();
        
        // Register default calculators, for now:
        registerCalculator(new AveragePollutionCalculator());
        registerCalculator(new PollutionExtremesCalculator());
        registerCalculator(new SimpleTrendsCalculator());
    }
    
    /**
     * Get the singleton instance.
     * @return The StatisticsManager instance.
     */
    public static StatisticsManager getInstance() {
        if (instance == null) {
            instance = new StatisticsManager();
        }
        return instance;
    }
    
    /**
     * Register a new statistics calculator.
     * @param calculator The calculator to register.
     */
    public void registerCalculator(StatisticsCalculator calculator) {
        calculators.add(calculator);
    }
    
    /**
     * @return List of all registered calculators.
     */
    public List<StatisticsCalculator> getCalculators() {
        return new ArrayList<>(calculators);
    }
    
    /**
     * Calculate statistics for a specific data set information.
     * @param year The year of data to analyse.
     * @param pollutant The pollutant to analyse.
     * @return Map of calculator names to their results.
     */
    public Map<String, StatisticsResult> calculateStatistics(int year, Pollutant pollutant) {
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        Map<String, StatisticsResult> results = new HashMap<>();
        
        for (StatisticsCalculator calculator : calculators) {
            String key = makeKey(calculator.getStatisticsName(), year, pollutant);
            
            // Check cache first:
            if (resultCache.containsKey(key)) {
                results.put(calculator.getStatisticsName(), resultCache.get(key));
                continue;
            }
            
            // Calculate and cache:
            StatisticsResult result = calculator.calculateStatistics(dataSet);
            resultCache.put(key, result);
            results.put(calculator.getStatisticsName(), result);
        }
        
        return results;
    }
    
    /**
     * Calculate time-series statistics for a pollutant across multiple years.
     * @param pollutant The pollutant to analyse.
     * @param startYear The first year to include.
     * @param endYear The last year to include.
     * @return Map of calculator names to their results.
     */
    public Map<String, StatisticsResult> calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        Map<String, StatisticsResult> results = new HashMap<>();
        
        for (StatisticsCalculator calculator : calculators) {
            String key = makeTimeSeriesKey(calculator.getStatisticsName(), pollutant, startYear, endYear);
            
            // Check cache first:
            if (resultCache.containsKey(key)) {
                results.put(calculator.getStatisticsName(), resultCache.get(key));
                continue;
            }
            
            // Calculate and cache:
            StatisticsResult result = calculator.calculateStatisticsOverTime(pollutant, startYear, endYear);
            resultCache.put(key, result);
            results.put(calculator.getStatisticsName(), result);
        }
        
        return results;
    }
    
    /**
     * Create a cache key for a specific calculation.
     */
    private String makeKey(String calculatorName, int year, Pollutant pollutant) {
        return calculatorName + "_" + pollutant + "_" + year;
    }
    
    /**
     * Create a cache key for time series calculations.
     */
    private String makeTimeSeriesKey(String calculatorName, Pollutant pollutant, int startYear, int endYear) {
        return calculatorName + "_" + pollutant + "_" + startYear + "_" + endYear;
    }
    
    /**
     * Clear the result cache.
     */
    public void clearCache() {
        resultCache.clear();
    }
}
