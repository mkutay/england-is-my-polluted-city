package app.uiControllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.*;

/**
 * The welcome page for the project. Will be shown on launch or by clicking "Tutorial" in the navigation bar
 *
 * @author Matthias Loong
 */
public class WelcomePageController {
    //Store path to images and descriptions in a linked hashmap (for easy iteration). Image path serves as a key.
    private static final Map<String, String> tutorialPicDescMap = new LinkedHashMap<>();
    private static List<String> imagePaths;
    private static Stage stage;

    private static final ImageView imageView = new ImageView();
    private static final Text descriptionText = new Text();
    private static final Button prevButton = new Button("Previous");
    private static final Button nextButton = new Button("Next");
    private static int currentPage = 0; // Store current page index

    private static final int MAX_WIDTH = 1000;
    private static final int MAX_HEIGHT = 800;


    static {
        initialiseMenu();
    }

    private static void initialiseMenu(){
        // Some string formatting
        String firstPage = """
                Welcome to the UK Emissions Interactive Map! This project was made by Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong and Chelsea Feliciano.
                This is a short tutorial on how to use the app effectively.
                To start, you can use your mouse to drag around the map and use your mousewheel or trackpad to zoom in or out by scrolling.
                Clicking on the "Legend" box will reveal the pollution values for the colours represented on the map.
                """;
        String secondPage = """
                On the side panel, you are able to select the Pollutant and historical data by year to view.
                Selecting an option will automatically load the data to be viewed.
                """;
        String thirdPage = """
                Right clicking on any area of the map will display detailed information about the selected area's location information such as coordinates and postal code on the map along with pollution details.
                The popup will also display live pollution details taken from the World Air Quality Index which is updated hourly.
                """;
        String fourthPage = """
                Clicking on "View Pollution Statistics" will bring up the Statistics page with a breakdown of various pollution statistics of the selected pollutant and date range.
                Use the selectors on the side panel to choose the pollutant and date range to search. Further statistical views are available by toggling the buttons on the bottom of the page
                """;

        tutorialPicDescMap.put("/resources/screenshots/main_page.png", firstPage);
        tutorialPicDescMap.put("/resources/screenshots/main_page_selection.png", secondPage);
        tutorialPicDescMap.put("/resources/screenshots/info_popup.png", thirdPage);
        tutorialPicDescMap.put("/resources/screenshots/statistics_page.png", fourthPage);

        // Store ordered image paths
        imagePaths = new ArrayList<>(tutorialPicDescMap.keySet());
    }


    public static void show() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tutorial");

        // Header
        Text header = new Text("Welcome to the UK Emissions Map!");
        header.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // Image View (Dynamically Resized)
        loadImage(imagePaths.get(currentPage)); // Load first image

        //Text tutorial
        descriptionText.setStyle("-fx-font-size: 14px;");
        updateDescription();

        // Buttons
        Button closeButton = new Button("Close");

        prevButton.setDisable(true); // Initially disabled
        prevButton.setOnAction(e -> navigate(-1));
        nextButton.setOnAction(e -> navigate(1));
        closeButton.setOnAction(e -> stage.close());

        //Button Layout
        HBox buttonBox = new HBox(10, prevButton, nextButton, closeButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        //Content Layout
        VBox contentBox = new VBox(10, header, imageView, descriptionText, buttonBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setFillWidth(true);

        //wrapping of text for screen
        descriptionText.wrappingWidthProperty().bind(contentBox.widthProperty().subtract(10));

        imageView.fitWidthProperty().bind(contentBox.widthProperty().multiply(0.8));
        imageView.setPreserveRatio(true);

        BorderPane root = new BorderPane();
        root.setCenter(contentBox);

        //Create the scene and set it on the stage
        Scene scene = new Scene(root, MAX_WIDTH, MAX_HEIGHT);
        stage.setScene(scene);
        stage.show();

        // Resize stage based on image size
        stage.sizeToScene();
    }

    private static void navigate(int direction) {
        currentPage += direction;
        loadImage(imagePaths.get(currentPage));
        updateDescription();
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage == (imagePaths.size() - 1));
    }

    private static void loadImage(String imagePath) {
        Image image = new Image(imagePath);
        imageView.setImage(image);

        //Scale image width down, preserving aspect ratio
        double newHeight = image.getHeight() * 0.6;
        imageView.setFitHeight(newHeight);
        imageView.setPreserveRatio(true); // Keep aspect ratio
        stage.sizeToScene();
    }

    private static void updateDescription() {
        descriptionText.setText(tutorialPicDescMap.get(imagePaths.get(currentPage)));
    }


}
