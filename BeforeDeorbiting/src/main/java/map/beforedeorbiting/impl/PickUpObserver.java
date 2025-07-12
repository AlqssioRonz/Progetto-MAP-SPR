/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.ui.InventoryUI;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.BDObjectChest;

/**
 * Questa classe rappresenta l'observer del comando 'PRENDI', permette di
 * prendere l'oggetto desiderato (anche dalla cassa) e inserirlo
 * nell'inventario. Per farlo, implmenta l'interfaccia GameObserver.
 *
 * @author ronzu
 */
public class PickUpObserver implements GameObserver, Serializable {

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta. Se il comando è stato eseguito correttamente,
     * l'oggetto viene aggiunto all'inventario e rimosso dalla stanza in cui si
     * trova il giocatore o dalla cassa, assieme ad un messaggio di conferma
     * dell'operazione.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di 'prendi'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder pickUpmsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.PICK_UP) {
            BDObject target = parserOutput.getObject();
            if (target != null) {

                // 1) Se c'è una cassa nella stanza e l'oggetto è al suo interno,
                //    lo prendo direttamente dalla cassa
                for (BDObject obj : game.getCurrentRoom().getObjects()) {
                    if (obj instanceof BDObjectChest) {
                        BDObjectChest chest = (BDObjectChest) obj;
                        if (chest.getList().contains(target)) {
                            chest.remove(target);
                            game.getInventory().add(target);
                            pickUpmsg.append("Hai preso: ")
                                    .append(target.getName())
                                    .append(" dalla cassa.");
                            InventoryUI.updateInventory(game);
                            return pickUpmsg.toString();
                        }
                    }
                }

                // 2) Se l'oggetto non è nella stanza (e non era nella cassa)
                if (!game.getCurrentRoom().getObjects().contains(target)) {
                    pickUpmsg.append("Sono contento che tu voglia raccoglierlo, ma non si trova qui! "
                            + "Magari dovresti provare in un altro modulo...");
                } else {
                    if (parserOutput.getObject().isPickupable()) {
                        game.getInventory().add(parserOutput.getObject());
                        game.getCurrentRoom().getObjects().remove(parserOutput.getObject());
                        if (parserOutput.getObject().getId() == 10) {
                            pickUpmsg.append("Strappo la tuta spaziale dal corpo freddo e rigido di Luke."
                                    + "La bombola dell'ossigeno... è danneggiata. Ho solo due opzioni."
                                    + "Posso restare qui, al sicuro, e lasciare che Susan affronti da sola qualunque incubo si nasconda in questa nave"
                                    + "o posso trattenere il fiato e tentare la traversata nello spazio."
                                    + "Non so se Susan sia ancora viva. Ma nella mia testa... so già cosa devo fare.");
                            game.getRoomByName("ZARYA").setRoomImage("src/main/resources/img/zarya_chiusa_tuta_presa.jpeg");
                        }

                        pickUpmsg.append("Hai preso: ")
                                .append(target.getName())
                                .append(" e si trova nel tuo inventario!");
                        InventoryUI.updateInventory(game);
                    } else {
                        pickUpmsg.append("Purtroppo questo oggetto non può essere raccolto! "
                                + "Magari riflettici un paio di volte prima di farlo.");
                    }
                }
            } else {
                pickUpmsg.append("Cosa pensi di voler raccogliere?");
            }
        }
        return pickUpmsg.toString();
    }
}
