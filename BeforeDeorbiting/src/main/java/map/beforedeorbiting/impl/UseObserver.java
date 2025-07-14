/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.swing.SwingUtilities;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.ui.NotebookUI;
import map.beforedeorbiting.ui.HALterminal;
import map.beforedeorbiting.ui.InventoryUI;
import map.beforedeorbiting.ui.InsertPasswordUI;
import map.beforedeorbiting.ui.RobotArmPuzzleUI;

/**
 * Questa classe rappresenta l'observer del comando 'USE', permette al giocatore
 * di utilizzare gli oggetti in gioco e di visualizzare un messaggio di
 * risposta. Per farlo, implementa l'interfaccia GameObserver.
 *
 * @author ronzu
 */
public class UseObserver implements GameObserver, Serializable {

    /**
     * Mappa che associa ogni {@link BDObject} utilizzabile alla sua funzione di
     * utilizzo.
     */
    private final Map<BDObject, Function<GameDesc, String>> uses = new HashMap<>();

    /**
     * Password necessaria per sbloccare il modulo DESTINY.
     */
    private final String DESTINY_PASSWORD = "NOESCAPE";

    /**
     * Sequenza di movimenti richiesta per risolvere il puzzle del braccio
     * robotico.
     */
    private final int[][] CORRECT_ROBOT_MOVEMET = {
        {0, -1, -1},
        {-1, 0, -1},
        {-1, 0, -2}
    };

    /**
     * Costruisce un UseObserver e registra le azioni per ciascun oggetto.
     *
     * @param game lo stato di gioco corrente
     */
    public UseObserver(GameDesc game) {
        uses.put(game.getObjectByID(0), this::combineModel);
        uses.put(game.getObjectByID(1), this::combineModel);
        uses.put(game.getObjectByID(2), this::combineModel);
        uses.put(game.getObjectByID(3), this::combineModel);
        uses.put(game.getObjectByID(4), this::readSusanDiary);
        uses.put(game.getObjectByID(5), this::readLukeNote);
        uses.put(game.getObjectByID(6), this::createPrism);
        uses.put(game.getObjectByID(7), this::createPrism);
        uses.put(game.getObjectByID(8), this::usePrism);
        uses.put(game.getObjectByID(9), this::useComputer);
        uses.put(game.getObjectByID(10), this::wearSpaceSuit);
        uses.put(game.getObjectByID(11), this::useNotebook);
        uses.put(game.getObjectByID(13), this::insertDestinyPassword);
        uses.put(game.getObjectByID(14), this::useTrapdor);
        uses.put(game.getObjectByID(15), this::useRobotTerminal);
    }

    /**
     * Gestisce l'evento di utilizzo di un oggetto. Verifica che il comando sia
     * di tipo USE e che l'oggetto sia utilizzabile, quindi applica l'azione
     * corrispondente.
     *
     * @param game lo stato di gioco corrente
     * @param parserOutput l'output del parser che contiene comando e oggetto
     * @return messaggio di risposta all'azione di utilizzo
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder useMsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            if (parserOutput.getObject() != null && parserOutput.getObject().isUsable()) {
                if (game.getInventory().getList().contains(parserOutput.getObject())) {
                    useMsg.append(uses.get(parserOutput.getObject()).apply(game));
                    InventoryUI.updateInventory(game);
                } else if (game.getCurrentRoom().getObjects().contains(parserOutput.getObject())
                        && (parserOutput.getObject().getId() == 9 || parserOutput.getObject().getId() == 13
                        || parserOutput.getObject().getId() == 14 || parserOutput.getObject().getId() == 15)) {
                    useMsg.append(uses.get(parserOutput.getObject()).apply(game));
                } else {
                    useMsg.append("Non possiedi questo oggetto al momento! Riprova, magari sarai più fortunato.");
                }
            } else {
                useMsg.append("Come pensi di poterlo usare?!");
            }
        }
        return useMsg.toString();
    }

    /**
     * Legge e restituisce il contenuto del diario di Susan.
     *
     * @param game lo stato di gioco (non utilizzato)
     * @return testo del diario di Susan
     */
    public String readSusanDiary(GameDesc game) {
        StringBuilder readDiary = new StringBuilder();
        readDiary.append("Le pagine sono poche, ordinate, scritte con una grafia decisa. \n"
                + "Non si perde in pensieri, ma qualcosa filtra tra le righe. \n"
                + "Si capisce che era entrata nel modulo Leonardo con uno scopo preciso. \n"
                + "Aveva notato discrepanze nei log di HAL, comportamenti anomali. \n"
                + "Scrive: “I dati non tornano. Ogni volta che controllo, c’è qualcosa che manca o è stato sovrascritto. "
                + "Nessuno sembra accorgersene. \n"
                + "Nell’ultima pagina, datata oggi, la scrittura è più rapida, quasi nervosa:\n"
                + "“È l’ultimo giorno. Prima di andarmene devo passare i dati principali manualmente da questo computer alla base sulla terra."
                + " È solo una precauzione… ma qualcosa nel modo in cui questa macchina ‘decide’ mi mette a disagio.”");
        return readDiary.toString();
    }

