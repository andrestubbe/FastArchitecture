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
        Layer exact = classToLayer.get(imported);
        if (exact != null) return exact;

        String lower = imported.toLowerCase();
        if (lower.contains(".model.")) return Layer.MODEL;
        if (lower.contains(".control.") || lower.contains(".controller.")) return Layer.CONTROL;
        if (lower.contains(".view.")) return Layer.VIEW;

        return Layer.UNKNOWN;
    }
}
