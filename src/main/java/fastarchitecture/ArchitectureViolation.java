package fastarchitecture;

import java.nio.file.Path;

/**
 * Details of an architectural rule violation.
 */
public record ArchitectureViolation(
        Path sourceFile,
        int lineNumber,
        String sourceClass,
        Layer sourceLayer,
        String targetClass,
        Layer targetLayer,
        ArchitectureRule violatedRule,
        String sourceLine
) {
    @Override
    public String toString() {
        return String.format("[%s -> %s Violation] in %s:%d\n  Rule: %s\n  Target: %s\n  Code: %s",
                sourceLayer.getDisplayName(),
                targetLayer.getDisplayName(),
                sourceFile.getFileName(),
                lineNumber,
                violatedRule,
                targetClass,
                sourceLine.trim());
    }
}
