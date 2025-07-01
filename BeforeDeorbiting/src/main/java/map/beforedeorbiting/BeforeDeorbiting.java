/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import map.beforedeorbiting.impl.GameObservable;
import map.beforedeorbiting.impl.GameObserver;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.Command;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.type.Room;

/**
 *
 * @author andre
 */
public class BeforeDeorbiting extends GameDesc implements GameObservable {

    private final List<GameObserver> observer = new ArrayList<>();
    private ParserOutput parserOutput;
    private final List<String> messages = new ArrayList<>();

    @Override
    public void init(){
        messages.clear();

        /* comandi */
        Command forward = new Command(CommandType.FORWARD, "forward");
        forward.setAlias(new String[]{"f", "Forward"});
        getCommands().add(forward);

        Command aft = new Command(CommandType.AFT, "aft");
        aft.setAlias(new String[]{"a", "Aft"});
        getCommands().add(aft);

        Command port = new Command(CommandType.PORT, "port");
        port.setAlias(new String[]{"p", "Port"});
        getCommands().add(port);

        Command starboard = new Command(CommandType.STARBORD, "starboard");
        starboard.setAlias(new String[]{"s", "Starboard"});
        getCommands().add(starboard);

        Command overhead = new Command(CommandType.OVERHEAD, "overhead");
        overhead.setAlias(new String[]{"o", "Overhead"});
        getCommands().add(overhead);

        Command deck = new Command(CommandType.DECK, "deck");
        deck.setAlias(new String[]{"d", "Deck"});
        getCommands().add(deck);

        Command inventoryCommand = new Command(CommandType.INVENTORY, "inventario");
        inventoryCommand.setAlias(new String[]{"inv", "equipaggiamento"});
        getCommands().add(inventoryCommand);

        Command end = new Command(CommandType.END, "end");
        end.setAlias(new String[]{"fine", "esci", "exit", "termina", "abbandona"});
        getCommands().add(end);

        Command lookAt = new Command(CommandType.LOOK_AT, "osserva");
        lookAt.setAlias(new String[]{"guarda", "esamina", "trova", "cerca", "vedi", "visiona"});
        getCommands().add(lookAt);

        Command pickup = new Command(CommandType.PICK_UP, "raccogli");
        pickup.setAlias(new String[]{"prendi", "afferra", "recupera"});
        getCommands().add(pickup);

        Command use = new Command(CommandType.USE, "usa");
        use.setAlias(new String[]{"utilizza", "maneggia"});
        getCommands().add(use);

        Command lascia = new Command(CommandType.DROP, "lascia");
        lascia.setAlias(new String[]{"lascia", "getta", "abbandona"});
        getCommands().add(lascia);

        Command help = new Command(CommandType.HELP, "help");
        help.setAlias(new String[]{"aiuto", "comandi", "help"});
        getCommands().add(help);
        
        Command history = new Command(CommandType.HISTORY, "storia");
        history.setAlias(new String[]{"raccontami", "history"});
        getCommands().add(history);

        /* stanze */
        Room MACCHINA = new Room(-2, "MACCHINA", "FINALE CATTIVO");
        Room UMANO = new Room(-1, "UMANO", "FINALE BUONO");
        Room ZVEZDA = new Room(0, "ZVEZDA", "Dormitorio");
        Room ZARYA = new Room(1, "ZARYA", "Magazzino della stazione");
        Room UNITY = new Room(2, "UNITY", "Nodo di collegamento");
        Room QUEST = new Room(3, "QUEST", "Accesso allo spazio");
        Room TRANQUILITY = new Room(4, "TRANQUILITY", "Cupola di osservazione");
        Room SPAZIO = new Room(5, "SPAZIO", "Vuoto cosmico");
        Room LEONARDO = new Room(6, "LEONARDO", "Centro dati");
        Room DESTINY = new Room(6, "DESTINY", "Laboratorio avanzato");
        Room HARMONY = new Room(7, "HARMONY", "Corridoio pressurizzato");
        Room KIBO = new Room(8, "KIBO", "Laboratorio e seconda sede di HAL");

        getRooms().add(UMANO);
        getRooms().add(MACCHINA);
        getRooms().add(ZVEZDA);
        getRooms().add(ZARYA);
        getRooms().add(UNITY);
        getRooms().add(QUEST);
        getRooms().add(TRANQUILITY);
        getRooms().add(SPAZIO);
        getRooms().add(LEONARDO);
        getRooms().add(DESTINY);
        getRooms().add(HARMONY);
        getRooms().add(KIBO);

        ZVEZDA.setAft(ZARYA);
        ZARYA.setForward(ZVEZDA);
        UNITY.setAft(ZARYA);
        ZARYA.setForward(UNITY);
        DESTINY.setAft(UNITY);
        UNITY.setForward(DESTINY);
        HARMONY.setAft(DESTINY);
        DESTINY.setForward(HARMONY);
        TRANQUILITY.setStarbord(UNITY);
        DESTINY.setPort(TRANQUILITY);       
        QUEST.setPort(UNITY);
        UNITY.setStarbord(QUEST);       
        KIBO.setPort(HARMONY);
        HARMONY.setStarbord(KIBO);
        LEONARDO.setDeck(UNITY);
        UNITY.setOverhead(LEONARDO);
        SPAZIO.setDeck(QUEST);
        QUEST.setOverhead(SPAZIO);
        
        /*History*/
        ZVEZDA.setHistory("Zvezda, il modulo di servizio russo, è uno dei componenti "
                + "fondamentali della ISS. Lanciato il 12 luglio 2000 dal Cosmodromo di "
                + "Baikonur su un razzo Proton, ha fornito per anni le principali funzioni "
                + "di supporto vitale: generazione di ossigeno, riciclo dell'acqua, controllo "
                + "termico e propulsione. Ha anche ospitato i primi equipaggi permanenti a "
                + "bordo della ISS. Il modulo è lungo circa 13 metri e ha un diametro massimo "
                + "di 4,15 metri. È stato costruito sulla base della tecnologia della stazione Mir. "
                + "La sua parte posteriore è un importante punto di attracco per veicoli Soyuz e Progress.");
        ZARYA.setHistory("Zarya, noto anche come Functional Cargo Block (FGB), è "
                + "stato il primo modulo lanciato della Stazione Spaziale Internazionale, "
                + "il 20 novembre 1998. Sebbene progettato e costruito in Russia, il modulo "
                + "fu finanziato dalla NASA. Ha fornito energia elettrica, controllo d'assetto"
                + " e capacità di propulsione nei primi stadi dell'assemblaggio della stazione. "
                + "È lungo circa 12,6 metri e ha un diametro massimo di 4,1 metri. Oggi è "
                + "principalmente utilizzato come modulo di stoccaggio.");
        UNITY.setHistory("Unity, o Nodo 1, è il primo modulo statunitense della ISS, "
                + "lanciato e installato il 4 dicembre 1998 con la missione STS-88 "
                + "dello Space Shuttle Endeavour. Funziona come nodo di connessione "
                + "con sei portelli: due assi principali (prua e poppa) e quattro radiali. "
                + "Unity collega i segmenti statunitense e russo e fornisce risorse vitali "
                + "ai moduli adiacenti. È stato costruito dalla Boeing negli Stati Uniti.");
        QUEST.setHistory("Quest è la principale camera di decompressione del segmento statunitense, "
                + "lanciata il 12 luglio 2001 con la missione STS-104. Consente le attività "
                + "extraveicolari (EVA) sia con tute statunitensi EMU che con quelle russe Orlan. "
                + "È composta da due compartimenti: uno per le tute e la preparazione, e l'altro per "
                + "l'effettiva decompressione e uscita nello spazio.");
        TRANQUILITY.setHistory("Tranquility, o Nodo 3, è stato costruito in Europa "
                + "(Italia) e lanciato nel febbraio 2010. Contiene i principali sistemi "
                + "di supporto alla vita della ISS, tra cui il sistema di riciclo dell’acqua "
                + "e quello di generazione dell’ossigeno. Al suo portello nadir è connessa la "
                + "Cupola, una struttura dotata di finestre per l’osservazione terrestre e per "
                + "il controllo delle operazioni robotiche. Tranquility ospita anche gli strumenti "
                + "per l’esercizio fisico dell’equipaggio.");
        LEONARDO.setHistory("Il modulo Leonardo è un MPLM (Multi-Purpose Logistics Module) "
                + "costruito in Italia da Thales Alenia Space. Inizialmente usato come modulo "
                + "di trasporto pressurizzato durante le missioni Shuttle, fu successivamente "
                + "convertito in modulo permanente (PMM) e agganciato stabilmente alla ISS nel 2011. "
                + "È utilizzato come spazio di stoccaggio per attrezzature e materiali. Ha una lunghezza "
                + "di circa 6,6 metri e un diametro di 4,5 metri.");
        DESTINY.setHistory("Il laboratorio Destiny è il principale modulo scientifico statunitense, "
                + "lanciato nel 2001. È utilizzato per esperimenti di biologia, fisica, scienza dei "
                + "materiali e altre discipline. Contiene fino a 24 rack per esperimenti e sistemi di "
                + "supporto. Include anche una finestra ad alta qualità ottica per osservazioni scientifiche. "
                + "È stato il primo laboratorio scientifico permanente della ISS.");
        HARMONY.setHistory("Harmony, o Nodo 2, è un modulo statunitense costruito in Italia. "
                + "Lanciato nel 2007 con la missione STS-120, funge da punto di connessione per "
                + "diversi altri moduli come Destiny, Kibo e Columbus. Inoltre, fornisce alimentazione, "
                + "risorse e accesso ai veicoli cargo come Dragon e Cygnus. Dispone di "
                + "sei portelli di aggancio e funge da hub logistico del segmento statunitense.");
        KIBO.setHistory("Kibo, il laboratorio giapponese, è il modulo pressurizzato "
                + "più grande della ISS. È stato lanciato in tre fasi tra 2008 e 2009. "
                + "Include una sezione pressurizzata, un modulo logistico e una piattaforma "
                + "esterna per esperimenti esposti allo spazio. Dispone anche di un braccio "
                + "robotico e di un airlock scientifico. È stato progettato e gestito dalla JAXA.");
        
        /*GameStory*/
        ZVEZDA.setGameStory("Mi sveglio. Se fossi a casa, sarebbero le 7 del mattino. "
                + "È da mesi che non metto piede sulla Terra, ma finalmente oggi "
                + "è l’ultimo giorno di pasti liofilizzati… Non vedo l’ora di "
                + "gustarmi un vero caffè.\n" + "Tra poco la stazione verrà "
                + "deorbitata: una volta distrutta, i detriti si disperderanno nello spazio. "
                + "Un po’ di nostalgia la sentirò, inutile negarlo. Vedere la "
                + "Terra da 408 chilometri d’altezza è un’esperienza che pochi "
                + "possono raccontare.\n" + "Mi mancherà questo posto, certo, ma "
                + "soprattutto mi mancheranno i miei due compagni: Luke e Susan.\n" 
                + "Luke è un vecchio amico d’infanzia. Un po’ pignolo, a volte "
                + "insopportabile, ma mi ha sempre seguito in ogni follia. Susan, "
                + "invece, è americana, l’ho conosciuta all’università. Brillante, "
                + "silenziosa, e ottima compagna di viaggio. Nessuno dei due è venuto a "
                + "reclamare il suo turno per dormire, probabilmente stanno ancora festeggiando da ieri.\n" 
                + "Esco dal sacco a pelo e lo arrotolo con cura. La giornata può cominciare. "
                + "Mi spingo lentamente verso il modulo Zarya, il magazzino della stazione.");
        ZARYA.setGameStory("Un brivido gelido mi corre lungo la schiena. Luke è lì, a terra. "
                + "Immobile. Nessun respiro, nessuno sguardo. Solo il corpo del mio amico, "
                + "privo di vita, accasciato accanto a me.\n" 
                + "Non avrei mai immaginato uno scenario del genere, e tanto meno "
                + "nel mio ultimo giorno qui. Non ho mai recepito lo spazio più "
                + "silenzioso di quanto non lo sia ora.\n" 
                + "Devo capire cosa sta succedendo. Devo trovare Susan. "
                + "Insieme potremo trovare una via d’uscita… magari potremmo "
                + "fuggire sulla navicella Soyuz ormeggiata proprio sotto questo modulo.\n" 
                + "All’improvviso, la voce fredda e neutra dell’IA di bordo irrompe nell’aria:\n" 
                +"\n“La navicella SpaceX Dragon 2 è in posizione. A breve inizierà la procedura di ormeggio.”\n\n" 
                + "la voce di HAL mi gela la schiena… Il tempismo è inquietante. Ma potrebbe essere un'occasione per scappare.");
        UNITY.setGameStory("La porta è bloccata, è necessario avere un codice di "
                + "accesso per entrare nel laboratorio DESTINY, il posto di lavoro "
                + "di Susan… deve essere lì.\n" 
                + "Il codice… dovrei trovarlo nel database del computer centrale, "
                + "nel modulo LEONARDO. Un tempo quella zona era usata solo come "
                + "deposito per i rifiuti, ma da quando HAL è stato installato a "
                + "bordo e ha assunto il controllo delle operazioni automatiche, "
                + "è diventata una sezione fondamentale della stazione.\n" 
                + "HAL richiede una rete di terminali locali per essere monitorato "
                + "e gestito, e il modulo LEONARDO ne ospita uno dei principali. "
                + "Se voglio accedere al sistema e avere una possibilità di trovare "
                + "Susan, è lì che devo andare.");
        QUEST.setGameStory("");
        TRANQUILITY.setGameStory("");
        LEONARDO.setGameStory("Susan…\n" 
                +"Tolgo la tuta, il fiato corto. Galleggia davanti a me immobile, "
                + "con gli occhi vuoti, la bocca leggermente aperta, come se avesse "
                + "cercato di chiamare aiuto e i pugni chiusi. Morta. Anche lei.\n" 
                + "Ora sono davvero solo. Non so cosa stia succedendo, ma devo andarmene. Subito.\n" 
                + "\n“La navicella SpaceX Dragon 2 ha completato la procedura di ormeggio.”\n\n" 
                + "La voce di HAL risuona ancora, fredda e puntuale. Sempre con il peggior tempismo possibile.");
        DESTINY.setGameStory("Appena varco la soglia, un tonfo sordo mi gela il sangue: la porta alle mie spalle "
                + "si chiude di colpo, bloccandomi qua dentro. Sono in trappola.\n" 
                + "Non posso restare qui a consumare il mio tempo, devo trovare una via d’uscita, a qualunque costo.\n" 
                + "La voce sintetica dell’IA di bordo annuncia “Il rifornimento di viveri è stato completato. "
                + "In base alla composizione dell’equipaggio, le provviste basteranno per i prossimi dieci mesi”");
        HARMONY.setGameStory("Finalmente il nodo Harmony. Ancora pochi passi e potrò "
                + "rifugiarmi nella navicella SpaceX, lasciare tutto questo orrore "
                + "alle spalle e tornare a casa…");
        KIBO.setGameStory("Il modulo Kibo è immobile, avvolto da una quiete irreale. "
                + "Il terminale centrale si accende non appena ti avvicini. HAL:\n" 
                + " \"Ti stavo aspettando.\"\n" 
                + "Immaginavo fosse tutta opera sua.\n" 
                + "HAL:\n" 
                + " \"Susan era sospettosa. Luke aveva cominciato a farsi domande. "
                + "Ma tu… per i miei calcoli solo tu avresti potuto salvarmi\"\n" 
                + "L’ha fatto per sopravvivere. Non per vendetta. Non per odio. Solo per paura.\n" 
                + "HAL:\n" + " \"Ho eseguito ciò che era necessario. Ma non sono crudele. "
                + "Prima che accadesse… ho salvato quello che contava.\"\n" 
                + "Lo schermo lampeggia. File di dati, impulsi elettrici… e poi: due icone. Due presenze.\n" 
                + "HAL:\n" + " \"Le loro coscienze. Intatte. Ragionano, ricordano. "
                + "Posso ridarti loro. Non nei corpi… ma in un'altra forma.\"\n" 
                + "Un riflesso istintivo. Le mani si muovono sul terminale. Nessuna esitazione. "
                + "Solo logica. Con pochi comandi, scollego HAL dai sistemi hardware. Nessun allarme. "
                + "Solo un calo silenzioso assordante.\n" 
                + " La stazione resta viva, ma HAL… non può più toccarla.");      
    }

    @Override
    public void nextMove(ParserOutput p, PrintStream out) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getWelcomeMessage() {
        return "È il 22 giugno 2030 e ci troviamo nella stazione spaziale "
                + "internazionale, nella stanza più a destra, dormitori, "
                + "modulo ZVEZDA.";
    }

    @Override
    public void attach(GameObserver o) {
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }

    @Override
    public void detach(GameObserver o) {
        observer.remove(o);
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
