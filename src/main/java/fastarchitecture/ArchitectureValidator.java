package fastarchitecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * High-performance, zero-dependency static source scanner and architecture validator.
 */
public final class ArchitectureValidator {

    private final ArchitectureManifest manifest;

    public ArchitectureValidator(ArchitectureManifest manifest) {
        this.manifest = manifest != null ? manifest : ArchitectureManifest.defaultStandard();
    }

    public ArchitectureReport validate(Path projectRoot) throws IOException {
        Path root = projectRoot.toAbsolutePath().normalize();

        // 1. Discover all Java source files (excluding build directories)
        List<Path> javaFiles = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.toString().endsWith(".java"))
                  .filter(p -> !isIgnored(p))
                  .forEach(javaFiles::add);
        }

        // 2. Pass 1: Register all classes and resolve their Architectural Layer
        Map<String, Layer> classToLayer = new HashMap<>();
        Map<Layer, List<String>> classesByLayer = new EnumMap<>(Layer.class);
        for (Layer l : Layer.values()) {
            classesByLayer.put(l, new ArrayList<>());
        }

        Map<Path, ParsedFile> parsedFiles = new HashMap<>();

        for (Path file : javaFiles) {
            ParsedFile parsed = parseFileStructure(file);
            parsedFiles.put(file, parsed);

            Layer layer = resolveLayer(file, parsed.packageName);
            if (layer != Layer.UNKNOWN && parsed.className != null) {
                String fullClassName = parsed.packageName.isEmpty() ? parsed.className : parsed.packageName + "." + parsed.className;
                classToLayer.put(fullClassName, layer);
                classToLayer.put(parsed.className, layer); // Also map simple name for same-package resolution
                classesByLayer.get(layer).add(fullClassName);
            }
        }

        // 3. Pass 2: Verify imports against architectural rules
        List<ArchitectureViolation> violations = new ArrayList<>();

        for (Map.Entry<Path, ParsedFile> entry : parsedFiles.entrySet()) {
            Path file = entry.getKey();
            ParsedFile parsed = entry.getValue();
            Layer sourceLayer = resolveLayer(file, parsed.packageName);

            if (sourceLayer == Layer.UNKNOWN) {
                continue;
            }

            for (ImportStatement imp : parsed.imports) {
                Layer targetLayer = resolveTargetLayer(imp.target, classToLayer);
                if (targetLayer != Layer.UNKNOWN && targetLayer != sourceLayer) {
                    ArchitectureRule rule = findViolatedRule(sourceLayer, targetLayer);
                    if (rule != null) {
                        violations.add(new ArchitectureViolation(
                                file,
                                imp.lineNumber,
                                parsed.className,
                                sourceLayer,
                                imp.target,
                                targetLayer,
                                rule,
                                imp.rawLine
                        ));
                    }
                }
            }
        }

        return new ArchitectureReport(root, manifest, classesByLayer, violations);
    }

    private Layer resolveLayer(Path file, String packageName) {
        String pathStr = file.toString().replace('\\', '/').toLowerCase();
        String pkgStr = packageName.toLowerCase();

        // Check package path first
        if (pkgStr.contains(".model.") || pkgStr.endsWith(".model") || pathStr.contains("/model/")) {
            return Layer.MODEL;
        }
        if (pkgStr.contains(".control.") || pkgStr.endsWith(".control") || pkgStr.contains(".controller.") || pathStr.contains("/control/")) {
            return Layer.CONTROL;
        }
        if (pkgStr.contains(".view.") || pkgStr.endsWith(".view") || pathStr.contains("/view/")) {
            return Layer.VIEW;
        }
        return Layer.UNKNOWN;
    }

    private Layer resolveTargetLayer(String imported, Map<String, Layer> classToLayer) {
        Layer exact = classToLayer.get(imported);
        if (exact != null) return exact;

        // Check package prefix
        String lower = imported.toLowerCase();
        if (lower.contains(".model.")) return Layer.MODEL;
        if (lower.contains(".control.") || lower.contains(".controller.")) return Layer.CONTROL;
        if (lower.contains(".view.")) return Layer.VIEW;

        return Layer.UNKNOWN;
    }

    private ArchitectureRule findViolatedRule(Layer from, Layer to) {
        for (ArchitectureRule r : manifest.getRules()) {
            if (r.from() == from && r.to() == to && !r.allowed()) {
                return r;
            }
        }
        return null;
    }

    private boolean isIgnored(Path path) {
        String s = path.toString();
        return s.contains(".git") || s.contains("target") || s.contains("build") || s.contains(".idea");
    }

    private record ImportStatement(int lineNumber, String target, String rawLine) {}

    private record ParsedFile(String packageName, String className, List<ImportStatement> imports) {}

    private ParsedFile parseFileStructure(Path file) throws IOException {
        String pkg = "";
        String simpleName = file.getFileName().toString();
        if (simpleName.endsWith(".java")) {
            simpleName = simpleName.substring(0, simpleName.length() - 5);
        }

        List<ImportStatement> imports = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        int lineNum = 0;
        for (String raw : lines) {
            lineNum++;
            String line = raw.trim();
            if (line.startsWith("package ")) {
                pkg = line.substring("package ".length()).replace(";", "").trim();
            } else if (line.startsWith("import ")) {
                String imp = line.substring("import ".length()).replace(";", "").trim();
                // Strip static keyword if present
                if (imp.startsWith("static ")) {
                    imp = imp.substring("static ".length()).trim();
                }
                imports.add(new ImportStatement(lineNum, imp, line));
            }
        }
        return new ParsedFile(pkg, simpleName, imports);
    }
}
