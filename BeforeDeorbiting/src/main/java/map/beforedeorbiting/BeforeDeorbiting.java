/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import map.beforedeorbiting.impl.*;
import map.beforedeorbiting.type.*;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.util.ISSPositionREST;

/**
 * Classe principale del gioco BeforeDeorbiting.
 * <p>
 * Configura stanze, oggetti, comandi, observer e storia iniziale, e gestisce il
 * flusso di esecuzione dell’avventura.
 * </p>
 * <p>
 * Estende {@link GameDesc} per la descrizione del gioco e implementa
 * {@link GameObservable} per il pattern Observer.
 * </p>
 *
 * @author ronzu
 */
public class BeforeDeorbiting extends GameDesc implements GameObservable {

    /**
     * Lista degli {@link GameObserver} registrati. Vengono notificati ad ogni
     * nuova mossa di gioco.
     */
    private final List<GameObserver> observer = new ArrayList<>();

    /**
     * Ultimo output del parser, utilizzato per notificare gli observer.
     * <p>
     * Può essere {@code null} solamente alla primissima invocazione di
     * {@link #nextMove(ParserOutput, PrintStream)}.
     * </p>
     */
    private ParserOutput parserOutput;

    /**
     * Messaggi generati dagli observer durante la notifica. Vengono poi
     * stampati in {@link #nextMove(ParserOutput, PrintStream)}.
     */
    private final List<String> messages = new ArrayList<>();

    /**
     * Flag per gestire la prima invocazione di
     * {@link #nextMove(ParserOutput, PrintStream)}: evita di processare
     * immediatamente il primo turno di gioco.
     */
    private boolean first = true;

    /**
     * Servizio REST per ottenere la posizione corrente dell'ISS, utilizzato
     * dagli observer di tipo "Continent".
     */
    private final ISSPositionREST issRest = new ISSPositionREST();

