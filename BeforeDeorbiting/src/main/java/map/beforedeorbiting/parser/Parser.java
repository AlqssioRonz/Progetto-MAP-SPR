/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.parser;

import map.beforedeorbiting.type.Command;
import map.beforedeorbiting.type.BDObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author lorenzopeluso
 */
public class Parser {

    private final Set<String> stopwords;

    /**
     *
     * @param stopwords
     */
    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    private int checkForCommand(String token, List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equals(token) || 
                    commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    private int checkForObject(String token, List<BDObject> obejcts) {
        for (int i = 0; i < obejcts.size(); i++) {
            if (obejcts.get(i).getName().equals(token) || 
                    obejcts.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param command
     * @param commands
     * @param objects
     * @param inventory
     * @return
     */
    public ParserOutput parse(String input, List<Command> commands, 
            List<BDObject> objects, List<BDObject> inventory) {
        
        List<String> tokens = parseString(input, stopwords);
        if (!tokens.isEmpty()) {
            
            Command command = null;
            BDObject object = null;
            BDObject inventoryObject = null;
            
            int commandIndex = -1;
            int commandTokenIndex = -1;
            boolean found = false;

            // Cerca il comando dinamicamente tra tutti i token
            for (int i = 0; i < tokens.size() && !found; i++) {
                commandIndex = checkForCommand(tokens.get(i), commands);
                if (commandIndex != -1) {
                    commandTokenIndex = i;
                    found = true;
                }
            }

            if (commandIndex == -1) {
                // Nessun comando trovato, restituisce un payload vuoto
                return new ParserOutput(null, null);
            }

            //altrimenti memorizza il comando
            command = commands.get(commandIndex);

            //ricerca degli oggetti
            for (int i = 0; i < tokens.size(); i++) {
                
                boolean sameToken = (i == commandTokenIndex);

                int objectIndex = -1;
                int inventoryIndex = -1;

                if (!sameToken && object == null) {
                    objectIndex = checkForObject(tokens.get(i), objects);
                    if (objectIndex != -1) {
                        object = objects.get(objectIndex);
                    }
                }

                if (!sameToken && inventoryObject == null) {
                    inventoryIndex = checkForObject(tokens.get(i), inventory);
                    if (inventoryIndex != -1) {
                        inventoryObject = inventory.get(inventoryIndex);
                    }
                }
            }
            return new ParserOutput(command, object, inventoryObject);
            //se ha trovato un comando, opzionalmente un oggetto, vengono restituiti nel payload
        }else{ 
            return null;
        }
    };
    
    /**
     *
     * @param string
     * @param stopwords
     * @return
     */
    public static List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }

}
