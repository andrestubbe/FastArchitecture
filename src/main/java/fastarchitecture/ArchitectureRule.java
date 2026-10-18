package fastarchitecture;

/**
 * Represents a directional architecture dependency rule (e.g. deny View -> Control, or deny view.cli -> view.gui).
 */
public record ArchitectureRule(
        Layer fromLayer,
        String fromSlice,
        Layer toLayer,
        String toSlice,
        boolean allowed
) {

    public ArchitectureRule(Layer from, Layer to, boolean allowed) {
        this(from, null, to, null, allowed);
    }

    public static ArchitectureRule deny(Layer from, Layer to) {
        return new ArchitectureRule(from, null, to, null, false);
    }

    public static ArchitectureRule allow(Layer from, Layer to) {
        return new ArchitectureRule(from, null, to, null, true);
    }

    public static ArchitectureRule deny(Layer fromLayer, String fromSlice, Layer toLayer, String toSlice) {
        return new ArchitectureRule(fromLayer, fromSlice, toLayer, toSlice, false);
    }

    public static ArchitectureRule isolate(String sliceA, String sliceB) {
        return new ArchitectureRule(null, sliceA, null, sliceB, false);
    }

    public Layer from() {
        return fromLayer != null ? fromLayer : Layer.UNKNOWN;
    }

    public Layer to() {
        return toLayer != null ? toLayer : Layer.UNKNOWN;
    }

    /**
     * Checks if this rule matches a specific source and target scope.
     */
    public boolean matches(ArchitectureScope source, ArchitectureScope target) {
        // Match from
        if (fromLayer != null && fromLayer != source.layer()) {
            return false;
        }
        if (fromSlice != null && !fromSlice.equals("*")) {
            if (!fromSlice.equalsIgnoreCase(source.slice())) {
                return false;
            }
        }

        // Match to
        if (toLayer != null && toLayer != target.layer()) {
            return false;
        }
        if (toSlice != null && !toSlice.equals("*")) {
            if (!toSlice.equalsIgnoreCase(target.slice())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String fromStr = (fromLayer != null ? fromLayer.getDisplayName() : "*") + (fromSlice != null ? "." + fromSlice : "");
        String toStr = (toLayer != null ? toLayer.getDisplayName() : "*") + (toSlice != null ? "." + toSlice : "");
        return (allowed ? "allow " : "deny ") + fromStr + " -> " + toStr;
    }
}

