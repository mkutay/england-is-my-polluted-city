package statistics.types;

import dataProcessing.Pollutant;
import statistics.back.StatisticsResult;

/**
 * A class to store histogram data results from HistogramCalculator.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class HistogramResult implements StatisticsResult {
    private final String title;
    private final String description;
    private final Pollutant pollutant;

    private long[] binCounts;
    private double[] binEdges;
    private double[] binCountsLog;
    private double minValue;
    private double maxValue;
    private long minCount;
    private long maxCount;

    public HistogramResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }

    public void setBinCounts(long[] binCounts) {
        this.binCounts = binCounts;
    }

    public void setBinEdges(double[] binEdges) {
        this.binEdges = binEdges;
    }

    public void setBinCountsLog(double[] binCountsLog) {
        this.binCountsLog = binCountsLog;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinCount(long minCount) {
        this.minCount = minCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    // Getters:
    public long[] getBinCounts() { return binCounts; }
    public double[] getBinEdges() { return binEdges; }
    public double[] getBinCountsLog() { return binCountsLog; }
    public double getMinValue() { return minValue; }
    public double getMaxValue() { return maxValue; }
    public long getMinCount() { return minCount; }
    public long getMaxCount() { return maxCount; }

    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public Pollutant getPollutant() {
        return pollutant;
    }
}
