/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;

/**
 * Questa classe rappresenta l'observer del comando 'HISTORY',
 * permette di visualizzare un breve paragrafo che racconta la 
 * storia del modulo in cui si trova il giocatore.
 * Per farlo, implmenta l'interfaccia GameObserver.
 *
 * @author lorenzopeluso
 */
public class HistoryObserver implements GameObserver {
    
    @Override
    public String update(GameDesc game, ParserOutput parserOutput){
        StringBuilder historyMessage = new StringBuilder();

        if (parserOutput.getCommand()!= null && parserOutput
                .getCommand().getType() == CommandType.HISTORY) {        
            historyMessage.append(game.getCurrentRoom().getHistory());
        }
        return historyMessage.toString();      
    };
}
