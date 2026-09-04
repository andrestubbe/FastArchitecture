> [!WARNING]
> **🚧 WORK IN PROGRESS (WIP) — Active Standard Development**
>
> **FastArchitecture** establishes a standardized, minimal cognitive architecture protocol specifically designed to organize **Human / AI Agent Co-Engineering** and **Vibe-Coding**.
>
> AI coding assistants often struggle with multi-thousand-token architectural drift, inventing arbitrary folder patterns, leaking UI state into data models, and drifting into spaghetti code. **FastArchitecture** solves this by reducing architecture to an explicit, ultra-compact 30-token grammar that humans, LLMs, and zero-dependency CI validators understand identically.

# FastArchitecture 0.1.0 — Minimal Cognitive Architecture Standard for Human + AI Co-Engineering

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Protocol](https://img.shields.io/badge/Protocol-FCA%201.0-brightgreen.svg)](SPEC.md)

---

## 🎯 Why FastArchitecture?

In modern AI-assisted engineering and **vibe-coding**, architecture is often the primary point of failure:
- **Token Limits:** Explaining complex architectures across thousands of tokens invites hallucinations and cognitive drift.
- **Architectural Bleed:** Agents invent ad-hoc packages, mutate models inside rendering routines, or create circular dependencies.
- **Review Overhead:** Developers spend excessive time checking if agent diffs violated structural boundaries.

**FastArchitecture** replaces architectural guesswork with a tiny, machine-verifiable grammar:

```text
FastArchitecture
     │
     ├── Model    (state, domain data)
     ├── Control  (behavior, decisions)
     └── View     (presentation, output)

Allowed:
  Control -> Model.read
  Control -> Model.write
  View    -> Model.read

Forbidden:
  Model -> Control
  Model -> View
  View  -> Control
```

---

## 🤖 The AI Prompt Snippet (30 Tokens)

Paste this exact block into your agent prompt (Claude, Gemini, ChatGPT, Copilot) or `.cursorrules`:

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

---

## 📂 Minimal Project Example

```text
MyApp/
 ├── architecture.fca
 └── src/main/java/myapp/
     ├── model/     # State snapshots, domain data
     ├── control/   # Actions, input processors, loop drivers
     └── view/      # Surfaces, overlays, render passes
```

### Minimal Violation Example

If an AI agent produces:
```java
// File: src/main/java/myapp/view/ZoomOverlay.java
package myapp.view;

import myapp.control.ZoomController; // ✗ FORBIDDEN

public class ZoomOverlay {
    private ZoomController controller;
}
```

The validator immediately flags:
```text
✗ VIOLATION: View -> Control
  Source: src/main/java/myapp/view/ZoomOverlay.java
  Import: myapp.control.ZoomController
  Rule:   'deny View -> Control'
```

---

## 🔍 Reference Validator (Java)

FastArchitecture includes a zero-dependency reference validator:
- **Zero dependencies**: No bytecode analyzers, reflection, or external libraries.
- **Sub-20ms execution**: Instantaneous check in commit hooks or agent loops.

```bash
# Validate architecture of a project
java -jar FastArchitecture.jar check <project-directory>

# Scaffold a compliant project skeleton
java -jar FastArchitecture.jar init <project-name>
```

---

## 📦 Optional Utilities

The `fastarchitecture.optional` package provides optional, zero-allocation primitives for reactive pipelines (such as CREAM or FastJava pipelines):
- **`FastState`**: Immutable snapshot state representation.
- **`FastDelta`**: Differential patch applied to transition state $S_t \to S_{t+1}$.

> *Note: These primitives are optional utilities and are not required to adopt the FastArchitecture standard.*

---

## 📚 Documentation

- **[SPEC.md](SPEC.md)** — Formal EBNF grammar (FCA 1.0) and layer semantics.
- **[MANIFEST.md](MANIFEST.md)** — Specification and token dictionary for `architecture.fca`.
- **[VALIDATION.md](VALIDATION.md)** — Reference validator pipeline and CLI guide.

---

## 📜 License

MIT License — see [LICENSE](LICENSE).
