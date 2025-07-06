/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package map.beforedeorbiting;

import map.beforedeorbiting.parser.Parser;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.database.DBConfig;
import map.beforedeorbiting.database.AstronautsDAO;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.util.HashSet;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author lorenzopeluso
 */
public class Engine {

    private final GameDesc game;

    private Parser parser;

    /**
     * Costruttore per l'engine di gioco.
     *
     * @param game il gioco.
     */
    public Engine(GameDesc game) {
        this.game = game;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try (InputStream in = Engine.class.getResourceAsStream("/stopwords.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            Set<String> stopwords = br.lines()
                    .map(s -> s.trim().toLowerCase())
                    .collect(Collectors.toSet());
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void execute() {
        System.out.println(game.getWelcomeMessage()+"\n\n"
                + game.getCurrentRoom().getGameStory());
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), 
                    game.getListObj(), game.getInventory().getList());
            if (p == null || p.getCommand() == null) {
                System.out.println("Quello che dici non ha senso, persino "
                        + "HAL alzerebbe un sopracciglio... se ne avesse uno.");
            } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.EXIT) {
                System.out.println("Se stai cercando l'uscita, "
                        + "si trova accanto al panico e alla rassegnazione.");
                break;
            } else {
                game.nextMove(p, System.out);
                if (game.getCurrentRoom().getId() == -1) {
                    System.out.println("La Terra ti accoglie. E i ricordi... "
                            + "resteranno tra le stelle.");
                    System.exit(0);
                } else if (game.getCurrentRoom().getId() == -2) {
                    System.out.println("Rinunci alla terra, ma ritrovi chi avevi perso.");
                    System.exit(0);
                }
            }
            System.out.print("> ");
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Engine engine = new Engine(new BeforeDeorbiting());

        try (Connection conn = DBConfig.getConnection()) {
            AstronautsDAO dao = new AstronautsDAO(conn);

            try {
                DBConfig.populateDatabase(dao);
            } catch (IOException e) {
                System.err.println("Errore nel caricamento dei dati da JSON: " + e.getMessage());
                return;
            } catch (Exception e) {
                System.err.println("Errore di inserimento dati nel DB:  " + e.getMessage());
                return;
            }

            engine.execute();

        } catch (SQLException e) {
            System.err.println("Could not connect to database: " + e.getMessage());
        }
    }

    /**
     * Carica le righe di un file in un set di stringhe
     * @param file
     * @return 
     * @throws java.io.IOException
     */
    public static Set<String> loadFileListInSet(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Il file non può essere nullo");
        }

        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line.trim().toLowerCase());
            }
        }
        return set;
    }
}
