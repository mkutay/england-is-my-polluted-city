package app;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import dataProcessing.Pollutant;
import utility.Namer;
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
    private static MapController mapController;
    private UIController uiController;
    private StatisticsController statisticsController;

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException {
        stage.setTitle(Namer.APP_NAME);
        mapController = new MapController(stage);
        statisticsController = new StatisticsController();

        // Create root layout
        BorderPane root = new BorderPane();
        
        // Create a StackPane to overlay legend inside the map
        StackPane mapOverlay = new StackPane();
        mapOverlay.getStyleClass().add("map-overlay");

        LegendPane legend = new LegendPane();
        // Position legend near the top right of map
        StackPane.setAlignment(legend, Pos.TOP_RIGHT);

        mapController.initialisePollutionLayer(2020, Pollutant.NO2);
        mapOverlay.getChildren().addAll(mapController.getMapView(), legend);
        
        uiController = new UIController(mapController, statisticsController, root);
        
        root.setTop(uiController.getTopNav());
        root.setLeft(uiController.getSidePanel());
        root.setCenter(mapOverlay);

        Scene scene = new Scene(root, 900, 900);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}