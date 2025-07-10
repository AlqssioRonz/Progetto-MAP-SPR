package map.beforedeorbiting.util;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Path("/")
public class HtmlResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndex() {
        try (InputStream is = getClass().getResourceAsStream("/web/index.html")) {
            if (is == null) {
                return Response.status(404)
                               .entity("<h1>404 - File non trovato</h1>")
                               .type(MediaType.TEXT_HTML)
                               .build();
            }
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return Response.ok(html, MediaType.TEXT_HTML).build();
        } catch (Exception e) {
            return Response.status(500)
                           .entity("<h1>500 – Errore interno</h1><pre>"
                                   + e.getMessage() + "</pre>")
                           .type(MediaType.TEXT_HTML)
                           .build();
        }
    }
}