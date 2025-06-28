/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.GameDesc;

/**
 *
 * @author lorenzopeluso
 */

public interface GameObserver {
    
    
    public String update(GameDesc game, ParserOutput parserOutput);
    
}
