package app;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuBar;

import app.uiControllers.NavigationBarController;
import app.uiControllers.SidePanelController;
import app.uiControllers.StatisticsController;

/**
 * Manages all UI elements that are not a part of the map. e.g. the side panel.
 *
 * Refactor and class by Anas Ahmed, contributions of functionality attributed to all authors.
 * @author Anas Ahmed, Mehmet Kutay Bozkurt, Matthias Loong, and Chelsea Feliciano
 * @version 2.0
 */
public class UIController {
    private final SidePanelController sidePanelController;
    private final NavigationBarController navBarController;

    /**
     * Constructor for UIController.
     * @param mapController The current map controller.
     * @param statisticsController The statistics controller.
     */
    public UIController(MapController mapController, StatisticsController statisticsController, BorderPane rootPane) {
        this.navBarController = new NavigationBarController();
        this.sidePanelController = new SidePanelController(mapController, statisticsController, rootPane);
    }

    // Getters:
    public VBox getSidePanel() { return sidePanelController.getSidePanel(); }
    public MenuBar getTopNav() { return navBarController.getMenuBar(); }
}
