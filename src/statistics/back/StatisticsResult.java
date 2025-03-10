package statistics.back;

import java.util.Map;

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
     * Get a specific value by key.
     * @param key The key for the desired value.
     * @return The value, or null if not found.
     */
    Object getValue(String key);
    
    /**
     * @return Map containing all key-value pairs.
     */
    Map<String, Object> getAllValues();
}
