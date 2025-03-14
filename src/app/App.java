package app;

import colors.ColorSchemeManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dataProcessing.Pollutant;

import app.uiControllers.StatisticsController;

/**
 * The main App class as the entry point to the application. This class creates
 * a JavaFX application that can display a map of UK with pollution data.
 * It centres the map on London.
 *
 * Refactor by Anas Ahmed
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 3.0
 */
public class App extends Application {
    public static final String APP_NAME = "UK Emissions Interactive Map";

    private static MapController mapController;
    private UIController uiController;
    private StatisticsController statisticsController;
    private ColorSchemeManager colorSchemeManager;

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException {
        stage.setTitle(APP_NAME);

        colorSchemeManager = new ColorSchemeManager();
        mapController = new MapController(stage, colorSchemeManager);
        statisticsController = new StatisticsController();

        // Create root layout
        BorderPane root = new BorderPane();

        mapController.initialisePollutionLayer(2018, Pollutant.NO2);
        uiController = new UIController(mapController, statisticsController, root);

        root.setTop(uiController.getTopNav());
        root.setLeft(uiController.getSidePanel());
        root.setCenter(mapController.getMapOverlay());

        Scene scene = new Scene(root, 900, 900);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}