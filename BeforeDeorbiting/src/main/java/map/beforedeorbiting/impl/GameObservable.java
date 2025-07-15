/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package map.beforedeorbiting.impl;

/**
 * Interfaccia per un oggetto osservabile nel gioco. Consente di registrare e
 * rimuovere {@link GameObserver} e di notificare tutti gli osservatori
 * registrati quando si verifica un evento di gioco rilevante.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public interface GameObservable {

    /**
     * Registra un nuovo osservatore interessato agli eventi del gioco.
     *
     * @param o l'osservatore da aggiungere (non null)
     */
    void attach(GameObserver o);

    /**
     * Rimuove un osservatore precedentemente registrato.
     *
     * @param o l'osservatore da rimuovere (non null)
     */
    void detach(GameObserver o);

    /**
     * Notifica tutti gli osservatori registrati riguardo a un evento del gioco.
     * Viene invocato tipicamente quando lo stato di gioco cambia, affinché ogni
     * {@link GameObserver} possa reagire di conseguenza.
     */
    void notifyObservers();

}
