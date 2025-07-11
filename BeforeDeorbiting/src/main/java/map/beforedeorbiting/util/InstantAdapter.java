// file: map/beforedeorbiting/util/gson/InstantAdapter.java
package map.beforedeorbiting.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

public class InstantAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(value.toString());            // es. "2025-07-11T14:23:30Z"
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.parse(in.nextString());
    }
}
