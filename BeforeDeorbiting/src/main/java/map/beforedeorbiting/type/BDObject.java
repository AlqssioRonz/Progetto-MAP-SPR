/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta un oggetto di gioco identificato da un ID univoco, con nome,
 * descrizione, alias e proprietà di utilizzo all’interno del gioco. Può essere
 * raccolto (pickupable), utilizzato (usable) o essere in uso (inUse).
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class BDObject {

    private final int id;
    private String name;
    private String description;
    private Set<String> alias = new HashSet<>();
    private boolean pickupable = false;
    private boolean usable = false;
    private boolean inUse = false;

    /**
     * Imposta il nome dell’oggetto.
     *
     * @param name il nuovo nome (non null)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Imposta la descrizione testuale dell’oggetto.
     *
     * @param description la descrizione (può essere null o vuota)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Costruisce un oggetto con solo l’ID.
     *
     * @param id l’identificativo univoco dell’oggetto
     */
    public BDObject(int id) {
        this.id = id;
    }

    /**
     * Costruisce un oggetto con ID, nome e descrizione.
     *
     * @param id l’identificativo univoco
     * @param name il nome dell’oggetto (non null)
     * @param description la descrizione dell’oggetto (può essere null)
     */
    public BDObject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Costruisce un oggetto con ID, nome, descrizione e alias.
     *
     * @param id l’identificativo univoco
     * @param name il nome dell’oggetto (non null)
     * @param description la descrizione (può essere null)
     * @param alias insieme di nomi alternativi per l’oggetto
     */
    public BDObject(int id, String name, String description, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
    }

    /**
     * Sostituisce l’insieme di alias con un nuovo insieme.
     *
     * @param alias i nuovi alias (non null)
     */
    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    /**
     * Marca l’oggetto come raccoglibile o meno.
     *
     * @param pickupable true se può essere raccolto, false altrimenti
     */
    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    /**
     * Marca l’oggetto come utilizzabile o meno.
     *
     * @param usable true se è utilizzabile, false altrimenti
     */
    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    /**
     * Restituisce l’ID univoco dell’oggetto.
     *
     * @return l’identificativo (immutabile)
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il nome corrente dell’oggetto.
     *
     * @return il nome (può essere null se non impostato)
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce la descrizione dell’oggetto.
     *
     * @return la descrizione (può essere null)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Restituisce l’insieme di alias associati all’oggetto.
     *
     * @return un Set di stringhe, non null
     */
    public Set<String> getAlias() {
        return alias;
    }

    /**
     * Indica se l’oggetto è raccoglibile nel gioco.
     *
     * @return true se raccoglibile, false altrimenti
     */
    public boolean isPickupable() {
        return pickupable;
    }

    /**
     * Indica se l’oggetto è utilizzabile nel gioco.
     *
     * @return true se utilizzabile, false altrimenti
     */
    public boolean isUsable() {
        return usable;
    }

    /**
     * Indica se l’oggetto è attualmente in uso.
     *
     * @return true se in uso, false altrimenti
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * Imposta lo stato “in uso” dell’oggetto.
     *
     * @param inUse true per indicare che è in uso, false altrimenti
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    /**
     * Confronta questo oggetto con un altro basato sull’ID.
     *
     * @param obj l’oggetto da confrontare
     * @return true se l’altro è un BDObject con lo stesso ID, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BDObject other = (BDObject) obj;
        return this.id == other.id;
    }

    /**
     * Calcola l’hash code basato sull’ID (necessario se si override equals).
     *
     * @return l’hash code dell’ID
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
