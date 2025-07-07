/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package map.beforedeorbiting;

import map.beforedeorbiting.parser.Parser;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        Set<String> stopwords;
        try {
            stopwords = loadFileListInSet("/stopwords.txt");
            parser = new Parser(stopwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Engine(GameDesc game, boolean load) {
        this.game = game;
        if (!load) {
            try {
                this.game.init();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        Set<String> stopwords;
        try {
            stopwords = loadFileListInSet("/stopwords.txt");
            parser = new Parser(stopwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'istanza di GameDesc.
     *
     * @return il gioco.
     */
    public GameDesc getGame() {
        return game;
    }

    public void execute() {
        System.out.println(game.getWelcomeMessage() + "\n\n"
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
        engine.execute();
    }

    /**
     * Carica le righe di un file in un set di stringhe
     *
     * @param resourcePath
     * @return
     * @throws java.io.IOException
     */
    public static Set<String> loadFileListInSet(String resourcePath) throws IOException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Il percorso della risorsa non può essere nullo o vuoto");
        }

        try (InputStream in = Engine.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Impossibile trovare la risorsa: " + resourcePath);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return br.lines()
                        .map(s -> s.trim().toLowerCase())
                        .collect(Collectors.toSet());
            }
        }
    }
}
