/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package map.beforedeorbiting.impl;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.util.ISSPositionREST;

/**
 * Osservatore per i comandi di selezione del continente. Verifica se il
 * giocatore si trova nella stanza "ZARYA", se ha già utilizzato l’oggetto
 * corretto e confronta il continente scelto con la posizione attuale della ISS
 * tramite {@link ISSPositionREST}. Se la condizione è soddisfatta, attiva il
 * flag della botola e restituisce il messaggio di scoperta; altrimenti
 * restituisce messaggi di errore o di input non sensato.
 *
 * @author lorenzopeluso
 * @see ISSPositionREST
 */
public class ContinentObserver implements GameObserver {

    /**
     * Esegue l’aggiornamento dello stato di gioco in base al comando di
     * selezione continente fornito dal parser.
     *
     * @param game l’istanza corrente del gioco
     * @param parserOutput output del parser contenente il comando e i dati
     * estratti
     * @return una stringa da visualizzare al giocatore in risposta al comando,
     * che può essere il messaggio di scoperta, di errore o di input non valido
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        CommandType continent = parserOutput.getCommand().getType();
        StringBuilder continentMsg = new StringBuilder();

        if (parserOutput.getCommand() != null
                && (continent == CommandType.AFRICA
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
                // deve essere nella stanza ZARYA e avere l’oggetto id 14 in uso
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

                        game.getCurrentRoom().setRoomImage("src/main/resources/img/zarya_esplosione.png");
                        game.getCurrentRoom().setRoomImage("src/main/resources/img/zarya_esplosione.png");
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
                continentMsg.append("Anche se il continente fosse giusto, ormai non serve più a nulla");
            }
        }

        return continentMsg.toString();
    }
}
