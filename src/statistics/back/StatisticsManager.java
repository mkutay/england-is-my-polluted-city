package statistics.back;

import dataProcessing.Pollutant;
import statistics.back.averagePollution.AveragePollutionCalculator;
import statistics.back.pollutionExtremes.PollutionExtremesCalculator;
import statistics.back.simpleTrends.SimpleTrendsCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
    
    /**
     * Private constructor for singleton pattern.
     */
    private StatisticsManager() {
        calculators = new ArrayList<>();
        resultCache = new HashMap<>();
        
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
    private void registerCalculator(StatisticsCalculator calculator) {
        calculators.add(calculator);
    }
    
    /**
     * @return List of all registered calculators.
     */
    public List<StatisticsCalculator> getCalculators() {
        return calculators;
    }
    
    /**
     * Calculate statistics for a specific data set information.
     * @param pollutant The pollutant to analyse.
     * @param year The year of data to analyse.
     * @return Map of calculator names to their results.
     */
    public Map<String, StatisticsResult> calculateStatistics(Pollutant pollutant, int year) {
        Map<String, StatisticsResult> results = new HashMap<>();
        
        for (StatisticsCalculator calculator : calculators) {
            String key = makeKey(calculator.getStatisticsName(), year, pollutant);
            
            // Check cache first:
            if (resultCache.containsKey(key)) {
                results.put(calculator.getStatisticsName(), resultCache.get(key));
                continue;
            }
            
            // Calculate and cache:
            StatisticsResult result = calculator.calculateStatistics(pollutant, year);
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
     * Asynchronously calculate statistics for a specific data set over a time.
     * @param pollutant The pollutant to analyse.
     * @param startYear The start year.
     * @param endYear The end year to analyse.
     * @return CompletableFuture.
     */
    public CompletableFuture<Map<String, StatisticsResult>> calculateStatisticsOverTimeAsync(Pollutant pollutant, int startYear, int endYear) {
        return CompletableFuture.supplyAsync(() -> calculateStatisticsOverTime(pollutant, startYear, endYear));
    }

    /**
     * Asynchronously calculate statistics for a specific data set information.
     * @param pollutant The pollutant to analyse.
     * @param year The year of data to analyse.
     * @return CompletableFuture.
     */
    public CompletableFuture<Map<String, StatisticsResult>> calculateStatisticsAsync(Pollutant pollutant, int year) {
        return CompletableFuture.supplyAsync(() -> calculateStatistics(pollutant, year));
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
}
