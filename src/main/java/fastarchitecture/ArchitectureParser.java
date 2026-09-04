package fastarchitecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for the formal FCA 1.0 architecture manifest specification (.fca).
 */
public final class ArchitectureParser {

    private ArchitectureParser() {}

    public static ArchitectureManifest parse(Path fcaPath) throws IOException {
        if (!Files.exists(fcaPath)) {
            return ArchitectureManifest.defaultStandard();
        }
        return parseString(Files.readString(fcaPath));
    }

    public static ArchitectureManifest parseString(String content) {
        String archName = "FastArchitecture";
        List<ArchitectureRule> rules = new ArrayList<>();

        String[] lines = content.split("\\R");
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
            return ArchitectureManifest.defaultStandard();
        }
        return new ArchitectureManifest(archName, rules);
    }
}
