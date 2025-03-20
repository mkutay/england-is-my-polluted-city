package app;

import app.uiControllers.MapController;
import app.uiControllers.NavigationBarController;
import app.uiControllers.SidePanelController;

import app.uiViews.MapOverlay;
import app.uiViews.SidePanel;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Separator;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import dataProcessing.Pollutant;
import colors.ColorSchemeManager;
import utility.CustomMapView;

/**
 * Manages the main UI layout of the application.
 *
 * The layout includes:
 * - A navigation bar at the top
 * - A side panel on the left
 * - A map overlay with the map, legend, zoom controls, and a toggle button for the side panel
 *
 * This class initialises and arranges these components to ensure a cohesive interface.
 *
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class MainLayoutHandler {
    private final BorderPane root;
    private final StackPane rootContainer;
    private final NavigationBarController navBarController;
    private final SidePanelController sidePanelController;
    private final MapController mapController;
    private final ColorSchemeManager colorSchemeManager;
    private final MapOverlay mapOverlay;

    /**
     * Initialises the main layout and UI components.
     *
     * @param stage The primary application stage.
     * @param app   The main application instance.
     * @throws PollutionLayerNotInitialisedException if pollution data fails to load.
     */
    public MainLayoutHandler(Stage stage, App app) throws PollutionLayerNotInitialisedException {
        // Set up main layout
        this.root = new BorderPane();
        this.rootContainer = new StackPane(root);

        // Initialise map components
        CustomMapView mapView = new CustomMapView();
        this.mapOverlay = new MapOverlay(mapView);
        this.colorSchemeManager = new ColorSchemeManager();
        this.mapController = new MapController(stage, colorSchemeManager, mapOverlay);

        // Initialise map with default values
        mapController.initialisePollutionLayer(2018, Pollutant.NO2);

        this.sidePanelController = new SidePanelController(mapController, root);
        SidePanel sidePanel = sidePanelController.getSidePanel();
        mapOverlay.setSidePanel(sidePanel);

        this.navBarController = new NavigationBarController(app);

        setupLayout();
    }

    /**
     * Arranges UI components in the layout.
     */
    private void setupLayout() {
        Separator verticalSeparator = new Separator(Orientation.VERTICAL);

        HBox leftPane = new HBox(sidePanelController.getSidePanel(), verticalSeparator);

        root.setTop(navBarController.getMenuBar());
        root.setLeft(leftPane);
        root.setCenter(mapOverlay);
    }

    /**
     * Creates and returns the main application scene.
     *
     * @return The primary Scene object.
     */
    public Scene createScene() {
        Scene scene = new Scene(rootContainer, 900, 800);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        return scene;
    }

    public StackPane getRootContainer() {
        return rootContainer;
    }
}
