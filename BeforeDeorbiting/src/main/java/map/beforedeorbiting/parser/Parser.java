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
 * Parser che interpreta l’input testuale dell’utente, rimuove le stopwords
 * e identifica comandi e oggetti sia nell’ambiente sia nell’inventario.
 * Fornisce il risultato dell’analisi in un {@link ParserOutput}.
 * 
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class Parser {

    private final Set<String> stopwords;

    /**
     * Costruisce un parser con il set di stopwords da escludere.
     *
     * @param stopwords insieme di parole da ignorare durante il parsing
     */
    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    /**
     * Controlla se un token corrisponde a un comando disponibile.
     *
     * @param token    la parola da verificare
     * @param commands lista dei comandi conosciuti
     * @return l’indice del comando trovato oppure -1 se non esiste
     */
    private int checkForCommand(String token, List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equals(token)
                    || commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifica se il token corrisponde a un oggetto disponibile.
     *
     * @param token   il testo da controllare
     * @param objects lista di oggetti nell’ambiente
     * @return l’indice dell’oggetto trovato oppure -1 se non presente
     */
    private int checkForObject(String token, List<BDObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getName().equals(token)
                    || objects.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Analizza l’input dell’utente, identifica il comando e
     * opzionalmente un oggetto nell’ambiente o nell’inventario.
     *
     * @param input     la stringa digitata dall’utente
     * @param commands  lista di comandi possibili
     * @param objects   lista di oggetti presenti nella stanza
     * @param inventory lista di oggetti nell’inventario
     * @return un {@link ParserOutput} contenente il comando,
     *         l’oggetto ambiente (o null) e l’oggetto inventario (o null),
     *         oppure null se la stringa dopo il filtraggio è vuota
     */
    public ParserOutput parse(String input,
            List<Command> commands,
            List<BDObject> objects,
            List<BDObject> inventory) {

        List<String> tokens = parseString(input, stopwords);
        if (tokens.isEmpty()) {
            return null;
        }

        Command command = null;
        BDObject object = null;
        BDObject inventoryObject = null;
        int commandIndex = -1, commandTokenIndex = -1;
        boolean found = false;

        // Cerca il comando tra i token
        for (int i = 0; i < tokens.size() && !found; i++) {
            commandIndex = checkForCommand(tokens.get(i), commands);
            if (commandIndex != -1) {
                commandTokenIndex = i;
                found = true;
            }
        }

        if (commandIndex == -1) {
            // Nessun comando valido trovato
            return new ParserOutput(null, null);
        }

        command = commands.get(commandIndex);

        // Ricerca di oggetti ambiente e inventario
        for (int i = 0; i < tokens.size(); i++) {
            if (i != commandTokenIndex && object == null) {
                int idx = checkForObject(tokens.get(i), objects);
                if (idx != -1)
                    object = objects.get(idx);
            }
            if (i != commandTokenIndex && inventoryObject == null) {
                int idx = checkForObject(tokens.get(i), inventory);
                if (idx != -1)
                    inventoryObject = inventory.get(idx);
            }
        }

        return new ParserOutput(command, object, inventoryObject);
    }

    /**
     * Divide una stringa in token separati da spazi e filtra le stopword.
     *
     * @param string    la stringa di input
     * @param stopwords l’insieme di parole da escludere
     * @return lista di token utili all’analisi
     */
    public static List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        for (String t : string.toLowerCase().split("\\s+")) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }
}
