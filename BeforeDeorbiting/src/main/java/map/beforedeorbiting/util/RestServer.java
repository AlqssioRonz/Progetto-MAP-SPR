package map.beforedeorbiting.util;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;

import java.io.IOException;
import java.net.URI;

public class RestServer {
    private HttpServer server;

    public void start() throws IOException {
        ResourceConfig cfg = new ResourceConfig()
            // Registrazione manuale delle risorse JAX-RS
            .register(HtmlResource.class)
            .register(LeaderboardResource.class)
            .register(JacksonFeature.class)
            // Disabilita discovery automatica e WADL
            .property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true)
            .property(ServerProperties.WADL_FEATURE_DISABLE, true);

        // Crea il server REST
        server = GrizzlyHttpServerFactory.createHttpServer(
            URI.create("http://localhost:8080/"), cfg, false);

        // Serve tutto ciò che sta in src/main/resources/web/
        CLStaticHttpHandler staticHandler = new CLStaticHttpHandler(
            RestServer.class.getClassLoader(),
            "/web/"
        );
        server.getServerConfiguration().addHttpHandler(staticHandler, "/");

        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        System.out.println("REST server avviato su http://localhost:8080");
    }

    public void stop() {
        if (server != null) server.shutdownNow();
    }
}