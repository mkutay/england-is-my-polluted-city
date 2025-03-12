package app;

import dataProcessing.Pollutant;
import javafx.scene.Scene;
import javafx.stage.Stage;
import statistics.back.StatisticsManager;
import statistics.back.StatisticsResult;
import statistics.ui.StatisticsPanel;
import statistics.ui.StatisticsPanelFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Handles all statistics ui elements
 * TODO implement fully and make gooder
 */
public class StatisticsController {
    public void showStatsPanel(Stage stage) {
        StatisticsManager sm = StatisticsManager.getInstance();
        Map<String, StatisticsResult> m = sm.calculateStatisticsOverTime(Pollutant.PM10, 2018, 2023);
        Iterator<String> it = m.keySet().iterator();
        it.next();
        it.next();
        StatisticsResult sr = m.get(it.next());
        System.out.println(sr.getTitle());

        StatisticsPanel statsRoot = StatisticsPanelFactory.createPanel(sr);
        Scene scene = new Scene(statsRoot, 900, 900);
        stage.setScene(scene);
        stage.show();
    }
}
