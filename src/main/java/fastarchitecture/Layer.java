package fastarchitecture;

/**
 * Fundamental architectural layers defined by the FastArchitecture protocol.
 */
public enum Layer {
    /**
     * Owns state, domain data, and immutable data structures.
     */
    MODEL("Model"),

    /**
     * Owns behavior, user inputs, business decisions, and state mutations.
     */
    CONTROL("Control"),

    /**
     * Owns presentation, rendering, UI components, and display output.
     */
    VIEW("View"),

    /**
     * Unclassified / neutral layer (e.g. general utilities or application entry points).
     */
    UNKNOWN("Unknown");

    private final String displayName;

    Layer(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Layer fromString(String name) {
        if (name == null) return UNKNOWN;
        String clean = name.trim().toLowerCase();
        return switch (clean) {
            case "model" -> MODEL;
            case "control", "controller" -> CONTROL;
            case "view" -> VIEW;
            default -> UNKNOWN;
        };
    }
}
