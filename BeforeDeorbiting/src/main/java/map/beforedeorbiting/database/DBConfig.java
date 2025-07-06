/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.database;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;



/**
 *
 * @author lorenzopeluso
 */
public class DBConfig {
    
    private static final String DB_URL = "jdbc:h2:~/astronauts_db";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static List<Astronaut> loadAstronautsFromJson() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("resources/Astronauts.json")));

        Gson gson = new Gson();
        Astronaut[] array = gson.fromJson(json, Astronaut[].class);
        List<Astronaut> astronauts = Arrays.asList(array);
        
        return astronauts;
    }

    public static void populateDatabase(AstronautsDAO dao) throws Exception {
        dao.createTable();
        List<Astronaut> astronauts = loadAstronautsFromJson();
        for (Astronaut a : astronauts) {
            dao.insert(a);
        }
    }
    
}
