/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.parser;

import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.Command;

/**
 *
 * @author lorenzopeluso
 */
public class ParserOutput {

    private Command command;

    private BDObject object;
    
    private BDObject invObject;

    /**
     *
     * @param command
     * @param object
     */
    public ParserOutput(Command command, BDObject object) {
        this.command = command;
        this.object = object;
    }

    /**
     *
     * @param command
     * @param object
     * @param invObejct
     */
    public ParserOutput(Command command, BDObject object, BDObject invObejct) {
        this.command = command;
        this.object = object;
        this.invObject = invObejct;
    }

    /**
     *
     * @return
     */
    public Command getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     *
     * @return
     */
    public BDObject getObject() {
        return object;
    }

    /**
     *
     * @param object
     */
    public void setObject(BDObject object) {
        this.object = object;
    }

    /**
     *
     * @return
     */
    public BDObject getInvObject() {
        return invObject;
    }

    /**
     *
     * @param invObject
     */
    public void setInvObject(BDObject invObject) {
        this.invObject = invObject;
    }
}
