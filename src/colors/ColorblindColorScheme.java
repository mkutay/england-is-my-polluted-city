package colors;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * A colorblind-friendly color scheme designed for pollution visualisation.
 * This scheme ensures distinguishability for individuals with color vision deficiencies,
 * including deuteranopia, protanopia, and tritanopia.
 *
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class ColorblindColorScheme extends ColorScheme {
    public ColorblindColorScheme() {
        colors = List.of(
            Color.rgb(0, 158, 115), // Greenish-blue (Low emissions).
            Color.rgb(240, 228, 66), // Yellow (Moderate emissions).
            Color.rgb(204, 121, 167) ,// Pinkish-purple (High emissions).
            Color.rgb(86, 180, 233), // Light Blue (Very high emissions).
            Color.rgb(0, 0, 0) // Black (Severe emissions).
        );
    }

    @Override
    public String toString() {
        return "Colourblind";
    }
}