    /**
     * Inizializza lo stato di gioco:
     * <ul>
     * <li>svuota l’inventario;</li>
     * <li>crea e registra tutti i comandi possibili;</li>
     * <li>crea e posiziona oggetti e stanze;</li>
     * <li>collega i neighbours tra stanze e registra gli observer;</li>
     * <li>imposta la stanza di partenza e lo stato iniziale degli oggetti.</li>
     * </ul>
     * <p>
     * Viene invocato da {@link GameDesc#init()} all’avvio del gioco.
     * </p>
     */
    @Override
    public void init() {
        Inventory.getInstance().getList().clear();
        Command forward = new Command(CommandType.FORWARD, "avanti");
        forward.setAlias(new String[]{"avanza", "forward", "x+", "nord"});
        getCommands().add(forward);
        Command aft = new Command(CommandType.AFT, "indietro");
        aft.setAlias(new String[]{"sud", "dietro", "retrocedi", "aft", "x-", "indietro"});
        getCommands().add(aft);
        Command starboard = new Command(CommandType.STARBOARD, "destra");
        starboard.setAlias(new String[]{"est", "starboard", "y+"});
        getCommands().add(starboard);
        Command port = new Command(CommandType.PORT, "sinistra");
        port.setAlias(new String[]{"port", "y-", "ovest"});
        getCommands().add(port);
        Command deck = new Command(CommandType.DECK, "giu");
        deck.setAlias(new String[]{"giù", "giu'", "deck", "z+"});
        getCommands().add(deck);
        Command overhead = new Command(CommandType.OVERHEAD, "su");
        overhead.setAlias(new String[]{"overhead", "z-"});
        getCommands().add(overhead);
        Command help = new Command(CommandType.HELP, "aiuto");
        help.setAlias(new String[]{"aiutami", "aiuti", "help"});
        getCommands().add(help);
        Command history = new Command(CommandType.HISTORY, "storia");
        history.setAlias(new String[]{"racconta", "raccontami", "history"});
        getCommands().add(history);
        Command inventoryCommand = new Command(CommandType.INVENTORY, "inventario");
        inventoryCommand.setAlias(new String[]{"inv", "borsa", "oggetti", "equipaggiamento"});
        getCommands().add(inventoryCommand);
        Command pickUp = new Command(CommandType.PICK_UP, "prendi");
        pickUp.setAlias(new String[]{"raccogli", "afferra", "recupera"});
        getCommands().add(pickUp);
        Command drop = new Command(CommandType.DROP, "lascia");
        drop.setAlias(new String[]{"rilascia", "abbandona", "getta"});
        getCommands().add(drop);
        Command use = new Command(CommandType.USE, "usa");
        use.setAlias(new String[]{"utilizza", "maneggia", "indossa", "leggi"});
        getCommands().add(use);
        Command lookAt = new Command(CommandType.LOOK_AT, "osserva");
        lookAt.setAlias(new String[]{"guarda", "vedi"});
        getCommands().add(lookAt);
        Command save = new Command(CommandType.SAVE, "salva");
        save.setAlias(new String[]{"salvataggio", "checkpoint", "save"});
        getCommands().add(save);
        Command wait = new Command(CommandType.WAIT, "aspetta");
        wait.setAlias(new String[]{"wait", "fermo", "tempo", "attendi"});
        getCommands().add(wait);
        Command rotate = new Command(CommandType.ROTATE, "ruota");
        rotate.setAlias(new String[]{"ruota", "gira", "sposta", "cambia", "angola"});
        getCommands().add(rotate);
        Command africa = new Command(CommandType.AFRICA, "africa");
        africa.setAlias(new String[]{"africa"});
        getCommands().add(africa);
        Command europa = new Command(CommandType.EUROPA, "europa");
        getCommands().add(europa);
        europa.setAlias(new String[]{"europa"});
        Command nordamerica = new Command(CommandType.NAMERICA, "namerica");
        getCommands().add(nordamerica);
        nordamerica.setAlias(new String[]{"nordamerica"});
        Command sudamerica = new Command(CommandType.SAMERICA, "samerica");
        getCommands().add(sudamerica);
        sudamerica.setAlias(new String[]{"sudamerica"});
        Command asia = new Command(CommandType.ASIA, "asia");
        getCommands().add(asia);
        asia.setAlias(new String[]{"asia"});
        Command oceania = new Command(CommandType.OCEANIA, "oceania");
        getCommands().add(oceania);
        oceania.setAlias(new String[]{"oceania"});
        Command antartide = new Command(CommandType.ANTARTIDE, "antartide");
        getCommands().add(antartide);
        antartide.setAlias(new String[]{"antartide"});
        Command oceano = new Command(CommandType.OCEANO, "oceano");
        getCommands().add(oceano);
        oceano.setAlias(new String[]{"oceano"});

        /* Lista di tutti gli oggetti del gioco */
        BDObject modellinoRusso = new BDObject(0, "Modellino russo",
                "Una parte del modellino, rappresenta il ramo russo.");
        modellinoRusso.setAlias(Set.of("modellinorusso", "modellino1", "russo", "modellinor"));
        modellinoRusso.setPickupable(true);
        modellinoRusso.setUsable(true);
        BDObject modellinoAmericano = new BDObject(1, "Modellino americano",
                "Una parte del modellino, rappresenta il modulo centrale della stazione.");
        modellinoAmericano.setAlias(Set.of("modellinoamericano", "modellino2", "americano", "modellinoa"));
        modellinoAmericano.setPickupable(true);
        modellinoAmericano.setUsable(true);
        BDObject modellinoDx = new BDObject(2, "Modellino pannelli solari Dx",
                "Una parte del modellino, rappresenta i pannelli solari dell'ala destra.");
        modellinoDx.setAlias(Set.of("modellinodestro", "modellino3", "destro", "modellinodx", "modellinod"));
        modellinoDx.setPickupable(true);
        modellinoDx.setUsable(true);
        BDObject modellinoSx = new BDObject(3, "Modellino pannelli solari Sx",
                "Una parte del modellino, rappresenta i pannelli solari dell'ala sinistra.");
        modellinoSx.setAlias(
                Set.of("modellinosinistro", "modellino4", "sinistro", "modellinosx", "modellinos"));
        modellinoSx.setPickupable(true);
        modellinoSx.setUsable(true);
        BDObject diarioSusan = new BDObject(4, "Diario Susan",
                "Il diario di Susan... contiene i suoi ultimi istanti.");
        diarioSusan.setAlias(Set.of("diario", "diariosusan", "susan"));
        diarioSusan.setPickupable(true);
        diarioSusan.setUsable(true);
        BDObject bigliettinoLuke = new BDObject(5, "Bigliettino Luke",
                "Un bigliettino di Luke... chissà cosa ci sarà scritto.");
        bigliettinoLuke.setAlias(Set.of("bigliettino", "biglietto", "bigliettinoluke", "bigliettoluke"));
        bigliettinoLuke.setPickupable(true);
        bigliettinoLuke.setUsable(true);
        BDObject pezzoDiVetro = new BDObject(6, "Pezzo di vetro",
                "Un comune pezzo di vetro. Magari in futuro potrebbe servirmi.");
        pezzoDiVetro.setAlias(
                Set.of("pezzo", "pezzo1", "pezzo2", "pezzo3", "pezzodivetro", "pezzovetro", "vetro"));
        pezzoDiVetro.setPickupable(true);
        pezzoDiVetro.setUsable(true);
        BDObject mezzoPrisma = new BDObject(7, "Metà prisma", "Creato dall'unione di due pezzi di vetro.");
        mezzoPrisma.setAlias(Set.of("metaprisma", "meta"));
        mezzoPrisma.setPickupable(true);
        mezzoPrisma.setUsable(true);
        BDObject prisma = new BDObject(8, "Prisma di vetro",
                "Creato dall'unione di tutti i pezzi di vetro. Riflette perfettamente la luce.");
        prisma.setAlias(Set.of("prisma", "prismacompleto", "prismavetro"));
        prisma.setPickupable(true);
        prisma.setUsable(true);
        BDObject computer = new BDObject(9, "Computer", "Permette di interagire col database della stazione.");
        computer.setAlias(Set.of("computer", "pc", "personalcomputer", "fisso", "portatile"));
        computer.setUsable(true);
        BDObject tutaSpaziale = new BDObject(10, "Tuta spaziale",
                "Utilizzata da Luke per navigare nello spazio.");
        tutaSpaziale.setAlias(Set.of("tuta", "tutaspaziale", "tutaluke", "tutaspazio"));
        tutaSpaziale.setPickupable(true);
        tutaSpaziale.setUsable(true);
        BDObject taccuino = new BDObject(11, "Taccuino", "Utile per segnarsi ogni dettaglio dell'avventura.");
        taccuino.setAlias(
                Set.of("taccuino", "blocknote", "blocknotes", "blocconote", "quaderno", "quadernino"));
        taccuino.setPickupable(true);
        taccuino.setUsable(true);
        // quando la prende per metterla deve usare "indossa" e dopo, quando torna,
        // verrà mostrato un output "ti sei tolto la tuta"
        BDObjectChest cassa = new BDObjectChest(12, "Cassa", "");
        cassa.setAlias(Set.of("chest", "cassa", "ripostiglio"));
        BDObject tastierinoDirezioni = new BDObject(13, "Tastierino",
                "Permette di inserire la password per entrare in Destiny.");
        tastierinoDirezioni.setAlias(Set.of("portellone", "codice", "tastierino", "tastiera"));
        tastierinoDirezioni.setUsable(true);
        BDObject tablet = new BDObject(14, "Tablet", "Permette di aprire la botola per l'accesso a Soyuz.");
        tablet.setAlias(Set.of("botola", "sportello ", "portellone", "tablet"));
        tablet.setUsable(true);
        // enigma canadarm - braccio robotico - matrice
        BDObject controlloRobot = new BDObject(15, "Controllo braccio robotico",
                "Permette di inserire le matrici di spostamendo per muovere il braccio meccanico.");
        controlloRobot.setAlias(Set.of("tastiera", "computer", "controllo", "braccio", "robot", "terminale"));
        controlloRobot.setUsable(true);

        getListObj().add(modellinoRusso);
        getListObj().add(modellinoAmericano);
        getListObj().add(modellinoDx);
        getListObj().add(modellinoSx);
        getListObj().add(diarioSusan);
        getListObj().add(bigliettinoLuke);
        getListObj().add(pezzoDiVetro);
        getListObj().add(mezzoPrisma);
        getListObj().add(prisma);
        getListObj().add(computer);
        getListObj().add(tutaSpaziale);
        getListObj().add(taccuino);
        getListObj().add(cassa);
        getListObj().add(tastierinoDirezioni);
        getListObj().add(tablet);
        getListObj().add(controlloRobot);

        /* Lista di tutte le stanze */
        Room macchina = new Room(-2, "MACCHINA", "Finale cattivo.");
        macchina.setAccessible(false);
        macchina.setRoomImage("src/main/resources/img/macchina.png");
        Room umano = new Room(-1, "UMANO", "Finale buono.");
        umano.setAccessible(false);
        umano.setRoomImage("src/main/resources/img/umano.png");
        Room zvezda = new Room(0, "ZVEZDA", "Dormitorio.");
        zvezda.setRoomImage("src/main/resources/img/zvezda.jpeg");
        zvezda.addObject(modellinoRusso);
        Room zarya = new Room(1, "ZARYA", "Magazzino della stazione.");
        zarya.setRoomImage("src/main/resources/img/zarya_chiusa.jpeg");
        zarya.addObject(pezzoDiVetro);
        zarya.addObject(tutaSpaziale);
        zarya.addObject(bigliettinoLuke);
        zarya.addObject(tablet);
        Room unity = new Room(2, "UNITY", "Nodo di collegamento.");
        unity.setRoomImage("src/main/resources/img/node1.jpeg");
        unity.addObject(modellinoAmericano);
        unity.addObject(cassa);
        unity.addObject(tastierinoDirezioni);
        cassa.add(pezzoDiVetro);
        Room quest = new Room(3, "QUEST", "Accesso allo spazio.");
        quest.setRoomImage("src/main/resources/img/airlock-quest.jpeg");
        quest.addObject(modellinoSx);
        Room tranquility = new Room(4, "TRANQUILITY", "Cupola di osservazione.");
        tranquility.setRoomImage("src/main/resources/img/cupola.png");
        tranquility.addObject(modellinoDx);
        Room spazio = new Room(5, "SPAZIO", "Vuoto cosmico.");
        spazio.setRoomImage("src/main/resources/img/SPAZIO1.jpg");
        spazio.setAccessible(false);
        spazio.addObject(tastierinoDirezioni);
        Room leonardo = new Room(6, "LEONARDO", "Centro dati.");
        leonardo.setRoomImage("src/main/resources/img/leonardo.jpeg");
        leonardo.addObject(diarioSusan);
        leonardo.addObject(computer);
        leonardo.setAccessible(false);
        Room destiny = new Room(7, "DESTINY", "Laboratorio avanzato.");
        destiny.setRoomImage("src/main/resources/img/destiny_buio.png");
        destiny.addObject(pezzoDiVetro);
        destiny.setVisible(false);
        destiny.setAccessible(false);
        Room harmony = new Room(8, "HARMONY", "Corridoio pressurizzato.");
        harmony.setRoomImage("src/main/resources/img/harmony.png");
        harmony.setAccessible(false);
        Room kibo = new Room(9, "KIBO", "Laboratorio e seconda sede di HAL.");
        kibo.setRoomImage("src/main/resources/img/kibo.jpeg");
        kibo.setAccessible(false);
        kibo.addObject(controlloRobot);
        Room scelta = new Room(11, "HARMONY ", "Corridoio pressurizzato.");
        scelta.setAccessible(true);
        scelta.setRoomImage("src/main/resources/img/scelta.png");

        getRooms().add(macchina);
        getRooms().add(umano);
        getRooms().add(zvezda);
        getRooms().add(zarya);
        getRooms().add(unity);
        getRooms().add(quest);
        getRooms().add(tranquility);
        getRooms().add(spazio);
        getRooms().add(leonardo);
        getRooms().add(destiny);
        getRooms().add(harmony);
        getRooms().add(kibo);
        getRooms().add(scelta);

        zvezda.setForward(zarya);
        zarya.setForward(unity);
        unity.setForward(destiny);
        destiny.setForward(harmony);
        zarya.setAft(zvezda);
        unity.setAft(zarya);
        destiny.setAft(unity);
        harmony.setAft(destiny);
        tranquility.setStarboard(unity);
        unity.setStarboard(quest);
        harmony.setPort(kibo);
        unity.setPort(tranquility);
        quest.setPort(unity);
        quest.setForward(spazio);
        kibo.setStarboard(harmony);
        unity.setDeck(leonardo);
        leonardo.setOverhead(unity);

        /* RoomLook */
        zvezda.setLook("""
                                Il nostro piccolo ma comodo dormitorio. Sacchi e attrezzature
                                legati alle pareti, un portello centrale aperto conduce al
                                modulo Zarya.""");

        zarya.setLook("""
                                Il modulo Zarya. Sacchi con tutte le attrezzature sono
                                legati alle pareti della stanza, un tablet a terra vicino
                                a una botola chiusa che porta alla navicella Soyuz.""");

        unity.setLook("""
                                Il nodo di collegamento Unity. A destra, un passaggio
                                stretto conduce verso il modulo QUEST e lo spazio esterno.
                                A sinistra, il collegamento verso il modulo TRANQUILITY, da lì
                                è visibile la Terra.
                                Nella stanza c'è una cassa, forse sarebbe utile controllare il suo contenuto.
                                """);

        destiny.setLook("");

        tranquility.setLook("""
                                La Terra riempie ogni finestra, immensa e silenziosa,
                                sospesa sotto di te.
                                Sembra così piccola vista dallo spazio.
                                """);

        quest.setLook("""
                                Il modulo QUEST. Attraverso quella botola si accede direttamente allo spazio.""");

        harmony.setLook("""
                                Il nodo Harmony è un corridoio pressurizzato, ampio e ordinato.
                                In fondo, il portellone che conduce alla navicella di fuga.
                                A sinistra si apre l’accesso al laboratorio Kibo.
                                A destra, l’ingresso è sigillato e inaccessibile.""");

        leonardo.setLook("""
                                Il modulo Leonardo, rivestito da contenitori imbottiti e
                                cavi. Sul lato sinistro, il terminale principale di HAL
                                emette un tenue bagliore verde.
                                La botola su si è aperta e porta direttamente a Unity.
                                Il corpo di Susan fluttua a mezz’aria, immobile.
                                """);
        // BUG: VIENE MOSTRATO DUE VOLTE!

        spazio.setLook(
                """
                                                Per sbloccare il portellone ti serve una sequenza di direzioni. Dovrei utilizzare il tastierino per inserirli.""");
        kibo.setLook(
                """
                                                Il modulo Kibo è carico di strumenti come sempre.
                                                Di fronte, il portello di comando del braccio robotico. A sinistra, il terminale di controllo del braccio meccanico. 
                                                Ora, tutto è immobile. Ma qualcosa nell’aria sembra ancora muoversi… devo scappare subito.""");

        // Inizializzare il formato base degli osserva - Lorenzo
        /* History */
        zvezda.setHistory("""
                                Zvezda, il modulo di servizio russo, è uno dei componenti
                                fondamentali della ISS. Lanciato il 12 luglio 2000
                                dal Cosmodromo di Baikonur su un razzo Proton, ha
                                fornito per anni le principali funzioni di supporto
                                vitale: generazione di ossigeno, riciclo dell'acqua,
                                controllo termico e propulsione. Ha anche ospitato i
                                primi equipaggi permanenti a bordo della ISS. Il modulo
                                è lungo circa 13 metri e ha un diametro massimo di
                                4,15 metri. È stato costruito sulla base della tecnologia
                                della stazione Mir. La sua parte posteriore è un importante
                                punto di attracco per veicoli Soyuz e Progress.
                                """);

        zarya.setHistory("""
                                Zarya, noto anche come Functional Cargo Block (FGB), è
                                stato il primo modulo lanciato della Stazione Spaziale Internazionale,
                                il 20 novembre 1998. Sebbene progettato e costruito in Russia,
                                il modulo fu finanziato dalla NASA. Ha fornito energia elettrica,
                                controllo d'assetto e capacità di propulsione nei primi stadi
                                dell'assemblaggio della stazione. È lungo circa 12,6 metri e ha
                                un diametro massimo di 4,1 metri. Oggi è principalmente utilizzato
                                come modulo di stoccaggio.
                                """);

        unity.setHistory("""
                                Unity, o Nodo 1, è il primo modulo statunitense della ISS,
                                lanciato e installato il 4 dicembre 1998 con la missione STS-88
                                dello Space Shuttle Endeavour. Funziona come nodo di connessione
                                con sei portelli: due assi principali (prua e poppa) e quattro radiali.
                                Unity collega i segmenti statunitense e russo e fornisce risorse vitali
                                ai moduli adiacenti. È stato costruito dalla Boeing negli Stati Uniti.
                                """);

        quest.setHistory("""
                                Quest è la principale camera di decompressione del segmento statunitense,
                                lanciata il 12 luglio 2001 con la missione STS-104. Consente le attività
                                extraveicolari (QUEST) sia con tute statunitensi EMU che con quelle russe Orlan.
                                È composta da due compartimenti: uno per le tute e la preparazione,
                                e l'altro per l'effettiva decompressione e uscita nello spazio.
                                """);

        tranquility.setHistory("""
                                Tranquility, o Nodo 3, è stato costruito in Europa (Italia)
                                e lanciato nel febbraio 2010. Contiene i principali sistemi
                                di supporto alla vita della ISS, tra cui il sistema di riciclo dell’acqua
                                e quello di generazione dell’ossigeno. Al suo portello nadir è connessa la
                                Cupola, una struttura dotata di finestre per l’osservazione terrestre e per
                                il controllo delle operazioni robotiche. Tranquility ospita anche gli strumenti
                                per l’esercizio fisico dell’equipaggio.
                                """);

        leonardo.setHistory("""
                                Il modulo Leonardo è un MPLM (Multi-Purpose Logistics Module)
                                costruito in Italia da Thales Alenia Space. Inizialmente usato come modulo
                                di trasporto pressurizzato durante le missioni Shuttle, fu successivamente
                                convertito in modulo permanente (PMM) e agganciato stabilmente alla ISS nel 2011.
                                È utilizzato come spazio di stoccaggio per attrezzature e materiali. Ha una lunghezza
                                di circa 6,6 metri e un diametro di 4,5 metri.
                                """);

        destiny.setHistory(
                """
                                                Il laboratorio Destiny è il principale modulo scientifico statunitense,
                                                lanciato nel 2001. È utilizzato per esperimenti di biologia, fisica, scienza dei
                                                materiali e altre discipline. Contiene fino a 24 rack per esperimenti e sistemi di
                                                supporto. Include anche una finestra ad alta qualità ottica per osservazioni scientifiche.
                                                È stato il primo laboratorio scientifico permanente della ISS.
                                                """);

        harmony.setHistory("""
                                Harmony, o Nodo 2, è un modulo statunitense costruito in Italia.
                                Lanciato nel 2007 con la missione STS-120, funge da punto di connessione per
                                diversi altri moduli come Destiny, Kibo e Columbus. Inoltre, fornisce alimentazione,
                                risorse e accesso ai veicoli cargo come Dragon e Cygnus. Dispone di
                                sei portelli di aggancio e funge da hub logistico del segmento statunitense.
                                """);

        kibo.setHistory("""
                                Kibo, il laboratorio giapponese, è il modulo pressurizzato più grande della ISS.
                                È stato lanciato in tre fasi tra 2008 e 2009. Include una sezione pressurizzata,
                                un modulo logistico e una piattaforma esterna per esperimenti esposti allo spazio.
                                Dispone anche di un braccio robotico e di un airlock scientifico. È stato progettato
                                e gestito dalla JAXA.
                                """);

        /* GameStory */
        zarya.setGameStory(
                """
                                                Un brivido gelido mi corre lungo la schiena. Luke è lì, a terra.
                                                Immobile. Nessun respiro, nessuno sguardo. Solo il corpo del mio amico,
                                                privo di vita, accasciato accanto a me.

                                                Non avrei mai immaginato uno scenario del genere, e tanto meno
                                                nel mio ultimo giorno qui. Non ho mai recepito lo spazio più
                                                silenzioso di quanto non lo sia ora.
                                                Devo capire cosa sta succedendo. Devo trovare Susan.
                                                Insieme potremmo trovare una via d’uscita… magari potremmo
                                                fuggire sulla navicella Soyuz ormeggiata proprio sotto questo modulo.

                                                All’improvviso, la voce fredda e neutra dell’IA di bordo irrompe nell’aria:

                                                “La navicella SpaceX Dragon 2 è in posizione. A breve inizierà la procedura di ormeggio.”

                                                La voce di HAL mi gela la schiena… Il tempismo è inquietante.
                                                Ma potrebbe essere un'occasione per scappare.
                                                """);

        unity.setGameStory("""
                                La porta davanti a me è bloccata, è necessario avere un codice di
                                accesso per entrare nel laboratorio DESTINY, il posto di lavoro
                                di Susan… deve essere lì.

                                Il codice… dovrei trovarlo nel database del computer centrale,
                                nel modulo LEONARDO.
                                Un tempo quella zona era usata solo come deposito per i rifiuti,
                                ma da quando HAL è stato installato a bordo e ha assunto il controllo
                                delle operazioni automatiche, è diventata una sezione fondamentale della stazione.

                                HAL richiede una rete di terminali locali per essere monitorato
                                e gestito, e il modulo LEONARDO ne ospita uno dei principali.
                                Se voglio accedere al sistema e avere una possibilità di trovare
                                Susan, è lì che devo andare.
                                """);

        quest.setGameStory("");
        tranquility.setGameStory("");

        leonardo.setGameStory("""
                                Susan…
                                Tolgo la tuta, il fiato corto. Galleggia davanti a me immobile,
                                con gli occhi vuoti, la bocca leggermente aperta, come se avesse
                                cercato di chiamare aiuto e i pugni chiusi. Morta. Anche lei.

                                Ora sono davvero solo. Non so cosa stia succedendo, ma devo andarmene.
                                Subito.

                                “La navicella SpaceX Dragon 2 ha completato la procedura di ormeggio.”

                                La voce di HAL risuona ancora, fredda e puntuale. Sempre con il peggior tempismo possibile.
                                """);

        destiny.setGameStory(
                """
                                                Appena varco la soglia, la luce si spegne di colpo, nemmeno l'oblò di questo modulo
                                                mi permette di vedere qualcosa, la stazione ora è dietro la Terra, è come se fosse in eclissi.

                                                La voce sintetica dell’IA di bordo annuncia:
                                                “Il rifornimento di viveri è stato completato.
                                                In base alla composizione dell’equipaggio, le provviste basteranno per i prossimi dieci mesi”
                                                """);

        harmony.setGameStory("""
                                Finalmente il nodo Harmony. Ancora pochi passi e potrò
                                rifugiarmi nella navicella SpaceX, lasciare tutto questo orrore
                                alle spalle e tornare a casa…
                                """);

        kibo.setGameStory("""
                          Il modulo Kibo è immobile, avvolto da una quiete irreale.
                          Il terminale centrale si accende non appena ti avvicini. 

                          HAL:
                          "Ti stavo aspettando."

                          Immaginavo fosse tutta opera sua.
                          HAL:
                           "Susan era sospettosa. Luke aveva cominciato a farsi domande.
                          Ma tu… per i miei calcoli solo tu avresti potuto salvarmi"

                          L’ha fatto per sopravvivere. Non per vendetta. Non per odio.
                          Solo per paura.
                          HAL:
                          "Ho eseguito ciò che era necessario. Ma non sono crudele.
                          Prima che accadesse… ho salvato quello che contava."

                          Lo schermo lampeggia. File di dati, impulsi elettrici… e poi: due icone. Due presenze.
                          HAL:
                          "Le loro coscienze. Intatte. Ragionano, ricordano.
                          Posso ridarti loro. Non nei corpi… ma in un'altra forma."

                          Un riflesso istintivo. Le mani si muovono sul terminale. Nessuna esitazione.
                          Solo logica. Con pochi comandi, scollego HAL dai sistemi hardware. Nessun allarme.
                          Solo un calo silenzioso assordante.

                          La stazione resta viva, ma HAL… non può più toccarla.
                                """);

        spazio.setGameStory(
                """
                                                Dopo la depressurizzazione esci nello spazio per una camminata spaziale nella tuta di Luke.
                                                La tuta non è in grandi condizioni ma può bastare.
                                                Usi i propulsori per raggiungere il punto di accesso esterno del modulo leonardo ma vedi un tastierino
                                                con delle direzioni.
                                                Serve per iniziare la sequenza di apertura e depressurizzazione. Non ho molto tempo prima che l'ossigeno finisca!
                                                """);

        /* Lista di tutti gli Observer */
        GameObserver dropObserver = new DropObserver();
        this.attach(dropObserver);
        GameObserver helpObserver = new HelpObserver();
        this.attach(helpObserver);
        GameObserver historyObserver = new HistoryObserver();
        this.attach(historyObserver);
        GameObserver inventoryObserver = new InventoryObserver();
        this.attach(inventoryObserver);
        GameObserver lookAtObserver = new LookAtObserver(this, issRest);
        this.attach(lookAtObserver);
        GameObserver movementObserver = new MovementObserver();
        this.attach(movementObserver);
        GameObserver pickUpObserver = new PickUpObserver();
        this.attach(pickUpObserver);
        GameObserver useObserver = new UseObserver(this);
        this.attach(useObserver);
        GameObserver saveObserver = new SaveObserver();
        this.attach(saveObserver);
        GameObserver waitObserver = new WaitObserver();
        this.attach(waitObserver);
        GameObserver rotateObserver = new RotateObserver();
        this.attach(rotateObserver);
        GameObserver continentObserver = new ContinentObserver();
        this.attach(continentObserver);
        //

        // Setta la stanza iniziale
        setCurrentRoom(zvezda);
        this.getInventory().add(taccuino);
        if (!cassa.getList().isEmpty()) {
            StringBuilder objectChest = new StringBuilder();
            for (BDObject obj : cassa.getList()) {
                objectChest.append(obj.getName()).append(" -> ").append(obj.getDescription())
                        .append("\n");
            }
            cassa.setDescription(
                    "Permette di contenere vari oggetti.\nAl momento ci trovi: \n"
                    + objectChest.toString());
        } else {
            cassa.setDescription("Permette di contenere vari oggetti.");
        }
        setNotebookText("");
    }

