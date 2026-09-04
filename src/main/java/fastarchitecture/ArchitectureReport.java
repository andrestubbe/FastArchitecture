package fastarchitecture;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Result report generated after validating a project's architecture.
 */
public final class ArchitectureReport {

    private final Path projectRoot;
    private final ArchitectureManifest manifest;
    private final Map<Layer, List<String>> classesByLayer;
    private final List<ArchitectureViolation> violations;

    public ArchitectureReport(Path projectRoot, ArchitectureManifest manifest,
                              Map<Layer, List<String>> classesByLayer,
                              List<ArchitectureViolation> violations) {
        this.projectRoot = projectRoot;
        this.manifest = manifest;
        this.classesByLayer = classesByLayer != null ? classesByLayer : Collections.emptyMap();
        this.violations = violations != null ? violations : Collections.emptyList();
    }

    public boolean isValid() {
        return violations.isEmpty();
    }

    public Path getProjectRoot() {
        return projectRoot;
    }

    public ArchitectureManifest getManifest() {
        return manifest;
    }

    public Map<Layer, List<String>> getClassesByLayer() {
        return classesByLayer;
    }

    public List<ArchitectureViolation> getViolations() {
        return violations;
    }

    /**
     * Formats the report as a clean, human-readable terminal tree.
     */
    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(" FastArchitecture Validation Report\n");
        sb.append(" Architecture: ").append(manifest.getArchitectureName()).append("\n");
        sb.append(" Target:       ").append(projectRoot.toAbsolutePath()).append("\n");
        sb.append("=================================================================\n\n");

        sb.append("[Topology]\n");
        sb.append(projectRoot.getFileName()).append("\n");
        int mCount = classesByLayer.getOrDefault(Layer.MODEL, Collections.emptyList()).size();
        int cCount = classesByLayer.getOrDefault(Layer.CONTROL, Collections.emptyList()).size();
        int vCount = classesByLayer.getOrDefault(Layer.VIEW, Collections.emptyList()).size();

        sb.append("├── Model   (").append(mCount).append(" classes)\n");
        sb.append("├── Control (").append(cCount).append(" classes)\n");
        sb.append("└── View    (").append(vCount).append(" classes)\n\n");

        if (isValid()) {
            sb.append("✓ Model boundaries isolated (no forbidden outgoing calls)\n");
            sb.append("✓ Control boundaries verified (state mutations permitted)\n");
            sb.append("✓ View boundaries verified (read-only presentation)\n");
            sb.append("✓ All ").append(manifest.getRules().size()).append(" dependency rules satisfied.\n\n");
            sb.append("RESULT: ARCHITECTURE VALID (0 violations)\n");
        } else {
            sb.append("✗ FAILED: ").append(violations.size()).append(" architectural violation(s) detected:\n\n");
            int idx = 1;
            for (ArchitectureViolation v : violations) {
                sb.append(idx++).append(") ").append(v).append("\n\n");
            }
            sb.append("RESULT: ARCHITECTURE INVALID (").append(violations.size()).append(" violation(s))\n");
        }
        sb.append("=================================================================");
        return sb.toString();
    }
}
