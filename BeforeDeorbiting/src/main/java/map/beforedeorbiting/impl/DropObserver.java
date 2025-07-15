/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.BDObjectChest;
import map.beforedeorbiting.ui.InventoryUI;

/**
 * Questa classe rappresenta l'observer del comando 'LASCIA', permette di
 * lasciare l'oggetto desiderato e inserirlo nella stanza o nella cassa della
 * stanza. Per farlo, implmenta l'interfaccia GameObserver.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class DropObserver implements GameObserver, Serializable {

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta. Se il comando è stato eseguito correttamente,
     * l'oggetto viene rimosso dall'inventario e aggiunto o alla cassa (se
     * presente) o alla stanza corrente, con messaggio di conferma.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di 'lascia'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder dropmsg = new StringBuilder();

        if (parserOutput.getCommand().getType() == CommandType.DROP) {
            BDObject invObj = parserOutput.getInvObject();
            if (invObj == null) {
                dropmsg.append("Stai provando a lasciare qualcosa che non hai in inventario... "
                        + "tipo la minima idea di cosa stai facendo. Ma continua pure a girare in orbita, "
                        + "l’universo è grande e la fortuna cieca.");
                return dropmsg.toString();
            }

            // Cerco la prima cassa nella stanza
            BDObjectChest chest = null;
            for (BDObject o : game.getCurrentRoom().getObjects()) {
                if (o instanceof BDObjectChest) {
                    chest = (BDObjectChest) o;
                    break;
                }
            }

            // Se c'è una cassa, deposito lì
            if (chest != null) {
                game.getInventory().remove(invObj);
                chest.add(invObj);
                dropmsg.append("Hai messo ").append(invObj.getName()).append(" nella cassa!");
                InventoryUI.updateInventory(game);
                if (!chest.getList().isEmpty()) {
                    StringBuilder objectChest = new StringBuilder();
                    for (BDObject objChest : chest.getList()) {
                        objectChest.append(objChest.getName()).append(" -> ").append(objChest.getDescription()).append("\n");
                    }
                    chest.setDescription("Permette di contenere vari oggetti.\nAl momento ci trovi: \n" + objectChest.toString());
                } else {
                    chest.setDescription("Permette di contenere vari oggetti.");
                }

                return dropmsg.toString();
            }
            // Altrimenti lo lascio semplicemente nella stanza
            game.getInventory().remove(invObj);
            game.getCurrentRoom().getObjects().add(invObj);
            dropmsg.append("Hai lasciato ").append(invObj.getName())
                    .append(" nella stanza. In caso volessi riprenderlo, lo ritrovi qui!");
            InventoryUI.updateInventory(game);
        }

        return dropmsg.toString();
    }
}
