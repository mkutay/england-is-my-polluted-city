package statistics.ui;

import java.util.HashMap;
import java.util.Map;

import statistics.back.StatisticsResult;
import statistics.back.pollutionExtremes.PollutionExtremesResult;
import statistics.back.trends.TrendsResult;
import statistics.ui.panels.*;

/**
 * Factory class for creating appropriate UI panels for different statistics results,
 * according to their dynamic type.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class StatisticsPanelFactory {
    private Map<StatisticsResult, StatisticsPanel> statisticsCache; // Cache of statistics results to avoid creating duplicate panels.
    private static StatisticsPanelFactory instance; // Singleton instance.

    /**
     * Constructor.
     */
    private StatisticsPanelFactory() {
        statisticsCache = new HashMap<>();
    }

    /**
     * @return The singleton instance of the factory.
     */
    public static StatisticsPanelFactory getInstance() {
        if (instance == null) {
            instance = new StatisticsPanelFactory();
        }
        return instance;
    }

    /**
     * Create an appropriate panel for the given statistics result.
     * @param result The statistics result to display.
     * @return A panel configured to display the provided statistics result.
     */
    public StatisticsPanel createPanel(StatisticsResult result) {
        if (statisticsCache.get(result) != null) { // Check if the panel is already cached.
            return statisticsCache.get(result);
        }

        return switch (result) {
            case TrendsResult stResult -> new TrendsPanel(stResult);
            case PollutionExtremesResult peResult -> new PollutionExtremesPanel(peResult);
            default -> throw new IllegalArgumentException("Unsupported statistics result type: " + result.getClass().getName());
        };
    }
}