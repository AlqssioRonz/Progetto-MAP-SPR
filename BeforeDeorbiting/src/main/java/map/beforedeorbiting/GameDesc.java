/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting;

import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.Command;
import map.beforedeorbiting.type.Room;
import map.beforedeorbiting.type.Inventory;
import map.beforedeorbiting.parser.ParserOutput;

import java.io.Serializable;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lorenzopeluso
 */
public abstract class GameDesc implements Serializable {
    
    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private final List<BDObject> listObj = new ArrayList<>();
    
    private Inventory inventory =  Inventory.getInstance();

    private Room currentRoom;

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<BDObject> getListObj() {
        return listObj;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
    
    /**
     * Metodi astratti per generalizzare GameDesc come
     * template per una avventura testuale
     * @param p
     * @param out 
     */
    public abstract void nextMove(ParserOutput p, PrintStream out);
    
    public abstract String getWelcomeMessage();
    
}
