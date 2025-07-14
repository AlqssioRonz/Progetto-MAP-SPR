/**
 * Questa classe rappresenta l'observer del comando 'OSSERVA',
 * permette di osservare l'ambiente circostante o l'oggetto desiderato.
 * Implementa l'interfaccia {@link GameObserver} e utilizza un servizio REST per
 * ottenere la posizione della ISS.
 *
 * @author
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.type.Room;
import map.beforedeorbiting.util.ISSPositionREST;

/**
 * Osservatore per il comando 'OSSERVA'. Genera descrizioni dettagliate in base
 * alla stanza corrente e allo stato degli oggetti presenti, incluse
 * informazioni sulla posizione attuale della ISS.
 */
public class LookAtObserver implements GameObserver, Serializable {

    /**
     * Mappa che associa ogni stanza a una funzione che restituisce la
     * descrizione dello stato specifico di quella stanza.
     */
    private final Map<Room, Function<GameDesc, String>> stateDescr = new HashMap<>();

    /**
     * Continente su cui si trova la ISS, ottenuto dal servizio REST.
     */
    private String continent;

    /**
     * Servizio REST per ottenere la posizione attuale della ISS.
     */
    private final ISSPositionREST issService;

    /**
     * Costruisce un nuovo {@code LookAtObserver} per il gioco. Inizializza le
     * descrizioni specifiche per ciascuna stanza.
     *
     * @param game descrizione del gioco contenente le stanze
     * @param issService servizio REST per ottenere la posizione della ISS
     */
    public LookAtObserver(GameDesc game, ISSPositionREST issService) {
        this.issService = issService;
        stateDescr.put(game.getRoomByName("ZVEZDA"), this::zvezdaDescr);
        stateDescr.put(game.getRoomByName("ZARYA"), this::zaryaDescr);
        stateDescr.put(game.getRoomByName("UNITY"), this::unityDescr);
        stateDescr.put(game.getRoomByName("DESTINY"), this::destinyDescr);
        stateDescr.put(game.getRoomByName("TRANQUILITY"), this::tranquilityDescr);
        stateDescr.put(game.getRoomByName("QUEST"), this::questDescr);
        stateDescr.put(game.getRoomByName("HARMONY"), this::harmonyDescr);
        stateDescr.put(game.getRoomByName("LEONARDO"), this::leonardoDescr);
        stateDescr.put(game.getRoomByName("KIBO"), this::kiboDescr);
    }

    /**
     * Aggiorna lo stato del gioco in risposta al comando 'OSSERVA'.
     *
     * @param game stato corrente del gioco
     * @param parserOutput output del parser contenente il comando e l'oggetto
     * @return stringa di risposta da mostrare al giocatore
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder lookAtMsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.LOOK_AT) {
            // Osservazione di oggetti nella stanza
            if (parserOutput.getObject() != null
                    && game.getCurrentRoom().getObjects().contains(parserOutput.getObject())) {
                if (parserOutput.getInvObject() != null) {
                    lookAtMsg.append(
                            "Provi a osservare due cose alla volta, ma il tuo cervello va in tilt."
                            + "Meglio uno alla volta, fidati. Intanto ti mostro solo il primo oggetto...");
                }
                lookAtMsg.append(parserOutput.getObject().getDescription());

                // Osservazione di oggetto in inventario
            } else if (parserOutput.getInvObject() != null) {
                lookAtMsg.append(parserOutput.getInvObject().getDescription());

                // Osservazione dell'ambiente
            } else {
                lookAtMsg.append(game.getCurrentRoom().getLook());
                if (!"SPAZIO".equals(game.getCurrentRoom().getName())) {
                    if ("DESTINY".equals(game.getCurrentRoom().getName()) || "LEONARDO".equals(game.getCurrentRoom().getName())) {
                        lookAtMsg.append(stateDescr.get(game.getCurrentRoom()).apply(game));
                    } else {
                        lookAtMsg.append("\n")
                                .append(stateDescr.get(game.getCurrentRoom()).apply(game));
                    }
                }
            }
        }
        return lookAtMsg.toString();
    }

    /**
     * Descrizione dello stato del modulo ZVEZDA.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Zvezda
     */
    public String zvezdaDescr(GameDesc game) {
        if (game.getCurrentRoom().getObject(0) != null) {
            return """
                    Tutto è in ordine, tranne per uno strano
                    dettaglio… sembra, un pezzo di modellino (modellinorusso)
                    che galleggia in aria?""";
        } else {
            return "Tutto è in ordine.";
        }
    }

    /**
     * Descrizione dello stato del modulo ZARYA. Varia in base alla presenza di
     * oggetti specifici.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Zarya
     */
    public String zaryaDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();
        boolean hasSuit = game.getCurrentRoom().getObjects().contains(game.getObjectByID(10));
        boolean hasNote = game.getCurrentRoom().getObjects().contains(game.getObjectByID(5));

