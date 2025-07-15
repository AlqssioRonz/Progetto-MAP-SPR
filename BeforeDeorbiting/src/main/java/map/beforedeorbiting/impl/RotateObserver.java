/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;

/**
 * Osservatore per il comando di rotazione (ROTATE). Intercetta l’azione di
 * rotazione dell’oggetto selezionato, verifica le condizioni di stanza e
 * oggetto in uso, e, se corrette, modifica l’immagine del laboratorio e abilita
 * l’accesso alle stanze Harmony e Kibo. In caso contrario restituisce messaggi
 * di errore o input non valido.
 *
 * @author ronzu
 */
public class RotateObserver implements GameObserver {

    /**
     * Gestisce l’evento di rotazione. Se il comando è ROTATE e l’oggetto
     * invocato corrisponde al prisma (ID 8) ed è in uso nella stanza DESTINY,
     * aggiorna l’immagine della stanza, disattiva l’uso del prisma e sblocca le
     * stanze Harmony e Kibo. Altrimenti fornisce un messaggio di errore o di
     * input non sensato.
     *
     * @param game lo stato corrente del gioco
     * @param parserOutput il risultato dell’analisi del comando con eventuale
     * oggetto
     * @return una stringa da visualizzare al giocatore in base all’esito della
     * rotazione
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder rotatemsg = new StringBuilder();

        // Comando ROTATE con oggetto specificato
        if (parserOutput.getCommand() != null
                && parserOutput.getCommand().getType() == CommandType.ROTATE
                && parserOutput.getObject() != null) {

            // Condizione stanza e oggetto in uso
            if ("DESTINY".equals(game.getCurrentRoom().getName())
                    && game.getObjectByID(8).isInUse()) {

                // Verifica che l’oggetto passato sia effettivamente il prisma (ID 8)
                if (parserOutput.getObject().equals(game.getObjectByID(8))) {
                    // Azioni da eseguire in caso di successo
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/destiny_aperto.png");
                    rotatemsg.append(
                            "Il prisma ha riflesso tutta la luce verso il modulo di connessione, "
                            + "riattivando tutte le luci e aprendo la porta davanti a te.");
                    game.getObjectByID(8).setInUse(false);
                    game.getRoomByName("HARMONY").setAccessible(true);
                    game.getRoomByName("KIBO").setAccessible(true);
                } else {
                    rotatemsg.append("Cosa pensi di voler ruotare?");
                }

            } else {
                rotatemsg.append("C'è un tempo e un luogo per ogni cosa, ma non ora...");
            }
            // Comando ROTATE senza oggetto valido
        } else if (parserOutput.getCommand() != null
                && parserOutput.getCommand().getType() == CommandType.ROTATE
                && parserOutput.getInvObject() == null) {
            rotatemsg.append("C'è un tempo e un luogo per ogni cosa, ma non ora...");
        }
        return rotatemsg.toString();
    }
}
