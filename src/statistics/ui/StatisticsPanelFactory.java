package statistics.ui;

import statistics.back.StatisticsResult;
import statistics.back.averagePollution.AveragePollutionResult;
import statistics.back.pollutionExtremes.PollutionExtremesResult;
import statistics.back.simpleTrends.SimpleTrendsResult;
import statistics.ui.panels.*;

/**
 * Factory class for creating appropriate UI panels for different statistics results,
 * according to their dynamic type.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class StatisticsPanelFactory {
    /**
     * Create an appropriate panel for the given statistics result.
     * @param result The statistics result to display.
     * @return A panel configured to display the provided statistics result.
     */
    public static StatisticsPanel createPanel(StatisticsResult result) {
        return switch (result) {
            case SimpleTrendsResult stResult -> new SimpleTrendsPanel(stResult);
            case AveragePollutionResult apResult -> new AveragePollutionPanel(apResult);
            case PollutionExtremesResult peResult -> new PollutionExtremesPanel(peResult);
            default -> throw new IllegalArgumentException("Unsupported statistics result type: " + result.getClass().getName());
        };
    }
}