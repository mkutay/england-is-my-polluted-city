import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
public class App extends Application {
    @Override
    public void start(Stage stage) {
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        
        // Enable JavaScript console logging
        webEngine.setOnError(e -> System.out.println("Error: " + e.getMessage()));
        
        // Load the leaflet.html file
        webEngine.load(getClass().getResource("leaflet.html").toString());

        // Create scene
        stage.setTitle("Web Map");
        Scene scene = new Scene(webView, 1000, 700, Color.web("#666970"));
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args){
        launch(args);
    }
}