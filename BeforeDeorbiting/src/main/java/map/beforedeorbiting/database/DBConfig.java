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
 *
 * @author lorenzopeluso
 */
public class DBConfig {
    
    private static final String DB_URL = "jdbc:h2:~/astronauts_db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void populateDatabase() {
        try (Connection conn = getConnection()) {
            System.out.println("Avviso: Connessione al database completata.");
            RunScript.execute(conn, 
                    new InputStreamReader(DBConfig.class.getClassLoader()
                            .getResourceAsStream("populateDB.sql")));
            System.out.println("Avviso: Database popolato.");
        } catch (SQLException e) {
            System.out.println("Errore: " + e);
        }
    }
    
}
