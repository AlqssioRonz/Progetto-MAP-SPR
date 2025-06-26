# Organizzazione del lavoro - Before Deorbiting

## Indice

- ### [# 1 - Introduzione](#1---introduzione)

  - #### [# 1.1 - Partecipanti al Progetto](#11---partecipanti-al-progetto)
  - #### [# 1.2 - Descrizione dell'avventura](#12---descrizione-dellavventura)
    
- ###  [# 2 - Progettazione](#2---progettazione)

## 1 - Introduzione

### 1.1 - Partecipanti al Progetto

Il team **SPR** è composto da:

- **Andrea Salonia ([AndreSal8021](https://github.com/AndreSal8021))**
- **Lorenzo Peluso ([LorenzoPeluso04](https://github.com/LorenzoPeluso04))**
- **Alessio Ronzullo ([AlqssioRonz](https://github.com/AlqssioRonz))**

### 1.2 - Descrizione dell'avventura

### Introduzione generale

### Caratteristiche principali

### Trama del gioco

22 giugno 2030. Sei a bordo della Stazione Spaziale Internazionale, al termine di una lunga missione. È il tuo ultimo giorno nello spazio: presto la stazione verrà deorbitata e distrutta.

Ma qualcosa interrompe la routine. Un silenzio anomalo, assenze inspiegabili, segnali che non tornano.
La stazione, che fino a ieri era casa, ora sembra diversa. Più fredda. Più ostile.

Dovrai esplorare, raccogliere indizi, interagire con strumenti e terminali, e affrontare l’ignoto.
Qualcosa sta accadendo. E il tempo a disposizione sta finendo.

Riuscirai a capire cosa sta succedendo… prima del deorbitaggio?

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
