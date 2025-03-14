package api.aqicn;

/**
 * Represents a response from the aqicn.org API. This is used to display information
 * about the air quality of a location.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AQIResponse {
    private String status; // The status of the response.
    AQICNData data; // List of all attributes and data.

    // Getters:
    public String getStatus() { return status; }
    public AQICNData getData() { return data; }

    @Override
    public String toString() {
        return "AQIResponse{" +
            "status=" + status +
            ", data=" + data +
            '}';
    }
}
