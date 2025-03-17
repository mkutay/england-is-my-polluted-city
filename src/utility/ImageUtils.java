package utility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utility class for creating JavaFX ImageView instances.
 * This class provides methods to load images from classpath resources.
 * If an image is not found, a warning is logged, and an empty ImageView is returned.
 *
 * @author Chelsea Feliciano
 * @version 1.0
 */
public class ImageUtils {
    /**
     * Loads an image from the given classpath resource path and returns an ImageView.
     * If the image is not found, an empty ImageView is returned.
     * @param resourcePath The classpath-relative path to the image (e.g., "/icons/rainbow.png").
     * @return An ImageView containing the image or an empty ImageView if not found.
     */
    public static ImageView createImage(String resourcePath) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            System.err.println("Warning: Image resource not found: " + resourcePath);
            return new ImageView();
        }

        Image img = new Image(resource.toExternalForm());
        return new ImageView(img); // Use natural size of the image
    }

    /**
     * Loads an image from the specified classpath resource path and resizes it to fit the given dimensions.
     * The aspect ratio is preserved. If the image is not found, returns an empty ImageView.
     * @param resourcePath The classpath-relative path to the image
     * @param width The desired width of the ImageView.
     * @param height The desired height of the ImageView.
     * @return An ImageView containing the loaded image with the given dimensions.
     */
    public static ImageView createImage(String resourcePath, double width, double height) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            System.err.println("Warning: Image resource not found: " + resourcePath);
            return new ImageView();
        }

        Image img = new Image(resource.toExternalForm());
        ImageView imgView = new ImageView(img);

        // Keep aspect ratio.
        imgView.setPreserveRatio(true);
        if (width > height) {
            imgView.setFitWidth(width);
        } else {
            imgView.setFitHeight(height);
        }

        return imgView;
    }

    /**
     * Loads an image from the specified classpath resource path and resizes it to a square.
     * The aspect ratio is preserved. If the image is not found, returns an empty ImageView.
     * @param resourcePath The classpath-relative path to the image.
     * @param side The desired size (both width and height).
     * @return An ImageView containing the resized image or an empty ImageView if not found.
     */
    public static ImageView createImage(String resourcePath, int side) {
        return createImage(resourcePath, side, side);
    }
}
