/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author lorenzopeluso
 */
public class BDObjectChest extends BDObject{
    
    private List<BDObject> content = new ArrayList<>();
    
    /**
     * 
     * @param id 
     */
    public BDObjectChest(int id){
        super(id);
    }

    /**
     * 
     * @param id
     * @param name
     * @param description 
     */
    public BDObjectChest(int id, String name, String description) {
        super(id, name, description);
    }
    
    /**
     * 
     * @param id
     * @param name
     * @param description
     * @param alias 
     */
    public BDObjectChest(int id, String name, String description, Set<String> alias) {
        super(id, name, description, alias);
    }
    
    //list handling
    
    public List<BDObject> getList() {
        return content;
    }
    
    public void setList(List<BDObject> list) {
        this.content = list;
    }
    
    public void add(BDObject o) {
        content.add(o);
    }
    
    public void remove(BDObject o) {
        content.remove(o);
    }
    
}