    /**
     * Esegue la mossa successiva nel gioco.
     * <p>
     * Viene invocato dal motore di gioco per processare l’output del parser e
     * notificare tutti gli observer registrati.
     * </p>
     *
     * @param p output del parser ({@code null} solo alla primissima chiamata)
     * @param out stream su cui stampare il risultato (es. {@code System.out} o
     * un {@code JTextPane})
     */
    @Override
    public void nextMove(ParserOutput p, PrintStream out) {
        parserOutput = p;
        messages.clear();
        if (first) {
            first = false;
            return;
        }

        if (p == null || p.getCommand() == null) {
            out.println("""
                                        Quello che dici non ha senso, persino
                                        HAL alzerebbe un sopracciglio... se ne avesse uno.""");
        } else {
            setLastCommand(p.getCommand());
            notifyObservers();
            if (!messages.isEmpty()) {
                for (String m : messages) {
                    if (m.length() > 0) {
                        out.println(m);
                    }
                }
            }
        }
    }

    /**
     * Restituisce il messaggio di benvenuto visualizzato all’avvio del gioco.
     *
     * @return stringa introduttiva con data e stanza iniziale
     */
    @Override
    public String getWelcomeMessage() {
        return "È il 22 giugno 2030 e ti trovi nella stazione spaziale internazionale, modulo "
                + getCurrentRoom().getName()
                + "\n";
    }

