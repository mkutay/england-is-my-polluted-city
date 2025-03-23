package statistics.back;

import dataProcessing.Pollutant;
import statistics.back.calculators.AllPollutionsCalculator;
import statistics.back.calculators.DistributionAnalysisCalculator;
import statistics.back.calculators.HistogramCalculator;
import statistics.back.calculators.PollutionExtremesCalculator;
import statistics.back.calculators.TrendsCalculator;
import statistics.types.StatisticsResult;

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
    
    /**
     * Private constructor for singleton pattern.
     */
    private StatisticsManager() {
        calculators = new ArrayList<>();
        resultCache = new HashMap<>();
        
        // Register default calculators, for now:
        registerCalculator(new TrendsCalculator());
        registerCalculator(new PollutionExtremesCalculator());
        registerCalculator(new AllPollutionsCalculator());
        registerCalculator(new HistogramCalculator());
        registerCalculator(new DistributionAnalysisCalculator());
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
     * Calculate time-series statistics for a pollutant across multiple years.
     * @param pollutant The pollutant to analyse.
     * @param startYear The first year to include.
     * @param endYear The last year to include.
     * @return Map of calculator names to their results.
     */
    public Map<String, StatisticsResult> calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        Map<String, StatisticsResult> results = new HashMap<>();
        
        for (StatisticsCalculator calculator : calculators) {
            final String key = makeTimeSeriesKey(calculator.getStatisticsName(), pollutant, startYear, endYear);
            
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
     * Create a cache key for time series calculations.
     */
    private String makeTimeSeriesKey(String calculatorName, Pollutant pollutant, int startYear, int endYear) {
        return calculatorName + "_" + pollutant + "_" + startYear + "_" + endYear;
    }
}
