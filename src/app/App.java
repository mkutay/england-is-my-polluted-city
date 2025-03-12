package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import dataProcessing.Pollutant;
import utility.Namer;

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
    private MapController mapController;
    private UIController uiController;

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException {
        stage.setTitle(Namer.APP_NAME);

        mapController = new MapController(stage);
        uiController = new UIController(mapController);

        mapController.initialisePollutionLayer(2020, Pollutant.NO2);

        BorderPane root = new BorderPane();
        root.setTop(uiController.getTopNav());
        root.setLeft(uiController.getSidePanel());
        root.setCenter(mapController.getMapView());

        Scene scene = new Scene(root, 900, 900);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}