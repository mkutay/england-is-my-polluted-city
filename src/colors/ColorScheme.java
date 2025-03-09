package colors;

import javafx.scene.paint.Color;

/**
 * Interface for color schemes used to visualize pollution data.
 * Different implementations can provide various ways to map data values to colors.
 * 
 * @author Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public interface ColorScheme {
    /**
     * Maps a normalized value (0.0 to 1.0) to a color.
     * @param normalizedValue A value between 0.0 and 1.0 representing data intensity.
     * @return Color corresponding to the value.
     */
    Color getColor(double normalizedValue);
}