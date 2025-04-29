# LogicLab - Schach & Mastermind Plattform

Eine moderne Webanwendung zum Spielen von Schach und Mastermind, entwickelt mit Angular und Java (Open Liberty).

## Features

### Schach
- Vollständig implementierte Schachregeln
- Spielen gegen einen KI-Gegner
- Anzeige möglicher Züge
- Automatische Erkennung von Schachmatt und Patt
- Responsive Schachbrett-Design

### Mastermind
- Klassisches Mastermind-Spielerlebnis
- Farbkombinationen erraten
- Feedback-System für Spielzüge

## Technologie-Stack

### Frontend
- Angular 19.2
- TypeScript
- TailwindCSS für das UI
- Standalone Components

### Backend
- Java
- Open Liberty Server
- RESTful API
- Maven für Dependency Management

## Installation

### Voraussetzungen
- Node.js & npm
- Java JDK 17+
- Maven

### Frontend starten
```bash
cd logiclab-frontend
npm install
ng serve
```

### Backend starten
```bash
cd logiclab-backend
mvn clean package
mvn liberty:run
```
