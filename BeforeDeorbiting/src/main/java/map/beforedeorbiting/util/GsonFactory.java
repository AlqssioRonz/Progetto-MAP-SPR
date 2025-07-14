package map.beforedeorbiting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Duration;
import java.time.Instant;

/**
 * Factory per la creazione e configurazione di un'istanza condivisa di
 * {@link Gson}.
 * <p>
 * Configura un {@link GsonBuilder} per:
 * <ul>
 * <li>Gestire correttamente la serializzazione/deserializzazione di
 * {@link Duration} tramite {@link DurationAdapter}.</li>
 * <li>Gestire correttamente la serializzazione/deserializzazione di
 * {@link Instant} tramite {@link InstantAdapter}.</li>
 * <li>Abilitare il pretty printing dell'output JSON.</li>
 * </ul>
 * <p>
 * La singola istanza {@link #GSON} può essere riutilizzata ovunque
 * nell'applicazione.
 *
 * @author ronzu
 */
public class GsonFactory {

    /**
     * Istanza singleton di {@link Gson}, configurata con adattatori per
     * {@link Duration} e {@link Instant} e con pretty printing.
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .setPrettyPrinting()
            .create();
}
