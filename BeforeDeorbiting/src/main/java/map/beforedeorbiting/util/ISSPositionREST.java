package map.beforedeorbiting.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servizio REST per recuperare la posizione corrente dell’ISS e determinare il
 * continente corrispondente sulla base di bounding boxes predefinite. Utilizza
 * l’endpoint <code>http://api.open-notify.org/iss-now.json</code>.
 *
 * @author andre
 */
public class ISSPositionREST {

    /**
     * URL dell’API per ottenere la posizione dell’ISS.
     */
    private static final String API_URL = "http://api.open-notify.org/iss-now.json";

    /**
     * Timeout in millisecondi per connessione e lettura dall’API.
     */
    private static final int TIMEOUT_MS = 4000;

    /**
     * Coordinate angolari (latMin, latMax, lonMin, lonMax) che delimitano
     * ciascun continente. Utilizzato per determinare in quale continente si
     * trova l’ISS.
     */
    private final Map<String, double[]> CONTINENT_BBOX = createContinentBbox();

    /**
     * Costruisce la mappa di bounding boxes per ogni continente.
     *
     * @return mappa da nome del continente (tutto minuscolo) a array di quattro
     * valori: {latMin, latMax, lonMin, lonMax}
     */
    private Map<String, double[]> createContinentBbox() {
        Map<String, double[]> m = new LinkedHashMap<>();
        m.put("africa", new double[]{-35.0, 37.0, -18.0, 51.0});
        m.put("europa", new double[]{34.5, 71.2, -25.0, 45.0});
        m.put("asia", new double[]{1.0, 81.0, 26.0, 180.0});
        m.put("nordamerica", new double[]{5.0, 83.0, -168.0, -52.0});
        m.put("sudamerica", new double[]{-56.0, 13.0, -81.0, -34.0});
        m.put("oceania", new double[]{-50.0, 10.0, 110.0, 180.0});
        m.put("antartide", new double[]{-90.0, -60.0, -180.0, 180.0});
        return m;
    }

    /**
     * Chiama l’API ISS e restituisce latitudine e longitudine.
     *
     * @return array di due stringhe:
     * <ul>
     * <li>indice 0 = latitudine</li>
     * <li>indice 1 = longitudine</li>
     * </ul>
     * In caso di errore restituisce {@code ["[Errore]","[Errore]"]}.
     */
    public String[] getISSCoordinates() {
        try {
            /*
             * Sappiamo bene che l’URL è deprecato, ma è quello che ci serve
             * per il progetto. In ambito professionale, si dovrebbe scegliere
             * un’API più recente o alternativa.
             */
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JsonObject root = JsonParser.parseString(sb.toString())
                        .getAsJsonObject();
                JsonObject pos = root.getAsJsonObject("iss_position");
                String lat = pos.get("latitude").getAsString();
                String lon = pos.get("longitude").getAsString();
                return new String[]{lat, lon};
            }
        } catch (Exception e) {
            return new String[]{"[Errore]", "[Errore]"};
        }
    }

    /**
     * Determina il continente in cui si trova l’ISS, basandosi sulle coordinate
     * restituite da {@link #getISSCoordinates()} e sulle bounding boxes
     * predefinite.
     *
     * @return il nome del continente (tutto minuscolo), "oceano" se non rientra
     * in alcuna bounding box, o "[Coordinata non valida]" in caso di parsing
     * non valido delle coordinate.
     */
    public String getContinentForCoordinates() {
        String[] coords = getISSCoordinates();
        try {
            double lat = Double.parseDouble(coords[0]);
            double lon = Double.parseDouble(coords[1]);
            for (Map.Entry<String, double[]> e : CONTINENT_BBOX.entrySet()) {
                double[] bbox = e.getValue();
                if (lat >= bbox[0] && lat <= bbox[1]
                        && lon >= bbox[2] && lon <= bbox[3]) {
                    return e.getKey();
                }
            }
        } catch (NumberFormatException nfe) {
            return "[Coordinata non valida]";
        }
        return "oceano";
    }
}
