package dataProcessing;

/**
 * Enum for the different pollutants that can be displayed on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public enum Pollutant {
    NO2, PM2_5, PM10;

    /**
     * Returns an index depending on the level of severity of the pollution concentration.
     * Most inputs will not yield above 1 or 2, but data conditions are fully represented here for completeness
     * INDEX MEANING
     * 1 - 3: Low Band
     * 4 - 6: Moderate Band
     * 7 - 9 : High Band
     * 10: Very High band
     * Data taken from https://uk-air.defra.gov.uk/air-pollution/daqi?view=more-info&pollutant=pm25#pollutant
     *
     * @param pollutionConcentration Pollution Concentration expressed as ug/m^3
     * @return
     */
    public int getPollutionLevel(double pollutionConcentration){
        return switch (this){
            case PM2_5:
                if (pollutionConcentration >= 71) yield 10;
                else if (pollutionConcentration >= 65) yield 9;
                else if (pollutionConcentration >= 59) yield 8;
                else if (pollutionConcentration >= 54) yield 7;
                else if (pollutionConcentration >= 48) yield 6;
                else if (pollutionConcentration >= 42) yield 5;
                else if (pollutionConcentration >= 36) yield 4;
                else if (pollutionConcentration >= 24) yield 3;
                else if (pollutionConcentration >= 11) yield 2;
                else yield 1;
            case NO2:
                if (pollutionConcentration >= 601) yield 10;
                else if (pollutionConcentration >= 535) yield 9;
                else if (pollutionConcentration >= 468) yield 8;
                else if (pollutionConcentration >= 401) yield 7;
                else if (pollutionConcentration >= 335) yield 6;
                else if (pollutionConcentration >= 268) yield 5;
                else if (pollutionConcentration >= 201) yield 4;
                else if (pollutionConcentration >= 135) yield 3;
                else if (pollutionConcentration >= 68) yield 2;
                else yield 1;
            case PM10:
                if (pollutionConcentration >= 101) yield 10;
                else if (pollutionConcentration >= 92) yield 9;
                else if (pollutionConcentration >= 84) yield 8;
                else if (pollutionConcentration >= 76) yield 7;
                else if (pollutionConcentration >= 67) yield 6;
                else if (pollutionConcentration >= 59) yield 5;
                else if (pollutionConcentration >= 51) yield 4;
                else if (pollutionConcentration >= 34) yield 3;
                else if (pollutionConcentration >= 17) yield 2;
                else yield 1;

        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case NO2 -> "NO2";
            case PM2_5 -> "PM2_5";
            case PM10 -> "PM10";
        };
    }

    /**
     * @return The display name of the pollutant.
     */
    public String getDisplayName() {
        return switch (this) {
            case NO2 -> "NO2";
            case PM2_5 -> "PM2.5";
            case PM10 -> "PM10";
        };
    }
}
