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
     * lasciare l'oggetto desiderato e inserirlo nella stanza o nella cassa
     * della stanza. Per farlo, implmenta l'interfaccia GameObserver.
     *
     * @author ronzu
     */
    public class DropObserver implements GameObserver, Serializable {

        /**
         * Aggiorna lo stato del gioco in base all'output del parser e
         * restituisce un messaggio di risposta. Se il comando è stato eseguito
         * correttamente, l'oggetto viene aggiunto agli oggetti della stanza in
         * cui si trova il giocatore e rimosso dal suo inventario, assieme ad un
         * messaggio con la conferma dell'operazione. Ci sono diversi possibili
         * scenari in cui esegueil comando: L'oggetto non può essere raccolto:
         * viene mostrato un messaggio di errore Se la stanza contiene una
         * cassa: viene mostrato una messaggio differente e lascia l'oggetto
         * nella prima cassa disponibile.
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
                if (parserOutput.getInvObject() != null) {
                    if (parserOutput.getInvObject().isPickupable()) {
                        if (parserOutput.getObject() instanceof BDObjectChest) {
                            //Per controllare che la stanza contenga una cassa
                            for (BDObject o : game.getCurrentRoom().getObjects()) {
                                if (o instanceof BDObjectChest bDObjectChest) {
                                    bDObjectChest.add(parserOutput.getInvObject());
                                    dropmsg.append("Hai lasciato ").append(parserOutput.getInvObject().getName()).append(" nella cassa!");
                                    break; //In questo modo, se dovessero essere creati più oggetti contenitori, prende il primo
                                }
                            }
                        } else {
                            game.getCurrentRoom().getObjects().add((parserOutput.getInvObject()));
                            dropmsg.append("Hai lasciato ").append(parserOutput.getInvObject().getName()).append("!"
                                    + "In caso volessi riprendere ").append(parserOutput.getInvObject().getName()).append(", lo ritrovi qui!");
                        }
                        game.getInventory().remove(parserOutput.getInvObject());
                        InventoryUI.updateInventory(game);
                    } else {
                        dropmsg.append("Non puoi prendere questo oggetto, figurati lasciarlo...");
                    }
                } else {
                    dropmsg.append("Stai provando a lasciare qualcosa che non hai in inventario..."
                            + "tipo la minima idea di cosa stai facendo. Ma continua pure a girare in orbita, l’universo è grande e la fortuna cieca.");
                }
            }
            return dropmsg.toString();
        }
    }
