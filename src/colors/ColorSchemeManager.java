package colors;

/**
 * Holds the current active colour scheme and handles updating it when necessary
 *
 * @author Anas Ahmed, Chelsea Feliciano
 */
public class ColorSchemeManager {
    private ColorScheme colorScheme;

    public ColorSchemeManager(){
        colorScheme = new DefaultColorScheme();
    }

    /**
     * Returns the currently active color scheme.
     * @return The active ColorScheme.
     */
    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * Updates the color scheme to a new scheme.
     * @param newScheme The new ColorScheme to apply.
     */
    public void updateColorScheme(ColorScheme newScheme) {
        if (newScheme != null) {
            this.colorScheme = newScheme;
        }
    }
}
