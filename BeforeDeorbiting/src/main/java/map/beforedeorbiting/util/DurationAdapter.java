// file: map/beforedeorbiting/util/gson/DurationAdapter.java
package map.beforedeorbiting.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

/**
 * Adatta un {@link Duration} per la serializzazione con Gson.
 */
public class DurationAdapter extends TypeAdapter<Duration> {

    /**
     * Scrive la durata in formato ISO-8601.
     */
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        out.value(value.toString()); // salva come "PT1H23M45S"
    }

    /**
     * Legge una durata in formato ISO-8601.
     */
    @Override
    public Duration read(JsonReader in) throws IOException {
        return Duration.parse(in.nextString()); // ricostruisce da ISO-8601
    }
}
