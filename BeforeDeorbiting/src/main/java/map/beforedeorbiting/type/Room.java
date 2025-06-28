/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;

import map.beforedeorbiting.type.BDObject;

/**
 *
 * @author lorenzopeluso
 * @author alessioronzullo
 * 
 */
public class Room {
    
    private final int id;

    private String name;

    private String description;

    private String look;
    
    private String history;

    private boolean visible = true;
    
    private boolean accessible = true;

    private Room forward = null;

    private Room aft = null;

    private Room starbord = null;

    private Room port = null;
    
    private Room overhead = null;
    
    private Room deck = null;

    private final List<BDObject> objects = new ArrayList<>();

    /**
     * 
     * @param id 
     */
    public Room(int id) {
        this.id = id;
    }

    /**
     * 
     * @param id
     * @param name
     * @param description 
     */
    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    

    /**
     * 
     * @param id
     * @param name
     * @param description
     * @param look 
     */
    public Room(int id, String name, String description, String look) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.look = look;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }
    
    public void setHistory(String history) {
        this.history = history;
    }
    
    public String getHistory() {
        return this.history;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public Room getForward() {
        return forward;
    }

    public void setForward(Room forward) {
        this.forward = forward;
    }

    public Room getAft() {
        return aft;
    }

    public void setAft(Room aft) {
        this.aft = aft;
    }

    public Room getStarbord() {
        return starbord;
    }

    public void setStarbord(Room starbord) {
        this.starbord = starbord;
    }

    public Room getPort() {
        return port;
    }

    public void setPort(Room port) {
        this.port = port;
    }

    public Room getOverhead() {
        return overhead;
    }

    public void setOverhead(Room overhead) {
        this.overhead = overhead;
    }

    public Room getDeck() {
        return deck;
    }

    public void setDeck(Room deck) {
        this.deck = deck;
    }
    
    /**
     * 
     * @return objects
     */
    public List<BDObject> getObjects(){
        return objects;
    }
    
    public void addObject(BDObject o){
        objects.add(o);
    }
    
    public void removeObject(BDObject o){
        objects.remove(o);
    }
    
    public BDObject getObject(int id){
        for (BDObject o : objects) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }
      
    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
}
