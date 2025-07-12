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
 * @author lorenzopeluso
 */
public class WaitObserver implements GameObserver {
    
    @Override
    public String update(GameDesc game, ParserOutput parserOutput){
        StringBuilder waitmsg = new StringBuilder();

        if (parserOutput.getCommand()!= null && parserOutput
                .getCommand().getType() == CommandType.WAIT
                && game.getCurrentRoom().equals(game.getRoomByName("DESTINY"))) {   
            game.getCurrentRoom().setRoomImage("src/main/resources/img/destiny_luce_oblo.png");
            game.getRoomByName("DESTINY").setVisible(true);
            waitmsg.append("Dopo qualche tempo la stazione avanza nella sua orbita e la luce "
                    + "del sole riflessa sulla Terra ti permette di vedere meglio.");
        } else if (parserOutput.getCommand()!= null && parserOutput
                .getCommand().getType() == CommandType.WAIT
                && !game.getCurrentRoom().equals(game.getRoomByName("DESTINY"))) {
            waitmsg.append("Aspettando qui non arriverà il principe azzurro.");
        }
        return waitmsg.toString();      
    };
    
}
