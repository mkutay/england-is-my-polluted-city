package api.aqicn;

/**
 * Represents the time data from the AQICN API response.
 * The variables from the response are cryptic so the comments will explain what the variables mean.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AQITimestamp {
    String s; // The date and time last updated in form "YYY-MM-DD HH:MM:SS".
    String tz; // The Timezome that this was updated in (Should be GMT +00:00).
    int v; // The Unix Timestamp.
    String iso; // The date and time in ISO format.

    // Getter methods:
    public String getDateTimeString() { return s; }
    public String getTimezone() { return tz; }
    public int getUnixTimestamp() { return v; }
    public String getISOTime() { return iso; }

    @Override
    public String toString() {
        return "time{" +
            "s='" + s + '\'' +
            ", tz='" + tz + '\'' +
            ", v='" + v + '\'' +
            ", iso='" + iso + '\'' +
            '}';
    }
}
