package infoPopup;

import java.util.Map;

import dataProcessing.Pollutant;

/**
 * A record that stores the real-time data to be shown in the info popup.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public record ShownRealtimeData(
    String lastUpdated, String aqiValue, Map<Pollutant, String> pollutantValues
) { }