    /**
     * Legge e restituisce la nota criptica di Luke.
     *
     * @param game lo stato di gioco (non utilizzato)
     * @return testo del bigliettino di Luke
     */
    public String readLukeNote(GameDesc game) {
        StringBuilder readNote = new StringBuilder();
        readNote.append("Nel bigliettino di Luke c'è una nota affrettata: \n");
        readNote.append("D4T4B4S3 -> 'Il dado è tratto. [4]' \n");
        readNote.append("Cosa vorrà mai dire...? \n");
        return readNote.toString();
    }

    /**
     * Combina pezzi di vetro per creare un mezzo prisma o un prisma completo.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo al risultato della combinazione
     */
    public String createPrism(GameDesc game) {
        StringBuilder prismMsg = new StringBuilder();
        if (game.getInventory().count(game.getObjectByID(6)) >= 2) {
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().add(game.getObjectByID(7));
            prismMsg.append("Unendo due pezzi di vetro hai creato un mezzo prisma\n Lo trovi nel tuo inventario.");
        } else {
            if (game.getInventory().getList().contains(game.getObjectByID(7))
                    && game.getInventory().count(game.getObjectByID(6)) == 1) {
                game.getInventory().remove(game.getObjectByID(6));
                game.getInventory().remove(game.getObjectByID(7));
                game.getInventory().add(game.getObjectByID(8));
                prismMsg.append("Unendo il mezzo prisma e il pezzo di vetro "
                        + "hai creato un prisma! Lo trovi nel tuo inventario.");
            } else {
                prismMsg.append("Non hai abbastanza pezzi di vetro!");
            }
        }
        return prismMsg.toString();
    }

    /**
     * Utilizza il prisma per riflettere la luce nel modulo DESTINY.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo all'uso del prisma
     */
    public String usePrism(GameDesc game) {
        StringBuilder usingPrismMsg = new StringBuilder();
        // servirebbe anche il controllo per capire se la luce dall'oblò arriva
        if (game.getCurrentRoom().getName().equals("DESTINY")) {
            if (game.getCurrentRoom().isVisible()) {
                game.getInventory().remove(game.getObjectByID(8));
                game.getObjectByID(8).setInUse(true);
                usingPrismMsg.append("Hai posizionato il prisma sulla finestra di vetro ai tuoi piedi, "
                        + "ma l'angolazione non permette di far arrivare la luce al modulo di connessione. \n"
                        + "Dovresti provare a ruotarlo.");
            } else {
                usingPrismMsg.append("Magari dovresti provare ad ASPETTARE...");
            }
        } else {
            usingPrismMsg.append("Non puoi usare qui il prisma!");
        }
        return usingPrismMsg.toString();
    }

    /**
     * Apre il terminale HAL in un'interfaccia grafica separata.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio di conferma dell'accensione del computer
     */
    public String useComputer(GameDesc game) {
        StringBuilder usingComputer = new StringBuilder();
        SwingUtilities.invokeLater(() -> {
            HALterminal terminal = new HALterminal(game);
            terminal.setVisible(true);
        });
        usingComputer.append("Hai acceso il computer.");
        return usingComputer.toString();
    }

    /**
     * Indossa la tuta spaziale di Luke e abilita l'accesso allo spazio.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio dopo l'indossamento della tuta
     */
    public String wearSpaceSuit(GameDesc game) {
        StringBuilder wearSuit = new StringBuilder();
        wearSuit.append("Hai indossato la tuta di Luke! \n"
                + "Iniziano a riaffiorare tutti i bei ricordi e le avventure passate insieme, "
                + "causandoti tanta nostalgia.\n");
        game.getInventory().remove(game.getObjectByID(10));
        game.getRoomByName("SPAZIO").setAccessible(true);
        // quando entrerà in leonardo, dopo il minigame, scrivere che la tuta si è
        // completamente rotta
        return wearSuit.toString();
    }

