package statistics.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import statistics.types.StatisticsResult;

/**
 * Base panel for displaying statistics results using JavaFX.
 * This serves as the parent class for all statistics display panels.
 * 
 * @author Mehmet Kutay Bozkurt
 * @version 2.0
 */
public abstract class StatisticsPanel extends BorderPane {
    protected StatisticsResult statisticsResult;

    private final VBox headerPanel;
    private final VBox contentPanel;
    
    /**
     * Constructor.
     * @param result The statistics result to display.
     */
    public StatisticsPanel(StatisticsResult result) {
        this.statisticsResult = result;
        
        // Create header with title and description:
        headerPanel = createHeaderPanel();
        setTop(headerPanel);
        
        // Create content panel for statistics data.
        contentPanel = new VBox(10);
        contentPanel.setPadding(new Insets(10));
        contentPanel.setAlignment(Pos.TOP_LEFT);
        
        // Add scrolling to content panel.
        ScrollPane scrollPane = new ScrollPane(contentPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        setCenter(scrollPane);
    }
    
    /**
     * Create the header panel with title and description.
     * @return The configured header panel.
     */
    private VBox createHeaderPanel() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.TOP_LEFT);
        header.setStyle("-fx-background-color: #dddddd;");
        
        // Add title:
        Label titleLabel = new Label(statisticsResult.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        header.getChildren().add(titleLabel);
        
        // Add description:
        Label descriptionLabel = new Label(statisticsResult.getDescription());
        descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        header.getChildren().add(descriptionLabel);
        
        return header;
    }

    /**
     * Initialise the main statistics content of the panel.
     * This method should be implemented by subclasses to display
     * their specific statistics data.
     */
    protected abstract void initialiseContent();
    
    /**
     * Update the displayed statistics with new data.
     * @param result The new statistics result to display.
     */
    public void updateStatistics(StatisticsResult result) {
        this.statisticsResult = result;
        contentPanel.getChildren().clear();
        initialiseContent();
    }
    
    /**
     * Helper method to add a key-value pair to the content panel, 
     * such as the highest over a period of time.
     * @param key The key description.
     * @param value The value to display.
     */
    protected void addKeyValueRow(String key, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(2, 5, 2, 5));
        
        Label keyLabel = new Label(key + ":");
        keyLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        keyLabel.setMinWidth(190);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        valueLabel.setWrapText(true);
        
        row.getChildren().addAll(keyLabel, valueLabel);
        contentPanel.getChildren().add(row);
    }
    
    /**
     * Add a node to the content panel.
     * @param node The node to add.
     */
    protected void addToContent(Node node) {
        contentPanel.getChildren().add(node);
    }
    
    /**
     * Add a separator to the content panel.
     */
    protected void addSeparator() {
        Separator separator = new Separator();
        contentPanel.getChildren().add(separator);
    }

    /**
     * Format a double value to 3 decimal places.
     * @param value The value to format.
     * @return The formatted value.
     */
    protected String formatDouble(double value) {
        if (Double.isNaN(value)) {
            return "N/A";
        }
        return String.format("%.2f", value);
    }
}
