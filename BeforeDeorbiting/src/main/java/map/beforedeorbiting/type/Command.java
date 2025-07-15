/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Rappresenta un comando di gioco, identificato da un tipo {@link CommandType},
 * un nome principale e un insieme di alias per riconoscere varianti testuali
 * del comando stesso.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class Command {

    private final CommandType type;
    private final String name;
    private Set<String> alias;

    /**
     * Costruisce un comando con tipo e nome, senza alias.
     *
     * @param type il tipo di comando (non null)
     * @param name la stringa primaria che rappresenta il comando (non null)
     */
    public Command(CommandType type, String name) {
        this.type = type;
        this.name = name;
        this.alias = new HashSet<>();
    }

    /**
     * Costruisce un comando con tipo, nome e alias.
     *
     * @param type il tipo di comando (non null)
     * @param name la stringa primaria che rappresenta il comando (non null)
     * @param alias insieme di parole alternative per il comando (non null)
     */
    public Command(CommandType type, String name, Set<String> alias) {
        this.type = type;
        this.name = name;
        this.alias = alias;
    }

    /**
     * Restituisce il tipo di questo comando.
     *
     * @return il CommandType associato
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Restituisce il nome principale del comando.
     *
     * @return la stringa primaria del comando
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l’insieme degli alias riconosciuti per questo comando.
     *
     * @return un Set di stringhe alias (non null)
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Sostituisce la lista di alias con un nuovo array di varianti.
     *
     * @param alias array di stringhe da usare come alias (non null)
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     * Confronta questo comando con un altro basandosi sul tipo.
     *
     * @param obj l’oggetto da confrontare
     * @return true se l’altro è un Command con lo stesso tipo, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Command other = (Command) obj;
        return this.type == other.type;
    }

    /**
     * Calcola l’hash code in base al tipo di comando.
     *
     * @return l’hash code del tipo
     */
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
