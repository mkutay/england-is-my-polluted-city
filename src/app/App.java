package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException, IOException, InterruptedException {
        stage.setTitle(APP_NAME);

        MainLayoutHandler layoutHandler = new MainLayoutHandler(stage);
        Scene scene = layoutHandler.createScene();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMaximized(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}