/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import map.beforedeorbiting.GameDesc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ronzu
 */
/**
 * Gestisce il salvataggio e caricamento dello stato di gioco in file JSON con timestamp.
 */
public class JSONSaveController {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path SAVE_DIR = Path.of("saves");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Salva il gioco creando un file con timestamp, es.save_20250702_153055.json
     * @param game
     * @throws java.io.IOException
     */
    public static void saveGameWithTimestamp(GameDesc game) throws IOException {
        Files.createDirectories(SAVE_DIR); // crea la cartella se non esiste

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String fileName = "save_" + timestamp + ".json";
        Path savePath = SAVE_DIR.resolve(fileName);

        String json = gson.toJson(game);
        Files.writeString(savePath, json, StandardCharsets.UTF_8);

        System.out.println("Gioco salvato in: " + savePath);
    }

    /**
     * Carica un file di gioco da un percorso specifico.
     * @param path
     * @return 
     * @throws java.io.IOException
     */
    public static GameDesc loadGame(Path path) throws IOException {
        String json = Files.readString(path, StandardCharsets.UTF_8);
        return gson.fromJson(json, GameDesc.class);
    }
    
}