package fastarchitecture;

/**
 * Full architectural coordinates of a class: its vertical Layer and optional horizontal Slice.
 * Example:
 *   speakplus.control.cli  -> Layer.CONTROL, slice "cli"
 *   speakplus.view.gui     -> Layer.VIEW,    slice "gui"
 *   speakplus.model.tts    -> Layer.MODEL,   slice "tts"
 */
public record ArchitectureScope(Layer layer, String slice) {

    public ArchitectureScope(Layer layer) {
        this(layer, null);
    }

    public boolean hasSlice() {
        return slice != null && !slice.isBlank();
    }

    /**
     * Formatted string e.g. "View.cli" or "Model".
     */
    public String getDisplayName() {
        if (hasSlice()) {
            return layer.getDisplayName() + "." + slice;
        }
        return layer.getDisplayName();
    }

    public static ArchitectureScope of(Layer layer, String slice) {
        return new ArchitectureScope(layer, (slice != null && !slice.isBlank()) ? slice.toLowerCase() : null);
    }

    public static ArchitectureScope unknown() {
        return new ArchitectureScope(Layer.UNKNOWN, null);
    }
}
