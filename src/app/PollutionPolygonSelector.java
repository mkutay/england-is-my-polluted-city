package app;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import utility.CustomMapView;

/**
 * Selection system for polygons
 *
 * TODO finish implementing or discard
 *
 * @author Anas Ahmed
 */
public class PollutionPolygonSelector {
    private final CustomMapView mapView;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private boolean isSelecting;

    private double currentMouseX;
    private double currentMouseY;

    private double initialMouseX;
    private double initialMouseY;

    public PollutionPolygonSelector(CustomMapView mapView, StackPane mapOverlay){
        this.mapView = mapView;

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        mapOverlay.getChildren().add(canvas);

        mapOverlay.setOnMousePressed(this::beginSelection);
        mapOverlay.setOnMouseReleased(this::endSelection);
        mapOverlay.setOnMouseDragged(this::updateMousePosition);

        isSelecting = false;
    }

    private void redrawSelection() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.setWidth(mapView.getWidth());
        canvas.setHeight(mapView.getHeight());

        if (isSelecting) {
            double x = Math.min(initialMouseX, currentMouseX);
            double y = Math.min(initialMouseY, currentMouseY);

            double width = Math.abs(currentMouseX - initialMouseX);
            double height = Math.abs(currentMouseY - initialMouseY);

            gc.setFill(Color.RED);
            gc.fillRect(x, y, width, height);
        }
    }

    private void updateMousePosition(MouseEvent event) {
        currentMouseX = event.getX();
        currentMouseY = event.getY();
        redrawSelection();

    }

    private void beginSelection(MouseEvent event) {
        isSelecting = true;
        initialMouseX = event.getX();
        initialMouseY = event.getY();
    }

    private void endSelection(MouseEvent event) {
        isSelecting = false;
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
