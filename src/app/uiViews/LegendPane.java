package app.uiViews;

import colors.ColorSchemeManager;
import dataProcessing.Pollutant;
import utility.ImageUtils;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.geometry.Insets;

import java.util.List;

/**
 * Adds a legend that displays the pollution level and its corresponding colour.
 * 
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class LegendPane extends VBox {
    private final VBox content;

    private boolean expanded = false; // Tracks whether the legend content area is expanded.

    /**
     * Constructor.
     */
    public LegendPane() {
        HBox header = new HBox();
        Label titleLabel = new Label("Legend");
        ImageView icon = ImageUtils.createImage("/resources/icons/hamburger.png", 18);
        HBox.setMargin(icon, new Insets(2.5, 0, 0, 0));
        Tooltip.install(header, new Tooltip("Click to expand"));

        header.getChildren().addAll(icon, titleLabel);
        titleLabel.getStyleClass().add("legend-title");
        header.getStyleClass().add("legend-header");
        getStyleClass().add("legend-pane");

        // Content area (initially hidden).
        content = new VBox();
        content.getStyleClass().add("legend-content");
        content.setVisible(false); // Start hidden.
        content.setManaged(false); // Prevent layout from affecting surrounding elements.

        setPrefWidth(USE_COMPUTED_SIZE);
        setMaxWidth(USE_PREF_SIZE);

        getChildren().addAll(header, content);
        getStyleClass().add("legend-pane");

        // Toggle visibility when clicking the title.
        header.setOnMouseClicked(e -> toggle());
    }

    /**
     * Updates the legend according to the color scheme and maximum pollution value.
     * @param colorSchemeManager The color scheme manager.
     * @param maxPollutionValue The maximum pollution value.
     */
    public void updateLegend(ColorSchemeManager colorSchemeManager, double maxPollutionValue) {
        clearLegendItems();

        List<Color> colors = colorSchemeManager.getColorScheme().getColors();
        for (int i = 0; i < colors.size(); i++) {
            Color color = colors.get(i);

            double lowerBound = ((double) i / colors.size()) * maxPollutionValue;
            double upperBound = ((double) (i + 1) / colors.size()) * maxPollutionValue;

            lowerBound = (double) (int) (lowerBound * 10) / 10; // Round to 1 decimal place.
            upperBound = (double) (int) (upperBound * 10) / 10;

            String label = lowerBound + "-" + upperBound + " " + Pollutant.UNITS;

            addLegendItem(label, color);
        }
    }

    /**
     * Adds a legend item to the legend.
     * @param label The label.
     * @param color The color.
     */
    private void addLegendItem(String label, Color color) {
        HBox item = new HBox(5);
        Label itemLabel = new Label(label);
        item.getStyleClass().add("legend-item");

        Circle colorBox = new Circle(7.5,color);
        colorBox.getStyleClass().add("legend-circle");
        
        item.getChildren().addAll(colorBox, itemLabel);
        content.getChildren().add(item);
    }

    /**
     * Clears all legend items.
     */
    private void clearLegendItems() {
        content.getChildren().clear();
    }

    /**
     * Toggles the visibility of the content area of the legend.
     */
    private void toggle() {
        expanded = !expanded;

        if (expanded) {
            content.setVisible(true);
            content.setManaged(true);

            // Expand animation:
            Timeline expand = new Timeline(
                new KeyFrame(Duration.millis(200),
                    new KeyValue(content.maxHeightProperty(), 100),
                    new KeyValue(content.opacityProperty(), 1)
                )
            );
            expand.play();
        } else {
            // Collapse animation:
            Timeline collapse = new Timeline(
                new KeyFrame(Duration.millis(200),
                    new KeyValue(content.maxHeightProperty(), 0),
                    new KeyValue(content.opacityProperty(), 0)
                )
            );
            collapse.setOnFinished(e -> {
                content.setVisible(false);
                content.setManaged(false);
            });
            collapse.play();
        }
    }
}