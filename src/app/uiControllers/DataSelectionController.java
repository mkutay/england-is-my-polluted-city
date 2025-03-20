package app.uiControllers;

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
 * Controller for managing data selection (pollutant and year) via dropdowns.
 * 
 * Refactor and class by Mehmet Kutay Bozkurt
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 3.0
 */
public class DataSelectionController {
    private final DataManager dataManager;
    private final ComboBox<Pollutant> pollutantDropdown;
    private final ComboBox<Integer> yearDropdown;
    private final ComboBox<Integer> endYearDropdown;

    private BiConsumer<Integer, Pollutant> onSelectionChangedCallback; // Single year callback with pollutant.
    private TriConsumer<Integer, Integer, Pollutant> onRangeSelectionChangedCallback; // Year range callback with pollutant.
    private Label yearLabel;
    
    private boolean updatingDropdowns = false; // Flag to prevent recursive updates.

    /**
     * Constructor for DataSelectionController.
     */
    public DataSelectionController() {
        this.dataManager = DataManager.getInstance();
        this.pollutantDropdown = new ComboBox<>();
        this.yearDropdown = new ComboBox<>();
        this.endYearDropdown = new ComboBox<>();
        this.yearLabel = new Label("Year:");

        initialiseDropdowns();
    }
    
    /**
     * Initialise the dropdown values and listeners.
     */
    private void initialiseDropdowns() {
        pollutantDropdown.getItems().addAll(Arrays.asList(Pollutant.values()));
        pollutantDropdown.setMaxWidth(Double.MAX_VALUE);
        
        // Setup cell factories for pollutant display names.
        setupPollutantCellFactory();
        
        yearDropdown.setMaxWidth(Double.MAX_VALUE);
        endYearDropdown.setMaxWidth(Double.MAX_VALUE);
        
        // Select initial pollutant.
        if (!pollutantDropdown.getItems().isEmpty()) {
            pollutantDropdown.getSelectionModel().selectFirst();
        }
        
        // Initialise years based on initial pollutant.
        loadYearsForCurrentPollutant();
        
        // Set up listeners.
        setupEventListeners();
    }
    
