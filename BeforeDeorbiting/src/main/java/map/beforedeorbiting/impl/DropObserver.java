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
 *
 * @author ronzu
 */

public class DropObserver implements GameObserver, Serializable {
    
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder dropmsg = new StringBuilder();
        if(parserOutput.getCommand().getType() == CommandType.DROP){
            if(parserOutput.getObject().isPickupable()){
                game.getInventory().remove(parserOutput.getObject());
                if(game.getCurrentRoom().getName().equalsIgnoreCase("unity")){
                    // mette nella chest
                    dropmsg.append("Hai lasciato ").append(parserOutput.getObject().getName()).append(" nella cassa!");
                }else{
                    game.getCurrentRoom().addObject(parserOutput.getObject());
                    //CONTINUARE
                }
            }else{
                dropmsg.append("Non puoi prendere questo oggetto, figurati lasciarlo...");
            }
        }
        return dropmsg.toString();
    }
    
}