    /**
     * Registra un observer interessato agli eventi di gioco.
     * <p>
     * Se l'observer è già registrato, non viene aggiunto di nuovo.
     * </p>
     *
     * @param o l’{@link GameObserver} da aggiungere
     */
    @Override
    public void attach(GameObserver o) {
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }

    /**
     * Rimuove un observer precedentemente registrato.
     *
     * @param o l’{@link GameObserver} da rimuovere
     */
    @Override
    public void detach(GameObserver o) {
        observer.remove(o);
    }

    /**
     * Notifica tutti gli observer registrati.
     * <p>
     * Raccoglie i messaggi restituiti da ciascun observer e li memorizza in
     * {@link #messages}.
     * </p>
     */
    @Override
    public void notifyObservers() {
        for (GameObserver o : observer) {
            messages.add(o.update(this, parserOutput));
        }
    }

    /**
     * Ripristina lo stato del gioco dopo il caricamento da JSON:
     * <ul>
     * <li>ricalcola i collegamenti tra stanze;</li>
     * <li>ricrea e registra nuovamente tutti gli observer;</li>
     * <li>ricalcola il riferimento alla stanza corrente.</li>
     * </ul>
     * <p>
     * Deve essere invocato immediatamente dopo il caricamento dello stato.
     * </p>
     */
    public void restoreAfterLoad() {
        initRoomConnections();
        initObservers();
        // ricolleghiamo currentRoom all'istanza corretta nella lista rooms
        Room cr = getCurrentRoom();
        Room real = getRoomByName(cr.getName()); // oppure match su ID
        if (real != null) {
            setCurrentRoom(real);
        }
    }

