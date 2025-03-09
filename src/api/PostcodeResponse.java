package api;
import java.util.List;

/**
 * Represents a response from the postcodes.io API. This is used to display information
 * about a location.
 * 
 * @author Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PostcodeResponse {
    private int status; // The status of the response.
    private List<PostcodeResult> result; // The result of the response.

    // Getters:
    public int getStatus() { return status; }
    public List<PostcodeResult> getResult() { return result; }

    @Override
    public String toString() {
        return "PostcodeResponse{" +
            "status=" + status +
            ", result=" + result +
            '}';
    }
}