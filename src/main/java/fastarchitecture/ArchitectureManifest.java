package fastarchitecture;

import java.io.IOException;
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
     * Parses an architecture.fca file using the ArchitectureParser.
     */
    public static ArchitectureManifest parse(Path fcaPath) throws IOException {
        return ArchitectureParser.parse(fcaPath);
    }
}
