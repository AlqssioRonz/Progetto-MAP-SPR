package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.type.Room;

/**
 * Questa classe rappresenta l'observer di comandi di movimento, permette di
 * muoversi nella stanza desiderata, se esiste. Per farlo, implmenta
 * l'interfaccia GameObserver.
 *
 * In questa classe utilizziamo un po' di programmazione funzionale per rendere
 * migliore il metodo update, di seguito una breve spiegazione:
 *
 * - il costruttore di MovementObserver crea un dizionario con chiave un
 * enumeratore e valore una function - la Function viene utilizzata nella
 * progrmmazione funzionale e permette di "trattare una funzione come una
 * variabile" - la Function ha <input, output> e con l'operatore :: possiamo
 * ottenere metodi del parametro - tramite il metodo apply() possiamo eseguire
 * il metodo contenuto nella Function
 *
 * @lorenzopeluso
 */
public class MovementObserver implements GameObserver, Serializable {

    private final Map<CommandType, Function<Room, Room>> moves = new EnumMap<>(CommandType.class);

    public MovementObserver() {
        moves.put(CommandType.FORWARD, Room::getForward);
        moves.put(CommandType.AFT, Room::getAft);
        moves.put(CommandType.STARBOARD, Room::getStarboard);
        moves.put(CommandType.PORT, Room::getPort);
        moves.put(CommandType.OVERHEAD, Room::getOverhead);
        moves.put(CommandType.DECK, Room::getDeck);
    }

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di movimento.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {

        StringBuilder movementMessage = new StringBuilder();

        if (moves.containsKey(parserOutput.getCommand().getType())) {

            Function<Room, Room> nextRoomGetter = moves.get(parserOutput.getCommand().getType());

            if (nextRoomGetter.apply(game.getCurrentRoom()) != null) {
                Room current = game.getCurrentRoom();
                if (current != null) {
                    Room target = nextRoomGetter.apply(current);
                    if (target != null && target.isAccessible()) {
                        game.setCurrentRoom(target);
                        if (!game.getCurrentRoom().getName().equals("ZVEZDA")) {
                            movementMessage.append(target.getGameStory());
                        }
                        movementMessage.append(target.getName()).append("\n")
                                .append(target.getDescription());
                    } else if (target != null && !target.isAccessible()) {
                        //id 10 tuta spaziale
                        if (target.equals(game.getRoomByName("SPAZIO"))
                                && !game.getObjectByID(10).isInUse()) {
                            game.setCurrentRoom(target);
                            movementMessage.append("""
                                                   Fare una camminata nello spazio senza indossare 
                                                   una tuta spaziale sarebbe un suicidio.""");
                        }
                        movementMessage.append("""
                                               Quel modulo sembra essere inaccessibile,
                                               forse potrei aprirlo in quealche modo...""");
                    }
                }
            } else {
                movementMessage.append("Non c'è nulla da quella parte, dove pensi di andare? Contro il muro?");
            }
        }
        return movementMessage.toString();

    }
}
