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
 * UI Component for the Welcome Page.
 * Handles layout and UI elements.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class WelcomePage extends BorderPane {
    private final ImageView imageView = new ImageView();
    private final Text descriptionText = new Text();
    private Button prevButton = new Button("Previous");
    private Button nextButton = new Button("Next");
    private Button closeButton = new Button("Close");

    public WelcomePage() {

        // Header
        Label title = new Label("Welcome to the UK Emissions Map!");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 30 0 0 0;");

        VBox header = new VBox(title, imageView);
        header.setAlignment(Pos.CENTER); // Center image & text

        // Text
        TextFlow textFlow = new TextFlow(descriptionText);
        textFlow.setStyle("-fx-padding: 0 60 0 60;");
        textFlow.setTextAlignment(TextAlignment.CENTER);

        // Buttons
        HBox buttonBox = new HBox(10, prevButton, nextButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setStyle("-fx-padding: 10 0 20 0;");
        
        setTop(header);
        setCenter(textFlow);
        setBottom(buttonBox);

        imageView.fitWidthProperty().bind(widthProperty().multiply(0.8));
        imageView.setPreserveRatio(true);
    }

    public void updateContent(String imagePath, String description, boolean isFirst, boolean isLast) {
        loadImage(imagePath);
        descriptionText.setText(description);
        prevButton.setDisable(isFirst);
        nextButton.setDisable(isLast);
    }

    private void loadImage(String imagePath) {
        Image image = new Image(imagePath);
        imageView.setImage(image);
        double newHeight = image.getHeight() * 0.6;
        imageView.setFitHeight(newHeight);
        imageView.setPreserveRatio(true); // Keep aspect ratio
    }

    // Getters:
    public Button getPrevButton() { return prevButton; }
    public Button getNextButton() { return nextButton; }
    public Button getCloseButton() { return closeButton; }
}
