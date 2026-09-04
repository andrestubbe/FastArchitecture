package fastarchitecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Public facade and primary entry point for FastArchitecture operations.
 */
public final class FastArchitecture {

    private FastArchitecture() {}

    /**
     * Validates the architecture of a project directory against its architecture.fca (or default standard).
     *
     * @param projectRoot Path to the project root directory
     * @return ArchitectureReport with full validation details
     */
    public static ArchitectureReport validate(Path projectRoot) throws IOException {
        Path fcaPath = projectRoot.resolve("architecture.fca");
        ArchitectureManifest manifest = ArchitectureManifest.parse(fcaPath);
        ArchitectureValidator validator = new ArchitectureValidator(manifest);
        return validator.validate(projectRoot);
    }

    /**
     * Initializes a compliant FastArchitecture project skeleton in the target directory.
     *
     * @param targetDirectory Directory where the project should be initialized
     * @param appName Name of the application (e.g. "ScreenZoom")
     */
    public static void initSkeleton(Path targetDirectory, String appName) throws IOException {
        Files.createDirectories(targetDirectory);

        // 1. Write architecture.fca
        Path fcaPath = targetDirectory.resolve("architecture.fca");
        if (!Files.exists(fcaPath)) {
            String fcaContent = """
                    architecture FastArchitecture
                    Model owns state,data
                    Control reads Model
                    Control writes Model
                    View reads Model
                    View owns output
                    deny Model -> Control
                    deny Model -> View
                    deny View -> Control
                    """;
            Files.writeString(fcaPath, fcaContent);
        }

        // 2. Create Model, Control, View directory structure
        String pkgBase = appName.toLowerCase().replaceAll("[^a-z0-9]", "");
        Path srcBase = targetDirectory.resolve("src/main/java/" + pkgBase);
        Path modelDir = srcBase.resolve("model");
        Path controlDir = srcBase.resolve("control");
        Path viewDir = srcBase.resolve("view");

        Files.createDirectories(modelDir);
        Files.createDirectories(controlDir);
        Files.createDirectories(viewDir);

        // 3. Write starter templates
        Path stateFile = modelDir.resolve(appName + "State.java");
        if (!Files.exists(stateFile)) {
            Files.writeString(stateFile, String.format("""
                    package %s.model;

                    /**
                     * Model Layer: Owns application state and domain data.
                     * Rule: Must never depend on Control or View.
                     */
                    public record %sState(boolean active, long timestamp) {
                        public static %sState initial() {
                            return new %sState(true, System.currentTimeMillis());
                        }
                    }
                    """, pkgBase, appName, appName, appName));
        }

        Path ctrlFile = controlDir.resolve(appName + "Controller.java");
        if (!Files.exists(ctrlFile)) {
            Files.writeString(ctrlFile, String.format("""
                    package %s.control;

                    import %s.model.%sState;

                    /**
                     * Control Layer: Handles inputs, business decisions, and state mutations.
                     * Rule: May read and write Model, but never render directly.
                     */
                    public final class %sController {
                        private %sState currentState = %sState.initial();

                        public %sState getState() {
                            return currentState;
                        }

                        public void triggerAction() {
                            this.currentState = new %sState(!currentState.active(), System.currentTimeMillis());
                        }
                    }
                    """, pkgBase, pkgBase, appName, appName, appName, appName, appName, appName));
        }

        Path viewFile = viewDir.resolve(appName + "View.java");
        if (!Files.exists(viewFile)) {
            Files.writeString(viewFile, String.format("""
                    package %s.view;

                    import %s.model.%sState;

                    /**
                     * View Layer: Handles presentation, UI layout, and display.
                     * Rule: May read Model, but never mutate Model or invoke Control.
                     */
                    public final class %sView {
                        public void render(%sState state) {
                            System.out.println("[%s View] Active: " + state.active());
                        }
                    }
                    """, pkgBase, pkgBase, appName, appName, appName, appName));
        }
    }
}