    /**
     * Ricrea i collegamenti (neighbours) tra le stanze.
     * <p>
     * Utilizzato da {@link #restoreAfterLoad()} per ripristinare i campi
     * transient dopo il caricamento da JSON.
     * </p>
     */
    private void initRoomConnections() {
        Room macchina = getRoomByName("MACCHINA");
        Room umano = getRoomByName("UMANO");
        Room zvezda = getRoomByName("ZVEZDA");
        Room zarya = getRoomByName("ZARYA");
        Room unity = getRoomByName("UNITY");
        Room quest = getRoomByName("QUEST");
        Room tranquility = getRoomByName("TRANQUILITY");
        Room spazio = getRoomByName("SPAZIO");
        Room leonardo = getRoomByName("LEONARDO");
        Room destiny = getRoomByName("DESTINY");
        Room harmony = getRoomByName("HARMONY");
        Room kibo = getRoomByName("KIBO");
        Room scelta = getRoomByName("HARMONY ");

        if (zvezda != null) {
            zvezda.setForward(zarya);
        }
        if (zarya != null) {
            zarya.setForward(unity);
            zarya.setAft(zvezda);
        }
        if (unity != null) {
            unity.setForward(destiny);
            unity.setAft(zarya);
            unity.setStarboard(quest);
            unity.setPort(tranquility);
            unity.setDeck(leonardo);
        }
        if (destiny != null) {
            destiny.setForward(harmony);
            destiny.setAft(unity);
        }
        if (harmony != null) {
            harmony.setAft(destiny);
            harmony.setPort(kibo);
        }
        if (tranquility != null) {
            tranquility.setStarboard(unity);
        }
        if (quest != null) {
            quest.setPort(unity);
            quest.setDeck(spazio);
        }
        if (kibo != null) {
            kibo.setStarboard(harmony);
        }
        if (leonardo != null) {
            leonardo.setOverhead(unity);
        }
    }

