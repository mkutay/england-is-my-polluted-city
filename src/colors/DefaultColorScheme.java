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
                Color.rgb(53, 215, 0),
                Color.rgb(255, 220, 0),
                Color.rgb(255, 20, 60),
                Color.rgb(115, 0, 120),
                Color.rgb(0, 0, 0)
        );

    }
}
