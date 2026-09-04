package fastarchitecture;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * High-performance, zero-dependency static source scanner and architecture validator.
 */
public final class ArchitectureValidator {

    private final ArchitectureManifest manifest;
    private final LayerResolver layerResolver;
    private final DependencyScanner dependencyScanner;

    public ArchitectureValidator(ArchitectureManifest manifest) {
        this.manifest = manifest != null ? manifest : ArchitectureManifest.defaultStandard();
        this.layerResolver = new LayerResolver();
        this.dependencyScanner = new DependencyScanner();
    }

    public ArchitectureReport validate(Path projectRoot) throws IOException {
        Path root = projectRoot.toAbsolutePath().normalize();

        // 1. Discover all Java source files (excluding build directories)
        List<Path> javaFiles = dependencyScanner.discoverSourceFiles(root);

        // 2. Pass 1: Register all classes and resolve their Architectural Layer
        Map<String, Layer> classToLayer = new HashMap<>();
        Map<Layer, List<String>> classesByLayer = new EnumMap<>(Layer.class);
        for (Layer l : Layer.values()) {
            classesByLayer.put(l, new ArrayList<>());
        }

        Map<Path, DependencyScanner.ParsedSource> parsedFiles = new HashMap<>();

        for (Path file : javaFiles) {
            DependencyScanner.ParsedSource parsed = dependencyScanner.parseSource(file);
            parsedFiles.put(file, parsed);

            Layer layer = layerResolver.resolveSourceLayer(file, parsed.packageName());
            if (layer != Layer.UNKNOWN && parsed.className() != null) {
                String fullClassName = parsed.packageName().isEmpty() ? parsed.className() : parsed.packageName() + "." + parsed.className();
                classToLayer.put(fullClassName, layer);
                classToLayer.put(parsed.className(), layer); // Also map simple name for same-package resolution
                classesByLayer.get(layer).add(fullClassName);
            }
        }

        // 3. Pass 2: Verify imports against architectural rules
        List<ArchitectureViolation> violations = new ArrayList<>();

        for (Map.Entry<Path, DependencyScanner.ParsedSource> entry : parsedFiles.entrySet()) {
            Path file = entry.getKey();
            DependencyScanner.ParsedSource parsed = entry.getValue();
            Layer sourceLayer = layerResolver.resolveSourceLayer(file, parsed.packageName());

            if (sourceLayer == Layer.UNKNOWN) {
                continue;
            }

            for (DependencyScanner.ImportStatement imp : parsed.imports()) {
                Layer targetLayer = layerResolver.resolveTargetLayer(imp.target(), classToLayer);
                if (targetLayer != Layer.UNKNOWN && targetLayer != sourceLayer) {
                    ArchitectureRule rule = findViolatedRule(sourceLayer, targetLayer);
                    if (rule != null) {
                        violations.add(new ArchitectureViolation(
                                file,
                                imp.lineNumber(),
                                parsed.className(),
                                sourceLayer,
                                imp.target(),
                                targetLayer,
                                rule,
                                imp.rawLine()
                        ));
                    }
                }
            }
        }

        return new ArchitectureReport(root, manifest, classesByLayer, violations);
    }

    private ArchitectureRule findViolatedRule(Layer from, Layer to) {
        for (ArchitectureRule r : manifest.getRules()) {
            if (r.from() == from && r.to() == to && !r.allowed()) {
                return r;
            }
        }
        return null;
    }
}
