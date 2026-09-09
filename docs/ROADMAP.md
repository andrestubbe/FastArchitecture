# FastArchitecture Roadmap

## Phase 1: Struktur und Dokumentation

- [x] FastVideoStream als separates Produktrepo etablieren
- [x] FastArchitecture als Architektur- und Specs-Struktur vorbereiten
- [x] Grundlegende Dokumente anlegen
- [x] TODO.md als interne Zukunftsplanung ergänzen

## Phase 2: Stream-Kern weiterentwickeln

- [x] CLI-Modi erweitern: Screen, Screen+Camera, Camera
- [x] Audio-Unterstützung ergänzen
- [x] Camera-Auswahl und PiP-Placement einbauen
- [x] GUI-Steuerung und Window-Exclusion ergänzen

## Phase 3: Architekturvertiefung

- [ ] Specs/ADR-Struktur sauber durchziehen
- [ ] Entscheidungslogik über mehrere Repos hinweg konsolidieren
- [ ] Zuordnung zwischen FastVideoStream, FastScreen, FastCamera, FastAudioCapture klar dokumentieren
- [ ] Public/Private-Strategie für Architekturartefakte festlegen

## Phase 4: Performance- und Pipeline-Optimierung

- [ ] Zero-Copy-Ansätze identifizieren
- [ ] GPU-/NVENC-Strategien als Architekturziele modellieren
- [ ] Puffer-, Frame- und Encoding-Engines sauber trennen
- [ ] Bewertung von DXGI-, MediaFoundation- oder FFmpeg-basierten Pfaden dokumentieren

## Phase 5: Langfristige Ausrichtung

- [ ] Mehrfach-Output-/Muxing-Strategien evaluieren
- [ ] Robustere Audio-Handling-Mechaniken planen
- [ ] Streaming-Resilience, Fehlerbehandlung und Recovery-Dokumentation ergänzen
- [ ] Grundsatzentscheidung über verteilte Architektur- oder monolithische Stream-Engine treffen

## Hinweis

Die Roadmap dient als strukturierte Planung. Sie ist bewusst nicht mit produktiver Laufzeitlogik belastet und soll langfristig als Leitfaden für weitere FastJava-Repos dienen.
