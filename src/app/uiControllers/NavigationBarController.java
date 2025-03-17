package app.uiControllers;

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

    /**
     * Constructor for NavigationBarController.
     * Creates and initializes the top navigation bar.
     */
    public NavigationBarController() {
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
        // Add About Page
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> AboutPageController.show());
        // Add Tutorial Page:
        MenuItem tutorialItem = new MenuItem("Tutorial");
        tutorialItem.setOnAction(e -> WelcomePageController.show());
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
