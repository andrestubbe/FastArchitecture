# AGENTS.md

Instructions and architectural boundaries for AI coding agents working on FastArchitecture.

## Commands
- **Build**: `mvn clean package`
- **Verify Architecture**: `java -jar target/FastArchitecture-0.1.0.jar check .`
- **Test Example**: `java -jar target/FastArchitecture-0.1.0.jar check examples/ScreenZoomDemo`

## Architecture Manifest (30 Tokens)
All code contributions must strictly conform to `architecture.fca`:
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

## Constraints and Code Style
- **Zero Dependencies**: Do not add external runtime libraries to `pom.xml`.
- **Pure Java 17+**: Use modern standard library features (records, pattern matching).
- **Anti-Over-Engineering (Ponytail / YAGNI)**: Never introduce abstractions or factories for single-use logic; write the minimum code that works safely.