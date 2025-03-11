package statistics.back;

import dataProcessing.Pollutant;

/**
 * Interface for statistics calculators.
 * All statistics calculation implementations should implement this interface.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public interface StatisticsCalculator {
    /**
     * Calculate statistics based on pollutant and a year.
     * @param pollutant The pollutant to analyse.
     * @param year The year to analyse.
     * @return A StatisticsResult containing the calculation results.
     */
    StatisticsResult calculateStatistics(Pollutant pollutant, int year);
    
    /**
     * Calculate statistics for a pollutant across multiple years.
     * @param pollutant The pollutant to analyse.
     * @param startYear The first year to include in the analysis.
     * @param endYear The last year to include in the analysis.
     * @return A StatisticsResult containing the calculation results.
     */
    StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear);
    
    /**
     * @return A String describing what this calculator analyses.
     */
    String getStatisticsName();
}