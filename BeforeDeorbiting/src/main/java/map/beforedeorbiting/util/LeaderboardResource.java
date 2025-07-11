package map.beforedeorbiting.util;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/api/classifica")
public class LeaderboardResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getClassifica() {
        // qui puoi recuperare la vera classifica da DB
        return List.of(
          Map.of("nome", "Alice", "punti", 1200),
          Map.of("nome", "Bob",   "punti",  950)
        );
    }
}
