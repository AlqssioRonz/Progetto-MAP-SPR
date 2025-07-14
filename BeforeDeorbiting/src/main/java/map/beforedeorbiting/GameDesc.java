/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting;

import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.Command;
import map.beforedeorbiting.type.Room;
import map.beforedeorbiting.type.Inventory;
import map.beforedeorbiting.parser.ParserOutput;

import java.io.Serializable;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import map.beforedeorbiting.type.CommandType;

/**
 * Template astratto per un’avventura testuale “Before Deorbiting”. Mantiene lo
 * stato del gioco (stanze, comandi, inventario, oggetti, musica, flag vari) e
 * definisce i metodi che una sottoclasse deve implementare per
 * inizializzazione, avanzamento della storia e messaggio di benvenuto.
 *
 * @author lorenzopeluso
 */
public abstract class GameDesc implements Serializable {

    /**
     * Elenco di tutte le stanze del gioco.
     */
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Elenco di tutti i comandi disponibili.
     */
    private final List<Command> commands = new ArrayList<>();

    /**
     * Elenco di tutti gli oggetti di gioco.
     */
    private final List<BDObject> listObj = new ArrayList<>();

    /**
     * Testo corrente del taccuino di gioco.
     */
    private String notebookText = "";

    /**
     * Ultimo comando eseguito dal giocatore.
     */
    private Command lastCommand;

    /**
     * Inventario del giocatore (singleton).
     */
    private Inventory inventory = Inventory.getInstance();

    /**
     * Stanza corrente in cui si trova il giocatore.
     */
    private Room currentRoom;

    /**
     * Flag che indica se la botola è già stata attivata.
     */
    private boolean flagTrapdoor = false;

    /**
     * Indica la presenza della botola nella stanza corrente.
     */
    private boolean trapdoor;

    /**
     * Indica se l’intelligenza artificiale di bordo è attiva.
     */
    private boolean aiActive = true;

    /**
     * True se il modulo Kibo è già stato visitato.
     */
    private boolean kiboVisited = false;

    /**
     * True se la musica del modulo Leonardo è già stata riprodotta.
     */
    private boolean leonardoMusicPlayed = false;

    /**
     * True se la prima traccia musicale è già stata riprodotta.
     */
    private boolean firstMusicPlayed = false;

    /**
     * Percorso della traccia audio corrente (es. "/music/void.wav").
     */
    private String currentMusicTrack;

    /**
     * Indica se la prima traccia musicale è già stata riprodotta.
     *
     * @return true se la prima traccia è stata eseguita, false altrimenti
     */
    public boolean isFirstMusicPlayed() {
        return firstMusicPlayed;
    }

    /**
     * Imposta lo stato relativo alla prima traccia musicale.
     *
     * @param firstMusicPlayed true per indicare che la prima musica è stata
     * riprodotta
     */
    public void setFirstMusicPlayed(boolean firstMusicPlayed) {
        this.firstMusicPlayed = firstMusicPlayed;
    }

    /**
     * Indica se la musica di Leonardo è già stata riprodotta.
     *
     * @return true se è già stata eseguita, false altrimenti
     */
    public boolean isLeonardoMusicPlayed() {
        return leonardoMusicPlayed;
    }

    /**
     * Imposta lo stato di riproduzione della musica di Leonardo.
     *
     * @param played true se la musica di Leonardo è stata riprodotta
     */
    public void setLeonardoMusicPlayed(boolean played) {
        this.leonardoMusicPlayed = played;
    }

    /**
     * Indica se il modulo Kibo è già stato visitato.
     *
     * @return true se è già stato visitato, false altrimenti
     */
    public boolean isKiboVisited() {
        return kiboVisited;
    }

    /**
     * Imposta lo stato di visita del modulo Kibo.
     *
     * @param v true per indicare che Kibo è stato visitato
     */
    public void setKiboVisited(boolean v) {
        kiboVisited = v;
    }

    /**
     * Indica se è già stato attivato il flag di botola.
     *
     * @return true se il flag è attivo, false altrimenti
     */
    public boolean isFlagTrapdoor() {
        return flagTrapdoor;
    }

    /**
     * Imposta il flag di botola.
     *
     * @param flagTrapdoor true per attivare la botola, false per disattivarla
     */
    public void setFlagTrapdoor(boolean flagTrapdoor) {
        this.flagTrapdoor = flagTrapdoor;
    }

    /**
     * Restituisce l’ultimo comando eseguito.
     *
     * @return l’istanza di Command corrispondente all’ultimo input
     */
    public Command getLastCommand() {
        return lastCommand;
    }

