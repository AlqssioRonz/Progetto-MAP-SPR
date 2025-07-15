/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.database;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.RunScript;

/**
 * Gestisce la configurazione e l’inizializzazione del database H2 utilizzato
 * per memorizzare i dati degli astronauti. Fornisce metodi per ottenere una
 * connessione e per popolare il database tramite script SQL.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class DBConfig {

    private static final String DB_URL = "jdbc:h2:~/astronauts_db";

    /**
     * Restituisce una connessione aperta al database H2.
     *
     * @return una {@link Connection} collegata al database
     * @throws SQLException se non è possibile stabilire la connessione
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Popola il database creando la tabella degli astronauti e caricando i dati
     * iniziali dallo script SQL presente nelle risorse. Stampa messaggi di log
     * sullo stato dell’operazione. In caso di errore SQL viene stampata
     * l’eccezione.
     */
    public static void populateDatabase() {
        try (Connection conn = getConnection()) {
            System.out.println("Avviso: Connessione al database completata.");
            AstronautsDAO astronautsdao = new AstronautsDAO(conn);
            astronautsdao.createTable();
            RunScript.execute(conn,
                    new InputStreamReader(DBConfig.class.getClassLoader()
                            .getResourceAsStream("populateDB.sql")));
            System.out.println("Avviso: Database popolato.");
        } catch (SQLException e) {
            System.out.println("Errore: " + e);
        }
    }

}
