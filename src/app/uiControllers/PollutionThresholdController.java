package app.uiControllers;

import javafx.scene.control.Slider;

/**
 * Handles the slider for the pollution threshold.
 *
 * @author Anas Ahmed
 * @version 1.0
 */
public class PollutionThresholdController {
    private final Slider thresholdSlider;

    /**
     * Constructor.
     */
    public PollutionThresholdController() {
        thresholdSlider = new Slider(0, 1, 0);
    }

    /**
     * @return The pollution threshold slider.
     */
    public Slider getThresholdSlider() {
        return thresholdSlider;
    }
}