    /**
     * Registra l’ultimo comando eseguito.
     *
     * @param lastCommand il comando da salvare
     */
    public void setLastCommand(Command lastCommand) {
        this.lastCommand = lastCommand;
    }

    /**
     * Restituisce la lista di tutte le stanze del gioco.
     *
     * @return lista immutabile di stanze
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Restituisce la lista di tutti i comandi disponibili.
     *
     * @return lista di Command
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Restituisce il percorso della traccia musicale correntemente in
     * riproduzione.
     *
     * @return path relativo al file audio
     */
    public String getCurrentMusicTrack() {
        return currentMusicTrack;
    }

    /**
     * Imposta la traccia musicale corrente.
     *
     * @param currentMusicTrack percorso relativo del file audio
     */
    public void setCurrentMusicTrack(String currentMusicTrack) {
        this.currentMusicTrack = currentMusicTrack;
    }

    /**
     * Inizializza lo stato iniziale del gioco. Implementato dalla sottoclasse
     * per popolare stanze, oggetti e comandi.
     */
    public abstract void init();

    /**
     * Restituisce la lista di tutti gli oggetti di gioco.
     *
     * @return lista di BDObject
     */
    public List<BDObject> getListObj() {
        return listObj;
    }

    /**
     * Restituisce l’inventario del giocatore.
     *
     * @return istanza singola di Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sostituisce l’inventario del giocatore.
     *
     * @param inventory nuova istanza di Inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Restituisce la stanza corrente in cui si trova il giocatore.
     *
     * @return stanza attiva
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Indica se la botola è presente nella stanza corrente.
     *
     * @return true se c’è una botola, false altrimenti
     */
    public Boolean getTrapdoor() {
        return trapdoor;
    }

    /**
     * Imposta la presenza della botola nella stanza corrente.
     *
     * @param trapdoor true per abilitare la botola, false per disabilitarla
     */
    public void setTrapdoor(boolean trapdoor) {
        this.trapdoor = trapdoor;
    }

    /**
     * Imposta la stanza corrente.
     *
     * @param currentRoom stanza da impostare come corrente
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Esegue la mossa successiva in base all’output del parser.
     *
     * @param p risultato dell’analisi del comando
     * @param out PrintStream su cui scrivere il risultato della mossa
     */
    public abstract void nextMove(ParserOutput p, PrintStream out);

    /**
     * Restituisce il testo corrente del taccuino di gioco.
     *
     * @return stringa con il contenuto del notebook
     */
    public String getNotebookText() {
        return notebookText;
    }

    /**
     * Imposta il contenuto del taccuino di gioco.
     *
     * @param notebookText nuovo testo del notebook
     */
    public void setNotebookText(String notebookText) {
        this.notebookText = notebookText;
    }

    /**
     * Restituisce il messaggio di benvenuto da mostrare all’avvio.
     *
     * @return messaggio di benvenuto
     */
    public abstract String getWelcomeMessage();

    /**
     * Cerca un oggetto per ID nella lista degli oggetti.
     *
     * @param id identificativo univoco dell’oggetto
     * @return BDObject corrispondente o null se non trovato
     */
    public BDObject getObjectByID(int id) {
        return listObj.stream()
                .filter(obj -> obj.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Conta quante volte un dato oggetto (stesso ID) compare nella lista.
     *
     * @param target oggetto di riferimento
     * @return numero di occorrenze
     */
    public int count(BDObject target) {
        int id = target.getId();
        return (int) listObj.stream()
                .filter(obj -> obj.getId() == id)
                .count();
    }

    /**
     * Cerca una stanza per nome.
     *
     * @param name nome della stanza
     * @return Room corrispondente o null se non trovata
     */
    public Room getRoomByName(String name) {
        return rooms.stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Cerca una stanza per ID.
     *
     * @param id identificativo della stanza
     * @return Room corrispondente o null se non trovata
     */
    public Room getRoomById(int id) {
        return rooms.stream()
                .filter(room -> room.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Restituisce il comando corrispondente a un determinato CommandType.
     *
     * @param command tipo di comando da cercare
     * @return Command trovato o null se non presente
     */
    public Command getCommand(CommandType command) {
        for (Command cmd : commands) {
            if (cmd.getType().equals(command)) {
                return cmd;
            }
        }
        return null;
    }

    /**
     * Attiva o disattiva il comportamento AI del gioco.
     *
     * @param aiActive true per attivare l’AI, false per disattivarla
     */
    public void setAiActive(boolean aiActive) {
        this.aiActive = aiActive;
    }

    /**
     * Indica se l’AI è attiva.
     *
     * @return true se l’AI è abilitata, false altrimenti
     */
    public boolean isAiActive() {
        return aiActive;
    }
}