    /**
     * Setup cell factories for the pollutant dropdown to display the pollutant names.
     */
    private void setupPollutantCellFactory() {
        pollutantDropdown.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        pollutantDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pollutant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });
    }
    
    /**
     * Loads years available for the currently selected pollutant while preserving selections.
     */
    private void loadYearsForCurrentPollutant() {
        updatingDropdowns = true;
        
        Pollutant selectedPollutant = pollutantDropdown.getValue();
        
        Integer previousStartYear = yearDropdown.getValue();
        Integer previousEndYear = endYearDropdown.getValue();
        
        List<Integer> years = dataManager.getAvailableYears(selectedPollutant);
        Collections.sort(years);
        
        // Update year dropdown.
        yearDropdown.getItems().clear();
        yearDropdown.getItems().addAll(years);
        
        // Try to restore previous start year selection, if available.
        if (years.contains(previousStartYear)) {
            yearDropdown.setValue(previousStartYear);
        } else {
            yearDropdown.getSelectionModel().selectFirst();
        }
        
        // Update end year dropdown with filtered values.
        updateEndYearDropdown(previousEndYear);

        updatingDropdowns = false;
        notifySelectionChanged();
    }
    
    /**
     * Updates the end year dropdown based on the selected start year.
     * @param preferredEndYear The end year to try to select, if available.
     */
    private void updateEndYearDropdown(Integer preferredEndYear) {
        Integer selectedYear = yearDropdown.getValue();
        if (selectedYear == null) return;
        
        Pollutant selectedPollutant = pollutantDropdown.getValue();
        List<Integer> allYears = dataManager.getAvailableYears(selectedPollutant);
        Collections.sort(allYears);
        
        List<Integer> validEndYears = allYears.stream()
            .filter(year -> year >= selectedYear)
            .toList();
        
        endYearDropdown.getItems().clear();
        endYearDropdown.getItems().addAll(validEndYears);
        
        // Try to select the preferred end year if it's valid, the last otherwise.
        if (validEndYears.contains(preferredEndYear)) {
            endYearDropdown.setValue(preferredEndYear);
        } else {
            endYearDropdown.getSelectionModel().selectLast();
        }
    }
    
    /**
     * Setup event listeners for dropdowns.
     */
    private void setupEventListeners() {
        // Pollutant changes.
        pollutantDropdown.setOnAction(e -> {
            if (updatingDropdowns || pollutantDropdown.getValue() == null) {
                return;
            }
            
            loadYearsForCurrentPollutant();
        });
        
        // Start year changes.
        yearDropdown.setOnAction(e -> {
            if (updatingDropdowns || yearDropdown.getValue() == null) {
                return;
            }
            
            updatingDropdowns = true;

            // Pass current end year to try to maintain it.
            updateEndYearDropdown(endYearDropdown.getValue());

            notifySelectionChanged();
            updatingDropdowns = false;
        });
        
        // End year changes.
        endYearDropdown.setOnAction(e -> {
            if (updatingDropdowns || endYearDropdown.getValue() == null) {
                return;
            }
            
            notifySelectionChanged();
        });
    }

    /**
     * Notify listeners that the selection has changed.
     */
    private void notifySelectionChanged() {
        Integer year = yearDropdown.getValue();
        Integer endYear = endYearDropdown.getValue();
        Pollutant pollutant = pollutantDropdown.getValue();
        
        if (year == null || pollutant == null) return;
        
        // Notify the single year callback.
        if (onSelectionChangedCallback != null) {
            onSelectionChangedCallback.accept(year, pollutant);
        }
        
        // Notify the year range callback, if we have a valid end year.
        if (onRangeSelectionChangedCallback != null && endYear != null && endYear >= year) {
            onRangeSelectionChangedCallback.accept(year, endYear, pollutant);
        }
    }
    
    /**
     * Set a callback for when either selection changes.
     * @param callback BiConsumer that takes the selected year and pollutant
     */
    public void setOnSelectionChanged(BiConsumer<Integer, Pollutant> callback) {
        this.onSelectionChangedCallback = callback;
        if (yearDropdown.getValue() != null && pollutantDropdown.getValue() != null) {
            callback.accept(yearDropdown.getValue(), pollutantDropdown.getValue());
        }
    }
    
    /**
     * Set a callback for when the year range selection changes.
     * @param callback TriConsumer that takes the start year, end year, and pollutant
     */
    public void setOnRangeSelectionChanged(TriConsumer<Integer, Integer, Pollutant> callback) {
        this.onRangeSelectionChangedCallback = callback;
        if (yearDropdown.getValue() != null && 
            endYearDropdown.getValue() != null && 
            pollutantDropdown.getValue() != null) {
            callback.accept(
                yearDropdown.getValue(), 
                endYearDropdown.getValue(), 
                pollutantDropdown.getValue()
            );
        }
    }
    
    /**
     * @return VBox containing the pollutant dropdown selection and label.
     */
    public VBox createPollutantSelector() {
        Label label = new Label("Pollutant:");
        return new VBox(6, label, pollutantDropdown);
    }
    
    /**
     * @return VBox containing the year dropdown and label.
     */
    public VBox createYearSelector() {
        yearLabel = new Label("Year:");
        return new VBox(6, yearLabel, yearDropdown);
    }
    
    /**
     * @return VBox containing the end year dropdown selection and label.
     */
    public VBox createEndYearSelector() {
        Label label = new Label("End Year:");
        return new VBox(6, label, endYearDropdown);
    }
    
    /**
     * Set the text of the year label.
     * @param text The new label text.
     */
    public void setYearLabelText(String text) {
        yearLabel.setText(text);
    }

    /**
     * @return The selected year from the dropdown.
     */
    public Integer getSelectedYear() {
        return yearDropdown.getValue();
    }
    
    /**
     * @return The selected end year from the dropdown.
     */
    public Integer getSelectedEndYear() {
        return endYearDropdown.getValue();
    }
    
    /**
     * @return The selected pollutant from the dropdown.
     */
    public Pollutant getSelectedPollutant() {
        return pollutantDropdown.getValue();
    }

    /**
     * Functional interface for a callback with two parameters.
     */
    @FunctionalInterface
    public interface BiConsumer<T, U> {
        void accept(T t, U u);
    }
    
    /**
     * Functional interface for a callback with three parameters.
     */
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}