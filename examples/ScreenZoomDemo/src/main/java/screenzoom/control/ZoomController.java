package screenzoom.control;

import screenzoom.model.ZoomState;

/**
 * Control Layer: Captures input, handles zoom actions, and mutates Model state.
 * Never performs direct rendering or imports View.
 */
public final class ZoomController {

    private ZoomState state = ZoomState.defaultState();

    public ZoomState getState() {
        return state;
    }

    public void onMouseWheel(int scrollDelta) {
        float step = scrollDelta > 0 ? 0.25f : -0.25f;
        this.state = state.withZoom(state.zoomLevel() + step);
    }

    public void onMouseMove(int x, int y) {
        this.state = state.withCursor(x, y);
    }

    public void onToggleZoom() {
        this.state = state.withActive(!state.active());
    }

    public void onReset() {
        this.state = ZoomState.defaultState();
    }
}
