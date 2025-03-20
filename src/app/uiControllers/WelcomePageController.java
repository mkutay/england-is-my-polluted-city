package app.uiControllers;

import app.App;
import app.uiViews.WelcomePage;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.*;

/**
 * The welcome page for the project. Will be shown on launch or by clicking "Tutorial" in the navigation bar
 *
 * @author Matthias Loong
 * @version 1.0
 */
public class WelcomePageController {
    private final WelcomePage welcomePage;
    private final Stage stage;
    private final App app;
    private int currentPage = 0;    // Current Page index

    // Store path to images and descriptions in a linked hashmap (for easy iteration). Image path serves as a key.
    private static final Map<String, String> tutorialPicDescMap = new LinkedHashMap<>();
    private static final List<String> imagePaths = new ArrayList<>();

    private static final int MAX_WIDTH = 900;
    private static final int MAX_HEIGHT = 800;

    static {
        initialiseMenu();
    }

    public WelcomePageController(App app) {
        this.app = app;
        welcomePage = new WelcomePage();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tutorial");
        stage.setScene(new Scene(welcomePage));

        welcomePage.getPrevButton().setOnAction(e -> navigate(-1));
        welcomePage.getNextButton().setOnAction(e -> navigate(1));
        welcomePage.getCloseButton().setOnAction(e -> close());

        updateUI();
    }

    private static void initialiseMenu(){
        // Some string formatting
        String welcomeMessage = """
                Welcome to the UK Emissions Interactive Map!
                This project was made by Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong and Chelsea Feliciano.
                
                This is a short tutorial on how to use the app effectively.
                To start, you can use your mouse to drag around the map and use your mousewheel or trackpad to zoom in or out by scrolling.
                Clicking on the "Legend" box will reveal the pollution values for the colours represented on the map.
                """;
        String sidePanelTutorial = """
                On the side panel, you are able to select the Pollutant and historical data by year to view.
                Selecting an option will automatically load the data to be viewed.
                
                Use the slider to only highlight areas that reach a certain pollution percentage.
                
                This app also allows changing the colour to be more colour blind friendly.
                """;
        String popUpTutorial = """
                Right clicking on any area of the map will display detailed information about the selected area's location information such as coordinates and postal code on the map along with pollution details.
                The popup will also display live pollution details taken from the World Air Quality Index which is updated hourly.
                """;
        String closeSidePanelTutorial = """
                Click on the cat next to the side panel to hide the side panel for easier map viewing!
                The buttons on the upper-right hand side of the application also allow for zooming in and out, as well as switching between windowed and full-screen modes.
                """;

        String statsTutorial = """
                Clicking on "View Pollution Statistics" will bring up the Statistics page with a breakdown of various pollution statistics of the selected pollutant and date range.
                Use the selectors on the side panel to choose the pollutant and date range to search. Further statistical views are available by toggling the buttons on the bottom of the page
                """;
        String lastPage = """
                You are now ready to use the interactive map!
                If you need to view this tutorial again, you can access it by clicking "Help" => "Tutorial" on the navigation bar.
                
                """;

        tutorialPicDescMap.put("/resources/screenshots/main_page.png", welcomeMessage);
        tutorialPicDescMap.put("/resources/screenshots/main_page_selection.png", sidePanelTutorial);
        tutorialPicDescMap.put("/resources/screenshots/main_page_colour_selector.png", sidePanelTutorial);
        tutorialPicDescMap.put("/resources/screenshots/info_popup.png", popUpTutorial);
        tutorialPicDescMap.put("/resources/screenshots/main_page_cat.png", closeSidePanelTutorial);
        tutorialPicDescMap.put("/resources/screenshots/fullscreen_cat.png", closeSidePanelTutorial);
        tutorialPicDescMap.put("/resources/screenshots/statistics_page.png", statsTutorial);
        tutorialPicDescMap.put("/resources/screenshots/main_screen_ready.png", lastPage);

        // Store ordered image paths
        imagePaths.addAll(tutorialPicDescMap.keySet());
    }


    public void show() {
        reset();
        stage.show();
    }

    private void navigate(int direction) {
        currentPage += direction;
        updateUI();
    }

    private void close() {
        stage.close();
    }

    private void reset() {
        currentPage = 0;
        updateUI();
    }

    private void updateUI() {
        boolean isFirst = (currentPage == 0);
        boolean isLast = (currentPage == imagePaths.size() - 1);
        String imagePath = imagePaths.get(currentPage);
        String description = tutorialPicDescMap.get(imagePath);

        welcomePage.updateContent(imagePath, description, isFirst, isLast);
    }
    // Allow app to access tutorial window's stage
    public Stage getStage() {
        return stage;
    }
}