    /**
     * Apre l'interfaccia del taccuino di bordo.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio di conferma dell'apertura del taccuino
     */
    public String useNotebook(GameDesc game) {
        NotebookUI.show(game);
        return "Apri il taccuino!";
    }

    /**
     * Combina i pezzi del modellino per ottenere il modellino completo della
     * ISS.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo al risultato della combinazione
     */
    public String combineModel(GameDesc game) {
        StringBuilder modelMsg = new StringBuilder();
        String descrModellino = "Un Modellino, rappresenta la stazione spaziale internazionale.\n"
                + "Unendo i pezzi del modellino compare una sequenza "
                + "di direzioni scritta con un pennarello: ↑ ↑ ↓ ↓ ← → ← →.\n"
                + "Che possa servire per sbloccare qualcosa?";

        if (game.getInventory().getList().contains(game.getObjectByID(0))
                && game.getInventory().getList().contains(game.getObjectByID(1))
                && game.getInventory().getList().contains(game.getObjectByID(2))
                && game.getInventory().getList().contains(game.getObjectByID(3))) {

            game.getInventory().remove(game.getObjectByID(0));
            game.getInventory().remove(game.getObjectByID(1));
            game.getInventory().remove(game.getObjectByID(2));
            game.getInventory().remove(game.getObjectByID(3));

            BDObject modellinoCompleto = new BDObject(0123, "Modellino della Stazione",
                    descrModellino);
            modellinoCompleto.setAlias(Set.of("modellinostazione", "modellinocompleto"));

            game.getInventory().add(modellinoCompleto);

            modelMsg.append("Hai ottenuto ").append(modellinoCompleto.getDescription());

        } else {
            modelMsg.append("Non te ne puoi fare ancora nulla. Ti servono gli altri pezzi.");
        }

        return modelMsg.toString();
    }

    /**
     * Mostra il dialogo per inserire la password di Destiny e sblocca il modulo
     * se corretta.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo al successo o fallimento dell'inserimento
     */
    public String insertDestinyPassword(GameDesc game) {
        StringBuilder pswMsg = new StringBuilder();

        boolean success = InsertPasswordUI.showPasswordDialog(DESTINY_PASSWORD);

        if (success) {
            pswMsg.append("Password Corretta. Apertura modulo Destiny");
            game.getRoomByName("DESTINY").setAccessible(true);
            game.getObjectByID(13).setUsable(false);
            if (game.getRoomByName("LEONARDO").isAccessible()) {
                game.getCurrentRoom().setRoomImage("src/main/resources/img/node1_tutto_aperto.png");
            } else {
                game.getCurrentRoom().setRoomImage("src/main/resources/img/node1_avanti_aperto.png");
            }
        } else {
            pswMsg.append("Password Errata");
        }

        return pswMsg.toString();
    }

    /**
     * Gestisce il puzzle del terminale robotico per l'ormeggio della navicella.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo al risultato del puzzle robotico
     */
    public String useRobotTerminal(GameDesc game) {
        StringBuilder rbtmsg = new StringBuilder();

        if (!game.isAiActive()) {
            boolean solved = RobotArmPuzzleUI.showPuzzleDialog(this.CORRECT_ROBOT_MOVEMET);

            if (solved) {
                game.setCurrentRoom(game.getRoomById(11)); // stanza della scelta;
                rbtmsg.append("Matrice di movimento riconosciuta. Completamento dell'ormeggio della navicella."
                        + "\nMi reco nel modulo harmony mentre penso il da farsi.");
            } else {
                rbtmsg.append("Matrice non riconosciuta.");
            }

        } else {
            rbtmsg.append("L'AI di bordo HAL, ha ancora accesso ai sistemi robotici "
                    + "della stazione. Dovrei prima disattivare il suo controllo "
                    + "prima che mi impedisca di ormeggiare la navicella");
        }
        return rbtmsg.toString();
    }

    /**
     * Fornisce la password per la botola verso la Soyuz, basata sul continente
     * sorvolato.
     *
     * @param game lo stato di gioco corrente
     * @return messaggio relativo all'uso del tablet per aprire la botola
     */
    public String useTrapdor(GameDesc game) {
        if (!game.isFlagTrapdoor()) {
            if (game.getTrapdoor()) {
                game.getObjectByID(14).setInUse(true);
                return "La password per aprire la botola è il nome del continente su cui stiamo sorvolando.";
            } else {
                return "Devo trovare Susan prima di scappare da qui.";
            }
        } else {
            return "La navicella per la fuga è eplosa, la mia unica speranza ora è la Dragon2.";
        }
    }
}
