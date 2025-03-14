package app.uiControllers;

import colors.ColorScheme;
import colors.ColorblindColorScheme;
import colors.DefaultColorScheme;
import dataProcessing.DataManager;
import dataProcessing.Pollutant;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Controller for the pollutant and year selection dropdowns in the side panel.
 * 
 * Refactor and class by Mehmet Kutay Bozkurt.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class PollutionSelectorController {
    private final DataManager dataManager;
    private final ComboBox<Pollutant> pollutantDropdown;
    private final ComboBox<Integer> yearDropdown;
    private final ComboBox<ColorScheme> colorDropdown; //todo move to separate class
    private TriConsumer<Integer, Pollutant, ColorScheme> onSelectionChangedCallback;

    /**
     * Constructor for PollutionSelectorController.
     */
    public PollutionSelectorController() {
        this.dataManager = DataManager.getInstance();
        this.pollutantDropdown = new ComboBox<>();
        this.yearDropdown = new ComboBox<>();
        this.colorDropdown = new ComboBox<>();

        initialiseDropdowns();
    }
    
    /**
     * Initialise the dropdown values and listeners.
     */
    private void initialiseDropdowns() {
        // Set up pollutant dropdown:
        //pollutantDropdown.getStyleClass().add("dropdown");
        pollutantDropdown.getItems().addAll(Arrays.asList(Pollutant.values()));
        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);
        //pollutantDropdown.setPromptText("Select Pollutant");
        pollutantDropdown.getSelectionModel().selectFirst();

        // Modifies names of pollutant dropdown items
        pollutantDropdown.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
        // Modifies name for selected item
        pollutantDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        // Set up year dropdown:
        //yearDropdown.getStyleClass().add("dropdown");
        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        //yearDropdown.setPromptText("Select Year");
        // Update year dropdown with years available for initial pollutant.
        updateYearDropdown();

        // Set up colour dropdown:
        //colorDropdown.getStyleClass().add("dropdown");
        colorDropdown.setMaxWidth(Double.MAX_VALUE);
        //colorDropdown.setPromptText("Select Colour");
        updateColorDropdown();

        
        // Set up listeners:
        pollutantDropdown.setOnAction(e -> {
            updateYearDropdown(); // Validate the year dropdown values when the pollutant changes.
            notifySelectionChanged();
        });
        yearDropdown.setOnAction(e -> notifySelectionChanged());
        colorDropdown.setOnAction(e -> notifySelectionChanged());
    }
    
    /**
     * Updates the year dropdown with years available for the selected pollutant.
     */
    private void updateYearDropdown() {
        Pollutant selectedPollutant = pollutantDropdown.getValue();
        Integer currentYear = yearDropdown.getValue();
        
        yearDropdown.getItems().clear();
        
        List<Integer> years = dataManager.getAvailableYears(selectedPollutant);
        Collections.sort(years);
        yearDropdown.getItems().addAll(years);
        
        // Try to maintain the current year selection, if possible.
        if (currentYear != null && yearDropdown.getItems().contains(currentYear)) {
            yearDropdown.setValue(currentYear);
        } else {
            yearDropdown.getSelectionModel().selectFirst();
        }
    }

    /**
     * Populates the color scheme dropdown with available options.
     */
    private void updateColorDropdown() {
        colorDropdown.getItems().clear();
        colorDropdown.getItems().addAll(new DefaultColorScheme(), new ColorblindColorScheme());
        colorDropdown.getSelectionModel().selectFirst();
    }

    /**
     * Notify listeners that the selection has changed.
     */
    private void notifySelectionChanged() {
        if (onSelectionChangedCallback != null && 
            yearDropdown.getValue() != null && 
            pollutantDropdown.getValue() != null &&
            colorDropdown.getValue() != null) {
            onSelectionChangedCallback.accept(yearDropdown.getValue(), pollutantDropdown.getValue(), colorDropdown.getValue());
        }
    }
    
    /**
     * Set a callback for when either selection changes.
     * @param callback BiConsumer that takes the selected year and pollutant
     */
    public void setOnSelectionChanged(TriConsumer<Integer, Pollutant, ColorScheme> callback) {
        this.onSelectionChangedCallback = callback;
        // Initial notification with current values.
        notifySelectionChanged();
    }
    
    /**
     * Create a VBox with the pollutant selection dropdown and label.
     * @return VBox containing the pollutant dropdown.
     */
    public VBox createPollutantSelector() {
        Label label = new Label("Pollutant:");
        return new VBox(6, label, pollutantDropdown);
    }
    
    /**
     * Create a VBox with the year selection dropdown and label.
     * @return VBox containing the year dropdown.
     */
    public VBox createYearSelector() {
        Label label = new Label("Year:");
        return new VBox(6, label, yearDropdown);
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
     * @return The selected year from the dropdown.
     */
    public Integer getSelectedYear() {
        return yearDropdown.getValue();
    }
    
    /**
     * @return The selected pollutant from the dropdown.
     */
    public Pollutant getSelectedPollutant() {
        return pollutantDropdown.getValue();
    }

    /**
     * @return The selected colour theme from the dropdown.
     */
    public ColorScheme getSelectedColorScheme() { return colorDropdown.getValue();}

    /**
     * Functional interface to allow three parameters in a callback.
     */
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

}