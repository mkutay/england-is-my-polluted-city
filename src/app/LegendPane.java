package app;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LegendPane extends VBox {
    private Label titleLabel;
    private Label titleIcon;
    private VBox content;
    private boolean expanded = false;

    public LegendPane() {
        getStyleClass().add("legend-pane");

        HBox header = new HBox();
        titleLabel = new Label("Legend");
        titleLabel.getStyleClass().add("legend-title");
        titleIcon = new Label("☰");
        titleIcon.getStyleClass().add("legend-title-icon");
        header.getChildren().addAll(titleIcon, titleLabel);
        header.getStyleClass().add("legend-header");

        // Content area (initially hidden)
        content = new VBox();
        content.getStyleClass().add("legend-content");
        content.setVisible(false); // Start hidden
        content.setManaged(false); // Prevent layout from affecting surrounding elements

        // Add pollutant ranges (visible when expanded)
        addLegendItem("0-3 ppm", Color.GREEN);
        addLegendItem("3-6 ppm", Color.ORANGE);
        addLegendItem(">6 ppm", Color.RED);

        getChildren().addAll(header, content);
        getStyleClass().add("legend-pane");

        // Toggle visibility when clicking the title
        header.setOnMouseClicked(e -> toggle());
    }

    private void addLegendItem(String label, Color color) {
        HBox item = new HBox(5);
        Label itemLabel = new Label(label);
        item.getStyleClass().add("legend-item");
        Circle colorBox = new Circle(7.5,color);
        item.getChildren().addAll(colorBox, itemLabel);
        content.getChildren().add(item);
    }

    private void toggle() {
        expanded = !expanded;

        if (expanded) {
            content.setVisible(true);
            content.setManaged(true);

            // Expand animation
            Timeline expand = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(content.maxHeightProperty(), 100),
                            new KeyValue(content.opacityProperty(), 1)
                    )
            );
            expand.play();
        } else {
            // Collapse animation
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
