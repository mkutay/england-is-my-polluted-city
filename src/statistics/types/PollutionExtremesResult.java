package statistics.types;

import dataProcessing.DataPoint;
import dataProcessing.Pollutant;
import javafx.util.Pair;
import statistics.back.StatisticsResult;

import java.util.Map;

/**
 * Specialised statistics result for pollution extremes data.
 * Provides type-safe setters/accessors for hotspots and extremes information.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class PollutionExtremesResult implements StatisticsResult {
    private final String title; // Title of the result.
    private final String description; // Description of the result.
    private final Pollutant pollutant; // The pollutant for which these results are calculated.
    
    private Pair<Integer, DataPoint> maxYear;
    private Pair<Integer, DataPoint> medianYear;
    private Pair<Integer, DataPoint> minYear;
    private Map<Integer, DataPoint> yearToMaxPoints;
    private Map<Integer, DataPoint> yearToMinPoints;
    private Map<Integer, DataPoint> yearToMedianPoints;
    
    /**
     * Constructor for PollutionExtremesResult.
     * @param title A short title describing these results
     * @param description A longer description of the results
     */
    public PollutionExtremesResult(String title, String description, Pollutant pollutant) {
        this.title = title;
        this.description = description;
        this.pollutant = pollutant;
    }
    
    /**
     * Set the year with the highest overall value.
     * @param year The year with highest value.
     * @param point The point with the highest value.
     */
    public void setMaxYear(int year, DataPoint point) {
        this.maxYear = new Pair<>(year, point);
    }

    /**
     * Set the year with the median value.
     * @param year The year with median value.
     * @param point The point with the median value.
     */
    public void setMedianYear(int year, DataPoint point) {
        this.medianYear = new Pair<>(year, point);
    }

    /**
     * Set the year with the lowest overall value.
     * @param year The year with lowest value.
     * @param point The point with the lowest value.
     */
    public void setMinYear(int year, DataPoint point) {
        this.minYear = new Pair<>(year, point);
    }

    /**
     * Store yearly max points.
     * @param yearToMaxPoint Map of year to max point.
     */
    public void setYearlyMaxPoints(Map<Integer, DataPoint> yearToMaxPoints) {
        this.yearToMaxPoints = yearToMaxPoints;
    }

    /**
     * Store yearly min points.
     * @param yearToMinPoint Map of year to min point.
     */
    public void setYearlyMinPoints(Map<Integer, DataPoint> yearToMinPoints) {
        this.yearToMinPoints = yearToMinPoints;
    }

    /**
     * Store yearly median points.
     * @param yearToMedianPoint Map of year to median point.
     */
    public void setYearlyMedianPoints(Map<Integer, DataPoint> yearToMedianPoints) {
        this.yearToMedianPoints = yearToMedianPoints;
    }
    
    // Simple getters:
    public Map<Integer, DataPoint> getYearToMaxPoints() { return yearToMaxPoints; }
    public Map<Integer, DataPoint> getYearToMinPoints() { return yearToMinPoints; }
    public Map<Integer, DataPoint> getYearToMedianPoints() { return yearToMedianPoints; }
    public Pair<Integer, DataPoint> getMaxYear() { return maxYear; }
    public Pair<Integer, DataPoint> getMedianYear() { return medianYear; }
    public Pair<Integer, DataPoint> getMinYear() { return minYear; }
    
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