package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.ui.CommandsUI;

/**
 * Questa classe rappresenta l'observer del comando 'AIUTO', permette di
 * visualizzare la lista di tutti i comandi di gioco. Per farlo, implmenta
 * l'interfaccia GameObserver.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class HelpObserver implements GameObserver, Serializable {

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di 'aiuto'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        if (parserOutput.getCommand() != null && parserOutput
                .getCommand().getType() == CommandType.HELP) {
            CommandsUI help = CommandsUI.getInstance();
            help.setVisible(true);
        }
        return "";
    }
}
