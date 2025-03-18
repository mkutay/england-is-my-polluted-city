package app;

import app.uiControllers.WelcomePageController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main App class as the entry point to the application. This class creates
 * a JavaFX application that can display a map of UK with pollution data.
 * It centres the map on London.
 *
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 6.0
 */
public class App extends Application {
    public static final String APP_NAME = "UK Emissions Interactive Map";
    private WelcomePageController welcomePageController;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException, IOException, InterruptedException {
        stage.setTitle(APP_NAME);

        MainLayoutHandler layoutHandler = new MainLayoutHandler(stage, this);
        Scene scene = layoutHandler.createScene();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        showWelcomePage(); // Show tutorial on launch
    }

    /**
     * Displays the welcome tutorial window.
     * Ensures only one instance is created and reused.
     */
    public void showWelcomePage() {
        if (welcomePageController == null) {
            welcomePageController = new WelcomePageController();
        }
        welcomePageController.show();
    }
}