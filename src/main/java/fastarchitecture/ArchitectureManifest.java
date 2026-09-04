package fastarchitecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parsed representation of an architecture.fca specification file.
 */
public final class ArchitectureManifest {

    private final String architectureName;
    private final List<ArchitectureRule> rules;

    public ArchitectureManifest(String architectureName, List<ArchitectureRule> rules) {
        this.architectureName = architectureName != null ? architectureName : "FastArchitecture";
        this.rules = rules != null ? Collections.unmodifiableList(rules) : Collections.emptyList();
    }

    public String getArchitectureName() {
        return architectureName;
    }

    public List<ArchitectureRule> getRules() {
        return rules;
    }

    /**
     * Creates default standard rules for FastArchitecture (deny Model->Control, Model->View, View->Control).
     */
    public static ArchitectureManifest defaultStandard() {
        List<ArchitectureRule> rules = new ArrayList<>();
        rules.add(ArchitectureRule.deny(Layer.MODEL, Layer.CONTROL));
        rules.add(ArchitectureRule.deny(Layer.MODEL, Layer.VIEW));
        rules.add(ArchitectureRule.deny(Layer.VIEW, Layer.CONTROL));
        return new ArchitectureManifest("FastArchitecture", rules);
    }

    /**
     * Parses an architecture.fca file from the given path.
     */
    public static ArchitectureManifest parse(Path fcaPath) throws IOException {
        if (!Files.exists(fcaPath)) {
            return defaultStandard();
        }

        String archName = "FastArchitecture";
        List<ArchitectureRule> rules = new ArrayList<>();

        List<String> lines = Files.readAllLines(fcaPath);
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }

            if (line.startsWith("architecture ")) {
                archName = line.substring("architecture ".length()).trim();
            } else if (line.startsWith("deny ")) {
                String statement = line.substring("deny ".length()).trim();
                String[] parts = statement.split("->");
                if (parts.length == 2) {
                    Layer from = Layer.fromString(parts[0].trim());
                    Layer to = Layer.fromString(parts[1].trim());
                    if (from != Layer.UNKNOWN && to != Layer.UNKNOWN) {
                        rules.add(ArchitectureRule.deny(from, to));
                    }
                }
            } else if (line.startsWith("allow ")) {
                String statement = line.substring("allow ".length()).trim();
                String[] parts = statement.split("->");
                if (parts.length == 2) {
                    Layer from = Layer.fromString(parts[0].trim());
                    Layer to = Layer.fromString(parts[1].trim());
                    if (from != Layer.UNKNOWN && to != Layer.UNKNOWN) {
                        rules.add(ArchitectureRule.allow(from, to));
                    }
                }
            }
        }

        if (rules.isEmpty()) {
            return defaultStandard();
        }
        return new ArchitectureManifest(archName, rules);
    }
}
