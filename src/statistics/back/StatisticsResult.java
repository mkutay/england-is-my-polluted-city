package statistics.back;

import dataProcessing.Pollutant;

/**
 * Interface for statistics calculation results. Different calculators can implement this
 * interface to provide specialised result types.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public interface StatisticsResult {
    /**
     * @return The title string
     */
    String getTitle();
    
    /**
     * @return The description string.
     */
    String getDescription();

    /**
     * @return The pollutant associated with this result.
     */
    Pollutant getPollutant();
}
