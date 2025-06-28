/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lorenzopeluso
 */
public class Inventory {
    
    private static final Inventory instance = new Inventory();
    private List<BDObject> list = new ArrayList<>();

    public Inventory() {
    }
    
    
    public static Inventory getInstance() {
        return instance;
    }
    
    /**
     *
     * @return
     */
    public List<BDObject> getList() {
        return list;
    }

    /**
     *
     * @param list
     */
    public void setList(List<BDObject> list) {
        this.list = list;
    }

    /**
     *
     * @param o
     */
    public void add(BDObject o) {
        list.add(o);
    }

    /**
     *
     * @param o
     */
    public void remove(BDObject o) {
        list.remove(o);
    }   
    
}