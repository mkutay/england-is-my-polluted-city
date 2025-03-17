package api.aqicn;

/**
 * Represents the "v" field nested under "iaqi" (Individual AQI) pollutant values.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AQIPollutantValue {
    private Object v;  // "v" is the actual value, and we store it as an Object, as this value can have multiple dynamic types.

    public Object getIAQIValue() { return v; }
}