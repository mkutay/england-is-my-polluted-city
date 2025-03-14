package colors;

/**
 * Holds the current active colour scheme and handles updating it when necessary
 * TODO complete
 * @author Anas Ahmed
 */
public class ColorSchemeManager {
    private ColorScheme colorScheme;

    public ColorSchemeManager(){
        colorScheme = new DefaultColorScheme();
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public void updateColorScheme(){}
}
