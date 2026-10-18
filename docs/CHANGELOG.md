# FastArchitecture Version History & Changelog

All notable changes to `FastArchitecture` are documented in this file.

---

## [0.1.1] - 2026-10-18

### Added
- **Feature-Slice Isolation (`isolate <sliceA> from <sliceB>`)**:
  - Enforce horizontal isolation across orthogonal feature domains and execution channels (e.g., `isolate *.cli from *.gui`).
  - Added `ArchitectureScope` and slice pattern matching in `LayerResolver` and `ArchitectureParser`.
  - Added dedicated unit tests in `ArchitectureValidatorTest`.
- **Generic 3-Tier Reference Topology**:
  - Formalized universal architectural mapping: vertical layers (`Model`, `Control`, `View`) sub-divided into domain slices (`core`, `service`, `cli`, `gui`).
- **Standardized FastJava Layout**:
  - Aligned badges, headers, and CI references with FastJava Blueprint standard.

---

## [0.1.0] - 2026-09-04

### Added
- **Initial Release of FastArchitecture**:
  - Ultra-compact 30-token architecture grammar (`architecture.fca`).
  - Zero-dependency Java 17+ reference validator (`FastArchitecture.jar`).
  - Sub-20ms static dependency scan time for git pre-commit hooks and CI.
  - Native integration with `AGENTS.md` and LLM prompting conventions.
