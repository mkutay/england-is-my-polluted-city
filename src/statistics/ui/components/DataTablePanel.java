package statistics.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A reusable JavaFX component for displaying tabular data.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 1.0
 */
public class DataTablePanel<T> extends VBox {
    private final GridPane gridPane;
    private final String[] columnHeaders;
    private final String[] columnHeaderStrings;
    private final List<T> dataItems;
    private final List<Function<T, String>> columnValueFunctions;
    private final List<Function<T, String>> columnTooltipFunctions;
    
    private List<Double> columnWidthPercentages;
    
    /**
     * Constructor for creating a table with column headers and data.
     * @param title The title of the table (optional, can be null).
     * @param columnHeaders Array of column header strings.
     */
    public DataTablePanel(String title, String[] columnHeaders, String[] columnHeaderMeanings) {
        this.columnHeaders = columnHeaders;
        this.columnHeaderStrings = columnHeaderMeanings;
        this.dataItems = new ArrayList<>();
        this.columnValueFunctions = new ArrayList<>();
        this.columnTooltipFunctions = new ArrayList<>();
        this.columnWidthPercentages = null;
        
        setPadding(new Insets(10));
        setSpacing(5);
        
        // Add title, if provided.
        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            titleLabel.setPadding(new Insets(0, 0, 5, 0));
            titleLabel.getStyleClass().add("data-table-title");
            getChildren().add(titleLabel);
        }
        
        // Create the grid for the table
        gridPane = new GridPane();
        gridPane.setHgap(0); // Remove horizontal gap between columns.
        gridPane.setVgap(0); // Remove vertical gap between rows.
        gridPane.setPadding(new Insets(0)); // Remove padding.
        gridPane.getStyleClass().add("data-table-grid");
        gridPane.setMaxWidth(Double.MAX_VALUE); // Allow grid to expand horizontally.
        
        addHeaderRow(); // Add column headers.
        
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        getChildren().add(gridPane);
        
        setEqualColumnWidths();
    }
    
    /**
     * Add a column with a function to extract the value from each data item.
     * @param valueFunction Function that takes a data item and returns a string value for the column.
     * @return This panel instance for method chaining.
     */
    public DataTablePanel<T> addColumnValueFunction(Function<T, String> valueFunction) {
        columnValueFunctions.add(valueFunction);
        return this;
    }

    /**
     * Add a column with a function to extract the tooltip from each data item.
     * @param tooltipFunction Function that takes a data item and returns a string value for the column tooltip.
     * @return This panel instance to chain methods.
     */
    public DataTablePanel<T> addColumnTooltipFunction(Function<T, String> tooltipFunction) {
        columnTooltipFunctions.add(tooltipFunction);
        return this;
    }
    
    /**
     * Set the data items for the table and refresh the display.
     * @param items List of data items.
     */
    public void setData(List<T> items) {
        dataItems.clear();
        dataItems.addAll(items);
        refreshTable();
    }
    
    /**
     * Set specific width percentages for each column.
     * @param percentages List of percentage values (should add up to 100).
     * @return This panel instance for method chaining.
     */
    public DataTablePanel<T> setColumnWidthPercentages(List<Double> percentages) {
        if (percentages.size() != columnHeaders.length) {
            throw new IllegalArgumentException("Number of percentages must match number of columns: " + percentages.size() + " vs " + columnHeaders.length);
        }
        
        this.columnWidthPercentages = new ArrayList<>(percentages);
        applyColumnConstraints();
        return this;
    }
    
    /**
     * Sets all columns to equal widths.
     * @return This panel instance for method chaining.
     */
    public DataTablePanel<T> setEqualColumnWidths() {
        List<Double> equalPercentages = new ArrayList<>();
        double percentage = 100.0 / columnHeaders.length;
        
        for (int i = 0; i < columnHeaders.length; i++) {
            equalPercentages.add(percentage);
        }
        
        this.columnWidthPercentages = equalPercentages;
        applyColumnConstraints();
        return this;
    }
    
    /**
     * Apply column constraints based on column width percentages.
     */
    private void applyColumnConstraints() {
        gridPane.getColumnConstraints().clear();
        
        for (int i = 0; i < columnHeaders.length; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            
            // Set percentage width, if available.
            if (columnWidthPercentages != null && i < columnWidthPercentages.size()) {
                columnConstraints.setPercentWidth(columnWidthPercentages.get(i));
            }
            
            columnConstraints.setFillWidth(true);
            columnConstraints.setHgrow(Priority.ALWAYS);
            
            gridPane.getColumnConstraints().add(columnConstraints);
        }
    }
    
    /**
     * Add a data item to the table and refresh the display.
     * @param item Data item to add.
     */
    public void addDataItem(T item) {
        dataItems.add(item);
        refreshTable();
    }
    
    /**
     * Clear all data items from the table.
     */
    public void clearData() {
        dataItems.clear();
        refreshTable();
    }
    
    /**
     * Add the header row to the table.
     */
    private void addHeaderRow() {
        // Clear existing header row
        for (int i = 0; i < columnHeaders.length; i++) {
            Label headerLabel = new Label(columnHeaders[i]);
            headerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
            headerLabel.getStyleClass().add("data-table-header");
            headerLabel.setMaxWidth(Double.MAX_VALUE); // Allow header to expand.
            headerLabel.setPadding(new Insets(4)); // Add padding inside header cell.
            Tooltip tooltip = new Tooltip(columnHeaderStrings[i]);
            tooltip.setShowDelay(Duration.millis(80));
            Tooltip.install(headerLabel, tooltip);
            GridPane.setFillWidth(headerLabel, true);
            gridPane.add(headerLabel, i, 0);
        }
    }
    
    /**
     * Refresh the table with current data items.
     */
    private void refreshTable() {
        // Clear all rows except header.
        gridPane.getChildren().clear();
        addHeaderRow();
        
        applyColumnConstraints();
        
        // Add data rows:
        for (int rowIndex = 0; rowIndex < dataItems.size(); rowIndex++) {
            T dataItem = dataItems.get(rowIndex);
            
            // Add a style class for alternating rows:
            String rowStyleClass = (rowIndex % 2 == 0) ? "data-table-row-even" : "data-table-row-odd";
            
            for (int colIndex = 0; colIndex < columnValueFunctions.size() && colIndex < columnHeaders.length; colIndex++) {
                String cellValue = columnValueFunctions.get(colIndex).apply(dataItem);
                Label valueLabel = new Label(cellValue);
                valueLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
                valueLabel.getStyleClass().addAll("data-table-cell", rowStyleClass);
                valueLabel.setMaxWidth(Double.MAX_VALUE); // Allow cell to expand.
                valueLabel.setPadding(new Insets(4)); // Add padding inside cell.
                Tooltip tooltip = new Tooltip(columnTooltipFunctions.get(colIndex).apply(dataItem));
                tooltip.setShowDelay(Duration.millis(80));
                Tooltip.install(valueLabel, tooltip);
                GridPane.setFillWidth(valueLabel, true);
                gridPane.add(valueLabel, colIndex, rowIndex + 1); // +1 for header row.
            }
        }
    }
    
    /**
     * Format a double value for display.
     * Static utility method for use in column value functions.
     * @param value The value to format.
     * @return Formatted string representation or "N/A" for NaN.
     */
    public static String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return "N/A";
        }
        return String.format("%.2f", value);
    }
}