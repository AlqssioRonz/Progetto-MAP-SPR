/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package map.beforedeorbiting.type;

/**
 * Elenca i tipi di comando supportati dal gioco Before Deorbiting. Ogni comando
 * rappresenta un’azione o una direzione che il giocatore può eseguire.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public enum CommandType {

    /**
     * Mostra l’elenco dei comandi disponibili.
     */
    HELP,
    /**
     * Visualizza la storia o il diario del modulo corrente.
     */
    HISTORY,
    /**
     * Mostra il contenuto dell’inventario del giocatore.
     */
    INVENTORY,
    /**
     * Sposta il giocatore in avanti (direzione “prua”).
     */
    FORWARD,
    /**
     * Sposta il giocatore indietro (direzione “poppa”).
     */
    AFT,
    /**
     * Sposta il giocatore verso destra (direzione “tribordo”).
     */
    STARBOARD,
    /**
     * Sposta il giocatore verso sinistra (direzione “babordo”).
     */
    PORT,
    /**
     * Sposta il giocatore verso l'alto (sopra la testa).
     */
    OVERHEAD,
    /**
     * Sposta il giocatore verso il basso (sotto i piedi).
     */
    DECK,
    /**
     * Raccoglie l’oggetto specificato dal pavimento o dall’ambiente.
     */
    PICK_UP,
    /**
     * Lascia l’oggetto specificato dall’inventario nell’ambiente.
     */
    DROP,
    /**
     * Usa l’oggetto specificato su un altro oggetto o meccanismo.
     */
    USE,
    /**
     * Osserva dettagliatamente l’oggetto o l’area specificata.
     */
    LOOK_AT,
    /**
     * Risposta per l'enigma di destiny: "ASPETTA".
     */
    WAIT,
    /**
     * Ruota l'oggetti specificato.
     */
    ROTATE,
    /**
     * Risposta per il minigame: "AFRICA".
     */
    AFRICA,
    /**
     * Risposta per il minigame: "EUROPA".
     */
    EUROPA,
    /**
     * Risposta per il minigame: "ASIA".
     */
    ASIA,
    /**
     * Risposta per il minigame: "NAMERICA".
     */
    NAMERICA,
    /**
     * Risposta per il minigame: "SAMERICA".
     */
    SAMERICA,
    /**
     * Risposta per il minigame: "OCEANIA".
     */
    OCEANIA,
    /**
     * Risposta per il minigame: "ANTARTIDE".
     */
    ANTARTIDE,
    /**
     * Risposta per il minigame: "OCEANO".
     */
    OCEANO,
    /**
     * Salva lo stato corrente del gioco su disco.
     */
    SAVE
}
