/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una stanza del gioco, con ID, nome, descrizione, testo di gioco
 * (gameStory), storia (history), immagine associata, visibilità e
 * accessibilità, oltre ai collegamenti alle stanze adiacenti (forward, aft,
 * starboard, port, overhead, deck). Contiene anche gli oggetti presenti nella
 * stanza.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class Room {

    private final int id;
    private String name;
    private String description;
    private String look;
    private String gameStory;
    private String history;
    private String roomImage;
    private boolean visible = true;
    private boolean accesed = false;
    private boolean accessible = true;
    private transient Room forward = null;
    private transient Room aft = null;
    private transient Room starboard = null;
    private transient Room port = null;
    private transient Room overhead = null;
    private transient Room deck = null;
    private final List<BDObject> objects = new ArrayList<>();

    /**
     * Costruisce una stanza identificata da un ID.
     *
     * @param id identificativo unico della stanza
     */
    public Room(int id) {
        this.id = id;
    }

    /**
     * Costruisce una stanza con ID, nome e descrizione.
     *
     * @param id identificativo unico della stanza
     * @param name nome della stanza
     * @param description descrizione dettagliata della stanza
     */
    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Costruisce una stanza con ID, nome, descrizione e testo per il comando
     * LOOK.
     *
     * @param id identificativo unico della stanza
     * @param name nome della stanza
     * @param description descrizione dettagliata della stanza
     * @param look testo restituito dal comando LOOK
     */
    public Room(int id, String name, String description, String look) {
        this(id, name, description);
        this.look = look;
    }

    /**
     * Costruisce una stanza con ID, nome, descrizione, look e testo di gioco
     * iniziale.
     *
     * @param id identificativo unico della stanza
     * @param name nome della stanza
     * @param description descrizione dettagliata della stanza
     * @param look testo restituito dal comando LOOK
     * @param gameStory testo mostrato alla prima entrata nella stanza
     */
    public Room(int id, String name, String description, String look, String gameStory) {
        this(id, name, description, look);
        this.gameStory = gameStory;
    }

    /**
     * Costruisce una stanza con ID, nome, descrizione, look, testo di gioco e
     * storia.
     *
     * @param id identificativo unico della stanza
     * @param name nome della stanza
     * @param description descrizione dettagliata della stanza
     * @param look testo restituito dal comando LOOK
     * @param gameStory testo mostrato alla prima entrata nella stanza
     * @param history storia o diario del modulo per il comando HISTORY
     */
    public Room(int id, String name, String description, String look, String gameStory, String history) {
        this(id, name, description, look, gameStory);
        this.history = history;
    }

    /**
     * Restituisce l’ID univoco della stanza.
     *
     * @return identificativo della stanza
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il nome della stanza.
     *
     * @return nome della stanza
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il nome della stanza.
     *
     * @param name nuovo nome della stanza
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Restituisce la descrizione della stanza.
     *
     * @return descrizione dettagliata
     */
    public String getDescription() {
        return description;
    }

    /**
     * Imposta la descrizione della stanza.
     *
     * @param description nuova descrizione
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce il testo per il comando LOOK.
     *
     * @return testo di “look”
     */
    public String getLook() {
        return look;
    }

    /**
     * Imposta il testo per il comando LOOK.
     *
     * @param look nuovo testo di “look”
     */
    public void setLook(String look) {
        this.look = look;
    }

    /**
     * Restituisce il testo di gioco mostrato alla prima entrata. Dopo il primo
     * accesso, non viene più mostrato.
     *
     * @return gameStory se non ancora mostrato, altrimenti stringa vuota
     */
    public String getGameStory() {
        StringBuilder message = new StringBuilder();
        if (!this.accesed) {
            message.append(gameStory);
            this.accesed = true;
        }
        return message.toString();
    }

    /**
     * Imposta il testo di gioco iniziale per la stanza.
     *
     * @param gameStory testo da mostrare una volta sola
     */
    public void setGameStory(String gameStory) {
        this.gameStory = gameStory;
    }

    /**
     * Verifica se la stanza è già stata visitata.
     *
     * @return true se già visitata, false altrimenti
     */
    public boolean isAccesed() {
        return accesed;
    }

    /**
     * Imposta lo stato di visita della stanza.
     *
     * @param accesed true se la stanza è stata visitata
     */
    public void setAccesed(boolean accesed) {
        this.accesed = accesed;
    }

    /**
     * Restituisce la storia o il diario associato alla stanza.
     *
     * @return testo history
     */
    public String getHistory() {
        return history;
    }

    /**
     * Imposta la storia o il diario della stanza.
     *
     * @param history nuovo testo di storia
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * Imposta il percorso dell’immagine associata alla stanza.
     *
     * @param roomImage path relativo dell’immagine
     */
    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    /**
     * Restituisce il percorso dell’immagine della stanza.
     *
     * @return path dell’immagine
     */
    public String getRoomImage() {
        return roomImage;
    }

    /**
     * Verifica se la stanza è visibile sulla mappa o interfaccia.
     *
     * @return true se visibile, false altrimenti
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Imposta la visibilità della stanza.
     *
     * @param visible true per rendere visibile, false altrimenti
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Verifica se la stanza è accessibile dal giocatore.
     *
     * @return true se accessibile, false altrimenti
     */
    public boolean isAccessible() {
        return accessible;
    }

    /**
     * Imposta lo stato di accessibilità della stanza.
     *
     * @param accessible true per rendere accessibile, false altrimenti
     */
    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    /**
     * Restituisce la stanza nella direzione “prua” (forward).
     *
     * @return stanza adiacente in avanti
     */
    public Room getForward() {
        return forward;
    }

    /**
     * Imposta la stanza adiacente in direzione “prua”.
     *
     * @param forward stanza da collocare in avanti
     */
    public void setForward(Room forward) {
        this.forward = forward;
    }

    /**
     * Restituisce la stanza nella direzione “poppa” (aft).
     *
     * @return stanza adiacente dietro
     */
    public Room getAft() {
        return aft;
    }

    /**
     * Imposta la stanza adiacente in direzione “poppa”.
     *
     * @param aft stanza da collocare dietro
     */
    public void setAft(Room aft) {
        this.aft = aft;
    }

    /**
     * Restituisce la stanza nella direzione “tribordo” (starboard).
     *
     * @return stanza adiacente a tribordo
     */
    public Room getStarboard() {
        return starboard;
    }

    /**
     * Imposta la stanza adiacente in direzione “tribordo”.
     *
     * @param starboard stanza da collocare a tribordo
     */
    public void setStarboard(Room starboard) {
        this.starboard = starboard;
    }

    /**
     * Restituisce la stanza nella direzione “babordo” (port).
     *
     * @return stanza adiacente a babordo
     */
    public Room getPort() {
        return port;
    }

    /**
     * Imposta la stanza adiacente in direzione “babordo”.
     *
     * @param port stanza da collocare a babordo
     */
    public void setPort(Room port) {
        this.port = port;
    }

    /**
     * Restituisce la stanza nella direzione “sopra” (overhead).
     *
     * @return stanza adiacente in alto
     */
    public Room getOverhead() {
        return overhead;
    }

    /**
     * Imposta la stanza adiacente in direzione “sopra”.
     *
     * @param overhead stanza da collocare in alto
     */
    public void setOverhead(Room overhead) {
        this.overhead = overhead;
    }

    /**
     * Restituisce la stanza nella direzione “sotto” (deck).
     *
     * @return stanza adiacente in basso
     */
    public Room getDeck() {
        return deck;
    }

    /**
     * Imposta la stanza adiacente in direzione “sotto”.
     *
     * @param deck stanza da collocare in basso
     */
    public void setDeck(Room deck) {
        this.deck = deck;
    }

    /**
     * Restituisce la lista di oggetti presenti nella stanza.
     *
     * @return lista di {@link BDObject}
     */
    public List<BDObject> getObjects() {
        return objects;
    }

    /**
     * Aggiunge un oggetto alla stanza se non è già presente.
     *
     * @param o oggetto da aggiungere (non null)
     */
    public void addObject(BDObject o) {
        if (!objects.contains(o)) {
            objects.add(o);
        }
    }

    /**
     * Rimuove un oggetto dalla stanza se presente.
     *
     * @param o oggetto da rimuovere
     */
    public void removeObject(BDObject o) {
        objects.remove(o);
    }

    /**
     * Cerca un oggetto all’interno della stanza per ID.
     *
     * @param id identificativo dell’oggetto
     * @return oggetto corrispondente oppure null
     */
    public BDObject getObject(int id) {
        for (BDObject o : objects) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    /**
     * Confronta due stanze basandosi sull’ID.
     *
     * @param obj oggetto da confrontare
     * @return true se l’altro è una Room con lo stesso ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Room other = (Room) obj;
        return this.id == other.id;
    }

    /**
     * Calcola l’hash code basato sull’ID (necessario se si override equals).
     *
     * @return hash code dell’ID
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.id;
        return hash;
    }

}
