package map.beforedeorbiting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.Instant;

public class GsonFactory {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .setPrettyPrinting()
            .create();
}