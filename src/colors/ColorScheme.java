package colors;

import javafx.scene.paint.Color;

/**
 * Maps a normalised value from 0-1 to a range of colours
 * Subclasses can define different colour sets to use
 *
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class ColorScheme {
    private Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED, Color.rgb(128, 0, 128)};

    private Color interpolateColor(Color start, Color end, double fraction) {
        int r = (int) (start.getRed() + fraction * (end.getRed() - start.getRed()));
        int g = (int) (start.getGreen() + fraction * (end.getGreen() - start.getGreen()));
        int b = (int) (start.getBlue() + fraction * (end.getBlue() - start.getBlue()));
        return Color.rgb(r, g, b);
    }

    /**
     * Maps a value from 0-1 to a colour based off of the colours defined in the color scheme
     * @param normalisedValue Normalised pollution value from 0-1
     * @return Color corresponding to the value.
     */
    public Color getColor(double normalisedValue){
        //TODO finish

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

    public Color[] getColors() {
        return colors;
    }
}