package dataProcessing;

/**
 * Enum for the different pollutants that can be displayed on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public enum Pollutant {
    NO2("NO2"), PM2_5("PM2_5"), PM10("PM10");

    private final String name;
    Pollutant(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }

    // returns default enum name, except for pollutant PM2_5
    public String getDisplayName() {
        if (this == PM2_5) return "PM2.5";
        return name();
    }

}