    /**
     * Ricrea e registra tutti gli observer di gioco.
     * <p>
     * Utilizzato da {@link #restoreAfterLoad()} per ripopolare la lista dopo il
     * caricamento da JSON.
     * </p>
     */
    private void initObservers() {
        observer.clear();
        GameObserver dropObserver = new DropObserver();
        this.attach(dropObserver);
        GameObserver helpObserver = new HelpObserver();
        this.attach(helpObserver);
        GameObserver historyObserver = new HistoryObserver();
        this.attach(historyObserver);
        GameObserver inventoryObserver = new InventoryObserver();
        this.attach(inventoryObserver);
        GameObserver lookAtObserver = new LookAtObserver(this, issRest);
        this.attach(lookAtObserver);
        GameObserver movementObserver = new MovementObserver();
        this.attach(movementObserver);
        GameObserver pickUpObserver = new PickUpObserver();
        this.attach(pickUpObserver);
        GameObserver useObserver = new UseObserver(this);
        this.attach(useObserver);
        GameObserver saveObserver = new SaveObserver();
        this.attach(saveObserver);
        GameObserver waitObserver = new WaitObserver();
        this.attach(waitObserver);
        GameObserver rotateObserver = new RotateObserver();
        this.attach(rotateObserver);
        GameObserver continentObserver = new ContinentObserver();
        this.attach(continentObserver);
    }

}
