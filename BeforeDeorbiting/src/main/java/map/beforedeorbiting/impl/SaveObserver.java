/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.util.JSONSaveController;

/**
 * Osservatore per il comando di salvataggio (SAVE). Quando l’utente invoca il
 * comando SAVE, legge il contenuto corrente del taccuino da file, aggiorna lo
 * stato del gioco e utilizza {@link JSONSaveController} per effettuare il
 * salvataggio con timestamp.
 *
 * @author ronzu
 * @see JSONSaveController
 */
public class SaveObserver implements GameObserver {

    /**
     * Gestisce l’azione di salvataggio del gioco. Se il comando è
     * {@link CommandType#SAVE}, legge prima il contenuto del file
     * "notebook.txt" (se esiste) e lo imposta in {@link GameDesc}, quindi
     * chiama {@link JSONSaveController#saveGameWithTimestamp(GameDesc)}. In
     * caso di successo restituisce un messaggio di conferma, altrimenti un
     * messaggio di errore con la descrizione dell’eccezione.
     *
     * @param game lo stato corrente del gioco da salvare
     * @param parserOutput il risultato dell’analisi del comando, utile per
     * individuare il comando SAVE
     * @return un messaggio da mostrare al giocatore indicante l’esito del
     * salvataggio (successo o errore)
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder saveMsg = new StringBuilder();

        if (parserOutput.getCommand().getType() == CommandType.SAVE) {
            try {
                Path notePath = Path.of("notebook.txt");
                String text = "";

                if (Files.exists(notePath)) {
                    // Legge il contenuto esistente del taccuino
                    text = Files.readString(notePath, StandardCharsets.UTF_8);
                }
                game.setNotebookText(text);

                // Salva lo stato di gioco con timestamp
                JSONSaveController.saveGameWithTimestamp(game);

                saveMsg.append("[✔] Salvataggio completato.");
            } catch (IOException e) {
                saveMsg.append("[✘] Errore durante il salvataggio: ")
                        .append(e.getMessage());
            }
        }

        return saveMsg.toString();
    }

}
