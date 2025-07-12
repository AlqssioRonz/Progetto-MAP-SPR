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
import map.beforedeorbiting.type.CommandType;

import java.io.Serializable;
import java.io.PrintStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import map.beforedeorbiting.ui.GameUI;

/**
 *
 * @author lorenzopeluso
 */
public abstract class GameDesc implements Serializable {

    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private final List<BDObject> listObj = new ArrayList<>();
    
    private String notebookText = "";
    
    private Command lastCommand;

    private Inventory inventory = Inventory.getInstance();

    private Room currentRoom;
    
    private final Duration duration = Duration.ofSeconds(100);

    public Command getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(Command lastCommand) {
        this.lastCommand = lastCommand;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Command> getCommands() {
        return commands;
    }
    
    /* Inizializza il gioco */
    public abstract void init();

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
     * Metodi astratti per generalizzare GameDesc come template per una
     * avventura testuale
     *
     * @param p
     * @param out
     */
    public abstract void nextMove(ParserOutput p, PrintStream out);

    public String getNotebookText() {
        return notebookText;
    }

    public void setNotebookText(String notebookText) {
        this.notebookText = notebookText;
    }

    public abstract String getWelcomeMessage();
    

    public BDObject getObjectByID(int id) {
        return listObj.stream()
                .filter(obj -> obj.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public int count(BDObject target) {
        int id = target.getId();

        return (int) listObj.stream()
                .filter(obj -> obj.getId() == id)
                .count();
    }

    public Room getRoomByName(String name) {

        return rooms.stream()
                .filter(room -> room.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
