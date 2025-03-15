package api.aqicn;

/**
 * Represents a response from the Air Quality Index API. This is used to display
 * information on real-time air quality updates.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AQICNData {
    private int aqi;
    private AQIPollutantData iaqi;
    private AQITimestamp time;

    // Getters:
    public AQIPollutantData getPollutantValues() { return iaqi; }
    public AQITimestamp getTimeData() { return time; }
    public int getAqi() { return aqi; }

    @Override
    public String toString() {
        return "Data{" +
            "aqi=" + aqi +
            ", iaqi=" + iaqi +
            ", time=" + time +
            '}';
    }
}