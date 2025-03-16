package app.uiControllers;

import javafx.scene.control.Slider;

public class PollutionThresholdController {
    private final Slider thresholdSlider;

    public PollutionThresholdController(){
        thresholdSlider = new Slider(0, 100, 0);
    }

    public Slider getThresholdSlider() {
        return thresholdSlider;
    }
}