        if (hasSuit) {
            msg.append("""
                    Luke è seduto contro il muro, immobile. La visiera riflette
                    la luce, ma non si muove.
                    Era il mio migliore amico, è probabilmente morto per mancanza
                    di ossigeno, come sarà mai potuto succedere… stringe un
                    bigliettino tra le sue mani.
                         """);
        } else {
            if (hasNote) {
                msg.append("""
                        Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta
                        mi rivoltava lo stomaco. Il suo bigliettino galleggia in aria.
                             """);
            } else {
                msg.append("""
                        Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta
                        mi rivoltava lo stomaco.
                             """);
            }
        }
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(6))) {
            msg.append(
                    "Nella stanza vedo anche un piccolo pezzo di vetro... a cosa potrà mai servire?");
        }

        return msg.toString();
    }

    /**
     * Descrizione dello stato del modulo UNITY. Include informazioni su porte e
     * oggetti fluttuanti.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Unity
     */
    public String unityDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();

        if (game.getRoomByName("DESTINY").isAccessible()) {
            msg.append("La porta davanti a me è aperta, posso procedere verso Destiny.\n");
        } else {
            msg.append("""
                    La porta davanti, quella che porta al laboratorio Destiny è
                    chiusa con tastierino numerico: serve un codice per aprirla.

                    """);
        }
        if (game.getRoomByName("LEONARDO").isAccessible()) {
            msg.append("""
                    La botola sotto i miei piedi che conduce nel modulo Leonardo
                    è aperta, ma non vorrei rivedere il cadavere di Susan.
                    """);
        } else {
            msg.append("La botola sotto i miei piedi che conduce nel modulo Leonardo è chiusa.\n");
        }
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(1))) {
            msg.append("Sul pavimento galleggia un pezzo del modellino (modellinoamericano).");
        }

        return msg.toString();
    }

    /**
     * Descrizione dello stato del laboratorio DESTINY. Varia a seconda
     * dell'accessibilità del modulo Harmony.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Destiny
     */
    public String destinyDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();
        boolean harmonyOpen = game.getRoomByName("HARMONY").isAccessible();
        boolean visible = game.getCurrentRoom().isVisible();

        if (!harmonyOpen) {
            if (visible) {
                msg.append("Adesso riesco a vedere qualcosa... ma non abbastanza per orientarmi.\n"
                        + "Riesco però a vedere il modulo di connessione, devo, in qualche modo, far riflettere la luce lì.\n");
                if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(6))) {
                    msg.append("Nella stanza vedo anche un piccolo pezzo di vetro... a cosa potrà mai servire?\n");
                }
            } else {
                msg.append("""
                        Cos’è stato… non vedo nulla se non per un flebile fascio di
                        luce riflesso dalla luna che proviene dalla finestra di
                        osservazione, ma non è sufficiente per orientarmi nella stanza.
                        In questo momento non posso fare nulla qui... dovrei ASPETTARE.
                        """);
            }
        } else {
            msg.append("Un laboratorio sporco e silenzioso. \n"
                    + "Monitor spenti, attrezzi fluttuanti, l’aria di qualcosa lasciato a metà.\n"
                    + "Dal pavimento, un oblò mostra la Terra.");
        }
        return msg.toString();
    }

    /**
     * Descrizione dello stato del modulo TRANQUILITY. Include aggiornamento
     * dell'immagine in base al continente.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Tranquility
     */
    public String tranquilityDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(2))) {
            msg.append("""
                    In mezzo alla calma, qualcosa di minuscolo fluttua davanti
                    all’oblò centrale: un pezzo rigido, rettangolare, troppo
                    ordinato per essere solo un detrito.
                    Potrebbe essere un altro pezzo del modellino (modellinodestro)""");
        }

        // Ottieni il continente corrente dalla ISS
        continent = issService.getContinentForCoordinates();
        if (continent != null) {
            switch (continent.toLowerCase()) {
                case "africa":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Africa.jpg");
                    break;
                case "europa":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Europa.png");
                    break;
                case "asia":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Asia.png");
                    break;
                case "nordamerica":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/NordAmerica.png");
                    break;
                case "sudamerica":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/SudAmerica.png");
                    break;
                case "oceania":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Oceania.png");
                    break;
                case "antartide":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Antartide.png");
                    break;
                case "oceano":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Oceano.png");
                    break;
            }
        }
        return msg.toString();
    }

    /**
     * Descrizione dello stato del modulo QUEST.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Quest
     */
    public String questDescr(GameDesc game) {
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(3))) {
            return """
                    Uno strano e piccolo oggetto fluttua davanti alla botola.
                    Potrebbe essere un altro pezzo del modellino (modellinosinistro)""";
        }
        return "";
    }

    /**
     * Descrizione dello stato del modulo HARMONY.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Harmony
     */
    public String harmonyDescr(GameDesc game) {
        return "Sembra che non ci sia nulla di che qui";
    }

    /**
     * Descrizione dello stato del modulo LEONARDO. Include informazioni sul
     * terminale HAL e sul diario di Susan.
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Leonardo
     */
    public String leonardoDescr(GameDesc game) {
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(4))) {
            return "C'è un diario vicino al corpo di Susan.";
        }
        return "Gestire questa situazione sta diventando impossibile.";
    }

    /**
     * Descrizione dello stato del modulo KIBO. (Ancora da implementare.)
     *
     * @param game stato corrente del gioco
     * @return descrizione specifica di Kibo
     */
    public String kiboDescr(GameDesc game) {
        return "";
    }
}
