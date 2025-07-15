/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;

/**
 * Osservatore per il comando di attesa (WAIT). Gestisce la comparsa di nuove
 * condizioni di luce nel modulo DESTINY o fornisce un messaggio alternativo se
 * si resta in altre stanze.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class WaitObserver implements GameObserver {

    /**
     * Esegue l’azione di “attesa” nel gioco. Se il giocatore è nel modulo
     * DESTINY, aggiorna l’immagine del finestrino e lo rende visibile, quindi
     * descrive il cambiamento della luce solare riflessa sulla Terra.
     * Altrimenti restituisce un messaggio di attesa inutile.
     *
     * @param game lo stato corrente del gioco
     * @param parserOutput il risultato dell’analisi del comando WAIT
     * @return un messaggio da mostrare al giocatore in base alla stanza
     * corrente
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder waitmsg = new StringBuilder();

        if (parserOutput.getCommand() != null
                && parserOutput.getCommand().getType() == CommandType.WAIT
                && game.getCurrentRoom().equals(game.getRoomByName("DESTINY"))
                && !game.getRoomByName("DESTINY").isVisible()) {
            // Nel modulo DESTINY la luce cambia dopo l'attesa
            game.getCurrentRoom().setRoomImage("src/main/resources/img/destiny_luce_oblo.png");
            game.getRoomByName("DESTINY").setVisible(true);
            waitmsg.append(
                    "Dopo qualche tempo la stazione avanza nella sua orbita e la luce "
                    + "del sole riflessa sulla Terra ti permette di vedere meglio.");
        } else if (parserOutput.getCommand() != null
                && parserOutput.getCommand().getType() == CommandType.WAIT
                && !game.getCurrentRoom().equals(game.getRoomByName("DESTINY"))) {
            // Attesa in stanze diverse da DESTINY non produce cambiamenti
            waitmsg.append("Aspettando qui non arriverà il principe azzurro.");
        }

        return waitmsg.toString();
    }
}
