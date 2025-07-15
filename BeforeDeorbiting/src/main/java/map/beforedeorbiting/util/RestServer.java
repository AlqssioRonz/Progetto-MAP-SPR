package map.beforedeorbiting.util;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;

import java.io.IOException;
import java.net.URI;

/**
 * {@code RestServer} avvia e controlla un piccolo server HTTP basato su Grizzly
 * + Jersey per esporre risorse REST e contenuti statici.
 *
 * <p>
 * Espone:
 * <ul>
 * <li>Risorse JAX-RS (es. {@link map.beforedeorbiting.util.HtmlResource})</li>
 * <li>File statici sotto <code>src/main/resources/web/</code></li>
 * </ul>
 *
 * <p>
 * Di norma viene avviato una sola volta, e arrestato alla chiusura
 * dell’applicazione.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class RestServer {

    /**
     * Il server Grizzly in esecuzione.
     */
    private HttpServer server;

    /**
     * Crea e avvia il server sulla porta 8080 in localhost.
     * <p>
     * Registra manualmente:
     * <ul>
     * <li>La risorsa HTML di base
     * ({@link map.beforedeorbiting.util.HtmlResource})</li>
     * <li>Il supporto a JSON via Jackson</li>
     * </ul>
     *
     * @throws IOException se fallisce la creazione o l’avvio del server
     */
    public void start() throws IOException {
        ResourceConfig cfg = new ResourceConfig()
                // Registrazione manuale delle risorse JAX-RS
                .register(HtmlResource.class)
                .register(JacksonFeature.class)
                // Disabilita discovery automatica e WADL
                .property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true)
                .property(ServerProperties.WADL_FEATURE_DISABLE, true);

        // Crea il server REST, ma non lo avvia immediatamente
        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://localhost:8080/"), cfg, false);

        // Serve i file statici contenuti in src/main/resources/web/ alla root "/"
        CLStaticHttpHandler staticHandler = new CLStaticHttpHandler(
                RestServer.class.getClassLoader(),
                "/web/");
        server.getServerConfiguration().addHttpHandler(staticHandler, "/");

        // Avvia il server e registra uno shutdown hook per la JVM
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        System.out.println("REST server avviato su http://localhost:8080");
    }

    /**
     * Ferma immediatamente il server REST se è in esecuzione, liberando tutte
     * le risorse legate alle socket.
     */
    public void stop() {
        if (server != null) {
            server.shutdownNow();
        }
    }
}
