package app;

import app.uiControllers.WelcomePageController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
    private Stage primaryStage;
    private Label tutorialOverlay; // Label for the clear text
    private StackPane rootContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws PollutionLayerNotInitialisedException {
        this.primaryStage = stage;
        stage.setTitle(APP_NAME);

        MainLayoutHandler layoutHandler = new MainLayoutHandler(stage, this);
        Scene scene = layoutHandler.createScene();

        this.rootContainer = layoutHandler.getRootContainer();
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
        if (primaryStage == null) {
            System.err.println("Error: primaryStage is null, cannot dim.");
            return;
        }
        // Makes window blur until welcomePage is closed.
        BoxBlur blurEffect = new BoxBlur(3, 3, 5);
        primaryStage.getScene().getRoot().setEffect(blurEffect);

        // Create and show a clear text overlay
        if (tutorialOverlay == null) {
            tutorialOverlay = new Label("Tutorial Active");
            tutorialOverlay.getStyleClass().add("tutorial-overlay");
        }

        // Add the text overlay to the scene
        if (!rootContainer.getChildren().contains(tutorialOverlay)) {
            rootContainer.getChildren().add(tutorialOverlay);
        }

        welcomePageController.show(); // Show tutorial window

        // Makes window transparent until welcomePage is closed
        // primaryStage.setOpacity(0.8); // Dim effect on the entire window
        // welcomePageController.getStage().setOnHidden(e -> primaryStage.setOpacity(1.0));

        welcomePageController.getStage().setOnHidden(e -> {
            primaryStage.getScene().getRoot().setEffect(null);
            rootContainer.getChildren().remove(tutorialOverlay);
        });
    }
}