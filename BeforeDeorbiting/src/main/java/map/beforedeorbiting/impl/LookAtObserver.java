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
        stateDescr.put(game.getRoomByName("ZARYA"), this::zaryaDescr);
        stateDescr.put(game.getRoomByName("UNITY"), this::unityDescr);
        stateDescr.put(game.getRoomByName("DESTINY"), this::destinyDescr);
        stateDescr.put(game.getRoomByName("TRANQUILITY"), this::tranquilityDescr);
        stateDescr.put(game.getRoomByName("QUEST"), this::questDescr);
        stateDescr.put(game.getRoomByName("HARMONY"), this::harmonyDescr);
        stateDescr.put(game.getRoomByName("LEONARDO"), this::leonardoDescr);
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
    
    
    public String zaryaDescr(GameDesc game) {
        String msg;
        
        //id = 10 spaceSuit
        if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(10))) {
            if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(11))) {
                msg = """
                      Luke è seduto contro il muro, immobile. La visiera riflette 
                      la luce, ma non si muove.
                      Era il mio migliore amico. è probabilmente morto per mancanza 
                      di ossigeno, come sarà mai potuto succedere… stringe un 
                      taccuino tra le sue mani""";
            } else {
                msg = """
                      Luke è seduto contro il muro, immobile. La visiera riflette 
                      la luce, ma non si muove.
                      Era il mio migliore amico. è probabilmente morto per mancanza 
                      di ossigeno, come sarà mai potuto succedere… """;
            }
        } else {
            if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(11))) {
                msg = """
                      Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta 
                      mi rivoltava lo stomaco. Il suo taccuino galleggia in aria.""";
            } else {
                msg = """
                      Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta 
                      mi rivoltava lo stomaco""";
            }
        }
        
        return msg;
    }
    
    public String unityDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();
        
        if(game.getRoomByName("DESTINY").isAccessible()) {
            msg.append("La porta davanti a me è aperta, posso procedere verso Destiny.\n");
        } else {
            msg.append("""
                       La porta davanti, quella che porta al laboratorio Destiny è 
                       chiusa con tastierino numerico: serve un codice per aprirla.""");
        }
        if(game.getRoomByName("LEONARDO").isAccessible()) {
            msg.append("""
                       La botola sotto i miei piedi che conduce nel modulo Leonardo 
                       è aperta, ma non vorrei rivedere il cadavere di Susan. """);
        } else {
            msg.append("La botola sotto i miei piedi che conduce nel modulo Leonardo è chiuso.");
        }
        if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(1))) {
            msg.append("Sul pavimento galleggia un piccolo oggetto scuro difficile "
                    + "non vederlo in un modulo così piccola");
        }
        
        return msg.toString();
    }
    
    public String destinyDescr(GameDesc game) {
        String msg;
        if(game.getCurrentRoom().isVisible()){
            msg = """
                  Un laboratorio sporco e silenzioso. Monitor spenti, attrezzi 
                  fluttuanti, l’aria di qualcosa lasciato a metà. Dal pavimento, 
                  un oblò mostra la Terra.""";
        } else {
            msg = "Questa stanza è troppo buia, non riesco a orientarmi";
        }
        return msg;
    }
    
    public String tranquilityDescr(GameDesc game) {
        String msg;
        //id 2 modellinodx
        if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(2))) {
            msg = """
                  In mezzo alla calma, qualcosa di minuscolo fluttua davanti 
                  all’oblò centrale: un frammento rigido, rettangolare, troppo 
                  ordinato per essere solo un detrito.""";
        } else {
            msg = "";
        }
        return msg;
    }

    public String questDescr(GameDesc game) {
        String msg;
        if(game.getCurrentRoom().getObjects().contains(game.getObjectByID(3))) {
            msg = """
                  Uno strano e piccolo oggetto fluttua davanti alla botola.""";
        } else {
            msg = "";
        }
        return msg;
    }
    
    public String harmonyDescr(GameDesc game) {
        String msg;
        msg = "";
        return msg;
    }
    
    public String leonardoDescr(GameDesc game) {
        String msg;
        msg = "";
        return msg;
    }
}
