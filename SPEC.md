# FastArchitecture Specification (FCA v1.0)

**FastArchitecture** defines an ultra-compact architectural grammar designed for **Human / AI Agent Co-Engineering** and **Vibe-Coding**.

---

## 1. The Core Grammar

Every software system is partitioned into exactly three primary layers:

```
FastArchitecture
├── Model      (owns state and domain data)
├── Control    (owns behavior, decisions, and mutations)
└── View       (owns presentation and output)
```

### Layer Responsibilities

| Layer | Owns | Permitted Dependencies | Forbidden Dependencies |
|---|---|---|---|
| **`Model`** | State, domain entities, immutable value objects, diffs | Only `Model` | `Control`, `View` |
| **`Control`** | Event handlers, input processors, logic loops, action triggers | `Model`, other `Control` | Direct rendering, `View` manipulation |
| **`View`** | Render passes, UI components, overlays, presentation adapters | `Model` (read-only), other `View` | `Control`, `Model` write mutations |

---

## 2. The `.fca` Manifest Format

A project declares its architecture via an `architecture.fca` file placed in the project root:

```text
architecture FastArchitecture
Model owns state,data
Control reads Model
Control writes Model
View reads Model
View owns output
deny Model -> Control
deny Model -> View
deny View -> Control
```

### Directed Dependency Rules

```
       ┌─────────┐
       │  Model  │
       └───┬─┬───┘
     read  │ │  read + write
  ┌────────┘ └────────┐
  ▼                   ▼
┌──────┐            ┌─────────┐
│ View │            │ Control │
└──────┘            └─────────┘
  │                      │
  X (deny View->Control) X (deny Control->View)
```

1. **`Model` is strictly isolated**: `Model` classes may NEVER import or call `Control` or `View`.
2. **`View` never mutates**: `View` classes read `Model` state to render output, but NEVER invoke `Control` or mutate `Model`.
3. **`Control` mediates**: `Control` captures input and state changes, modifies `Model`, but NEVER performs direct rendering.

---

## 3. Convention over Configuration

Project directory structures map directly to architectural layers:

```text
MyProject/
├── architecture.fca
├── src/main/java/myproject/
│   ├── model/         <-- Layer: Model
│   │   └── AppState.java
│   ├── control/       <-- Layer: Control
│   │   └── InputController.java
│   └── view/          <-- Layer: View
│       └── MainWindow.java
```

---

## 4. Mechanical Verification

Run the validator via CLI:

```bash
fastarchitecture check .
```

Output on success:
```text
FastArchitecture Validator v0.1.0
Target: C:\MyProject
Manifest: architecture.fca

[Tree]
MyProject
├── Model   (4 classes)
├── Control (3 classes)
└── View    (2 classes)

✓ Model boundaries respected (0 violations)
✓ Control boundaries respected (0 violations)
✓ View boundaries respected (0 violations)
✓ Dependency rules verified (deny Model->View, deny Model->Control, deny View->Control)

Architecture: VALID (0 errors)
```

Output on violation:
```text
✗ VIOLATION: View -> Control
  File: src/main/java/myproject/view/MainWindow.java
  Line: import myproject.control.InputController;
  Rule: 'deny View -> Control' in architecture.fca

Architecture: INVALID (1 error found)
```
