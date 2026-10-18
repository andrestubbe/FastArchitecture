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
        String sourceLine,
        ArchitectureScope sourceScope,
        ArchitectureScope targetScope
) {
    public ArchitectureViolation(
            Path sourceFile,
            int lineNumber,
            String sourceClass,
            Layer sourceLayer,
            String targetClass,
            Layer targetLayer,
            ArchitectureRule violatedRule,
            String sourceLine
    ) {
        this(sourceFile, lineNumber, sourceClass, sourceLayer, targetClass, targetLayer, violatedRule, sourceLine,
                new ArchitectureScope(sourceLayer), new ArchitectureScope(targetLayer));
    }

    @Override
    public String toString() {
        String sLabel = sourceScope != null ? sourceScope.getDisplayName() : sourceLayer.getDisplayName();
        String tLabel = targetScope != null ? targetScope.getDisplayName() : targetLayer.getDisplayName();
        return String.format("[%s -> %s Violation] in %s:%d\n  Rule: %s\n  Target: %s\n  Code: %s",
                sLabel,
                tLabel,
                sourceFile.getFileName(),
                lineNumber,
                violatedRule,
                targetClass,
                sourceLine.trim());
    }
}
