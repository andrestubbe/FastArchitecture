# FastArchitecture Specification (FCA v1.0)

**FastArchitecture** establishes a formal, ultra-compact architectural grammar designed for **Human / AI Agent Co-Engineering** and **Vibe-Coding**.

---

## 1. Formal Grammar (EBNF)

```ebnf
File          ::= ArchDecl Statement* EOF
ArchDecl      ::= "architecture" Identifier
Statement     ::= LayerDecl | PermissionStmt | DenyRule | Comment
LayerDecl     ::= Identifier "owns" IdentList
PermissionStmt::= Identifier ("reads" | "writes") Identifier
DenyRule      ::= "deny" Identifier "->" Identifier
IdentList     ::= Identifier ("," Identifier)*
Identifier    ::= [a-zA-Z0-9_]+
Comment       ::= "#" [^\n]*
```

---

## 2. The Core Topology

Every system following FastArchitecture is structured into three primary layers:

```text
FastArchitecture
     │
     ├── Model    (state, domain data)
     ├── Control  (behavior, decisions)
     └── View     (presentation, output)
```

### Invariants & Directional Rules

```text
Allowed:
  Control -> Model.read
  Control -> Model.write
  View    -> Model.read

Forbidden:
  Model -> Control
  Model -> View
  View  -> Control
```

1. **Model Isolation**: `Model` owns domain data and state snapshots. It must never have outgoing dependencies to `Control` or `View`.
2. **View Read-Only**: `View` reads `Model` state to produce visual or output representations. It must never import `Control` or mutate `Model`.
3. **Control Orchestration**: `Control` captures user input or system triggers, executes business decisions, and mutates `Model`. It must never perform rendering.

---

## 3. Reference Implementation Guarantees

Any compliant validator must adhere to the following invariants:
- **Determinism**: The same codebase and `.fca` manifest must produce identical validation results on all platforms.
- **Zero Runtime Overhead**: Validation is purely static; no reflection, instrumentation, or runtime proxies are required.
- **Fail-Fast**: A single architectural violation must return exit code `1` in CI/CD environments.
