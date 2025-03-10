package colors;

import javafx.scene.paint.Color;

/**
 * Default color scheme implementation for pollution visualization.
 * Uses a gradient from green (low) to yellow (medium) to red (high).
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DefaultColorScheme implements ColorScheme {
    @Override
    public Color getColor(double normalizedValue) {
        // Ensure value is within range:
        normalizedValue = Math.min(1.0, Math.max(0.0, normalizedValue));
        
        // Green (low) to yellow (medium) to red (high) gradient
        if (normalizedValue < 0.5) {
            // Green to yellow (0.0 - 0.5)
            double ratio = normalizedValue * 2;
            return Color.color(ratio, 1.0, 0.0);
        } else {
            // Yellow to red (0.5 - 1.0)
            double ratio = (normalizedValue - 0.5) * 2;
            return Color.color(1.0, 1.0 - ratio, 0.0);
        }
    }
}
