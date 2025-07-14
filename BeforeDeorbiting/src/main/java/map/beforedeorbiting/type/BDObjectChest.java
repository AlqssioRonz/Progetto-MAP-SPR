/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Estende {@link BDObject} per rappresentare un contenitore (cassettina) che
 * può contenere più oggetti di gioco. Fornisce metodi per gestire il contenuto
 * interno (lista di {@link BDObject}).
 *
 * @author lorenzopeluso
 */
public class BDObjectChest extends BDObject {

    private List<BDObject> content = new ArrayList<>();

    /**
     * Costruisce un cassettina vuota con solo l’ID.
     *
     * @param id l’identificativo univoco del contenitore
     */
    public BDObjectChest(int id) {
        super(id);
    }

    /**
     * Costruisce un cassettina con ID, nome e descrizione.
     *
     * @param id l’identificativo univoco del contenitore
     * @param name il nome da associare al contenitore
     * @param description la descrizione del contenitore
     */
    public BDObjectChest(int id, String name, String description) {
        super(id, name, description);
    }

    /**
     * Costruisce un cassettina con ID, nome, descrizione e alias.
     *
     * @param id l’identificativo univoco del contenitore
     * @param name il nome da associare al contenitore
     * @param description la descrizione del contenitore
     * @param alias insieme di nomi alternativi per il contenitore
     */
    public BDObjectChest(int id, String name, String description, Set<String> alias) {
        super(id, name, description, alias);
    }

    /**
     * Restituisce la lista degli oggetti contenuti.
     *
     * @return lista di {@link BDObject} contenuti nel cassettina
     */
    public List<BDObject> getList() {
        return content;
    }

    /**
     * Sostituisce completamente il contenuto del cassettina.
     *
     * @param list nuova lista di {@link BDObject} da inserire
     */
    public void setList(List<BDObject> list) {
        this.content = list;
    }

    /**
     * Aggiunge un oggetto al contenuto del cassettina.
     *
     * @param o l’oggetto da aggiungere (non null)
     */
    public void add(BDObject o) {
        content.add(o);
    }

    /**
     * Rimuove un oggetto dal contenuto del cassettina.
     *
     * @param o l’oggetto da rimuovere (se presente)
     */
    public void remove(BDObject o) {
        content.remove(o);
    }

}
