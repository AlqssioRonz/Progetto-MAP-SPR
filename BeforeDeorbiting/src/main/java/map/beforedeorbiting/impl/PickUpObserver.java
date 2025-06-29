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
 * Questa classe rappresenta l'observer del comando 'PRENDI',
 * permette di prendere l'oggetto desiderato e inserirlo nell'inventario.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * @author ronzu
 */
public class PickUpObserver implements GameObserver, Serializable {
    
    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce un messaggio di risposta.
     * Se il comando è stato eseguito correttamente, l'oggetto viene aggiunto all'inventario
     * e rimosso dalla stanza in cui si trova il giocatore, assieme ad un messaggio con la conferma dell'operazione.
     * Ci sono diversi possibili scenari in cui esegue il comando:
     * L'oggetto non può essere raccolto: viene mostrato un messaggio di errore
     * Se la stanza corrente è vuota: viene mostrata una messaggio di warning
     * 
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
     * @param parserOutput l'output del parser utile per conoscere l'input dell'utente
     * @return il messaggio di risposta in base all'azione di 'prendi'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder pickUpmsg = new StringBuilder();
        if(parserOutput.getCommand().getType() == CommandType.PICK_UP){
            if(!game.getCurrentRoom().getObjects().contains(parserOutput.getObject())){
                pickUpmsg.append("Sono contento che tu voglia raccoglierlo, ma non si trova qui! Magari dovresti provare in un altro modulo...");
            }else{
                if(parserOutput.getObject().isPickupable()){
                    game.getInventory().add(parserOutput.getObject());
                    game.getCurrentRoom().getObjects().remove(parserOutput.getObject());
                    if(parserOutput.getObject().getName().equalsIgnoreCase("tuta")){
                        pickUpmsg.append("""
                                         Strappo la tuta spaziale dal corpo freddo e rigido di Luke.
                                         La bombola dell'ossigeno... è danneggiata. Ho solo due opzioni.
                                         Posso restare qui, al sicuro, e lasciare che Susan affronti da sola qualunque incubo si nasconda in questa nave
                                         o posso trattenere il fiato e tentare la traversata nello spazio.
                                         Non so se Susan sia ancora viva. Ma nella mia testa... so già cosa devo fare.
                                         """);
                    }
                    //Quando raccogli il diario di Susan c'è un altro messaggio del genere (forse anche altre volte)
                    pickUpmsg.append("Hai preso: ").append(parserOutput.getObject().getName()).append(" e si trova nel tuo inventario!");
                }else{
                    pickUpmsg.append("Purtroppo questo oggetto non può essere raccolto! Magari riflettici un paio di volte prima di farlo");
                }
            }
        }
        return pickUpmsg.toString();
    }
    
}
