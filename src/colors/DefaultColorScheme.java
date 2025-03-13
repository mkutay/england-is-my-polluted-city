package colors;

import javafx.scene.paint.Color;

/**
 * Default color scheme implementation for pollution visualization.
 * Uses a gradient from green (low) to yellow (medium) to red (high).
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DefaultColorScheme extends ColorScheme {
    @Override
    public Color getColor(double normalisedValue) {
        // Ensure value is within range:
        normalisedValue = Math.min(1.0, Math.max(0.0, normalisedValue));
        
        // Green (low) to yellow (medium) to red (high) gradient
        if (normalisedValue < 0.5) {
            // Green to yellow (0.0 - 0.5)
            double ratio = normalisedValue * 2;
            return Color.color(ratio, 1.0, 0.0);
        } else {
            // Yellow to red (0.5 - 1.0)
            double ratio = (normalisedValue - 0.5) * 2;
            return Color.color(1.0, 1.0 - ratio, 0.0);
        }
    }
}
