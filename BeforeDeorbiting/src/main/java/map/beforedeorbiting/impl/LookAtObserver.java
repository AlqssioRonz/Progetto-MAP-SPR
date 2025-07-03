/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;

/**
 * Questa classe rappresenta l'observer del comando 'OSSERVA',
 * permette di osservare l'ambiente circostante/l'oggetto desiderato.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * @author ronzu
 */
public class LookAtObserver  implements GameObserver, Serializable {
    
    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce un messaggio di risposta.
     * 
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
     * @param parserOutput l'output del parser utile per conoscere l'input dell'utente
     * @return il messaggio di risposta in base all'azione di 'osserva'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder lookAtmsg = new StringBuilder();
        if(parserOutput.getCommand().getType() == CommandType.LOOK_AT){
            if(parserOutput.getObject() != null && game.getCurrentRoom().getObjects().contains(parserOutput.getObject())){
                if(parserOutput.getInvObject() != null){
                    lookAtmsg.append("Provi a osservare due cose alla volta, ma il tuo cervello va in tilt."
                            + "Meglio uno alla volta, fidati. Intanto ti mostro solo il primo oggetto...");
                }
                lookAtmsg.append(parserOutput.getObject().getDescription());
            }else if(parserOutput.getInvObject() != null){
                lookAtmsg.append(parserOutput.getInvObject().getDescription());
            }else{
                game.getCurrentRoom().getLook();
            }
        }
        return lookAtmsg.toString();
    }
    
}
