package statistics.back.simpleTrends;

import dataProcessing.DataManager;
import dataProcessing.DataPoint;
import dataProcessing.DataSet;
import dataProcessing.Pollutant;
import statistics.back.StatisticsCalculator;
import statistics.back.StatisticsResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyser for pollution trends over time.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public class SimpleTrendsCalculator implements StatisticsCalculator {
    private final DataManager dataManager; // Data manager instance
    
    /**
     * Constructor.
     */
    public SimpleTrendsCalculator() {
        this.dataManager = DataManager.getInstance();
    }
    
    @Override
    public StatisticsResult calculateStatistics(Pollutant pollutant, int year) {
        // For a single dataset, we can't calculate much trend information,
        // but we can provide some basic statistics that will be useful for comparison.
        
        DataSet dataSet = dataManager.getPollutantData(year, pollutant);
        
        SimpleTrendsResult result = new SimpleTrendsResult(
            "Pollution Snapshot", 
            "Basic statistics for " + dataSet.getPollutant() + " in " + dataSet.getYear(),
            pollutant
        );
        
        List<DataPoint> dataPoints = dataSet.getData();
        
        // Calculate the total sum of all pollution values:
        double sum = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .sum();
        
        // Calculate count of valid data points:
        long count = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0)
            .count();
        
        result.setSnapshotData(sum, count > 0 ? sum / count : 0, count);
        
        return result;
    }
    
    @Override
    public StatisticsResult calculateStatisticsOverTime(Pollutant pollutant, int startYear, int endYear) {
        SimpleTrendsResult result = new SimpleTrendsResult(
            "Pollution Trends Over Time", 
            "Analysis of pollution trends for " + pollutant.getDisplayName() + " from " + startYear + " to " + endYear,
            pollutant
        );
        
        Map<Integer, Double> yearlyAverages = new HashMap<>();
        Map<Integer, Double> yearlyTotals = new HashMap<>();
        
        // Calculate yearly values for each year:
        for (int year = startYear; year <= endYear; year++) {
            DataSet dataSet = dataManager.getPollutantData(year, pollutant);
            List<DataPoint> dataPoints = dataSet.getData();
            
            // Calculate total and average for this year:
            double total = dataPoints.stream()
                .mapToDouble(DataPoint::value)
                .filter(value -> value >= 0)
                .sum();
            
            long count = dataPoints.stream()
                .mapToDouble(DataPoint::value)
                .filter(value -> value >= 0)
                .count();
            
            double average = count > 0 ? total / count : 0;
            
            yearlyTotals.put(year, total);
            yearlyAverages.put(year, average);
        }
        
        // Store complete sets for charting:
        result.setYearlyAverages(yearlyAverages);
        result.setYearlyTotals(yearlyTotals);
        
        // Calculate overall trend (simple linear regression):
        double[] trendCoefficients = calculateLinearTrend(yearlyAverages, startYear, endYear);
        result.setTrendCoefficients(trendCoefficients[0], trendCoefficients[1]);
        
        // Calculate percent change from start to end:
        double startValue = yearlyAverages.get(startYear);
        double endValue = yearlyAverages.get(endYear);
        
        if (startValue > 0) {
            double percentChange = ((endValue - startValue) / startValue) * 100.0;
            result.setPercentChange(percentChange);
        }
        
        return result;
    }
    
    @Override
    public String getStatisticsName() {
        return "Pollution Trends";
    }
    
    /**
     * Calculate linear regression coefficients for trend analysis.
     * @param yearlyValues Map of year to value.
     * @param startYear First year in the range.
     * @param endYear Last year in the range.
     * @return Array with [slope, intercept].
     */
    private double[] calculateLinearTrend(Map<Integer, Double> yearlyValues, int startYear, int endYear) {
        int n = endYear - startYear + 1;
        
        if (n <= 1) {
            return new double[] { 0.0, 0.0 };
        }
        
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        
        for (int year = startYear; year <= endYear; year++) {
            double x = year;
            double y = yearlyValues.getOrDefault(year, 0.0);
            
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }
        
        // Calculate slope and intercept.
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        return new double[] { slope, intercept };
    }
}