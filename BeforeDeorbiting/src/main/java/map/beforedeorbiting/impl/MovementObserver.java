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
 * Observer per i comandi di movimento nella mappa. Permette al giocatore di
 * spostarsi da una stanza all'altra, utilizzando programmazione funzionale per
 * mappare ogni {@link CommandType} a una funzione che restituisce la
 * {@link Room} di destinazione.
 * <p>
 * Nel costruttore viene popolato un
 * {@code EnumMap<CommandType, Function<Room,Room>>} in cui la chiave è il tipo
 * di comando di movimento e il valore è il metodo di {@link Room}
 * corrispondente (es. {@link Room#getForward}). Tramite il metodo
 * {@code apply()} della {@link java.util.function.Function} viene poi
 * recuperata la stanza di destinazione.
 *
 * @author lorenz
 */
public class MovementObserver implements GameObserver, Serializable {

    /**
     * Mappa che associa ogni comando di movimento a una funzione che, data la
     * stanza corrente, restituisce la stanza di destinazione.
     */
    private final Map<CommandType, Function<Room, Room>> moves = new EnumMap<>(CommandType.class);

    /**
     * Costruisce un MovementObserver inizializzando la mappa dei comandi di
     * movimento.
     * <p>
     * Ogni {@link CommandType} di movimento viene associato a una
     * {@link Function} che, data la stanza corrente, restituisce la stanza di
     * destinazione:
     * <ul>
     * <li>{@link CommandType#FORWARD} → {@link Room#getForward()}</li>
     * <li>{@link CommandType#AFT} → {@link Room#getAft()}</li>
     * <li>{@link CommandType#STARBOARD}→ {@link Room#getStarboard()}</li>
     * <li>{@link CommandType#PORT} → {@link Room#getPort()}</li>
     * <li>{@link CommandType#OVERHEAD} → {@link Room#getOverhead()}</li>
     * <li>{@link CommandType#DECK} → {@link Room#getDeck()}</li>
     * </ul>
     */
    public MovementObserver() {
        moves.put(CommandType.FORWARD, Room::getForward);
        moves.put(CommandType.AFT, Room::getAft);
        moves.put(CommandType.STARBOARD, Room::getStarboard);
        moves.put(CommandType.PORT, Room::getPort);
        moves.put(CommandType.OVERHEAD, Room::getOverhead);
        moves.put(CommandType.DECK, Room::getDeck);
    }

    /**
     * Esegue lo spostamento del giocatore in base al comando ricevuto. Verifica
     * che il comando sia uno di quelli di movimento, ottiene la stanza di
     * destinazione e, se accessibile, aggiorna lo stato del gioco e restituisce
     * il messaggio appropriato.
     *
     * @param game lo stato di gioco corrente
     * @param parserOutput output del parser contenente il comando di movimento
     * @return il messaggio di risultato dello spostamento, oppure un messaggio
     * di errore o di inaccessibilità
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder movementMessage = new StringBuilder();

        if (moves.containsKey(parserOutput.getCommand().getType())) {
            Function<Room, Room> nextRoomGetter = moves.get(parserOutput.getCommand().getType());
            Room current = game.getCurrentRoom();

            if (current != null && nextRoomGetter.apply(current) != null) {
                Room target = nextRoomGetter.apply(current);

                if (target.isAccessible()) {
                    // Imposta eventuali flag musicali o di visita
                    if (parserOutput.getCommand().getType() == CommandType.OVERHEAD
                            && "LEONARDO".equals(current.getName())) {
                        game.setLeonardoMusicPlayed(true);
                    }
                    if (parserOutput.getCommand().getType() == CommandType.AFT
                            && "ZARYA".equals(current.getName())) {
                        game.setFirstMusicPlayed(true);
                    }
                    if (parserOutput.getCommand().getType() == CommandType.PORT
                            && "HARMONY".equals(current.getName())) {
                        game.setKiboVisited(true);
                    }

                    // Esegue lo spostamento e costruisce il messaggio
                    game.setCurrentRoom(target);
                    if (!"ZVEZDA".equals(target.getName())) {
                        movementMessage.append(target.getGameStory());
                    }
                    if ("TRANQUILITY".equals(target.getName())) {
                        target.setRoomImage("src/main/resources/img/cupola.png");
                    }
                    movementMessage.append(target.getName()).append("\n")
                            .append(target.getDescription())
                            .append("\n");

                } else {
                    // Stanza esistente ma non accessibile
                    if ("SPAZIO".equals(target.getName()) && !game.getObjectByID(10).isInUse()) {
                        movementMessage.append(
                                "Fare una camminata nello spazio senza indossare \n"
                                + "una tuta spaziale sarebbe un suicidio.");
                    } else {
                        movementMessage.append(
                                "Quel modulo sembra essere inaccessibile,\n"
                                + "forse potrei aprirlo in qualche modo...");
                    }
                }

            } else {
                // Nessuna stanza in quella direzione
                movementMessage.append("Non c'è nulla da quella parte, dove pensi di andare? Contro il muro?");
            }
        }

        return movementMessage.toString();
    }
}
