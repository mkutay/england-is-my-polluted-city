package app.uiControllers;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A simple about page for our app
 * @author Matthias Loong
 */
public class AboutPageController {

    public static void show() {
        Stage aboutStage = new Stage();
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setTitle("About");

        //Create header of about page with a HBox
        HBox headerLabelBox = new HBox();
        Label headerLabel = new Label("UK Emissions Interactive Map");
        headerLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-padding: 0 0 0 20;");
        //App logo
        Image img = new Image(AboutPageController.class.getResourceAsStream("/resources/rainbow.png"));
        ImageView icon = new ImageView(img);
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        headerLabelBox.getChildren().addAll(icon, headerLabel);

        //Authors of app
        Label authorsLabel = new Label("Created with Love by: Mehmet Kutay Bozkurt, Anas Ahmed, Matthias Loong, Chelsea Feliciano");
        //Separator line to separate Header + authors from credits text
        Separator separator = new Separator();

        //Credits text
        Text creditsText = getText();
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

    private static Text getText() {
        String credits = """
                The project makes use of the following external libraries and APIs:
                
                - Gluon Maps (https://github.com/gluonhq/maps) - Maps Library by Gluon (maintainer of JavaFX) that implements OpenStreetMaps
                
                - OSGB by DST (https://github.com/dstl/osgb]) - Library to convert British Grid System (Easting / Northing) to Latitude and Longitude
                
                - GeographicLib (https://github.com/geographiclib/geographiclib-java) - Used for geodesic distance calculation
                
                - Postcodes.io (https://postcodes.io/) - API used to get location & address data from the specified longitude and latitude on the map
                
                - World Air Quality Index API (https://aqicn.org/api/) - An API to get real time Air Quality Index Updates from the World Air Quality Index Project
                
                - GSON (https://github.com/google/gson) - A library by Google to convert between Json and Java Objects
                """;


        // Resources
        return new Text(credits);
    }
}
