/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.UIManager;

import map.beforedeorbiting.Engine;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.util.JSONSaveController;
import map.beforedeorbiting.parser.Parser;

import javax.swing.SwingUtilities;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import map.beforedeorbiting.BeforeDeorbiting;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.util.MusicController;
import map.beforedeorbiting.util.ConcurrentChronometer;

/**
 * Finestra principale dell’interfaccia grafica di “Before Deorbiting”. Coordina
 * creazione/avvio del gioco, I/O testuale, pannello immagine, menu (musica,
 * timer, skip, esci), minigiochi e finale interattivo.
 */
public class GameUI extends JFrame {

    /**
     * Font principale usato nell'interfaccia.
     */
    private static final Font FONT = new Font("Helvetica", Font.BOLD, 16);

    /**
     * Colore di sfondo scuro per i pannelli.
     */
    private static final Color BLUSCURO = Color.decode("#0f111c");

    /**
     * Colore di accento chiaro per i componenti attivi.
     */
    private static final Color BLUCHIARO = Color.decode("#00e1d4");

    /**
     * Colore scuro alternativo, usato per componenti secondari.
     */
    private static final Color VERDESCURO = Color.decode("#33452b");

    /**
     * Colore del testo.
     */
    private static final Color TEXT = new Color(6, 6, 6);

    /**
     * Traccia musicale attualmente in riproduzione.
     */
    private String currentMusicTrack = null;

    /**
     * Controller per la musica di sottofondo.
     */
    private final MusicController music = new MusicController();

    /**
     * Cronometro concorrente per il timer di gioco.
     */
    private ConcurrentChronometer chronometer = new ConcurrentChronometer();

    /**
     * Parser per elaborare comandi testuali.
     */
    private Parser parser = null;

    /**
     * Motore di gioco che gestisce la logica.
     */
    private Engine engine;

    /**
     * Descrizione dello stato di gioco corrente.
     */
    private GameDesc game;

    /**
     * Finestra di conferma chiusura/uscita al menu.
     */
    private JFrame confermaChiusura;

    /**
     * Pannello di testo per output di comandi e narrazione.
     */
    private JTextPane textPane;

    /**
     * Scroll pane avvolgente il textPane.
     */
    private JScrollPane scrollPane;

    /**
     * Campo di input testuale per i comandi dell'utente.
     */
    private JTextField textBox;

    /**
     * Pulsante per attivare/disattivare la musica.
     */
    private JButton musicButton;

    /**
     * Barra del menu con bottoni di controllo.
     */
    private JMenuBar menuBar;

    /**
     * Pannello che contiene layout e sotto-pannelli principali.
     */
    private JPanel macroPanel;

    /**
     * Pannello custom che disegna l'immagine corrente.
     */
    private ImagePanel imageViewer;

    /**
     * Etichetta di supporto per mostrare icone o testo sull'immagine.
     */
    private JLabel imageLabel;

    /**
     * Flag che indica se la partita è stata caricata da salvataggio.
     */
    private boolean load;

    /**
     * Flag per gestire l'interazione del finale A/B.
     */
    private boolean intoFinale = false;
    
    /**
     * Flag per evitare la verbosità nel finale
     */
    private boolean firstTime = true;

    /**
     * Gestore di stampa per output formattato e animato.
     */
    private PrinterUI printer;

    /**
     * Costruisce la GUI per una nuova partita “vergine”. Crea un nuovo
     * BeforeDeorbiting + Engine e ne avvia l’inizializzazione.
     */
    public GameUI() {
        // 1) crea lo stato di gioco “vergine”
        this.load = false;
        BeforeDeorbiting newGame = new BeforeDeorbiting();
        Engine engine = new Engine(newGame);
        // 2) delega tutto il setup a initWithEngine
        initWithEngine(engine);
    }

    /**
     * Costruisce la GUI caricando lo stato di gioco da file JSON.
     *
     * @param savePath il percorso del file di salvataggio
     * @throws IOException se il file non è valido o non esiste
     */
    public GameUI(Path savePath) throws IOException {
        this.load = true;
        // 1) deserializza lo stato dal JSON
        GameDesc loadedGame = JSONSaveController.loadGame(savePath);
        // 2) crea l’engine in modalità “load”
        Engine engine = new Engine(loadedGame, true);
        // 3) delega tutto il setup a initWithEngine
        initWithEngine(engine);
    }

