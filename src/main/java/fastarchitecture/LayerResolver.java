package fastarchitecture;

import java.nio.file.Path;
import java.util.Map;

/**
 * Resolves source files and imported type signatures to Architectural Layers via convention.
 */
public final class LayerResolver {

    public Layer resolveSourceLayer(Path file, String packageName) {
        return resolveSourceScope(file, packageName).layer();
    }

    public ArchitectureScope resolveSourceScope(Path file, String packageName) {
        String pathStr = file.toString().replace('\\', '/').toLowerCase();
        String pkgStr = packageName.toLowerCase();

        Layer layer = Layer.UNKNOWN;
        int layerIdx = -1;
        String layerToken = null;

        if (pkgStr.contains(".model.") || pkgStr.endsWith(".model") || pathStr.contains("/model/")) {
            layer = Layer.MODEL;
            layerToken = "model";
        } else if (pkgStr.contains(".control.") || pkgStr.endsWith(".control") || pkgStr.contains(".controller.") || pathStr.contains("/control/")) {
            layer = Layer.CONTROL;
            layerToken = pkgStr.contains(".controller.") ? "controller" : "control";
        } else if (pkgStr.contains(".view.") || pkgStr.endsWith(".view") || pathStr.contains("/view/")) {
            layer = Layer.VIEW;
            layerToken = "view";
        }

        if (layer == Layer.UNKNOWN) {
            return ArchitectureScope.unknown();
        }

        // Extract slice from package if present: e.g. "speakplus.view.gui" -> "gui"
        String slice = extractSliceFromPackage(pkgStr, layerToken);
        if (slice == null) {
            slice = extractSliceFromPath(pathStr, layerToken);
        }

        return ArchitectureScope.of(layer, slice);
    }

    private String extractSliceFromPackage(String pkgStr, String layerToken) {
        if (layerToken == null) return null;
        String target = "." + layerToken + ".";
        int idx = pkgStr.indexOf(target);
        if (idx >= 0) {
            String remainder = pkgStr.substring(idx + target.length());
            int nextDot = remainder.indexOf('.');
            return nextDot >= 0 ? remainder.substring(0, nextDot) : remainder;
        }
        return null;
    }

    private String extractSliceFromPath(String pathStr, String layerToken) {
        if (layerToken == null) return null;
        String target = "/" + layerToken + "/";
        int idx = pathStr.indexOf(target);
        if (idx >= 0) {
            String remainder = pathStr.substring(idx + target.length());
            int nextSlash = remainder.indexOf('/');
            if (nextSlash >= 0) {
                return remainder.substring(0, nextSlash);
            }
        }
        return null;
    }

    public Layer resolveTargetLayer(String imported, Map<String, Layer> classToLayer) {
        if (imported == null || imported.isEmpty()) {
            return Layer.UNKNOWN;
        }

        // 1. Exact match against known project classes
        Layer exact = classToLayer.get(imported);
        if (exact != null) {
            return exact;
        }

        // 2. Structural matching for wildcards and qualified signatures
        String lower = imported.toLowerCase();
        if (lower.contains(".model.") || lower.startsWith("model.") || lower.endsWith(".model.*") || lower.equals("model.*") || lower.endsWith(".model")) {
            return Layer.MODEL;
        }
        if (lower.contains(".control.") || lower.startsWith("control.") || lower.contains(".controller.") || lower.startsWith("controller.") || lower.endsWith(".control.*") || lower.equals("control.*") || lower.endsWith(".control")) {
            return Layer.CONTROL;
        }
        if (lower.contains(".view.") || lower.startsWith("view.") || lower.endsWith(".view.*") || lower.equals("view.*") || lower.endsWith(".view")) {
            return Layer.VIEW;
        }

        return Layer.UNKNOWN;
    }

    public ArchitectureScope resolveTargetScope(String imported, Map<String, ArchitectureScope> classToScope) {
        if (imported == null || imported.isEmpty()) {
            return ArchitectureScope.unknown();
        }

        ArchitectureScope exact = classToScope.get(imported);
        if (exact != null) {
            return exact;
        }

        Layer layer = Layer.UNKNOWN;
        String layerToken = null;
        String lower = imported.toLowerCase();

        if (lower.contains(".model.") || lower.startsWith("model.") || lower.endsWith(".model.*") || lower.equals("model.*") || lower.endsWith(".model")) {
            layer = Layer.MODEL;
            layerToken = "model";
        } else if (lower.contains(".control.") || lower.startsWith("control.") || lower.contains(".controller.") || lower.startsWith("controller.") || lower.endsWith(".control.*") || lower.equals("control.*") || lower.endsWith(".control")) {
            layer = Layer.CONTROL;
            layerToken = lower.contains("controller") ? "controller" : "control";
        } else if (lower.contains(".view.") || lower.startsWith("view.") || lower.endsWith(".view.*") || lower.equals("view.*") || lower.endsWith(".view")) {
            layer = Layer.VIEW;
            layerToken = "view";
        }

        if (layer == Layer.UNKNOWN) {
            return ArchitectureScope.unknown();
        }

        String slice = extractSliceFromPackage(lower, layerToken);
        return ArchitectureScope.of(layer, slice);
    }
}