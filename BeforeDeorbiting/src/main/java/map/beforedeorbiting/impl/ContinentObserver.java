/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.util.ISSPositionREST;

/**
 *
 * @author lorenzopeluso
 */
public class ContinentObserver implements GameObserver {

    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {

        CommandType continent = parserOutput.getCommand().getType();
        StringBuilder continentMsg = new StringBuilder();
        if (parserOutput.getCommand() != null && (continent == CommandType.AFRICA
                || continent == CommandType.EUROPA
                || continent == CommandType.ASIA
                || continent == CommandType.NAMERICA
                || continent == CommandType.SAMERICA
                || continent == CommandType.OCEANIA
                || continent == CommandType.ANTARTIDE
                || continent == CommandType.OCEANO)) {

            ISSPositionREST issService = new ISSPositionREST();

            String ISSContinent = issService.getContinentForCoordinates();
            if (!game.isFlagTrapdoor()) {
                if (game.getCurrentRoom().equals(game.getRoomByName("ZARYA"))
                        && game.getObjectByID(14).isInUse()) {
                    if ((continent == CommandType.AFRICA && ISSContinent.equalsIgnoreCase("africa"))
                            || (continent == CommandType.EUROPA && ISSContinent.equalsIgnoreCase("europa"))
                            || (continent == CommandType.NAMERICA && ISSContinent.equalsIgnoreCase("nordamerica"))
                            || (continent == CommandType.SAMERICA && ISSContinent.equalsIgnoreCase("sudamerica"))
                            || (continent == CommandType.ASIA && ISSContinent.equalsIgnoreCase("asia"))
                            || (continent == CommandType.OCEANIA && ISSContinent.equalsIgnoreCase("oceania")
                            || (continent == CommandType.ANTARTIDE && ISSContinent.equalsIgnoreCase("antartide"))
                            || (continent == CommandType.OCEANO && ISSContinent.equalsIgnoreCase("oceano")))) {
                        continentMsg.append("COSA DIAVOLO È STATO QUEL RUMORE?!\n"
                                + "mi scappa un urlo prima di vedere la verità. "
                                + "Mi avvicino al finestrino e vedo solo macerie, rottami che galleggiano nel vuoto.\n"
                                + "Resta solo una possibilità: la capsula Starlink, oltre il laboratorio Destiny.\n"
                                + "Non so cosa diavolo sta succedendo in questa stazione… ma so una cosa: io non morirò qui.");
                        game.setFlagTrapdoor(true);
                    } else {
                        continentMsg.append("Errore! Continente sbagliato");
                    }
                } else {
                    continentMsg.append("Quello che dici non ha senso, persino HAL alzerebbe un sopracciglio... se ne avesse uno.");
                }
            } else {
                continentMsg.append("Anche se il continente fosse giusto, ormai non serve piu a nulla");
            }
        }
        return continentMsg.toString();

    }
}
