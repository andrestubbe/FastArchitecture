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
                    ArchitectureRule rule = parseRule(parts[0].trim(), parts[1].trim(), false);
                    if (rule != null) {
                        rules.add(rule);
                    }
                }
            } else if (line.startsWith("allow ")) {
                String statement = line.substring("allow ".length()).trim();
                String[] parts = statement.split("->");
                if (parts.length == 2) {
                    ArchitectureRule rule = parseRule(parts[0].trim(), parts[1].trim(), true);
                    if (rule != null) {
                        rules.add(rule);
                    }
                }
            } else if (line.startsWith("isolate ")) {
                String statement = line.substring("isolate ".length()).trim();
                String[] parts = statement.split("(?i)\\s+from\\s+");
                if (parts.length == 2) {
                    ArchitectureRule rule1 = parseRule(parts[0].trim(), parts[1].trim(), false);
                    ArchitectureRule rule2 = parseRule(parts[1].trim(), parts[0].trim(), false);
                    if (rule1 != null) rules.add(rule1);
                    if (rule2 != null) rules.add(rule2);
                }
            }
        }

        if (rules.isEmpty()) {
            return ArchitectureManifest.defaultStandard();
        }
        return new ArchitectureManifest(archName, rules);
    }

    private static ArchitectureRule parseRule(String fromToken, String toToken, boolean allowed) {
        LayerAndSlice from = parseToken(fromToken);
        LayerAndSlice to = parseToken(toToken);

        if (from == null || to == null) return null;
        return new ArchitectureRule(from.layer, from.slice, to.layer, to.slice, allowed);
    }

    private static LayerAndSlice parseToken(String token) {
        if (token == null || token.isBlank()) return null;
        token = token.trim();

        if (token.equals("*")) {
            return new LayerAndSlice(null, null);
        }

        if (token.contains(".")) {
            String[] parts = token.split("\\.", 2);
            Layer layer = parts[0].equals("*") ? null : Layer.fromString(parts[0]);
            String slice = parts[1].equals("*") ? null : parts[1].toLowerCase();
            return new LayerAndSlice(layer, slice);
        }

        Layer layer = Layer.fromString(token);
        if (layer != Layer.UNKNOWN) {
            return new LayerAndSlice(layer, null);
        }
        // If not a known layer, treat as slice (e.g. "cli")
        return new LayerAndSlice(null, token.toLowerCase());
    }

    private record LayerAndSlice(Layer layer, String slice) {}
}
