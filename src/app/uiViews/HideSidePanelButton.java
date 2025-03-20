package app.uiViews;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import utility.ImageUtils;

/**
 * A button that toggles the visibility of a side panel.
 * The button updates its icon and tooltip based on the panel's state.
 * @author Chelsea Feliciano
 */
public class HideSidePanelButton extends Label {
    private final SidePanel sidePanel;
    private final ImageView icon_hide;  // Icon when panel is hidden
    private final ImageView icon_show;  // Icon when panel is visible
    private boolean isHidden = false;   // Tracks panel state
    private final Tooltip tooltip;      // Hover text for hidden/visible

    /**
     * Creates a button to show or hide the side panel.
     *
     * @param sidePanel The panel controlled by this button.
     */
    public HideSidePanelButton(SidePanel sidePanel) {
        this.sidePanel = sidePanel;
        icon_hide = ImageUtils.createImage("/resources/icons/cat_hide.png",90);
        icon_show = ImageUtils.createImage("/resources/icons/cat_show.png",90);
        setGraphic(icon_show);
        setStyle("-fx-cursor: hand;");

        // Initialise Tooltip
        tooltip = new Tooltip("Hide Side Panel");
        Tooltip.install(this, tooltip); // Attach tooltip to button

        setOnMouseClicked(event -> togglePanel());
    }

    /**
     * Toggles the panel's visibility and updates the button's icon and tooltip.
     */
    private void togglePanel() {
        if (isHidden) {
            sidePanel.setVisible(true);
            sidePanel.setManaged(true);
            setGraphic(icon_show);
            tooltip.setText("Hide Side Panel");
        } else {
            sidePanel.setVisible(false);
            sidePanel.setManaged(false);
            setGraphic(icon_hide);
            tooltip.setText("Show Side Panel");
        }
        isHidden = !isHidden;
    }
}
