# Relazione Tecnica - Before Deorbiting

## Indice

- ### [# 1 - Introduzione](#1---introduzione)

  - #### [# 1.1 - Partecipanti al Progetto](#11---partecipanti-al-progetto)
  - #### [# 1.2 - Descrizione dell'Avventura](#12---descrizione-dellavventura)
    
- ###  [# 2 - Progettazione](#2---progettazione)

  - #### [# 2.1 - Diagramma delle classi](#21---diagramma-delle-classi)
  - #### [# 2.2 - Specifica algebrica](#22---specifica-algebrica)
  - #### [# 2.3 - Dettagli implementativi](#23---dettagli-implementativi)
    - #### [# 2.3.1 - OOP (Object-Oriented Programming)](#231---oop-object-oriented-programming)
    - #### [# 2.3.2 - File](#232---file)
    - #### [# 2.3.3 - Database (JDBC)](#233---database-jdbc)
    - #### [# 2.3.4 - Lambda Expression](#234---lambda-expression)
    - #### [# 2.3.5 - SWING (GUI)](#235---swing-gui)
    - #### [# 2.3.6 - Thread](#236---thread)
    - #### [# 2.3.7 - REST e Socket](#237---rest-e-socket)

## 1 - Introduzione

### 1.1 - Partecipanti al Progetto

Il team **SPR** è composto da:

- **Andrea Salonia ([AndreSal8021](https://github.com/AndreSal8021))**
- **Lorenzo Peluso ([LorenzoPeluso04](https://github.com/LorenzoPeluso04))**
- **Alessio Ronzullo ([AlqssioRonz](https://github.com/AlqssioRonz))**

---

### 1.2 - Descrizione dell'Avventura

### Introduzione generale

---

### Caratteristiche principali

---

### Trama del gioco

22 giugno 2030. Sei a bordo della Stazione Spaziale Internazionale, al termine di una lunga missione. È il tuo ultimo giorno nello spazio: presto la stazione verrà deorbitata e distrutta.

Ma qualcosa interrompe la routine. Un silenzio anomalo, assenze inspiegabili, segnali che non tornano.
La stazione, che fino a ieri era casa, ora sembra diversa. Più fredda. Più ostile.

Dovrai esplorare, raccogliere indizi, interagire con strumenti e terminali, e affrontare l’ignoto.
Qualcosa sta accadendo. E il tempo a disposizione sta finendo.

Riuscirai a capire cosa sta succedendo… prima del deorbitaggio?

#### [Ritorna all'Indice](#indice)

## 2 - Progettazione

Il progetto sarà suddiviso nei seguenti package:

- Package per il main:
  - Engine: contiene tutte  del gioco assieme alla procedura main da eseguire
- Package per la struttura del Parser utile allo sviluppo del gioco:
  - Parser: interpreta i comandi dell’utente e li converte in istruzioni comprensibili per il gioco
  - ParserOutput: contiene il risultato dell’analisi di un comando, con azione e oggetti coinvolti
- Package con l'inizializzazione del gioco e le implementazioni degli observer:
  - BeforeDeorbiting: possiede tutta l'inizializzazione del gioco
  - inserire tutti gli observer
- Package per i type con:
  -  Setter
  -  Getter
  -  Metodi base
- Package per le componenti grafiche
- Package per le componenti della base di dati
- Package contenente le applicazioni RESTful

---

### 2.1 - Diagramma delle classi

---

### 2.2 - Specifica algebrica

---

### 2.3 - Dettagli implementativi

In questa sezione analizziamo come i diversi concetti trattati nel corso sono stati applicati nel progetto, illustrando le relative implementazioni e funzionalità nel codice. Verranno mostrati anche alcuni frammenti di codice tratti dal gioco, utilizzati a scopo esplicativo: si tenga presente che tali esempi sono stati semplificati per facilitare la comprensione e non rappresentano necessariamente il codice completo o definitivo.

---

### 2.3.1 - OOP (Object-Oriented Programming)

---

### 2.3.2 - File

---

### 2.3.3 - Database (JDBC)

---

### 2.3.4 - Lambda Expression

---

### 2.3.5 - SWING (GUI)

---

### 2.3.6 - Thread

---

### 2.3.7 - REST e Socket

#### [Ritorna all'Indice](#indice)
