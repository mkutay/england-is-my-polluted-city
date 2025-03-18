package app;

import app.uiControllers.MapController;
import app.uiControllers.NavigationBarController;
import app.uiControllers.SidePanelController;
import app.uiViews.MapOverlay;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Separator;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import dataProcessing.Pollutant;
import colors.ColorSchemeManager;
import utility.CustomMapView;

/**
 * The MainLayoutHandler class is responsible for setting up the main UI layout of the application.
 * It initialises the map, user interface components, and manages the scene structure.
 *
 * This class separates layout handling from the App class, improving modularity and maintainability.
 * It initialises the MapController and UIController and arranges them within a BorderPane.
 *
 * The UI consists of:
 * A top navigation bar
 * A side panel on the left with a vertical separator
 * A central map overlay
 *
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class MainLayoutHandler {
    private final BorderPane root;
    private final NavigationBarController navBarController;
    private final SidePanelController sidePanelController;
    private final MapController mapController;
    private final ColorSchemeManager colorSchemeManager;
    private final MapOverlay mapOverlay;

    public MainLayoutHandler(Stage stage, App app) throws PollutionLayerNotInitialisedException {
        // Setup UI controller
        this.root = new BorderPane();

        CustomMapView mapView = new CustomMapView();
        this.mapOverlay = new MapOverlay(mapView);
        this.colorSchemeManager = new ColorSchemeManager();
        this.mapController = new MapController(stage, colorSchemeManager, mapOverlay);

        // Initialise map with default values
        mapController.initialisePollutionLayer(2018, Pollutant.NO2);

        this.navBarController = new NavigationBarController(app);
        this.sidePanelController = new SidePanelController(mapController, root);

        setupLayout();
    }

    private void setupLayout() {
        Separator verticalSeparator = new Separator(Orientation.VERTICAL);

        HBox leftPane = new HBox();
        leftPane.getChildren().addAll(sidePanelController.getSidePanel(), verticalSeparator);

        root.setTop(navBarController.getMenuBar());
        root.setLeft(leftPane);
        root.setCenter(mapOverlay.getOverlayPane());
    }

    public Scene createScene() {
        Scene scene = new Scene(root, 900, 800);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        return scene;
    }
}