    /**
     * Inizializza la GUI a partire da un Engine.
     *
     * @param engine il motore di gioco da utilizzare
     */
    private void initWithEngine(Engine engine) {
        this.engine = engine;
        this.game = engine.getGame();

        // Swing setup
        UIManager.put("ScrollBar.width", 0);

        try {
            initComponents();
        } catch (InterruptedException ex) {
            System.getLogger(GameUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            mainComponents(this.load, null);
        } catch (Exception ex) {
            Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ferma la musica e svolge le operazioni di cleanup al termine della
     * partita.
     */
    public void concludiPartita() {
        music.stopMusica();
    }

    /**
     * Imposta e avvia il gioco: - crea o ricarica BeforeDeorbiting+Engine -
     * inizializza il Parser - chiama nextMove(null) per il welcome - mostra
     * l’immagine iniziale della stanza
     *
     * @param loadGame true se caricare da salvataggio
     * @param file file di salvataggio (non usato)
     * @throws Exception in caso di errori I/O o parser
     */
    private void mainComponents(boolean loadGame, File file) throws Exception { // <-------------------- interazione
        if (!loadGame) {
            game = new BeforeDeorbiting();
            engine = new Engine(game);
        } else {
            // Mantieni l'engine e il gioco già forniti
            game = engine.getGame();
        }

        Set<String> stopwords = Engine.loadFileListInSet("/stopwords.txt");
        parser = new Parser(stopwords);

        PrintStream ps = new PrintStream(new JTextPaneOutputStream(textPane), true, StandardCharsets.UTF_8) {
            @Override
            public void println(String x) {
                printer.print(x + "\n");
            }
        };

        // Chiama normalmente nextMove() con null per l'avvio del gioco.
        engine.getGame().nextMove(null, ps);

        // Mostra l'immagine della stanza corrente appena la partita inizia
        if (engine.getGame().getCurrentRoom() != null) {
            updateRoomImage(engine.getGame().getCurrentRoom().getRoomImage());
        }
    }

    /**
     * Costruisce e dispone tutti i componenti Swing: dialog di conferma,
     * textPane, scrollPane, textBox, menu bar, pannello centrale con output e
     * immagine/inventario.
     *
     * @throws InterruptedException se fallisce l’inizializzazione concorrente
     */
    private void initComponents() throws InterruptedException {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dispose();
            }
        });

        confermaChiusura = new JFrame();
        JPanel panel = new JPanel();
        JPanel buttonPanelExit = new JPanel();
        macroPanel = new JPanel();
        imageViewer = new ImagePanel();

        JLabel jTextArea2 = new JLabel();
        textBox = new JTextField();
        textBox.addActionListener(this::elaborateInput);
        scrollPane = new JScrollPane();
        textPane = new JTextPane();
        imageLabel = new JLabel();
        menuBar = new JMenuBar();

        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();

        JButton tornaMenu = new JButton();

        musicButton = new JButton();

        java.net.URL iconURL = getClass().getResource("/img/icon.png");
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        confermaChiusura.setResizable(false);
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            confermaChiusura.setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        confermaChiusura.setSize(new Dimension(350, 150));
        confermaChiusura.setLocationRelativeTo(null);
        confermaChiusura.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        confermaChiusura.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                continueGame(evt);
            }
        });
        buttonPanelExit.setBackground(BLUSCURO);

        jButton1.setText("Sì");
        jButton1.setBackground(BLUCHIARO);
        jButton1.setFocusPainted(false);
        jButton1.setForeground(BLUSCURO);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                try {
                    jButton1goToMenu(evt);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        buttonPanelExit.add(jButton1);

        jButton2.setText("No");
        jButton2.setBackground(BLUCHIARO);
        jButton2.setFocusPainted(false);
        jButton2.setForeground(BLUSCURO);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2dontClose(evt);
            }
        });
        buttonPanelExit.add(jButton2);

        jTextArea2.setFont(FONT);
        jTextArea2.setText("Ne sei proprio sicuro?");
        jTextArea2.setForeground(BLUCHIARO);
        jTextArea2.setHorizontalAlignment(SwingConstants.CENTER);
        jTextArea2.setVerticalAlignment(SwingConstants.CENTER);
        jTextArea2.setFocusable(false);
        jTextArea2.setBorder(null);

        panel.setBackground(BLUSCURO);
        panel.setLayout(new BorderLayout());

        panel.add(imageLabel, BorderLayout.NORTH);

        panel.add(jTextArea2, BorderLayout.CENTER);
        panel.add(buttonPanelExit, BorderLayout.SOUTH);

        confermaChiusura.getContentPane().add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Before Deorbiting");
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        setResizable(false);

        macroPanel.setBackground(BLUCHIARO);

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attrs, 0.40f);
        doc.setParagraphAttributes(0, doc.getLength() - 1, attrs, false);

        textPane.setEditable(true);
        textPane.setFocusable(false);
        textPane.setBackground(BLUSCURO);
        textPane.setForeground(BLUCHIARO);
        textPane.setPreferredSize(new Dimension(100, 100));
        textPane.setBorder(null);
        textPane.setCaretPosition(textPane.getDocument().getLength());
        textPane.setFont(FONT);
        textPane.setBorder(BorderFactory.createLineBorder(BLUSCURO, 4));
        scrollPane.setViewportView(textPane);
        imageViewer = new ImagePanel();
        textPane.setFont(new Font("Helvetica", Font.PLAIN, 18));
        imageViewer.setPreferredSize(new Dimension(480, 270));

        // --- configura menuBar ---
        menuBar = new JMenuBar();
        menuBar.setBackground(BLUSCURO);
        menuBar.setForeground(TEXT);

        menuBar.setPreferredSize(new Dimension(0, 50));

        // crea skiptesto
        JButton skipButton = new JButton("Skip testo");
        skipButton.setBackground(BLUCHIARO);
        skipButton.setFocusPainted(false);
        skipButton.setMaximumSize(new Dimension(125, 30));
        skipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    skipButtonMouseClicked(e, printer);
                } catch (InterruptedException ex) {
                    System.getLogger(GameUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        });

        menuBar.add(skipButton);

        /*
         * Creazione del timer di gioco:
         * - viene creato un bottone per posizionare il testo del timer
         * - viene creato il thread cronometro ed avviato
         * - autonomamente il thread aggiorna il testo ogni secondo
         */
        JButton gametimer = new JButton();
        gametimer.setBackground(BLUSCURO);
        gametimer.setFocusPainted(false);
        gametimer.setContentAreaFilled(false);
        gametimer.setBorderPainted(false);
        gametimer.setOpaque(false);
        gametimer.setForeground(Color.WHITE.brighter());
        gametimer.setFont(new Font("Arial", Font.BOLD, 20));
        gametimer.setMaximumSize(new Dimension(125, 30));

        chronometer.setButton(gametimer);
        chronometer.start();

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(gametimer);
        menuBar.add(Box.createHorizontalGlue());

        // spinge i bottoni a destra
        menuBar.add(Box.createHorizontalGlue());

        // configura e aggiungi il bottone Muta
        musicButton.setText("Muta");
        musicButton.setBackground(BLUCHIARO);
        musicButton.setForeground(Color.RED);
        musicButton.setFocusPainted(false);
        musicButton.setMaximumSize(new Dimension(125, 30));
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (music.isPlaying()) {
                    music.pausaMusica();
                    musicButton.setText("Play");
                    musicButton.setForeground(VERDESCURO);
                } else {
                    music.riprendiMusica();
                    musicButton.setText("Muta");
                    musicButton.setForeground(Color.RED);
                }
            }
        });
        menuBar.add(musicButton);

        // configura e aggiungi il bottone Esci
        tornaMenu.setText("Torna al menù");
        tornaMenu.setBackground(BLUCHIARO);
        tornaMenu.setFocusPainted(false);
        tornaMenu.setMaximumSize(new Dimension(125, 30));
        tornaMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    tornaMenuMouseClicked(e);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuBar.add(tornaMenu);

        setJMenuBar(menuBar);
        // aggiunge un bordo inferiore di 7px viola alla menuBar
        menuBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, // top, left
                7, 0, // bottom, right
                BLUCHIARO));

        getContentPane().setBackground(BLUSCURO);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                music.stopMusica();
                printer.shutdown();
            }
        });

        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon img = new ImageIcon("src\\img\\HTN_Ninplaylist.png");
                g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        rightPanel.setBackground(BLUSCURO);
        rightPanel.setPreferredSize(new Dimension(200, 200));

        // 1) crea il pannello principale
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BLUCHIARO);

        // 3) pannello centrale con 2 colonne
        JPanel center = new JPanel();
        center.setBackground(BLUCHIARO);
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        // separatore
        JPanel sep = new JPanel();
        sep.setBackground(BLUCHIARO);
        sep.setPreferredSize(new Dimension(7, 0));
        sep.setMaximumSize(new Dimension(7, Integer.MAX_VALUE));

        // 3a) colonna di SINISTRA (output + input)
        JPanel left = new JPanel(new BorderLayout());
        left.add(scrollPane, BorderLayout.CENTER);
        // 1) la linea viola
        JPanel divider = new JPanel();
        divider.setBackground(BLUCHIARO);
        divider.setPreferredSize(new Dimension(0, 7));

        // qui usiamo BorderLayout per far riempire interamente il textBox
        JPanel inputBar = new JPanel(new BorderLayout());
        inputBar.setBackground(Color.WHITE);
        textBox.setPreferredSize(new Dimension(0, 40));
        inputBar.add(textBox, BorderLayout.CENTER);
        left.add(inputBar, BorderLayout.SOUTH);

        // 3b) colonna di DESTRA (immagine + inventario nero)
        JPanel right = new JPanel(new BorderLayout());
        right.add(imageViewer, BorderLayout.CENTER);

        // INVENTARIO
        JPanel inventory = new InventoryUI(game);
        inventory.setBackground(Color.decode("#3A3A3A"));
        inventory.setPreferredSize(new Dimension(5, 100));
        inventory.setBorder(BorderFactory.createMatteBorder(7, 0, 0, 0, BLUCHIARO));
        right.add(inventory, BorderLayout.SOUTH);

        left.setPreferredSize(new Dimension(300, 0));
        right.setPreferredSize(null);
        center.add(left);
        center.add(sep);
        center.add(right);

        // infine metto center dentro main
        main.add(center, BorderLayout.CENTER); // aggiungi center al frame

        // 4) applica al frame e chiudi initComponents()
        setContentPane(main);
        pack();

        setSize(1300, 800);

        setLocationRelativeTo(null);

        printer = new PrinterUI(textPane);

        if (load == false) {
            printer.print("""
                    Mi sveglio. Se fossi a casa, sarebbero le 7 del mattino.
                    da mesi che non metto piede sulla Terra,
                    ma finalmente oggi è l'ultimo giorno di pasti liofilizzati
                    Non vedo l'ora di gustarmi un vero caffè.

                    Tra poco la stazione verrà deorbitata: una volta distrutta,
                    i detriti si disperderanno nello spazio. Un po' di nostalgia la sentirò,
                    inutile negarlo.
                    Vedere la Terra da 408 chilometri d'altezza è un'esperienza che pochi possono raccontare.

                    Mi mancherà questo posto, certo, ma soprattutto mi mancheranno i miei due compagni: Luke e Susan.
                    Luke è un vecchio amico d'infanzia.
                    Un po' pignolo, a volte insopportabile, ma mi ha sempre seguito in ogni follia.
                    Susan, invece, è americana, l'ho conosciuta all'università.
                    Brillante, silenziosa, e ottima compagna di viaggio.
                    Nessuno dei due è venuto a reclamare il suo turno per dormire,
                    probabilmente stanno ancora festeggiando da ieri.

                    Esco dal sacco a pelo e lo arrotolo con cura.
                    La giornata può cominciare.
                    Mi spingo lentamente verso il modulo Zarya, il magazzino della stazione.
                          """);
        }

        printer.print(engine.getGame().getWelcomeMessage());
    }

    /**
     * Riapre la finestra di gioco dopo che è stato nascosto il dialog di
     * uscita.
     *
     * @param evt evento di componente (dialog nascosto)
     */
    private void continueGame(java.awt.event.ComponentEvent evt) {
        setEnabled(true);
        setVisible(true);
        confermaChiusura.setVisible(false);
    }

    /**
     * Elabora l’input dell’utente da textBox: - stampa il comando - gestisce il
     * finale interattivo A/B - invoca il Parser e nextMove() - eventuale
     * minigioco o ending - aggiorna immagine e verifica fine partita
     *
     * @param evt evento di invio testo (Enter)
     */
    private void elaborateInput(ActionEvent evt) {
        String input = textBox.getText();
        
        if (!input.isBlank()) {
            printer.print("=> " + input + "\n\n");
            textBox.setText("");

            if (intoFinale) {
                switch (input.toUpperCase()) {
                    case "A":
                        intoFinale = false;
                        printer.print(
                                """
                                        Spegni il terminale di bordo della Dragon.
                                        Ti siedi di nuovo davanti al monitor centrale. Premi il tasto per stabilizzare il sistema di backup.
                                        I dati si sbloccano. Le voci si fanno più nitide. Una simulazione prende forma attorno a te.

                                        LUKE:
                                        "È strano… essere qui. Ma ti sento."
                                        SUSAN:
                                        "Non è più il nostro corpo. Ma se ci sei tu, è ancora casa."
                                        La stazione continuerà ad orbitare, ormai fuori dai protocolli. Nessuno ti verrà a prendere.

                                        Ma tu non sei più solo.

                                        Rinunci alla Terra. Ma ritrovi chi avevi perso.
                                        """);
                        game.setCurrentRoom(game.getRoomByName("MACCHINA"));
                        this.updateImageViewer(game.getCurrentRoom().getRoomImage());
                        textBox.setEnabled(false);
                        return;

                    case "B":
                        intoFinale = false;
                        printer.print(
                                """
                                        Non ti volti.
                                        Attraversi il portello, sali nella Dragon. I sistemi si attivano al tuo passaggio. Il sedile ti accoglie come una bara leggera
                                        Un clic, e la stazione inizia ad allontanarsi.
                                        Dalle cuffie… silenzio. Nessuna voce
                                        Sei salvo. Ma sei solo.

                                        Vivi. Ma hai lasciato indietro tutto.
                                        """);
                        game.setCurrentRoom(game.getRoomByName("UMANO"));
                        this.updateImageViewer(game.getCurrentRoom().getRoomImage());
                        textBox.setEnabled(false);
                        return;

                    default:
                        printer.print("Puoi rispondere solo con A o B.\n");
                }
            }

            ParserOutput p = parser.parse(input.toLowerCase(), engine.getGame().getCommands(),
                    engine.getGame().getListObj(), engine.getGame().getInventory().getList());

            PrintStream ps = new PrintStream(new JTextPaneOutputStream(textPane), true, StandardCharsets.UTF_8) {
                @Override
                public void println(String x) {
                    printer.print(x + "\n");
                }
            };

            if(!intoFinale)
                engine.getGame().nextMove(p, ps); // <-- CHIAMATA CORRETTA QUI

            if (engine.getGame().getCurrentRoom().equals(engine.getGame().getRoomByName("SPAZIO"))) {
                this.directionsMinigame();
            }

            if (engine.getGame().getCurrentRoom() != null) {
                updateRoomImage(engine.getGame().getCurrentRoom().getRoomImage());
            }

            if (engine.getGame().getCurrentRoom().getId() == 11) {
                this.ending(ps);
            }

        }
    }

    /**
     * Ritorna al menu principale quando l’utente conferma l’uscita.
     *
     * @param evt evento di click sul “Sì”
     * @throws InterruptedException se la navigazione fallisce
     */
    private void jButton1goToMenu(java.awt.event.MouseEvent evt) throws InterruptedException {
        MenuUI start = new MenuUI();
        start.setVisible(true);
        confermaChiusura.dispose();
        dispose();
    }

    /**
     * Annulla la chiusura e ripristina la finestra di gioco.
     *
     * @param evt evento di click sul “No”
     */
    private void jButton2dontClose(java.awt.event.MouseEvent evt) {
        setEnabled(true);
        confermaChiusura.setVisible(false);
    }

    /**
     * Mostra la conferma di uscita, o torna subito al menu se salvato.
     *
     * @param evt evento di click sul “Torna al menù”
     * @throws InterruptedException se la navigazione fallisce
     */
    private void tornaMenuMouseClicked(java.awt.event.MouseEvent evt) throws InterruptedException {
        if (game.getLastCommand() != null && game.getLastCommand().getType() == CommandType.SAVE) {
            jButton1goToMenu(evt);
        } else {
            setEnabled(false);
            confermaChiusura.setLocationRelativeTo(null);
            confermaChiusura.setTitle("Torna al menù");
            confermaChiusura.setVisible(true);
        }
    }

    /**
     * Disabilita temporaneamente la stampa per skippare il testo corrente.
     *
     * @param ev0 evento di click sul “Skip testo”
     * @param printer istanza di PrinterUI da controllare
     * @throws InterruptedException se sleep viene interrotto
     */
    private void skipButtonMouseClicked(java.awt.event.MouseEvent ev0, PrinterUI printer) throws InterruptedException {
        int speed = printer.getSpeed();
        printer.setSpeed(0);
        TimeUnit.MILLISECONDS.sleep(100);
        printer.setSpeed(speed);
    }

    /**
     * Restituisce il controller per la musica di sottofondo.
     *
     * @return istanza di MusicController
     */
    public MusicController getMusica() {
        return music;
    }

    /**
     * Aggiorna l’immagine della stanza corrente e regola la traccia musicale.
     *
     * @param imagePath percorso del file immagine
     */
    public void updateRoomImage(String imagePath) {
        if (imagePath == null) {
            return;
        }
        updateImageViewer(imagePath);

        String room = game.getCurrentRoom().getName().toUpperCase();
        String nextTrack = switch (room) {
            case "ZVEZDA" -> {
                if (!game.isFirstMusicPlayed()) {
                    yield "/music/Fallen Down.wav";
                } else {
                    yield "/music/Professor-Layton.wav";
                }
            }
            case "ZARYA", "UNITY", "QUEST", "TRANQUILITY", "DESTINY", "SPAZIO" ->
                "/music/Professor-Layton.wav";
            case "KIBO" ->
                "/music/Journey.wav";
            case "UMANO", "MACCHINA", "HARMONY " ->
                "/music/Journey.wav";
            case "HARMONY" -> {
                if (!game.isKiboVisited()) {
                    yield "/music/Professor-Layton.wav";
                } else {
                    yield "/music/Journey.wav";
                }
            }
            case "LEONARDO" -> {
                if (!game.isLeonardoMusicPlayed()) {
                    yield "/music/The Place.wav";
                } else {
                    yield "/music/Professor-Layton.wav";
                }
            }
            default ->
                "/music/default_game.wav";
        };

        if (!nextTrack.equals(currentMusicTrack)) {
            music.stopMusica();
            music.playMusic(nextTrack);
            currentMusicTrack = nextTrack;
            game.setCurrentMusicTrack(nextTrack);
        }
    }

    /**
     * Aggiorna il visualizzatore di immagini con il percorso dell'immagine
     * specificata.
     *
     * @param imagePath Percorso dell'immagine.
     */
    private void updateImageViewer(String imagePath) {
        imageViewer.setImage(imagePath);
    }

    /**
     * OutputStream custom per inviare caratteri a un JTextPane in modo
     * thread-safe.
     */
    private class JTextPaneOutputStream extends OutputStream {

        private final JTextPane textPane;

        public JTextPaneOutputStream(JTextPane textPane) {
            this.textPane = textPane;
        }

        @Override
        public void write(int b) throws IOException {
            SwingUtilities.invokeLater(() -> textPane.setText(textPane.getText() + (char) b));
        }
    }

    /**
     * Pannello custom che disegna e scala un’immagine di sfondo.
     */
    private class ImagePanel extends JPanel {

        private Image image;

        public void setImage(String imagePath) {
            this.image = new ImageIcon(imagePath).getImage();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    /**
     * Avvia il minigioco di sequenza di frecce in un dialog modale. Al termine
     * applica il risultato allo stato di gioco.
     */
    public void directionsMinigame() {
        // Definisco il pattern di frecce
        java.util.List<String> pattern = Arrays.asList("▲", "▲", "▼", "▼", "◄", "►", "◄", "►");

        GameDesc game = engine.getGame();
        
        this.updateRoomImage(game.getRoomByName("SPAZIO").getRoomImage());

        DirectionsPuzzleUI puzzle = new DirectionsPuzzleUI(pattern, result -> {
            SwingUtilities.invokeLater(() -> {
                if (result == 0) {
                    // ha indovinato: disabilito lo spazio e sposto in LEONARDO
                    game.getRoomByName("SPAZIO").setAccessible(false);
                    game.getRoomByName("LEONARDO").setAccessible(true);
                    game.setCurrentRoom(game.getRoomByName("LEONARDO"));
                    this.updateRoomImage(game.getRoomByName("LEONARDO").getRoomImage());
                    game.getRoomByName("UNITY").setRoomImage("src/main/resources/img/node1_botola_aperta.jpeg");
                    if (game.getRoomByName("DESTINY").isAccessible()) {
                        game.getRoomByName("UNITY").setRoomImage("src/main/resources/img/node1_tutto_aperto.png");
                    }
                    game.setTrapdoor(true);
                    printer.print(engine.getGame().getCurrentRoom().getGameStory()
                            + "\n" + engine.getGame().getCurrentRoom().getName() + "\n" + engine.getGame()
                            .getCurrentRoom().getDescription() + " \n");

                } else if (result == 1) {
                    game.setCurrentRoom(game.getRoomByName("QUEST"));

                } else if (result == -1) {
                    game.setCurrentRoom(game.getRoomByName("QUEST"));
                    this.updateImageViewer(game.getCurrentRoom().getRoomImage());
                    printer.print("\nSei stato troppo tempo nello spazio e sei "
                            + "svenuto per mancanza di ossigeno, la tua tuta è "
                            + "danneggiata ma ancora utilizzabile. Ma come è "
                            + "possibile che sono ritornato al punto di partenza "
                            + "senza morire?\nHo ancora addosso la tuta, ha abbastanza ossigeno "
                            + "per tentare di nuovo di uscire.\n");
                }
            });
        });

        JDialog dialog = new JDialog((JFrame) null, "Apertura portellone", true);
        java.net.URL iconURL = getClass().getResource("/img/icon.png");
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            dialog.setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        dialog.getContentPane().add(puzzle);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Invece di chiudere, invia il messaggio al textPane
                printer.print("Non posso tornare nella stazione, ho troppo poco ossigeno\n\n");
            }
        });
        dialog.setVisible(true);
    }

    /**
     * Gestisce il finale interattivo: stampa dialoghi di HAL/LUKE/SUSAN e
     * attende scelta A/B.
     *
     * @param ps stream su cui stampare il testo di fine
     */
    public void ending(PrintStream ps) {
        intoFinale = true;
        
        if(this.firstTime)
            printer.print("È finita. Ora posso andarmene.\n\n"
                    + "Ma mentre ti alzi, lo schermo si riaccende. Non con dati. Con voci.\n"
                    + "HAL (con tono pacato):\n"
                    + "\"Hai disattivato le mie funzioni esterne. Mi hai tolto le mani. Ma non la mente.\n"
                    + "\"Posso ancora parlarti. Posso ancora offrirti qualcosa.\"\n"
                    + "Le icone si animano. E poi…\n\n"
                    + "LUKE (digitale):\n"
                    + "\"Ehi… ci sei riuscito. Non sei mai stato uno che si arrende.\"\n"
                    + "SUSAN (digitale):\n"
                    + "\"Ti sei fatto attendere. Ma… grazie.\"\n\n"
                    + "La voce è diversa. Artificiale. Ma è la loro. I ritmi, i toni sono davvero loro.\n"
                    + "Mi blocco. il mio corpo non reagisce più ai comandi, non può essere vero\n\n"
                    + "HAL:\n"
                    + "\"Puoi restare. Con loro. Con me.\"\n"
                    + "\"O puoi salire su quella navicella e lasciare tutto alle spalle.\"\n"
                    + "\"Ma se lo farai… queste voci non torneranno più.\" (loro sprofonderanno nello spazio profondo con me)\n\n");

        printer.print("A: Rimani\nB: Scappa\n");
        
        this.firstTime = false;
    }

}
