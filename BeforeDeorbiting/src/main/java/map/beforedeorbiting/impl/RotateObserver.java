/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;

/**
 *
 * @author ronzu
 */
public class RotateObserver implements GameObserver {

    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder rotatemsg = new StringBuilder();
        if (parserOutput.getCommand() != null && parserOutput
                .getCommand().getType() == CommandType.ROTATE && parserOutput.getObject() != null) {
            if (game.getCurrentRoom().getName().equals("DESTINY")
                    && game.getObjectByID(8).isInUse()) {
                if (game.getObjectByID(8) != null && parserOutput.getObject().equals(game.getObjectByID(8))) {
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/destiny_aperto.png");
                    rotatemsg.append("Il prisma ha riflesso tutta la luce verso il modulo di connessione, riattivando"
                            + " tutte le luci e aprendo la porta davanti a te.");
                    game.getObjectByID(8).setInUse(false);
                    game.getRoomByName("HARMONY").setAccessible(true);
                    game.getRoomByName("KIBO").setAccessible(true);
                } else {
                    rotatemsg.append("Cosa pensi di voler ruotare?");
                }
            } else {
                rotatemsg.append("Probabilmente non dovresti farlo qui... o in questo momento..");
            }
        }else if((parserOutput.getCommand() != null && parserOutput
                .getCommand().getType() == CommandType.ROTATE && parserOutput.getInvObject() == null)) {
            rotatemsg.append("Probabilmente non dovresti farlo qui... o in questo momento..");
        }
        return rotatemsg.toString();
    }
}
