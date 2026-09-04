# FastArchitecture Manifest Specification (`architecture.fca`)

`architecture.fca` is a declarative, ultra-compact architectural contract located at the root of a project.

---

## 1. Syntax & Token Dictionary

The format is plain UTF-8 text, line-oriented, whitespace-agnostic per token.

### Keywords
- `architecture <Name>`: Declares the architectural standard in use.
- `<Layer> owns <aspect1>,<aspect2>`: Assigns domain responsibilities to a layer.
- `<Layer> reads <TargetLayer>`: Permits read-only inspection of target state.
- `<Layer> writes <TargetLayer>`: Permits mutation of target state.
- `deny <SourceLayer> -> <TargetLayer>`: Explicitly forbids any outgoing dependency/import from `SourceLayer` to `TargetLayer`.
- `# <comment>`: Lines beginning with `#` are ignored as comments.

---

## 2. The Standard 30-Token Manifest

```text
architecture FastArchitecture

Model   owns   state,data
Control reads  Model
Control writes Model
View    reads  Model
View    owns   output

deny Model -> Control
deny Model -> View
deny View  -> Control
```

---

## 3. Semantics

### Layer Ownership
| Layer | Semantic Role |
|---|---|
| `Model` | Domain entities, state snapshots, records, immutable diffs. Has zero operational awareness of user interfaces or controllers. |
| `Control` | Event queues, input routers, logic loops, business services. Dispatches mutations to `Model`. |
| `View` | Display surfaces, render passes, widgets, shaders, output streams. Consumes snapshots from `Model` without side effects. |

### Rule Evaluation
1. Any source file mapped to a `SourceLayer` must not contain import statements, package references, or fully-qualified type usages belonging to any forbidden `TargetLayer`.
2. Implicit allowance: Any edge not forbidden by a `deny` rule is permissible (e.g., `Control -> Model`, `View -> Model`).

---

## 4. Error Cases & Parser Behavior

- **Unknown Layer**: Referencing an undefined layer name triggers a manifest validation error.
- **Circular Denials**: Multiple `deny` statements in opposite directions (`deny A -> B` and `deny B -> A`) enforce strict total separation.
- **Empty Manifest**: If `architecture.fca` is absent, the validator checks against the default standard `FastArchitecture`.
