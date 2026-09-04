package screenzoom.view;

import screenzoom.model.ZoomState;

/**
 * View Layer: Renders zoom viewport overlay based on Model state.
 * Reads Model, never calls Control, never mutates Model.
 */
public final class ZoomOverlay {

    public void render(ZoomState state) {
        if (!state.active()) {
            return;
        }

        // Render pass: Present magnified viewport centered on cursor
        System.out.printf("[ZoomOverlay] Rendering Viewport | Zoom: %.2fx | Center: (%d, %d)\n",
                state.zoomLevel(), state.cursorX(), state.cursorY());
    }
}
