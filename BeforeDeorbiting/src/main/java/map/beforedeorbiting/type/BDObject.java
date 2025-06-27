/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.Set;


/**
 *
 * @author lorenzopeluso
 */
public class BDObject {
    
    private final int id;
    
    private String name;
    
    private String description;
    
    private Set<String> alias;
    
    private boolean pickupable = false;
    
    private boolean usable = false;
    
    /**
     * 
     * @param id
     * @param name
     * @param description 
     */
    public BDObject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * 
     * @param id
     * @param name
     * @param description
     * @param alias 
     */
    public BDObject(int id, String name, String description, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
    }
    
    /**
     * 
     * @param alias 
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     * 
     * @param pickupable 
     */
    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    /**
     * 
     * @param usable 
     */
    public void setUsable(boolean usable) {
        this.usable = usable;
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public boolean isUsable() {
        return usable;
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
        final BDObject other = (BDObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
  
}
