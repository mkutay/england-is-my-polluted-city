package dataProcessing;

/**
 * Enum for the different pollutants that can be displayed on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public enum Pollutant {
    NO2("NO2"), PM2_5("PM2_5"), PM10("PM10"); //TODO: Find a way to get the string to display PM2.5 and not mess up file loading

    private final String name;
    Pollutant(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
}
