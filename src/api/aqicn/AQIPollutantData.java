package api.aqicn;

/**
 * Stores the values of the various pollutants.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AQIPollutantData {
    private AQIPollutantValue co;
    private AQIPollutantValue h;
    private AQIPollutantValue no2;
    private AQIPollutantValue o3;
    private AQIPollutantValue p;
    private AQIPollutantValue pm10;
    private AQIPollutantValue pm25;
    private AQIPollutantValue so2;
    private AQIPollutantValue t;
    private AQIPollutantValue w;

    // Getters for the data:
    public AQIPollutantValue getCo() { return co; }
    public AQIPollutantValue getH() { return h; }
    public AQIPollutantValue getNo2() { return no2; }
    public AQIPollutantValue getO3() { return o3; }
    public AQIPollutantValue getP() { return p; }
    public AQIPollutantValue getPm10() { return pm10; }
    public AQIPollutantValue getPm25() { return pm25; }
    public AQIPollutantValue getSo2() { return so2; }
    public AQIPollutantValue getT() { return t; }
    public AQIPollutantValue getW() { return w; }

    @Override
    public String toString() {
        return "PollutantValue{" +
            "no2='" + no2 + '\'' +
            ", pm25='" + pm25 + '\'' +
            ", pm10='" + pm10 + '\'' +
            '}';
    }

}
