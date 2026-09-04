package fastarchitecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ArchitectureValidatorTest {

    @Test
    public void testScreenZoomDemoIsValid() throws IOException {
        Path demoDir = Path.of("examples/ScreenZoomDemo");
        if (Files.exists(demoDir)) {
            ArchitectureReport report = FastArchitecture.validate(demoDir);
            assertTrue(report.isValid(), "ScreenZoomDemo must be 100% compliant with FastArchitecture");
            assertEquals(0, report.getViolations().size());
        }
    }

    @Test
    public void testSelfValidationIsValid() throws IOException {
        ArchitectureReport report = FastArchitecture.validate(Path.of("."));
        assertTrue(report.isValid(), "FastArchitecture itself must satisfy its architectural rules");
        assertEquals(0, report.getViolations().size());
    }

    @Test
    public void testValidDependenciesPass(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\npublic class State {}",
            "package app.control;\nimport app.model.State;\npublic class Controller { State s; }",
            "package app.view;\nimport app.model.State;\npublic class Overlay { State s; }"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertTrue(report.isValid(), "Control->Model and View->Model must be allowed");
        assertEquals(0, report.getViolations().size());
    }

    @Test
    public void testModelToControlViolationDetected(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\nimport app.control.Controller;\npublic class State { Controller c; }",
            "package app.control;\npublic class Controller {}",
            "package app.view;\npublic class Overlay {}"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertFalse(report.isValid(), "Model must not import Control");
        assertEquals(1, report.getViolations().size());
        ArchitectureViolation v = report.getViolations().get(0);
        assertEquals(Layer.MODEL, v.sourceLayer());
        assertEquals(Layer.CONTROL, v.targetLayer());
    }

    @Test
    public void testViewToControlViolationDetected(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\npublic class State {}",
            "package app.control;\npublic class Controller {}",
            "package app.view;\nimport app.control.Controller;\npublic class Overlay { Controller c; }"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertFalse(report.isValid(), "View must not import Control");
        assertEquals(1, report.getViolations().size());
        ArchitectureViolation v = report.getViolations().get(0);
        assertEquals(Layer.VIEW, v.sourceLayer());
        assertEquals(Layer.CONTROL, v.targetLayer());
    }

    @Test
    public void testModelToViewViolationDetected(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\nimport app.view.Overlay;\npublic class State { Overlay o; }",
            "package app.control;\npublic class Controller {}",
            "package app.view;\npublic class Overlay {}"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertFalse(report.isValid(), "Model must not import View");
        assertEquals(1, report.getViolations().size());
        ArchitectureViolation v = report.getViolations().get(0);
        assertEquals(Layer.MODEL, v.sourceLayer());
        assertEquals(Layer.VIEW, v.targetLayer());
    }

    @Test
    public void testWildcardImportViolationDetected(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\npublic class State {}",
            "package app.control;\npublic class Controller {}",
            "package app.view;\nimport app.control.*;\npublic class Overlay {}"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertFalse(report.isValid(), "View must not wildcard-import Control");
        assertEquals(1, report.getViolations().size());
        ArchitectureViolation v = report.getViolations().get(0);
        assertEquals(Layer.VIEW, v.sourceLayer());
        assertEquals(Layer.CONTROL, v.targetLayer());
    }

    @Test
    public void testInlineQualifiedTypeViolationDetected(@TempDir Path tempDir) throws IOException {
        setupProject(tempDir,
            "package app.model;\npublic class State {}",
            "package app.control;\npublic class Controller {}",
            "package app.view;\npublic class Overlay { private app.control.Controller controller; }"
        );

        ArchitectureReport report = FastArchitecture.validate(tempDir);
        assertFalse(report.isValid(), "Inline qualified usage of Control in View must trigger violation");
        assertEquals(1, report.getViolations().size());
        ArchitectureViolation v = report.getViolations().get(0);
        assertEquals(Layer.VIEW, v.sourceLayer());
        assertEquals(Layer.CONTROL, v.targetLayer());
    }

    @Test
    public void testDefaultStandardManifestFallback(@TempDir Path tempDir) throws IOException {
        ArchitectureManifest manifest = ArchitectureParser.parse(tempDir.resolve("nonexistent.fca"));
        assertNotNull(manifest);
        assertEquals("FastArchitecture", manifest.getArchitectureName());
        assertEquals(3, manifest.getRules().size());
    }

    private void setupProject(Path root, String modelSrc, String controlSrc, String viewSrc) throws IOException {
        Path modelDir = root.resolve("src/app/model");
        Path controlDir = root.resolve("src/app/control");
        Path viewDir = root.resolve("src/app/view");

        Files.createDirectories(modelDir);
        Files.createDirectories(controlDir);
        Files.createDirectories(viewDir);

        Files.writeString(modelDir.resolve("State.java"), modelSrc);
        Files.writeString(controlDir.resolve("Controller.java"), controlSrc);
        Files.writeString(viewDir.resolve("Overlay.java"), viewSrc);

        String fca = "architecture FastArchitecture\nModel owns state\nControl reads Model\nControl writes Model\nView reads Model\ndeny Model -> Control\ndeny Model -> View\ndeny View -> Control\n";
        Files.writeString(root.resolve("architecture.fca"), fca);
    }
}