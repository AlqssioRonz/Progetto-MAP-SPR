package map.beforedeorbiting.util;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Risorsa JAX-RS che espone, all’endpoint radice ("/"), il file HTML statico
 * presente in classpath sotto {@code /web/index.html}.
 * <p>
 * Gestisce:
 * <ul>
 * <li>Richieste GET per restituire il contenuto HTML con media type
 * {@code text/html}.</li>
 * <li>Situazioni di file non trovato (404) o errori interni (500).</li>
 * </ul>
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
@Path("/")
public class HtmlResource {

    /**
     * Risponde a una richiesta GET con il contenuto del file HTML di index.
     *
     * @return una {@link Response} HTTP:
     * <ul>
     * <li>200 OK con body HTML se il file viene caricato correttamente.</li>
     * <li>404 Not Found con pagina di errore se il file non esiste.</li>
     * <li>500 Internal Server Error con messaggio di eccezione in caso di altri
     * errori.</li>
     * </ul>
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndex() {
        try (InputStream is = getClass().getResourceAsStream("/web/index.html")) {
            if (is == null) {
                // file non trovato
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("<h1>404 - File non trovato</h1>")
                        .type(MediaType.TEXT_HTML)
                        .build();
            }
            // legge l’intero contenuto in UTF-8
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return Response.ok(html, MediaType.TEXT_HTML).build();
        } catch (Exception e) {
            // errore interno
            String errorPage = "<h1>500 – Errore interno</h1><pre>"
                    + e.getMessage() + "</pre>";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorPage)
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
    }
}
