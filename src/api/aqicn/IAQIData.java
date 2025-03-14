package api.aqicn;

/**
 * Represents the "iaqi" field in the AQI API response.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class IAQIData {
    AQIPollutantData v; // All pollutant values and their concentrations (typically microgram per metre cube).
    AQITimestamp time; // Time data associated with last updated data.

    // Simple getters:
    public AQIPollutantData getPollutantValue() { return v; }
    public AQITimestamp getTimeData() { return time; }
}
