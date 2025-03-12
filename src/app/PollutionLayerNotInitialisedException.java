package app;

/**
 * An exception class that is thrown when the pollution layer is not initialised.
 * 
 * @author Anas Ahmed
 * @version 1.0
 */
public class PollutionLayerNotInitialisedException extends RuntimeException {
    /**
     * Constructor.
     */
    public PollutionLayerNotInitialisedException(String message) {
        super(message);
    }
}
