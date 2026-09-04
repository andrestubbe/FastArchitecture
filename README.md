> [!WARNING]
> **🚧 WORK IN PROGRESS (WIP) — Active Standard Development**
>
> **FastArchitecture** establishes a standardized, minimal cognitive architecture protocol specifically designed to organize **Human / AI Agent Co-Engineering** and **Vibe-Coding**.
>
> AI coding assistants often struggle with multi-thousand-token architectural drift, inventing arbitrary folder patterns, leaking UI state into data models, and drifting into spaghetti code. **FastArchitecture** solves this by reducing architecture to an explicit, ultra-compact 30-token grammar that humans, LLMs, and zero-dependency CI validators understand identically.

# FastArchitecture 0.1.0 [ALPHA-2026-09] — Minimal Cognitive Architecture Standard & Verification for Human + AI Co-Engineering

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Cross--Platform-lightgrey.svg)]()
[![Architecture](https://img.shields.io/badge/Protocol-FCA%201.0-brightgreen.svg)](SPEC.md)

---

## 🎯 The Core Philosophy: Stop Guessing Architecture

In modern AI-assisted engineering and **vibe-coding**, architecture is often the biggest failure point:
- **Token Limits:** Asking an LLM to "figure out" your architectural boundaries across 20,000 lines of code consumes vast context windows and invites hallucinations.
- **Architectural Drift:** Agents invent new helper packages, mutate data models directly inside UI render loops, and create circular dependencies.
- **Cognitive Overload:** Developers must constantly review agent diffs to check whether structural boundaries were broken.

**FastArchitecture** replaces architectural guesswork with a tiny, machine-checkable grammar:

```
FastArchitecture
├── Model      (owns state and domain data)
├── Control    (owns behavior, decisions, mutations)
└── View       (owns presentation and output)
```

Instead of a heavy, rigid runtime framework, FastArchitecture is a **declarative protocol** (`architecture.fca`) that fits into ~30 tokens.

---

## 🤖 The AI Prompt Snippet (30 Tokens)

Feed this exact block to any LLM (Claude, ChatGPT, Gemini, Copilot) or add it to `.cursorrules` / system prompts:

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

With just those lines, any AI agent knows immediately:
- ✅ May `Control` modify `Model`? **Yes.**
- ❌ May `View` call `Control`? **Denied.**
- ❌ May `Model` reference `View`? **Denied.**
- ❌ May `Control` render pixels? **Denied.**

---

## 📂 Project Structure: Convention over Configuration

```text
MyProject/
├── architecture.fca
└── src/main/java/myproject/
    ├── model/               # Model: Data classes, State snapshots, Diffs
    │   ├── AppState.java
    │   └── UserSession.java
    ├── control/             # Control: Event handlers, Actions, Controllers
    │   ├── InputController.java
    │   └── ActionHandler.java
    └── view/                # View: Windows, Canvas, FastVulkan overlays
        ├── MainWindow.java
        └── RenderPass.java
```

---

## 🔍 Zero-Dependency Validator CLI

FastArchitecture includes a standalone, lightning-fast static import scanner in pure Java (zero external libraries):

```bash
# Check compliance of current project
java -jar FastArchitecture.jar check .

# Initialize a new compliant skeleton
java -jar FastArchitecture.jar init MyApp
```

### CLI Output Example

```text
FastArchitecture Scanner v0.1.0
Scanning: C:\MyProject
Rules: architecture.fca

[Topology]
MyProject
├── Model   (4 classes)
├── Control (3 classes)
└── View    (2 classes)

✓ Model isolated (no outgoing dependencies to Control or View)
✓ Control verified (valid dependencies to Model)
✓ View verified (read-only dependencies to Model)
✓ Forbidden cross-layer calls (0 detected)

Result: ARCHITECTURE VALID
```

---

## 📦 Optional Primitives

For reactive / diff-driven architectures (like CREAM or FastJava pipelines), FastArchitecture provides two optional, zero-allocation primitives in `fastarchitecture.optional`:
- **`FastState`**: Immutable snapshot state representation.
- **`FastDelta`**: Differential patch applied to transition from state $S_t$ to $S_{t+1}$.

---

## 📚 Documentation

- **[SPEC.md](SPEC.md)** — Full specification of the FCA grammar and verification rules.
- **[architecture.fca](architecture.fca)** — Standard manifest template.

---

## 📜 License

MIT License — see [LICENSE](LICENSE) for details. Part of the FastJava Ecosystem.
