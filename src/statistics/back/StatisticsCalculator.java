package statistics.back;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import dataProcessing.DataPoint;
import dataProcessing.Pollutant;

/**
 * Abstract class for statistics calculators.
 * All statistics calculation implementations should extend this abstract class.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 3.0
 */
public abstract class StatisticsCalculator {
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

    /**
     * Calculate the mean value from a list of data points.
     * @param dataPoints List of data points.
     * @return The mean value.
     */
    protected double calculateMean(List<DataPoint> dataPoints) {
        OptionalDouble mean = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .average();
        
        return mean.orElse(0.0);
    }
    
    /**
     * Calculate the median value from a list of data points.
     * @param dataPoints List of data points.
     * @return The median value.
     */
    protected double calculateMedian(List<DataPoint> dataPoints) {
        List<Double> sortedValues = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .sorted()
            .boxed()
            .collect(Collectors.toList());
        
        if (sortedValues.isEmpty()) {
            return 0.0;
        }
        
        int middle = sortedValues.size() / 2;
        if (sortedValues.size() % 2 == 1) {
            return sortedValues.get(middle);
        } else {
            return (sortedValues.get(middle - 1) + sortedValues.get(middle)) / 2.0;
        }
    }
    
    /**
     * Calculate the standard deviation from a list of data points.
     * @param dataPoints List of data points.
     * @param mean Precalculated mean value.
     * @return The standard deviation.
     */
    protected double calculateStandardDeviation(List<DataPoint> dataPoints, double mean) {
        double sumSquaredDiffs = dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0) // Filter out invalid values.
            .map(value -> Math.pow(value - mean, 2))
            .sum();
        
        int validCount = (int) dataPoints.stream()
            .mapToDouble(DataPoint::value)
            .filter(value -> value >= 0)
            .count();
        
        return Math.sqrt(sumSquaredDiffs / validCount);
    }

    /**
     * Calculate linear regression coefficients for trend analysis.
     * @param yearlyValues Map of year to value.
     * @param startYear First year in the range.
     * @param endYear Last year in the range.
     * @return Array with [slope, intercept].
     */
    protected double[] calculateLinearTrend(Map<Integer, Double> yearlyValues, int startYear, int endYear) {
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