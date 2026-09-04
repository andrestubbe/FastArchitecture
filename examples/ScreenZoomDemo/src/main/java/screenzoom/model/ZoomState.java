package screenzoom.model;

/**
 * Model Layer: Pure state and domain data.
 * Zero dependencies on Control or View.
 */
public record ZoomState(float zoomLevel, int cursorX, int cursorY, boolean active) {

    public static ZoomState defaultState() {
        return new ZoomState(1.0f, 0, 0, false);
    }

    public ZoomState withZoom(float newZoom) {
        return new ZoomState(Math.max(1.0f, Math.min(10.0f, newZoom)), cursorX, cursorY, active);
    }

    public ZoomState withCursor(int x, int y) {
        return new ZoomState(zoomLevel, x, y, active);
    }

    public ZoomState withActive(boolean newActive) {
        return new ZoomState(zoomLevel, cursorX, cursorY, newActive);
    }
}
