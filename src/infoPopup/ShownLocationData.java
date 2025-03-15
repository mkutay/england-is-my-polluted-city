package infoPopup;

/**
 * A record that stores the location data to be shown in the info popup.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public record ShownLocationData(
    String borough, String constituency, String postCode, String country
) { }
