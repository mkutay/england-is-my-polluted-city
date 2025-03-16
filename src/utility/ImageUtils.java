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
        return new ImageView(img); // Use natural size of the image
    }

    /**
     * Creates an ImageView from a given classpath resource path with specified dimensions.
     * The aspect ratio is preserved to prevent image distortion.
     *
     * @param resourcePath The classpath-relative path to the image
     * @param width The desired width of the ImageView.
     * @param height The desired height of the ImageView.
     * @return An ImageView containing the loaded image with the given dimensions.
     */
    public static ImageView createImage(String resourcePath, double width, double height) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }

        Image img = new Image(resource.toExternalForm());
        ImageView imgView = new ImageView(img);

        // Keep aspect ratio
        imgView.setPreserveRatio(true);
        if (width > height) {
            imgView.setFitWidth(width);
        } else {
            imgView.setFitHeight(height);
        }

        return imgView;
    }

    /**
     * Creates a square ImageView from a given classpath resource path with a specified side length.
     * The aspect ratio is preserved.
     *
     * @param resourcePath The classpath-relative path to the image (e.g., "/icons/rainbow.png").
     * @param side The desired size (both width and height).
     * @return An ImageView containing the loaded image with the given size.
     */
    public static ImageView createImage(String resourcePath, int side) {
        var resource = ImageUtils.class.getResource(resourcePath);

        if (resource == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }

        Image img = new Image(resource.toExternalForm());
        ImageView imgView = new ImageView(img);
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(side); // adjusts height automatically

        return imgView;
    }
}
