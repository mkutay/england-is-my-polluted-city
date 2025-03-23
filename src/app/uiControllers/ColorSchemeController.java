package app.uiControllers;

import colors.*;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * Controller for managing color scheme selection via dropdown.
 * 
 * Refactor and class by Mehmet Kutay Bozkurt
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class ColorSchemeController {
    private final ComboBox<ColorScheme> colorDropdown;

    private Consumer<ColorScheme> onColorSchemeChangedCallback;

    /**
     * Constructor for ColorSchemeController.
     */
    public ColorSchemeController() {
        this.colorDropdown = new ComboBox<>();
        initialiseColorDropdown();
    }

    /**
     * Initialises the color scheme dropdown with available options.
     */
    private void initialiseColorDropdown() {
        colorDropdown.setMaxWidth(Double.MAX_VALUE);
        colorDropdown.getItems().clear();
        colorDropdown.getItems().addAll(new DefaultColorScheme(), new ColorblindColorScheme());
        colorDropdown.getSelectionModel().selectFirst();
        
        colorDropdown.setOnAction(e -> notifyColorSchemeChanged());
    }

    /**
     * Notify listeners that the color scheme has changed.
     */
    private void notifyColorSchemeChanged() {
        if (onColorSchemeChangedCallback != null && colorDropdown.getValue() != null) {
            onColorSchemeChangedCallback.accept(colorDropdown.getValue());
        }
    }

    /**
     * Set a callback for when the color scheme changes.
     * @param callback Consumer that takes the selected ColorScheme.
     */
    public void setOnColorSchemeChanged(Consumer<ColorScheme> callback) {
        this.onColorSchemeChangedCallback = callback;
        // Initial notification with current value:
        notifyColorSchemeChanged();
    }

    /**
     * Creates a VBox containing the color scheme selection dropdown and label.
     * @return VBox with color scheme selector.
     */
    public VBox createColorSelector() {
        Label label = new Label("Color Scheme:");
        return new VBox(6, label, colorDropdown);
    }

    /**
     * @return The currently selected color scheme.
     */
    public ColorScheme getSelectedColorScheme() {
        return colorDropdown.getValue();
    }
}
