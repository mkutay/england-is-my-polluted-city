package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.scene.layout.HBox;
import javafx.scene.control.Separator;

import java.io.IOException;

import dataProcessing.Pollutant;
import app.uiControllers.StatisticsController;
import colors.ColorSchemeManager;

/**
 * The main App class as the entry point to the application. This class creates
 * a JavaFX application that can display a map of UK with pollution data.
 * It centres the map on London.
 *
 * Refactor by Anas Ahmed
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 3.1
 */
public class App extends Application {
    public static final String APP_NAME = "UK Emissions Interactive Map";

    private static MapController mapController;
    private UIController uiController;
    private ColorSchemeManager colorSchemeManager;

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException, IOException, InterruptedException {
        stage.setTitle(APP_NAME);

        colorSchemeManager = new ColorSchemeManager();
        mapController = new MapController(stage, colorSchemeManager);

        // Create root layout
        BorderPane root = new BorderPane();

        mapController.initialisePollutionLayer(2018, Pollutant.NO2);
        uiController = new UIController(mapController, root);

        Separator verticalSeparator = new Separator(Orientation.VERTICAL);

        HBox leftPane = new HBox();
        leftPane.getChildren().addAll(uiController.getSidePanel(),verticalSeparator);

        root.setTop(uiController.getTopNav());
        root.setLeft(leftPane);
        root.setCenter(mapController.getMapOverlay());

        Scene scene = new Scene(root, 900, 800);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}