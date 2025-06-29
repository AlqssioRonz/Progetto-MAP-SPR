package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;

/**
 * Questa classe rappresenta l'observer del comando 'AIUTO', permette di
 * visualizzare la lista di tutti i comandi di gioco. Per farlo, implmenta
 * l'interfaccia GameObserver.
 *
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
        StringBuilder helpmsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.HELP) {
            helpmsg.append("Per eseguire un comando, scrivi il nome del comando e, se dovesse servire, aggiungi gli oggetti da utilizzare");
            helpmsg.append("I comandi disponibili sono:\n"
                    + "INVENTARIO: visualizza gli oggetti nell'inventario\n"
                    + "AVANTI: per muoverti in avanti\n"
                    + "INDIETRO: per muoverti indietro\n"
                    + "DESTRA: per muoverti a destra\n"
                    + "SINISTRA: per muoverti a sinistra\n"
                    + "SOPRA: per andare sopra\n"
                    + "SOTTO: per andare sotto\n"
                    + "PRENDI: per prendere un oggetto\n"
                    + "LASCIA: per lasciare un oggetto\n"
                    + "USA: per usare un oggetto o più se concatenati\n"
                    + "OSSERVA: per osservare l'ambiente circostante o un oggetto\n"
                    + "SALVA: per salvare la partita\n"
                    + "AIUTO: per visualizzare la lista dei comandi\n");
        }
        return helpmsg.toString();
    }

}
