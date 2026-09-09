# FastArchitecture

## Ziel

FastArchitecture ist die Architektur- und Spec-Schicht für die FastJava-Ökosysteme rund um Streaming, Capture und verwandte Audio-/Video-Workflows.

## Grundprinzipien

- Trennung zwischen Produkt-Repos und Architektur-/Spec-Repos
- Public Repos enthalten lauffähige, nutzbare Software
- FastArchitecture dokumentiert Designentscheidungen, Ziele und Vorgaben
- Alle wesentlichen Architekturentscheidungen werden in Dokumenten statt nur in Diskussionen festgehalten

## Repo-Logik

### FastVideoStream

FastVideoStream ist das produktive, nutzbare Repo für Streaming-Funktionalität.

- Zweck: Screen-, Camera- und Screen+Camera-Streaming
- Interface: CLI und Swing-App
- Output: vor allem produktive Software für Endanwender und Entwickler

### FastArchitecture

FastArchitecture dient als strukturierte Dokumentations- und Entscheidungsbasis.

- Zweck: Architektur, Ziele, Entwurfsentscheidungen und Roadmap
- Inhalt: Specs, ADRs/DECISIONS, Architekturüberblick, offene Punkte
- Ziel: Wiederverwendbarkeit und nachvollziehbare Entscheidungslogik über mehrere Repos hinweg

## Kernprinzipien des FastJava-Stacks

- Capture- und Stream-Logik soll von UI- und Steuerungslogik getrennt bleiben
- CLI soll die eigentliche Stream-Engine sein
- Swing- oder GUI-Schichten sollen nur als Steuerungsoberfläche dienen
- Audio, Kamera, Screen und Exclusions müssen bewusst modelliert werden
- Performance-Optimierungen sollen als technische Architekturziele dokumentiert werden

## Aktuelle Erkenntnisse

- FastVideoStream ist bereits die produktive Implementation
- FastArchitecture soll nicht als zweites Produktrepo fungieren, sondern als Architektur- und Specs-Sammlung
- Zukünftige Optimierungen wie Null-Copy-/GPU-/NVENC-Pfade sollten explizit als Architekturziele geführt werden

## Offene Architekturfragen

- Ist FastArchitecture ein öffentliches Repo oder nur eine interne Struktur?
- Sollen specs/adr in einem eigenen Repo liegen oder im jeweiligen Product-Repo?
- Wie eng sollen FastVideoStream, FastScreen, FastCamera und FastAudioCapture architektonisch gekoppelt werden?

## Empfehlung

Die sauberste erste Ausrichtung ist:

1. FastVideoStream bleibt das öffentliche Produktrepo
2. FastArchitecture dokumentiert Architektur, Spezifikationen und Entscheidungen
3. TODO.md bleibt der interne, nicht-öffentliche Plan für technische Zukunftsarbeit
