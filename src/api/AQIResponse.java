package api;

/**
 * Parases the first "data" field of the API response
 */
public class AQIResponse {
    private String status; // The status of the response.
    AQICNData data; // List of all Attributions and data

    //Getters
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
