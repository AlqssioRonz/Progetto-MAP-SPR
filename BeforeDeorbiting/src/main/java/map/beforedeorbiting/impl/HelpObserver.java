package map.beforedeorbiting.impl;

import java.io.Serializable;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;

/**
 * Questa classe rappresenta l'observer del comando 'AIUTO',
 * permette di visualizzare la lista di tutti i comandi di gioco.
 * Per farlo, implmenta l'interfaccia GameObserver.
 * @author ronzu
 */
public class HelpObserver implements GameObserver, Serializable {
    
     /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce un messaggio di risposta.
     * 
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
     * @param parserOutput l'output del parser utile per conoscere l'input dell'utente
     * @return il messaggio di risposta in base all'azione di 'aiuto'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder helpmsg = new StringBuilder();
        if(parserOutput.getCommand().getType() == CommandType.HELP){
            helpmsg.append("Per eseguire un comando, scrivi il nome del comando e, se dovesse servire, aggiungi gli oggetti da utilizzare");
            helpmsg.append("""
                           I comandi disponibili sono:
                           INVENTARIO: visualizza gli oggetti nell'inventario
                           AVANTI: per muoverti in avanti
                           INDIETRO: per muoverti indietro
                           DESTRA: per muoverti a destra
                           SINISTRA: per muoverti a destra
                           SOPRA: per andare sopra
                           SOTTO: per andare sotto
                           PRENDI: per prendere un oggetto
                           LASCIA: per lasciare un oggetto
                           USA: per usare un oggetto o più se concatenati
                           OSSERVA: per osservare l'ambiente circostante/l'oggetto
                           SALVA: per salvare la partita
                           AIUTO: per visualizzare la lista dei comandi
                           """);
        }
        return helpmsg.toString();
    }
    
}