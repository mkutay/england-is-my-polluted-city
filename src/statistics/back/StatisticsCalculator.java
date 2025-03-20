package statistics.back;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import dataProcessing.DataPoint;
import dataProcessing.Pollutant;

/**
 * An interface for statistics calculators.
 * All statistics calculators should implement this interface.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 3.0
 */
public interface StatisticsCalculator {
    /**
     * Calculate statistics for a pollutant across multiple years.
     * @param pollutant The pollutant to analyse.
     * @param startYear The first year to include in the analysis.
     * @param endYear The last year to include in the analysis.
     * @return A StatisticsResult containing the calculation results.
     */
    public abstract StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear);
    
    /**
     * @return A String describing what this calculator analyses.
     */
    public abstract String getStatisticsName();
}