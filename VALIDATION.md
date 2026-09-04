# FastArchitecture Reference Validator Specification

The FastArchitecture validator is a zero-dependency static analysis tool designed for instantaneous (<20 ms) verification in local development loops, CI/CD pipelines, and agent worktrees.

---

## 1. Architecture Pipeline

```
Source Files (.java) + architecture.fca
                │
                ▼
      [ ArchitectureParser ]
        Parse .fca rules
                │
                ▼
       [ LayerResolver ]
        Map files -> Layers (via convention)
                │
                ▼
     [ DependencyScanner ]
        Inspect imports & class usages
                │
                ▼
    [ ArchitectureValidator ]
        Match dependencies against deny rules
                │
                ▼
      [ Exit Code & Report ]
        0: VALID | 1: VIOLATION
```

---

## 2. Layer Resolution Strategy

To maintain zero runtime dependencies and sub-second execution, the reference validator uses **Convention over Configuration**:

1. **Folder & Package Matching**:
   - Files located in `*/model/*` or with package containing `.model` are classified as **`Layer.MODEL`**.
   - Files located in `*/control/*` or with package containing `.control` are classified as **`Layer.CONTROL`**.
   - Files located in `*/view/*` or with package containing `.view` are classified as **`Layer.VIEW`**.
2. **Ambiguity**:
   - Classes not in a declared layer folder (e.g. root `Main.java`) are classified as `Layer.UNKNOWN` and serve as application composition roots.

---

## 3. Dependency Scanning

For every resolved class file, the scanner parses:
- Explicit single-type imports (e.g., `import com.app.control.Handler;`)
- Package-on-demand imports (e.g., `import com.app.control.*;`)
- Fully-qualified name mentions in method signatures or fields

If a file classified under `SourceLayer` imports or invokes any type belonging to a layer forbidden by `deny SourceLayer -> TargetLayer`, an `ArchitectureViolation` is recorded.

---

## 4. Exit Codes & CLI Usage

| Code | Meaning |
|---|---|
| `0` | **VALID**: All files satisfy all declared architectural rules. |
| `1` | **VIOLATIONS DETECTED**: One or more forbidden cross-layer imports were found. |
| `2` | **ERROR**: Manifest syntax error or invalid target path. |

### CLI Commands
```bash
# Run validation check on directory
java -jar FastArchitecture.jar check <project-dir>

# Generate new compliant project skeleton
java -jar FastArchitecture.jar init <project-name>
```
