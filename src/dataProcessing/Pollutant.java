package dataProcessing;

import java.util.Map;

/**
 * Enum for the different pollutants that can be displayed on the map.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public enum Pollutant {
    NO2, PM2_5, PM10;

    /**
     * Concentration bands include an array of 9 integers
     * The indexes of this array refer to the following:
     * 0 - 2: Low Band
     * 3 - 5: Moderate Band
     * 6 - 8 : High Band
     * 9: Very High band
     */
    public final static Map<Pollutant, int[]> CONCENTRATION_BANDS = Map.of(
            NO2, new int[]{68, 135, 201, 268, 335, 401, 468, 535, 601},
            PM2_5, new int[]{11, 24, 36, 42, 48, 54, 59, 65, 71},
            PM10, new int[]{17, 34, 51, 59, 67, 76, 84, 92, 101}
    );

    /**
     * All data is in microgram per meter cubed. This string is the symbol for that
     */
    public final static String UNITS = "µg/m³";
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
     * TODO decide what to do with this - this can be useful as an extra thing to display data for, but may also be redundant
     *
     * @param pollutionConcentration Pollution Concentration expressed as ug/m^3
     * @return the pollution band this pollution falls into
     */
    public static int getPollutionLevel(Pollutant pollutant, double pollutionConcentration){
        int[] concentrationBand = CONCENTRATION_BANDS.get(pollutant);
        for (int i = 0; i < concentrationBand.length; i++) {
            if (pollutionConcentration < concentrationBand[i]) {
                return i + 1;
            }
        }
        return concentrationBand.length + 1;
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
