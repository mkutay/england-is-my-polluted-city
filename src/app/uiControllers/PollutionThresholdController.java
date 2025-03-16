package app.uiControllers;

import javafx.scene.control.Slider;

/**
 * Handles the slider for the pollution threshold
 *
 * @author Anas Ahmed
 */
public class PollutionThresholdController {
    private final Slider thresholdSlider;

    public PollutionThresholdController(){
        thresholdSlider = new Slider(0, 1, 0);
    }

    public Slider getThresholdSlider() {
        return thresholdSlider;
    }
}
