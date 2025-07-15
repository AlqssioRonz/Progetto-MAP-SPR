package map.beforedeorbiting.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * {@link TypeAdapter} per serializzare e deserializzare istanze di
 * {@link Instant} in formato ISO-8601, tramite Gson.
 * <p>
 * Durante la scrittura (serialization), converte l’{@code Instant} in stringa
 * ISO-8601 (es. {@code "2025-07-11T14:23:30Z"}). Durante la lettura
 * (deserialization), ricrea l’{@code Instant} a partire dalla stringa.
 * </p>
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class InstantAdapter extends TypeAdapter<Instant> {

    /**
     * Scrive un {@link Instant} nel {@link JsonWriter} come stringa ISO-8601.
     *
     * @param out il writer su cui scrivere il valore
     * @param value l’istanza di {@code Instant} da serializzare
     * @throws IOException se si verifica un errore di I/O durante la scrittura
     */
    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        out.value(value.toString());
    }

    /**
     * Legge una stringa ISO-8601 dal {@link JsonReader} e la converte in
     * un’istanza di {@link Instant}.
     *
     * @param in il reader da cui leggere la stringa ISO-8601
     * @return l’istanza di {@code Instant} risultante dal parsing
     * @throws IOException se si verifica un errore di I/O durante la lettura
     */
    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.parse(in.nextString());
    }
}
