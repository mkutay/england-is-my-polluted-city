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

/**
 * UI Component for the Welcome Page.
 * Handles layout and UI elements.
 */
public class WelcomePage extends BorderPane {
    private final ImageView imageView = new ImageView();
    private final Text descriptionText = new Text();
    private Button prevButton = new Button("Previous");
    private Button nextButton = new Button("Next");
    private Button closeButton = new Button("Close");

    public WelcomePage() {
        // Header
        Label header = new Label("Welcome to the UK Emissions Map!");
        header.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-padding: 10px;");

        HBox buttonBox = new HBox(10, prevButton, nextButton, closeButton);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox contentBox = new VBox(10, header, imageView, descriptionText, buttonBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setFillWidth(true);

        descriptionText.wrappingWidthProperty().bind(contentBox.widthProperty().subtract(10));
        imageView.fitWidthProperty().bind(contentBox.widthProperty().multiply(0.8));
        imageView.setPreserveRatio(true);

        this.setCenter(contentBox);
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

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
