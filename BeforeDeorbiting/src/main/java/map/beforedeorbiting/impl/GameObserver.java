/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;

/**
 * Rappresenta un osservatore degli eventi di gioco. Una classe che implementa
 * {@code GameObserver} viene notificata ad ogni comando valido inserito dal
 * giocatore e può reagire producendo un messaggio da visualizzare.
 *
 * @author lorenzopeluso
 * @see GameObservable
 */
public interface GameObserver {

    /**
     * Viene invocato per notificare l’osservatore dell’ultima azione
     * interpretata dal parser.
     *
     * @param game lo stato corrente del gioco
     * @param parserOutput il risultato dell’analisi del comando da parte del
     * parser
     * @return una stringa da mostrare al giocatore in risposta all’azione
     */
    String update(GameDesc game, ParserOutput parserOutput);

}
