package app.uiControllers;

import app.App;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Controls the top navigation bar of the application.
 *
 * Refactor by Mehmet Kutay Bozkurt
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 1.0
 */
public class NavigationBarController {
    private final MenuBar menuBar;
    private final App app;

    /**
     * Constructor for NavigationBarController.
     * Creates and initialises the top navigation bar.
     */
    public NavigationBarController(App app) {
        this.app = app;
        menuBar = createTopNavBar();
        menuBar.getStyleClass().add("top-nav");
    }

    /**
     * Creates menus file and help, and then adds them to top navigation bar.
     * @return Top navigation bar.
     */
    private MenuBar createTopNavBar() {
        MenuBar topNavBar = new MenuBar();

        // File Menu:
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Help Menu:
        Menu helpMenu = new Menu("Help");
        // Add About Page:
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> AboutPageController.show());
        // Add Tutorial Page:
        MenuItem tutorialItem = new MenuItem("Tutorial");
        tutorialItem.setOnAction(e -> app.showWelcomePage());
        helpMenu.getItems().addAll(aboutItem, tutorialItem);

        topNavBar.getMenus().addAll(fileMenu, helpMenu);
        return topNavBar;
    }

    /**
     * @return The menu bar.
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
