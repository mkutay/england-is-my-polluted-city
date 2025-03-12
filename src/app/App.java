package app;

import dataProcessing.DataManager;
import dataProcessing.Pollutant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main app.App class
 * This class creates a JavaFX application that can display a map of UK with pollution
 * data. It centres the map on London.
 *
 * Refactor by Anas Ahmed
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class App extends Application {
    private MapController mapController;
    private UIController uiController;
    private DataManager dataManager;

    private static final String APP_NAME = "UK Pollution Explorer"; //The name of the project to be displayed at the top of the application


    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException {
        stage.setTitle(APP_NAME);

        dataManager = DataManager.getInstance();

        mapController = new MapController(stage);
        uiController = new UIController(dataManager, mapController);

        mapController.initialisePollutionLayer(2020, Pollutant.NO2, dataManager);

        BorderPane root = new BorderPane();
        root.setTop(uiController.getTopNav());
        root.setLeft(uiController.getSidePanel());
        root.setCenter(mapController.getMapView());

        Scene scene = new Scene(root, 900, 900);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resources/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}