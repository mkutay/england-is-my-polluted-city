package app.uiControllers;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utility.ImageUtils;

import javafx.scene.control.Hyperlink;
import java.awt.Desktop;
import java.net.URI;
import javafx.scene.text.TextFlow;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;

import static app.App.APP_NAME;

/**
 * A simple about page for the application.
 * 
 * @author Matthias Loong
 * @version 1.0
 */
public class AboutPageController {

    public static void show() {
        Stage aboutStage = new Stage();
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setTitle("About");

        // Create header of about page with a HBox.
        HBox headerLabelBox = new HBox();
        Label headerLabel = new Label(APP_NAME);
        headerLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-padding: 15 0 0 15;");

        // App logo.
        ImageView icon = ImageUtils.createImage("/resources/icons/rainbow.png", 50);
        headerLabelBox.getChildren().addAll(icon, headerLabel);

        // Authors of app.
        Label authorsLabel = new Label("Created with Love by: Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, Chelsea Feliciano");
        // Separator line to separate Header + authors from credits text.
        Separator separator = new Separator();

        // Credits text:
        TextFlow creditsText = getText();
        creditsText.setStyle("-fx-font-size: 14px; -fx-padding: 10");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> aboutStage.close());
        HBox buttonBox = new HBox(closeButton);
        buttonBox.setStyle("-fx-alignment: bottom-right; -fx-padding: 10;");

        VBox layout = new VBox(10, headerLabelBox, authorsLabel, separator, creditsText, buttonBox);
        layout.setStyle("-fx-padding: 20;");

        aboutStage.setScene(new Scene(layout));
        aboutStage.sizeToScene();
        aboutStage.show();
    }

    private static TextFlow getText() {
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.LEFT);

        textFlow.getChildren().addAll(
                new Text("The project makes use of the following external libraries and APIs:\n\n"),
                createHyperlink("Gluon Maps", "https://github.com/gluonhq/maps"),
                new Text(" - Maps Library by Gluon (maintainer of JavaFX) that implements OpenStreetMaps\n\n"),
                createHyperlink("OSGB by DST", "https://github.com/dstl/osgb"),
                new Text(" - Library to convert British Grid System (Easting / Northing) to Latitude and Longitude\n\n"),
                createHyperlink("GeographicLib", "https://github.com/geographiclib/geographiclib-java"),
                new Text(" - Used for geodesic distance calculation\n\n"),
                createHyperlink("Postcodes.io", "https://postcodes.io/"),
                new Text(" - API used to get location & address data from the specified longitude and latitude on the map\n\n"),
                createHyperlink("World Air Quality Index API", "https://aqicn.org/api/"),
                new Text(" - An API to get real-time Air Quality Index Updates from the World Air Quality Index Project\n\n"),
                createHyperlink("GSON", "https://github.com/google/gson"),
                new Text(" - A library by Google to convert between JSON and Java Objects\n")
        );

        return textFlow;
    }

    // Helper method to create clickable links
    private static Hyperlink createHyperlink(String text, String url) {
        Hyperlink hyperlink = new Hyperlink(text);
        hyperlink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return hyperlink;
    }
}
