/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;


import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;


/**
 *
 * @author lorenzopeluso
 */
public class Command {
    
    private final CommandType type;
    
    private final String name;
    
    private Set<String> alias;

    public Command(CommandType type, String name) {
        this.type = type;
        this.name = name;
        this.alias = new HashSet<>();
    }

    public Command(CommandType type, String name, Set<String> alias) {
        this.type = type;
        this.name = name;
        this.alias = alias;
    }

    public CommandType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

}
