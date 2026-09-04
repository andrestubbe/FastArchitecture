package fastarchitecture;

/**
 * Represents a directional layer dependency rule (e.g. deny View -> Control).
 */
public record ArchitectureRule(Layer from, Layer to, boolean allowed) {

    public static ArchitectureRule deny(Layer from, Layer to) {
        return new ArchitectureRule(from, to, false);
    }

    public static ArchitectureRule allow(Layer from, Layer to) {
        return new ArchitectureRule(from, to, true);
    }

    @Override
    public String toString() {
        return (allowed ? "allow " : "deny ") + from.getDisplayName() + " -> " + to.getDisplayName();
    }
}
