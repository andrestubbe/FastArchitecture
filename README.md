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
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastArchitecture)

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
- [Key Features](#key-features)
- [Architecture Topology](#architecture-topology)
- [The AI Agent Contract (AGENTS.md)](#the-ai-agent-contract-agentsmd)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [Minimal Project Structure](#minimal-project-structure)
- [Minimal Violation Example](#minimal-violation-example)
- [Zero-Dependency Reference Validator](#zero-dependency-reference-validator)
- [Installation](#installation)
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

## Key Features

- **30-Token Standard (`architecture.fca`)** — Fits completely inside `.cursorrules`, [AGENTS.md](AGENTS.md), or system prompts without wasting token context.
- **Strict Boundary Isolation** — Enforces unidirectional state flow (`deny View -> Control`, `deny Model -> View`).
- **Zero-Dependency Reference Validator** — Standalone static scanner in pure Java 17+ with zero third-party libraries.
- **Sub-20ms Execution Speed** — Runs instantly in git pre-commit hooks and local agent interaction loops.
- **Natively Compatible with AGENTS.md** — Directly supports the industry standard for AI coding agents.
- **Micro-Discipline Synergy** — Pairs macroscopic boundary enforcement with the [Ponytail](https://github.com/DietrichGebert/ponytail) / YAGNI anti-over-engineering principle.

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

## Real-World Use Cases

- **Autonomous AI & Vibe-Coding Sessions**: Give LLMs (Claude Code, Cursor, Codex) an indisputable set of boundaries. The validator catches illegal cross-layer imports before PRs are created.
- **High-Performance Engines ([FastVulkan](https://github.com/andrestubbe/FastVulkan))**: Ensure GPU render passes and Vulkan pipeline descriptors (`View`) never mutate simulation logic or event loops (`Control`).
- **Low-Latency Input Systems ([FastKeyboard](https://github.com/andrestubbe/FastKeyboard))**: Guarantee that hardware scancode interceptors feed strictly into `Control` and update `Model` state without direct UI couplings.
- **CI/CD Quality Gates**: Enforce zero architectural decay on every git commit in under 20 milliseconds without spinning up heavyweight test containers.

---

## Performance Benchmarks

Empirical validation benchmarks measured on a standard developer workstation (Windows 11, Oracle JDK 21, 8 CPUs, scanning 100 Java classes across 100 iterations via `ArchitectureBenchmark`):

| Tool | Approach | Dependencies | Execution Time |
|---|---|---|---|
| **FastArchitecture** | Static Source Parsing | **0 (Pure Java)** | **~18 ms** (Min: 16.1 ms) |
| [ArchUnit](https://www.archunit.org/) | Bytecode Reflection + JUnit | ~15 external JARs | ~2,450 ms |
| [SonarQube Scanner](https://www.sonarsource.com/products/sonarqube/) | Full AST Semantic Graph | Heavy JVM agent | ~18,200 ms |

FastArchitecture completes in ~18 ms (approx. 1 display frame at 60 Hz), making it suitable for continuous execution on every file save or pre-commit hook. The reproducible benchmark harness is located in [`ArchitectureBenchmark.java`](src/test/java/fastarchitecture/benchmark/ArchitectureBenchmark.java).

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

## Installation

### Option 1: Maven (via JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastArchitecture</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastArchitecture:0.1.0'
}
```

### Option 3: Standalone Executable CLI (Direct Download)

Download the standalone executable JAR from [Releases](https://github.com/andrestubbe/FastArchitecture/releases) and run directly without any external dependencies:

```bash
# Validate architecture of current project
java -jar FastArchitecture-0.1.0.jar check .
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