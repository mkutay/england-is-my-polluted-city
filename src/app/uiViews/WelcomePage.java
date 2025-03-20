package app.uiViews;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * UI Component for the Welcome Page. It inherits BorderPane to utilise its functionality.
 * Handles layout and UI elements.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class WelcomePage extends BorderPane {
    private final ImageView imageView = new ImageView();
    private final Text descriptionText = new Text();
    private final Button prevButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button closeButton = new Button("Close");

    /**
     * Constructor for the Welcome Page. This holds the entire layout of the WelcomePage.
     */
    public WelcomePage() {

        //Header
        Label title = new Label("Welcome to the UK Emissions Map!");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 30 0 0 0;");

        //Vbox to store the title of the page and the image view
        VBox header = new VBox(title, imageView);
        header.setAlignment(Pos.CENTER); // Center image & text

        //Description / Tutorial text
        TextFlow textFlow = new TextFlow(descriptionText);
        textFlow.setStyle("-fx-padding: 0 60 0 60;");
        textFlow.setTextAlignment(TextAlignment.CENTER);

        //Navigation Buttons
        HBox buttonBox = new HBox(10, prevButton, nextButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-padding: 10 0 20 0;");

        //Fit the image within the imageview
        imageView.fitWidthProperty().bind(widthProperty().multiply(0.8));
        imageView.setPreserveRatio(true);

        //Sets the layout
        setTop(header);
        setCenter(textFlow);
        setBottom(buttonBox);


    }

    /**
     * Method to update the content currently shown on the WelcomePage
     * @param imagePath The image path relative to the root directory of the project (src)
     * @param description The tutorial text associated with the current page
     * @param isFirst Checks if the current page is the first page
     * @param isLast Checks if the current page is the last page
     */
    public void updateContent(String imagePath, String description, boolean isFirst, boolean isLast) {
        loadImage(imagePath);
        descriptionText.setText(description);
        prevButton.setDisable(isFirst);
        nextButton.setDisable(isLast);

    }

    /**
     * Loads the image into the ImageView and resizes it to prepare it to be shown on the WelcomePage
     * @param imagePath The image path relative to the root directory of the project (src)
     */
    private void loadImage(String imagePath) {
        Image image = new Image(imagePath);
        imageView.setImage(image);
        //Scale the image down to make it look nicer
        double newHeight = image.getHeight() * 0.7;
        imageView.setFitHeight(newHeight);
        //Retains aspect ratio
        imageView.setPreserveRatio(true);
    }

    // Getters:
    public Button getPrevButton() { return prevButton; }
    public Button getNextButton() { return nextButton; }
    public Button getCloseButton() { return closeButton; }
}
