/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.GameDesc;


/**
 * Questa classe rappresenta l'observer del comando inventory, 
 * pemette di implementare il comando visualizzando i contenuti dell'inventario
 * 
 * @author lorenzopeluso
 */
public class InventoryObserver implements GameObserver {
    
    /**
     * 
     * @param game
     * @param parserOutput
     * @return 
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.INVENTORY) {
            if (game.getInventory().getList().isEmpty()) {
                msg.append("Guardi nel tuo inventario spaziale, ma con tua sorpresa non ci sono nemmeno le caramelle della Nonna!");
            } else {
                msg.append("Ecco cosa hai nel tuo inventario :\n");
                for (BDObject o : game.getInventory().getList()) {
                    msg.append(o.getName()).append(" -> ").append(o.getDescription()).append("\n");
                }
            }
        }
        
        return msg.toString();
    }
    
}
