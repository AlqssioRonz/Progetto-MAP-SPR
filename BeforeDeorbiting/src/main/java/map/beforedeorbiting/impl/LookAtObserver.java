/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

/**
 * Questa classe rappresenta l'observer del comando 'OSSERVA',
 * permette di osservare l'ambiente circostante/l'oggetto desiderato.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * @author ronzu
 */
public class LookAtObserver  implements GameObserver, Serializable {
    
    
    private final Map<Room, Function<GameDesc, String>> stateDescr = new HashMap<>();
    
    /**
     *
     * @param game
     */
    public LookAtObserver(GameDesc game) {
        stateDescr.put(game.getRoomByName("ZVEZDA"), this::zvezdaDescr);
        /*stateDescr.put(game.getRoomByName("ZARYA"), this::zaryaDescr);
        stateDescr.put(game.getRoomByName("UNITY"), this::unityDescr);
        stateDescr.put(game.getRoomByName("DESTINY"), this::destinyDescr);
        stateDescr.put(game.getRoomByName("TRANQUILITY"), this::tranquilityDescr);
        stateDescr.put(game.getRoomByName("QUEST"), this::questDescr);
        stateDescr.put(game.getRoomByName("HARMONY"), this::harmonyDescr);
        stateDescr.put(game.getRoomByName("LEONARDO"), this::leonardoDescr);*/
    }
    
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
            if(parserOutput.getObject() != null && game.getCurrentRoom()
                    .getObjects().contains(parserOutput.getObject())){
                if(parserOutput.getInvObject() != null){
                    lookAtmsg.append("Provi a osservare due cose alla volta, ma il tuo cervello va in tilt."
                            + "Meglio uno alla volta, fidati. Intanto ti mostro solo il primo oggetto...");
                }
                lookAtmsg.append(parserOutput.getObject().getDescription());
            }else if(parserOutput.getInvObject() != null){
                lookAtmsg.append(parserOutput.getInvObject().getDescription());
            }else{
                lookAtmsg.append(game.getCurrentRoom().getLook()).append("\n")
                        .append(stateDescr.get(game.getCurrentRoom()).apply(game));
            }
        }
        return lookAtmsg.toString();
    }
    
    public String zvezdaDescr(GameDesc game) {
        String msg;
        
        if(game.getCurrentRoom().getObject(0) != null)
            msg = """
                  Tutto è in ordine tranne per uno strano 
                  dettaglio… sembra, un pezzo di modellino 
                  che galleggia in aria?""";
        else
            msg = """
                  Tutto è in ordine.""";
        
        return msg;
    }
    
    
    /*
    * Da completare - Lorenzo
    public String zaryaDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String unityDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String destinyDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String tranquilityDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String questDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String harmonyDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    
    public String leonardoDescr(GameDesc game) {
        String msg;
        
        return msg;
    }
    */
}
