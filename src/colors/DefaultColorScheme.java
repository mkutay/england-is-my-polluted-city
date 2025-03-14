package colors;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Default color scheme implementation for pollution visualization.
 * Uses a gradient from green (low) to yellow (medium) to red (high) to purple (very high).
 * 
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public class DefaultColorScheme extends ColorScheme {
    public DefaultColorScheme() {
        //colors = List.of(Color.GREEN, Color.YELLOW, Color.RED, Color.rgb(128, 0, 128));
        colors = List.of(
                Color.rgb(0, 128, 0),    // Green (Low emissions)
                Color.rgb(255, 215, 0),  // Yellow (Moderate emissions)
                Color.rgb(255, 165, 0),  // Orange (High emissions)
                Color.rgb(255, 69, 0),   // Red (Very high emissions)
                Color.rgb(128, 0, 0)     // Dark Red (Severe emissions)
        );
    }

    /**
     * Overrides the default toString() method to display a user-friendly name.
     * @return A string representing the name of this color scheme.
     */
    @Override
    public String toString() {
        return "Default";
    }
}
