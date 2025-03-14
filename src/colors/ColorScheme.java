package colors;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Maps a normalised value from 0-1 to a range of colours
 * Subclasses can define different colour sets to use
 *
 * @author Mehmet Kutay Bozkurt and Anas Ahmed
 * @version 1.0
 */
public abstract class ColorScheme {
    protected List<Color> colors;

    /**
     * Linearly interpolate from a to b with ratio t
     */
    private static double lerp(double a, double b, double t){
        return a + t * (b - a);
    }

    /**
     * Linearly interpolates between two colours
     * @param start the start colour
     * @param end the end colour
     * @param ratio the percentage from the start to the end colour to interpolate by
     * @return the linearly interpolated colour
     */
    private Color interpolateColor(Color start, Color end, double ratio) {
        int r = (int) (lerp(start.getRed(), end.getRed(), ratio) * 255);
        int g = (int) (lerp(start.getGreen(), end.getGreen(), ratio) * 255);
        int b = (int) (lerp(start.getBlue(), end.getBlue(), ratio) * 255);
        return Color.rgb(r, g, b);
    }


    /**
     * Maps a value from 0-1 to a colour based off of the colours defined in the color scheme
     * @param normalisedValue Normalised pollution value from 0-1
     * @return Color corresponding to the value.
     */
    public Color getColor(double normalisedValue){
        int numSegments = colors.size() - 1;
        for (int i = 0; i < numSegments; i++) {
            double lowerBound = (double) i / numSegments;
            double upperBound = (double) (i + 1) / numSegments;

            if (normalisedValue >= lowerBound && normalisedValue <= upperBound) {
                double ratio = (normalisedValue - lowerBound) / (upperBound - lowerBound);
                return interpolateColor(colors.get(i), colors.get(i + 1), ratio);
            }
        }

        return colors.getLast(); //Fallback case
    }

    public List<Color> getColors() {
        return colors;
    }
}