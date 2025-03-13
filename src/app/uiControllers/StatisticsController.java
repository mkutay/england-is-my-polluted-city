package app.uiControllers;

import dataProcessing.Pollutant;
import statistics.back.StatisticsManager;
import statistics.back.StatisticsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.StatisticsPanelFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Handles all statistics UI elements and changes.
 * 
 * @author Mehmet Kutay Bozkurt
 */
public class StatisticsController {
    private StatisticsPanel statisticsPanel;
    private Integer currentYear;
    private Pollutant currentPollutant;
    
    public StatisticsController() {
        updateDataSet(2018, Pollutant.NO2);
    }

    /**
     * Updates the statistics dataset with the specified year and pollutant.
     * @param year The year to show statistics for
     * @param pollutant The pollutant to show statistics for
     */
    public void updateDataSet(Integer year, Pollutant pollutant) {
        // Only update if the selection has actually changed
        if (year.equals(currentYear) && pollutant.equals(currentPollutant)) {
            return;
        }
        
        currentYear = year;
        currentPollutant = pollutant;
        
        StatisticsManager sm = StatisticsManager.getInstance();
        Map<String, StatisticsResult> m = sm.calculateStatistics(pollutant, year);
        
        // Get the third statistics result (this seems to be a temporary solution)
        Iterator<String> it = m.keySet().iterator();
        String key = null;
        for (int i = 0; i < 3 && it.hasNext(); i++) {
            key = it.next();
        }
        
        if (key != null) {
            StatisticsResult sr = m.get(key);
            statisticsPanel = StatisticsPanelFactory.createPanel(sr);
        }
    }

    /**
     * @return The statistics panel for display
     */
    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }
}
