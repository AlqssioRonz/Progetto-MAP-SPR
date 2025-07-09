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
 *
 * @author ronzu
 */
public class SaveObserver implements GameObserver {

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di 'salva'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder saveMsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.SAVE) {
            try {
                Path notePath = Path.of("notebook.txt");
                String text = "";
                if (Files.exists(notePath)) {
                    text = Files.readString(notePath, StandardCharsets.UTF_8);
                }
                game.setNotebookText(text);
                JSONSaveController.saveGameWithTimestamp(game);
                saveMsg.append("[✔] Salvataggio completato.");
            } catch (IOException e) {
                saveMsg.append("[✘] Errore durante il salvataggio: ").append(e.getMessage());
            }
        }
        return saveMsg.toString();
    }

}
