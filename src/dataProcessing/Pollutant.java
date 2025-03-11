package dataProcessing;

/**
 * Enum for the different pollutants that can be displayed on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public enum Pollutant {
    NO2, PM2_5, PM10;

    @Override
    public String toString() {
        return switch (this) {
            case NO2 -> "NO2";
            case PM2_5 -> "PM2.5";
            case PM10 -> "PM10";
        };
    }
}
