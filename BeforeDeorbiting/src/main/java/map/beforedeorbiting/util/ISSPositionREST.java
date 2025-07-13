package map.beforedeorbiting.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashMap;
import java.util.Map;

public class ISSPositionREST {

    private final String API_URL = "http://api.open-notify.org/iss-now.json";
    private final int TIMEOUT_MS = 4000;

    /**
     * Coordinate angolari che delimitano ogni continente: {latMin, latMax,
     * lonMin, lonMax}
     */
    private final Map<String, double[]> CONTINENT_BBOX = createContinentBbox();

    private Map<String, double[]> createContinentBbox() {
        Map<String, double[]> m = new LinkedHashMap<>();
        m.put("africa", new double[] { -35.0, 37.0, -18.0, 51.0 });
        m.put("europa", new double[] { 34.5, 71.2, -25.0, 45.0 });
        m.put("asia", new double[] { 1.0, 81.0, 26.0, 180.0 });
        m.put("nordamerica", new double[] { 5.0, 83.0, -168.0, -52.0 });
        m.put("sudamerica", new double[] { -56.0, 13.0, -81.0, -34.0 });
        m.put("oceania", new double[] { -50.0, 10.0, 110.0, 180.0 });
        m.put("antartide", new double[] { -90.0, -60.0, -180.0, 180.0 });
        return m;
    }

    /**
     * Chiama l'API ISS e restituisce latitudine e longitudine.
     *
     * @return array di due elementi: [0]=latitudine, [1]=longitudine
     */
    public String[] getISSCoordinates() {
        try {
            /*
             * Sappiamo bene che l'URL è deprecato, ma è quello che ci serve
             * per il progetto. In ambito professionale, si dovrebbe scegliere
             * una via alternativa o un'API più recente.
             * Tuttavia, per il progetto, l'API è ancora funzionante.
             */
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
                JsonObject pos = root.getAsJsonObject("iss_position");
                String lat = pos.get("latitude").getAsString();
                String lon = pos.get("longitude").getAsString();
                return new String[] { lat, lon };
            }
        } catch (Exception e) {
            return new String[] { "[Errore]", "[Errore]" };
        }
    }

    /**
     * Determina il continente in cui si trova l'ISS.
     *
     * @return nome del continente o "Oceano" se non rientra in alcuna bounding
     *         box
     */
    public String getContinentForCoordinates() {
        String[] coords = getISSCoordinates();
        String latString = coords[0];
        String lonString = coords[1];
        try {
            double lat = Double.parseDouble(latString);
            double lon = Double.parseDouble(lonString);
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
