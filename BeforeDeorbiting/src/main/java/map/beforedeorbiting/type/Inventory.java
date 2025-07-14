/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton che rappresenta l’inventario del giocatore, contenente gli oggetti
 * raccolti durante l’avventura. Fornisce metodi per accedere, aggiungere,
 * rimuovere e contare gli oggetti presenti.
 *
 * @author lorenzopeluso
 */
public class Inventory {

    private static final Inventory instance = new Inventory();
    private List<BDObject> list = new ArrayList<>();

    /**
     * Costruttore privato per implementare il pattern singleton.
     */
    private Inventory() {
    }

    /**
     * Restituisce l’istanza unica di Inventory.
     *
     * @return l’unico oggetto Inventory
     */
    public static Inventory getInstance() {
        return instance;
    }

    /**
     * Restituisce la lista di oggetti presenti nell’inventario.
     *
     * @return lista di {@link BDObject}
     */
    public List<BDObject> getList() {
        return list;
    }

    /**
     * Sostituisce completamente il contenuto dell’inventario.
     *
     * @param list nuova lista di {@link BDObject}
     */
    public void setList(List<BDObject> list) {
        this.list = list;
    }

    /**
     * Aggiunge un oggetto all’inventario.
     *
     * @param o l’oggetto da aggiungere (non null)
     */
    public void add(BDObject o) {
        list.add(o);
    }

    /**
     * Rimuove un oggetto dall’inventario.
     *
     * @param o l’oggetto da rimuovere (se presente)
     */
    public void remove(BDObject o) {
        list.remove(o);
    }

    /**
     * Conta quante volte un dato oggetto (stesso ID) compare nell’inventario.
     *
     * @param target oggetto di riferimento
     * @return numero di occorrenze del medesimo ID
     */
    public int count(BDObject target) {
        int id = target.getId();
        return (int) list.stream()
                .filter(obj -> obj.getId() == id)
                .count();
    }

}
