# Decision Log

## ADR-01: Trennung von Produkt und Architektur

**Status:** Accepted

### Context

Es gab mehrere FastJava-Repos mit ähnlichen Themen und Ansätzen. Die Produktlogik, insbesondere Streaming, sollte sauber von der Architektur-/Entscheidungsdokumentation getrennt werden.

### Decision

- FastVideoStream bleibt das Produktrepo
- FastArchitecture dokumentiert Architekturprinzipien, Specs und Entscheidungen
- Das Architektur-Repo enthält keine laufende Produktlogik, sondern nur dokumentarische, strukturgebende Inhalte

### Consequences

- Klarere Verantwortlichkeiten
- Einfachere Nachvollziehbarkeit von Designentscheidungen
- Bessere Trennung zwischen Laufzeitcode und Architekturreflexion

## ADR-02: CLI als eigentliche Stream-Engine

**Status:** Accepted

### Context

Für Streaming war die Verwendung einer GUI als alleinige Oberfläche zu unflexibel. Es bestand Bedarf für CLI-basierte Ausführung, automatisierbare Nutzung und einfache Wiederverwendung.

### Decision

- Die eigentliche Streamlogik läuft über CLI
- Swing-/App-Schichten sind nur Steuerungs- und Bedienoberflächen

### Consequences

- Mehr Flexibilität für Automatisierung und Skripting
- Saubere Trennung zwischen Steuerung und Kernfunktion
- Eindeutiger Einstiegspunkt für spätere Erweiterungen

## ADR-03: Audio als erster Zusatzpfad

**Status:** Accepted

### Context

Streaming ohne Audio ist für viele Anwendungsfälle unzureichend. Mikrofon und Systemaudio sollen als Teil der Stream-Architektur unterstützt werden.

### Decision

- Audio-Input wird als integraler Bestandteil des Stream-Workflows unterstützt
- Mic und System-Audio werden in der CLI-Argumentation abgebildet

### Consequences

- Bessere Nutzbarkeit für Live- und Recording-Workflows
- Mehr Komplexität bei Aufbau und Fehlerbehandlung
- Zukunftsbedarf für robustere Audio-Reconnect-/QoS-Mechaniken

## ADR-04: Kamera als Auswahlmodus

**Status:** Accepted

### Context

Es ist notwendig, zwischen verschiedenen Quellmodi zu unterscheiden: nur Screen, Screen+Camera und nur Camera.

### Decision

- Die Stream-Architektur kennt explizite Source-Modi
- Kamera kann als Overlay- oder eigenständiger Input konfiguriert werden
- Camera-Selection ist über Indizes bzw. Koordinaten konfigurierbar

### Consequences

- Höhere Flexibilität
- Einfachere Bedienung für verschiedene Streaming-Szenarien
- Mehr Komplexität bei Argument-Parsing und UI-Validierung

## ADR-05: Self-Exclusion für Capture-Fenster

**Status:** Accepted

### Context

Wenn die Steuerungs-App selbst mitgescreent wird, entsteht ein Rückkopplungseffekt.

### Decision

- Die GUI- oder Steuerungsanwendung wird bei der Capture-Logik ausdrücklich ausgeschlossen

### Consequences

- Bessere Capture-Qualität
- Reduktion von Schleifen-/Echo-Effekten
- Abhängigkeit von Window-Exclusion-Mechanismen der zugrundeliegenden Capture-Stack-Technologie
