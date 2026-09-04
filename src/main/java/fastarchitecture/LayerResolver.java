package fastarchitecture;

import java.nio.file.Path;
import java.util.Map;

/**
 * Resolves source files and imported type signatures to Architectural Layers via convention.
 */
public final class LayerResolver {

    public Layer resolveSourceLayer(Path file, String packageName) {
        String pathStr = file.toString().replace('\\', '/').toLowerCase();
        String pkgStr = packageName.toLowerCase();

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
}