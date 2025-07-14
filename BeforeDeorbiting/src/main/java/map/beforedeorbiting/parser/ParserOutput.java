/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.parser;

import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.Command;

/**
 * Rappresenta il risultato dell’analisi dell’input utente: contiene il comando
 * identificato, l’oggetto ambiente (se presente) e l’oggetto preso
 * dall’inventario (se presente).
 *
 * @author lorenzopeluso
 */
public class ParserOutput {

    private Command command;
    private BDObject object;
    private BDObject invObject;

    /**
     * Crea un ParserOutput con comando e oggetto ambiente.
     *
     * @param command il comando riconosciuto (può essere null)
     * @param object l’oggetto ambiente associato al comando (può essere null)
     */
    public ParserOutput(Command command, BDObject object) {
        this.command = command;
        this.object = object;
    }

    /**
     * Crea un ParserOutput con comando, oggetto ambiente e oggetto inventario.
     *
     * @param command il comando riconosciuto (può essere null)
     * @param object l’oggetto ambiente associato al comando (può essere null)
     * @param invObject l’oggetto prelevato dall’inventario (può essere null)
     */
    public ParserOutput(Command command, BDObject object, BDObject invObject) {
        this.command = command;
        this.object = object;
        this.invObject = invObject;
    }

    /**
     * Restituisce il comando identificato dal parser.
     *
     * @return il comando (null se non riconosciuto)
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Imposta il comando nel risultato del parser.
     *
     * @param command il comando da assegnare (può essere null)
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Restituisce l’oggetto ambiente rilevato.
     *
     * @return l’oggetto ambiente (null se non presente)
     */
    public BDObject getObject() {
        return object;
    }

    /**
     * Imposta l’oggetto ambiente nel risultato del parser.
     *
     * @param object l’oggetto da assegnare (può essere null)
     */
    public void setObject(BDObject object) {
        this.object = object;
    }

    /**
     * Restituisce l’oggetto rilevato nell’inventario.
     *
     * @return l’oggetto inventario (null se non presente)
     */
    public BDObject getInvObject() {
        return invObject;
    }

    /**
     * Imposta l’oggetto inventario nel risultato del parser.
     *
     * @param invObject l’oggetto da assegnare (può essere null)
     */
    public void setInvObject(BDObject invObject) {
        this.invObject = invObject;
    }
}
