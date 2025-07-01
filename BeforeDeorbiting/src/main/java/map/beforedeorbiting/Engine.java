/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package map.beforedeorbiting;

import map.beforedeorbiting.parser.Parser;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.util.HashSet;
import java.io.FileReader;
import java.util.Set;

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
        try {
            Set<String> stopwords = loadFileListInSet(new File("./resources/stopwords.txt"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void execute() {
        System.out.println(game.getWelcomeMessage());
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(command, game.getCommands(), game.getListObj(), game.getInventory().getList());
            if (p == null || p.getCommand() == null) {
                System.out.println("Quello che dici non ha senso, persino HAL alzerebbe un sopracciglio... se ne avesse uno");
            } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                System.out.println("Se stai cercando l'uscita, si trova accanto al panico e alla rassegnazione");
                break;
            } else {
                game.nextMove(p, System.out);
                if (game.getCurrentRoom().getId() == -1) {
                    System.out.println("La Terra ti accoglie. E i ricordi... resteranno tra le stelle");
                    System.exit(0);
                } else if (game.getCurrentRoom().getId() == -2) {
                    System.out.println("Rinunci alla terra, ma ritrovi chi avevi perso");
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
