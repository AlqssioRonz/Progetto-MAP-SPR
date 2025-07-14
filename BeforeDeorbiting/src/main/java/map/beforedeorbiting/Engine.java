/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package map.beforedeorbiting;

import map.beforedeorbiting.parser.Parser;
import map.beforedeorbiting.parser.ParserOutput;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import map.beforedeorbiting.database.DBConfig;

/**
 * Motore di gioco che gestisce il ciclo di lettura dei comandi da console,
 * utilizza {@link Parser} per interpretare l’input e invoca il {@link GameDesc}
 * fornito per eseguire la logica del gioco. All’avvio popola il database e
 * stampa i messaggi di benvenuto e di uscita.
 *
 * @author lorenzopeluso
 */
public class Engine {

    private final GameDesc game;
    private Parser parser;

    /**
     * Crea un motore di gioco e inizializza lo stato del gioco. Viene sempre
     * richiamato {@link GameDesc#init()}.
     *
     * @param game l’istanza di gioco da utilizzare
     */
    public Engine(GameDesc game) {
        this.game = game;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            Set<String> stopwords = loadFileListInSet("/stopwords.txt");
            parser = new Parser(stopwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un motore di gioco e opzionalmente inizializza lo stato. Se
     * {@code load} è false, {@link GameDesc#init()} viene richiamato,
     * altrimenti si salta l’inizializzazione per caricare uno stato esistente.
     *
     * @param game l’istanza di gioco da utilizzare
     * @param load se false inizializza il gioco, se true mantiene lo stato
     * corrente
     */
    public Engine(GameDesc game, boolean load) {
        this.game = game;
        if (!load) {
            try {
                this.game.init();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        try {
            Set<String> stopwords = loadFileListInSet("/stopwords.txt");
            parser = new Parser(stopwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l’istanza di {@link GameDesc} associata al motore.
     *
     * @return l’oggetto di gioco
     */
    public GameDesc getGame() {
        return game;
    }

    /**
     * Avvia il ciclo principale di lettura dei comandi da console. Stampa il
     * messaggio di benvenuto e la descrizione della stanza corrente, quindi
     * legge comandi finché l’utente non termina il gioco.
     */
    public void execute() {
        System.out.println(game.getWelcomeMessage() + "\n\n"
                + game.getCurrentRoom().getGameStory());
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ParserOutput p = parser.parse(
                    command,
                    game.getCommands(),
                    game.getListObj(),
                    game.getInventory().getList());
            if (p == null || p.getCommand() == null) {
                System.out.println(
                        "Quello che dici non ha senso, persino HAL alzerebbe un sopracciglio... se ne avesse uno.");
            } else {
                game.nextMove(p, System.out);
                int roomId = game.getCurrentRoom().getId();
                if (roomId == -1) {
                    System.out.println("La Terra ti accoglie. E i ricordi... resteranno tra le stelle.");
                    System.exit(0);
                } else if (roomId == -2) {
                    System.out.println("Rinunci alla terra, ma ritrovi chi avevi perso.");
                    System.exit(0);
                }
            }
            System.out.print("> ");
        }
    }

    /**
     * Entry point dell’applicazione. Crea un motore con una nuova istanza di
     * {@link BeforeDeorbiting}, popola il database e avvia l’esecuzione.
     *
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        Engine engine = new Engine(new BeforeDeorbiting());
        DBConfig.populateDatabase();
        engine.execute();
    }

    /**
     * Carica tutte le righe di un file di risorse in un {@link Set}. Ogni riga
     * viene trimmata, convertita in minuscolo e raccolta, utile per generare la
     * lista di stopwords.
     *
     * @param resourcePath percorso interno alla classpath (ad es.
     * "/stopwords.txt")
     * @return un Set di stringhe contenente le righe del file
     * @throws IOException se il file non viene trovato o non è leggibile
     * @throws IllegalArgumentException se {@code resourcePath} è null o vuoto
     */
    public static Set<String> loadFileListInSet(String resourcePath) throws IOException {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Il percorso della risorsa non può essere nullo o vuoto");
        }

        try (InputStream in = Engine.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Impossibile trovare la risorsa: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return br.lines()
                        .map(s -> s.trim().toLowerCase())
                        .collect(Collectors.toSet());
            }
        }
    }
}
