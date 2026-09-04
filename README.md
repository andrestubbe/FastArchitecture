> [!WARNING]
> **🚧 WORK IN PROGRESS (WIP) — Active Standard Development**
>
> **FastArchitecture** establishes a standardized, minimal cognitive architecture protocol specifically designed to organize **Human / AI Agent Co-Engineering** and **Vibe-Coding**.
>
> AI coding assistants often struggle with multi-thousand-token architectural drift, inventing arbitrary folder patterns, leaking UI state into data models, and drifting into spaghetti code. **FastArchitecture** solves this by reducing architecture to an explicit, ultra-compact 30-token grammar that humans, LLMs, and zero-dependency CI validators understand identically.

# FastArchitecture 0.1.0 — Minimal Cognitive Architecture Standard & Validator for Human + AI Co-Engineering

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Protocol](https://img.shields.io/badge/Protocol-FCA%201.0-brightgreen.svg)](SPEC.md)
[![Agents](https://img.shields.io/badge/Agents-AGENTS.md-blueviolet.svg)](AGENTS.md)

---

**Ultra-compact 30-token architecture standard and zero-dependency Java validator designed to eliminate cognitive drift and boundary bleed in Human/AI Agent co-engineering.**

---

## Quick Start: The 30-Token Manifest

Add this exact contract to `architecture.fca` or paste it directly into your AI prompt (`.cursorrules`, [AGENTS.md](AGENTS.md)):

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

## Table of Contents

- [Quick Start: The 30-Token Manifest](#quick-start-the-30-token-manifest)
- [Why FastArchitecture?](#why-fastarchitecture)
- [Architecture Topology](#architecture-topology)
- [The AI Agent Contract (AGENTS.md)](#the-ai-agent-contract-agentsmd)
- [Minimal Project Structure](#minimal-project-structure)
- [Minimal Violation Example](#minimal-violation-example)
- [Zero-Dependency Reference Validator](#zero-dependency-reference-validator)
- [Optional Utilities](#optional-utilities)
- [Documentation](#documentation)
- [License](#license)

---

## Why FastArchitecture?

In modern AI-assisted engineering and **vibe-coding**, architecture is often the primary point of failure:

- **Token Limits:** Asking an LLM to "figure out" architectural boundaries across thousands of lines consumes precious context windows and invites hallucinations.
- **Architectural Bleed:** Agents invent ad-hoc packages, mutate models directly inside render loops, or introduce circular cross-layer dependencies.
- **Review Overhead:** Developers spend excessive time verifying whether agent diffs broke structural contracts.

**FastArchitecture** replaces architectural guesswork with a tiny, machine-verifiable grammar.

---

## Architecture Topology

Every system following FastArchitecture is partitioned into three strictly segregated layers:

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

1. **Model Isolation**: `Model` owns state snapshots and domain data. It must never import or reference `Control` or `View`.
2. **View Read-Only**: `View` reads `Model` state to produce output. It must never import `Control` or mutate `Model`.
3. **Control Orchestration**: `Control` captures input and state changes, modifies `Model`, but never performs rendering.

---

## The AI Agent Contract (AGENTS.md)

FastArchitecture natively supports the open [AGENTS.md](https://agents.md) standard. 

By placing an [AGENTS.md](AGENTS.md) file at the root of your project, autonomous coding agents (Codex, Cursor, Jules, Claude Code) automatically discover:
- The exact build and test commands (`mvn clean package`).
- The static verification command (`java -jar FastArchitecture.jar check .`).
- The 30-token architecture rules.
- Micro-level code discipline: pair FastArchitecture's macro boundaries with the [Ponytail](https://github.com/DietrichGebert/ponytail) / YAGNI principle to prevent over-engineering.

---

## Minimal Project Structure

```text
MyApp/
 ├── architecture.fca
 ├── AGENTS.md
 └── src/main/java/myapp/
     ├── model/     # State snapshots, domain records
     ├── control/   # Actions, input processors, loop drivers
     └── view/      # Surfaces, overlays, render passes
```

---

## Minimal Violation Example

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
  Source: src/main/java/myapp/view/ZoomOverlay.java (Line 3)
  Import: myapp.control.ZoomController
  Rule:   'deny View -> Control' in architecture.fca
```

---

## Zero-Dependency Reference Validator

FastArchitecture includes a high-performance reference validator implemented in pure Java:
- **Zero dependencies**: No bytecode analyzers, reflection, or third-party libraries.
- **Sub-20ms execution**: Instantaneous validation in commit hooks and agent loops.

```bash
# Validate architecture of current project
java -jar FastArchitecture.jar check .

# Validate specific project directory
java -jar FastArchitecture.jar check /path/to/project

# Scaffold a compliant project skeleton
java -jar FastArchitecture.jar init <project-name>
```

---

## Optional Utilities

The `fastarchitecture.optional` package provides optional, zero-allocation primitives for reactive pipelines (such as CREAM or FastJava pipelines):
- **`FastState`**: Immutable snapshot state representation.
- **`FastDelta`**: Differential patch applied to transition state $S_t \to S_{t+1}$.

> *Note: These primitives are optional utilities and are not required to adopt the FastArchitecture standard.*

---

## Documentation

- **[AGENTS.md](AGENTS.md)** — Standard agent instructions and prompt contract.
- **[SPEC.md](SPEC.md)** — Formal EBNF grammar (FCA 1.0) and layer semantics.
- **[MANIFEST.md](MANIFEST.md)** — Specification and token dictionary for `architecture.fca`.
- **[VALIDATION.md](VALIDATION.md)** — Reference validator pipeline and CLI guide.

---

## License

MIT License — see [LICENSE](LICENSE). Part of the **FastJava Ecosystem**.