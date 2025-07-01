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
    }

    @Override
    public void nextMove(ParserOutput p, PrintStream out) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getWelcomeMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
