package colors;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Default color scheme implementation for pollution visualization.
 * Uses a gradient from green (low) to yellow (medium) to red (high) to purple (very high).
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DefaultColorScheme extends ColorScheme {
    public DefaultColorScheme() {
        colors = List.of(Color.GREEN, Color.YELLOW, Color.RED, Color.rgb(128, 0, 128));
    }
}
