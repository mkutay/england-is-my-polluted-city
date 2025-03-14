package api;

/**
 * Parses the "iaqi" field in the AQI API json response
 * @author Matthias Loong
 */
public class IAQIData {
    AQIPollutantData v; //All pollutant values and their concentrations (typically microgram per metre cube)
    AQITimestamp time; //Time data associated with last updated data

    public AQIPollutantData getPollutantValue(){
        return v;
    }
}
