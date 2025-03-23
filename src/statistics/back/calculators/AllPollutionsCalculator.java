package statistics.back.calculators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.types.AllPollutionsResult;
import statistics.types.StatisticsResult;
import statistics.types.TrendsResult;

/**
 * Calculator for all pollutants.
 * Calculates statistics for all pollutants over a given time range.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class AllPollutionsCalculator implements StatisticsCalculator {
    private final TrendsCalculator trendsCalculator;
    
    /**
     * Constructor.
     */
    public AllPollutionsCalculator() {
        this.trendsCalculator = new TrendsCalculator();
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        AllPollutionsResult result = new AllPollutionsResult(
            "Average Pollution Trends", 
            "Trend analysis of average pollution levels from "
            + startYear + " to " + endYear
        );

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Map<Pollutant, TrendsResult> trends = new ConcurrentHashMap<>();

        for (Pollutant p : Pollutant.values()) {
            final Pollutant finalPollutant = p;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                trends.put(finalPollutant, (TrendsResult) trendsCalculator.calculateStatisticsOverTime(finalPollutant, startYear, endYear));
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        result.setTrends(trends);

        Double totalMean = trends.values()
            .stream()
            .mapToDouble(TrendsResult::getMean)
            .average()
            .orElse(0.0);
        
        result.setTotalMean(totalMean);

        Double totalMedian = trends.values()
            .stream()
            .mapToDouble(TrendsResult::getMedian)
            .average()
            .orElse(0.0);
        
        result.setTotalMedian(totalMedian);

        Double totalStandardDeviation = trends.values()
            .stream()
            .mapToDouble(TrendsResult::getStandardDeviation)
            .average()
            .orElse(0.0);

        result.setTotalStandardDeviation(totalStandardDeviation);

        return result;
    }
    
    @Override
    public String getStatisticsName() {
        return "All Pollution Trends";
    }
}
