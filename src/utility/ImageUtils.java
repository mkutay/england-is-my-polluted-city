package utility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utility class for creating JavaFX ImageView instances.
 * Allows users to load an image with its natural size or specify width and height.
 *
 * @author Chelsea Feliciano
 */
public class ImageUtils {

    /**
     * Creates an ImageView from a given classpath resource path using its natural size.
     *
     * @param resourcePath The classpath-relative path to the image (e.g., "/icons/rainbow.png").
     * @return An ImageView containing the loaded image **with its original size**.
     * @throws IllegalArgumentException if the resource is not found.
     */
    public static ImageView createImage(String resourcePath) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }

        Image img = new Image(resource.toExternalForm());
        return new ImageView(img); // Uses natural size of the image
    }

    /**
     * Creates an ImageView from a given classpath resource path with specified dimensions.
     * The aspect ratio is preserved to prevent image distortion.
     *
     * @param resourcePath The classpath-relative path to the image (e.g., "/icons/rainbow.png").
     * @param width The desired width of the ImageView.
     * @param height The desired height of the ImageView.
     * @return An ImageView containing the loaded image with the given dimensions.
     * @throws IllegalArgumentException if the resource is not found.
     */
    public static ImageView createImage(String resourcePath, double width, double height) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }

        Image img = new Image(resource.toExternalForm());
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        imgView.setPreserveRatio(true); // Keep aspect ratio
        return imgView;
    }
}
