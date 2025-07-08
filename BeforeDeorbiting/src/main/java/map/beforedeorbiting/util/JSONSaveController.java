package map.beforedeorbiting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import map.beforedeorbiting.BeforeDeorbiting;
import map.beforedeorbiting.GameDesc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestisce il salvataggio e caricamento dello stato di gioco in file JSON con
 * timestamp. Ora registra un InstanceCreator per GameDesc in modo da istanziare
 * BeforeDeorbiting().
 *
 * @author ronzu
 */
public class JSONSaveController {
    private static final DateTimeFormatter TIMESTAMP_FORMAT
            = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final Path SAVE_DIR = Path.of("saves");

    /**
     * Il Gson configurato con pretty printing e con il TypeAdapter per
     * GameDesc.
>>>>>>> 90d5649 (Modificate alcune cose)
     */
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            // Registriamo un InstanceCreator per GameDesc
            .registerTypeAdapter(GameDesc.class, new InstanceCreator<GameDesc>() {
                @Override
                public GameDesc createInstance(Type type) {
                    // Ogni volta che serve un GameDesc, ne creiamo uno di tipo BeforeDeorbiting
                    return new BeforeDeorbiting();
                }
            })
            .create();

    /**
     * Salva il gioco creando un file con timestamp, es.
     * save_20250702_153055.json
     *
     * @param game il GameDesc da serializzare
     * @throws IOException se il write fallisce
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
     * Carica un file di gioco da un percorso specifico. Grazie
     * all’InstanceCreator, il GameDesc astratto verrà materializzato come
     * BeforeDeorbiting.
     *
     * @param path percorso del file .json
     * @return il GameDesc deserializzato
     * @throws IOException se il read fallisce
     */
    public static GameDesc loadGame(Path path) throws IOException {
        String json = Files.readString(path, StandardCharsets.UTF_8);
        return gson.fromJson(json, GameDesc.class);
    }
}
