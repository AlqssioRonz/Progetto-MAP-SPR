/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.GameDesc;


/**
 * Questa classe rappresenta l'observer del comando 'INVENTORY', 
 * pemette di implementare il comando visualizzando i contenuti dell'inventario.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * 
 * @author lorenzopeluso
 */
public class InventoryObserver implements GameObserver, Serializable {
    
    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     * 
    * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
    * @param parserOutput l'output del parser utile per conoscere l'input dell'utente
    * @return il messaggio di risposta in base all'azione di 'inventario'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        
        StringBuilder inventoryMessage = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.INVENTORY) {
            if (game.getInventory().getList().isEmpty()) {
                inventoryMessage.append("Guardi nel tuo inventario spaziale, ma con tua sorpresa non ci sono nemmeno le caramelle della Nonna!");
            } else {
                inventoryMessage.append("Ecco cosa hai nel tuo inventario :\n");
                for (BDObject o : game.getInventory().getList()) {
                    inventoryMessage.append(o.getName()).append(" -> ").append(o.getDescription()).append("\n");
                }
            }
        }
        
        return inventoryMessage.toString();
    }
    
}
