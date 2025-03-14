package dataProcessing;

import java.util.*;

import javafx.util.Pair;

/**
 * A DataSet object holds all the data from a pollution data file.
 * 
 * It is assumed that the data is derived from a DEFRA air pollution file (see
 * https://uk-air.defra.gov.uk/data/pcm-data). 
 * 
 * The data consists of a few bits of information about the nature of the data, and a list
 * of data points.
 * 
 * @author Michael Kölling and Mehmet Kutay Bozkurt
 * @version 2.1
 */
public class DataSet {
    private final String pollutant;
    private final String year;
    private final String metric;
    private final String units;
    
    // Key is a pair of easting and northing respectively.
    private final HashMap<Pair<Integer, Integer>, DataPoint> data;

    /**
     * Constructor for objects of class DataSet
     */
    public DataSet(String pollutant, String year, String metric, String units) {
        this.pollutant = pollutant;
        this.year = year;
        this.metric = metric;
        this.units = units;
        
        data = new HashMap<>();
    }

    /**
     * Return the pollutant information for this dataset.
     */
    public String getPollutant() {
        return pollutant;
    }
    
    /**
     * Return the year information for this dataset.
     */
    public String getYear() {
        return year;
    }
    
    /**
     * Return the metric information for this dataset.
     */
    public String getMetric() {
        return metric;
    }
    
    /**
     * Return the units information for this dataset.
     */
    public String getUnits() {
        return units;
    }
    
    /**
     * Return the data points of this dataset.
     */
    public List<DataPoint> getData() {
        return new ArrayList<>(data.values());
    }

    /**
     * @return The maximum pollution value in this dataset.
     */
    public double getMaxPollutionValue() {
        return data.values().stream()
            .mapToDouble(DataPoint::value)
            .max()
            .orElse(Double.NaN); // Return NaN if list is empty.
    }

    /**
     * Return a specific data point from this dataset.
     * @param easting The easting value of the data point.
     * @param northing The northing value of the data point.
     * @return The data point at the specified easting and northing, or null if no such data point exists.
     */
    public DataPoint getDataPoint(int easting, int northing) {
        return data.get(new Pair<>(easting, northing));
    }
    
    /**
     * Add a data point to this dataset. 
     * A data point consists of 4 pieces od data:
     * 
     *     gridcode, x, y, value
     *     
     * The data is provided in a String array of length 4. If the value is invalid or
     * missing, it will be stored as -1.
     *
     * Adds to hash map of all data points with key of "easting,northing"
     *
     * @param values An array with the four data values (as Strings)
     */
    public void addData(String[] values) {
        DataPoint dp = new DataPoint(
            toInt(values[0]),
            toInt(values[1]),
            toInt(values[2]),
            toDouble(values[3])
        );

        data.put(new Pair<>(dp.x(), dp.y()), dp);
    }
    
    /**
     * Convert a string to int. 
     * @param intString The String holding the int value
     * @return The int value, or -1 if the string is not a readable number
     */
    private int toInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException exc) {
            return -1;
        }
    }

    /**
     * Convert a string to double. 
     * @param doubleString The String holding the double value
     * @return The double value, or -1.0 if the string is not a readable number
     */
    private double toDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException exc) {
            return -1.0;
        }
    }

    /**
     * Return a string representation of this dataset info.
     */
    @Override
    public String toString() {
        return String.format("Dataset: Pollutant: %s, Year: %s, Metric: %s, Units: %s (%d data points)",
            pollutant, year, metric, units, data.size());
    }
}
