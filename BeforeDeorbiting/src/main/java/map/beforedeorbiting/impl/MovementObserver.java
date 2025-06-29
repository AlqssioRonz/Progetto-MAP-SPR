package map.beforedeorbiting.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.type.Room;

/**
 * Questa classe rappresenta l'observer di comandi di movimento,
 * permette di muoversi nella stanza desiderata, se esiste.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * 
 * In questa classe utilizziamo un po' di programmazione funzionale per rendere 
 * migliore il metodo update, di seguito una breve spiegazione:
 * 
 *  - il costruttore di MovementObserver crea un dizionario con chiave un enumeratore e valore una function
 *  - la Function viene utilizzata nella progrmmazione funzionale e permette di "trattare una funzione come una variabile"
 *  - la Function ha <input, output> e con l'operatore :: possiamo ottenere metodi del parametro
 *  - tramite il metodo apply() possiamo eseguire il metodo contenuto nella Function
 *
 * @lorenzopeluso
 */
public class MovementObserver implements GameObserver {

    private final Map<CommandType, Function<Room, Room>> moves = new EnumMap<>(CommandType.class);

    public MovementObserver() {
        moves.put(CommandType.FORWARD, Room::getForward);
        moves.put(CommandType.AFT, Room::getAft);
        moves.put(CommandType.STARBORD, Room::getStarbord);
        moves.put(CommandType.PORT, Room::getPort);
        moves.put(CommandType.OVERHEAD, Room::getOverhead);
        moves.put(CommandType.DECK, Room::getDeck);
    }

    /**
     * 
     * @param game
     * @param parserOutput
     * @return 
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        
        StringBuilder movementMessage = new StringBuilder();
        
        if (moves.containsKey(parserOutput.getCommand().getType())){

            Function<Room, Room> nextRoomGetter = moves.get(parserOutput.getCommand().getType());
            
            if (nextRoomGetter != null) {
                Room current = game.getCurrentRoom();
                if (current != null) {
                    Room target = nextRoomGetter.apply(current);
                    if (target != null && target.isAccessible()) {
                        game.setCurrentRoom(target);
                        movementMessage.append(target.getGameStory())
                                .append(target.getDescription());
                    } else if (target != null && !target.isAccessible()) {
                        //aggiungere il controllo tentativo di EVA senza tuta indossata
                        movementMessage.append("Quel modulo sembra essere inaccessibile, forse potrei aprirlo in quealche modo...");
                    }
                }
            } else {
                movementMessage.append("Non c'è nulla da quella parte, solo spazio profondo a -270°C");
            }
        }
        return movementMessage.toString();
        
    }
}