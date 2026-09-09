# FastArchitecture Philosophy

## Kernidee

FastArchitecture ist kein zweites Produkt-Repository, sondern eine Leitlinie für wie die FastJava-Workflows organisiert, entworfen und erweitert werden sollten.

## Grundsätze

### 1. Produkt vor Architektur

Der Code, der tatsächlich läuft, gehört in die jeweiligen Produkt-Repos wie FastVideoStream, FastScreen, FastCamera oder FastAudioCapture.

### 2. Architektur dokumentiert, statt zu konkurrieren

FastArchitecture soll Entscheidungen, Ziele, Grenzen und Design-Muster festhalten, nicht die laufende Implementierung ersetzen.

### 3. Trennung von Verantwortung

- Capture- und Stream-Logik: Produkt-Repo
- Architektur, Specs und Entscheidungen: FastArchitecture
- Zukunfts- und Forschungsarbeit: TODO.md

### 4. CLI als Kern, UI als Steuerung

Die eigentliche Funktionalität soll als CLI- oder Engine-Schicht nutzbar sein. GUI-Applikationen sind nur Bedienoberflächen.

### 5. Performance ist eine Architekturfrage

Zwischen reiner Funktionalität und hoher Performance gibt es einen klaren Unterschied. Optimierungen wie Zero-Copy, GPU-Integration oder NVENC-Pfade sollten als architektonische Entscheidungen geführt werden.

### 6. Dokumentation ist Teil der Qualität

Jede wichtige Entscheidung, jedes Modul und jede Richtlinie sollten nachvollziehbar dokumentiert sein, damit zukünftige Erweiterungen nicht im Dunkeln erfolgen.

## Wie man FastArchitecture nutzt

- Wenn etwas Produktfunktionalität ist, liegt es im passenden Fast*-Repo.
- Wenn etwas Design, Zweck, Ziel, Abgrenzung oder langfristige Richtung ist, gehört es nach FastArchitecture.
- Wenn etwas noch explorativ, intern und noch nicht verbindlich ist, gehört es in TODO.md.

## Zielbild

FastArchitecture soll dazu dienen, dass die FastJava-Stack-Repos nicht nur funktionieren, sondern auch eine klare innere Logik haben: sauber getrennte Verantwortlichkeiten, nachvollziehbare Entscheidungen und eine sichtbare Roadmap für das nächste Level.
