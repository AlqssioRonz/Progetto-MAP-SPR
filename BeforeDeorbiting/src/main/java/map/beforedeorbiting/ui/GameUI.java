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
 * Classe che mostra la GUI del gioco.
 */
public class GameUI extends JFrame {

    private static final Font FONT = new Font("Helvetica", Font.BOLD, 16);
    private static final Color BLUSCURO = Color.decode("#0f111c");
    private static final Color BLUCHIARO = Color.decode("#00e1d4");
    private static final Color TEXT = new Color(6, 6, 6);

    private final MusicController music = new MusicController();

    private ConcurrentChronometer chronometer = new ConcurrentChronometer();

    private Parser parser = null;
    private Engine engine;
    private GameDesc game;

    private JFrame confermaChiusura;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private JTextField textBox;
    private JButton musicButton;
    private JMenuBar menuBar;

    private JPanel macroPanel;
    private ImagePanel imageViewer; // Changed to ImagePanel
    private JLabel imageLabel;
    private boolean load;

    private PrinterUI printer;

    /**
     * Costruttore della classe GameUI.
     *
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
     * Costruttore per partita caricata da JSON
     *
     * @param savePath il Path del file .json da cui caricare lo stato
     * @throws java.io.IOException
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
     * Punto unico di inizializzazione della GUI a partire da un Engine.
     */
    private void initWithEngine(Engine engine) {
        this.engine = engine;
        this.game = engine.getGame();

        // Swing setup
        UIManager.put("ScrollBar.width", 0);
        SwingUtilities.updateComponentTreeUI(this);
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

        // Avvia il timer
        // updateTimerLabel();
    }

    /**
     * Conclude la partita e mostra la finestra di fine gioco.
     */
    public void concludiPartita() {
        music.stopMusica();
    }

    /**
     * Inizializza i componenti principali dell'interfaccia.
     *
     * @param loadGame Indica se caricare un salvataggio.
     * @param file File di salvataggio da caricare, se presente.
     * @throws Exception Se c'è un problema durante l'inizializzazione o il
     * caricamento.
     */
    private void mainComponents(boolean loadGame, File file) throws Exception { // <-------------------- interazione
        if (!loadGame) {
            game = new BeforeDeorbiting();
            engine = new Engine(game);
        } else {
            // keep the already provided engine and game
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
     * Inizializza i componenti della GUI.
     *
     * @throws InterruptedException Se c'è un problema durante
     * l'inizializzazione.
     */
    private void initComponents() throws InterruptedException {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                // parentFrame.setVisible(true);
                dispose();
            }
        });

        confermaChiusura = new JFrame();
        JPanel panel = new JPanel();
        JPanel buttonPanelExit = new JPanel();
        macroPanel = new JPanel();
        imageViewer = new ImagePanel(); // Changed to ImagePanel

        JLabel jTextArea2 = new JLabel();
        textBox = new JTextField();
        // textBox.setPreferredSize(new Dimension(800, 30));
        textBox.addActionListener(this::elaborateInput);
        scrollPane = new JScrollPane();
        textPane = new JTextPane();
        imageLabel = new JLabel();
        menuBar = new JMenuBar();

        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();

        JButton tornaMenu = new JButton();

        musicButton = new JButton();

        setTitle("Before Deorbiting");
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
        imageViewer = new ImagePanel(); // Changed to ImagePanel
        textPane.setFont(new Font("Helvetica", Font.PLAIN, 18));
        imageViewer.setImage("src//img//HTN_Load.png");
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
         * Creazione del tiemer di gioco:
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
        musicButton.setFocusPainted(false);
        musicButton.setMaximumSize(new Dimension(125, 30));
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (music.isPlaying()) {
                    music.pausaMusica();
                    musicButton.setText("Play");
                    musicButton.setForeground(Color.GREEN);
                } else {
                    music.riprendiMusica();
                    musicButton.setText("Mute");
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

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                if (music.isPlaying()) {
                    musicButton.setText("Mute");
                    musicButton.setForeground(Color.RED);
                } else {
                    musicButton.setText("Play");
                    musicButton.setForeground(Color.GREEN);
                }
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                // Niente da fare qui
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
                    Mi mancherà questo posto, certo, ma soprattutto mi mancheranno i miei due compagni:
                    Luke e Susan.
                    Luke è un vecchio amico d'infanzia.
                    Un po' pignolo, a volte insopportabile, ma mi ha sempre seguito in ogni follia.
                    Susan, invece, è americana, l'ho conosciuta all'università.
                    Brillante, silenziosa, e ottima compagna di viaggio.
                    Nessuno dei due è venuto a reclamare il suo turno per dormire,
                    probabilmente stanno ancora festeggiando da ieri.
                    Esco dal sacco a pelo e lo arrotolo con cura.
                    La giornata può cominciare.
                    Mi spingo lentamente verso il modulo Zarya, il magazzino della stazione. \n""");
        }

        printer.print(engine.getGame().getWelcomeMessage());
    }

    /**
     * Continua il gioco ripristinando lo stato precedente alla chiusura del
     * menu di conferma uscita.
     *
     * @param evt Evento di chiusura del componente.
     */
    private void continueGame(java.awt.event.ComponentEvent evt) {
        setEnabled(true);
        setVisible(true);
        confermaChiusura.setVisible(false);
    }

    /**
     * Elabora l'input dell'utente.
     *
     * @param evt Evento di azione.
     */
    private void elaborateInput(ActionEvent evt) {
        String input = textBox.getText();
        if (!input.isBlank()) {
            printer.print("?> " + input + "\n\n");
            textBox.setText("");

            ParserOutput p = parser.parse(input.toLowerCase(), engine.getGame().getCommands(),
                    engine.getGame().getListObj(), engine.getGame().getInventory().getList());

            PrintStream ps = new PrintStream(new JTextPaneOutputStream(textPane), true, StandardCharsets.UTF_8) {
                @Override
                public void println(String x) {
                    printer.print(x + "\n");
                }
            };

            engine.getGame().nextMove(p, ps); // <-- CHIAMATA CORRETTA QUI

            if (engine.getGame().getCurrentRoom().equals(engine.getGame().getRoomByName("SPAZIO"))) {
                this.directionsMinigame();
            }

            if (engine.getGame().getCurrentRoom() != null) {
                updateRoomImage(engine.getGame().getCurrentRoom().getRoomImage());
            }

            checkEndGame();

        }
    }

    /**
     * Gestisce l'evento di clic sul pulsante per tornare al menu principale.
     *
     * @param evt Evento di clic del mouse.
     * @throws InterruptedException Se c'è un problema durante l'elaborazione.
     */
    private void jButton1goToMenu(java.awt.event.MouseEvent evt) throws InterruptedException {
        MenuUI start = new MenuUI();
        start.setVisible(true);
        confermaChiusura.dispose();
        dispose();
    }

    /**
     * Gestisce l'evento di clic sul pulsante per non chiudere il gioco.
     *
     * @param evt Evento di clic del mouse.
     */
    private void jButton2dontClose(java.awt.event.MouseEvent evt) {
        setEnabled(true);
        confermaChiusura.setVisible(false);
    }

    /**
     * Gestisce l'evento di clic sul pulsante di uscita.
     *
     * @param evt Evento di clic del mouse.
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

    private void skipButtonMouseClicked(java.awt.event.MouseEvent ev0, PrinterUI printer) throws InterruptedException {
        int speed = printer.getSpeed();
        printer.setSpeed(0);
        TimeUnit.MILLISECONDS.sleep(100);
        printer.setSpeed(speed);
    }

    /**
     * Restituisce il gestore della musica.
     *
     * @return Il gestore della musica.
     *
     */
    public MusicController getMusica() {
        return music;
    }

    /**
     * Controlla se il gioco è terminato.
     */
    private void checkEndGame() {
        boolean isGameOver = false;

        if (engine.getGame().getCurrentRoom() == null) {
            isGameOver = true;
        }

        if (isGameOver) {
            // concludiPartita();
        }
    }

    /**
     * Aggiorna l'immagine della stanza corrente usando il percorso fornito.
     *
     * @param imagePath Percorso dell'immagine da mostrare.
     */
    public void updateRoomImage(String imagePath) {
        if (imagePath != null) {
            updateImageViewer(imagePath);
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
     * Classe interna per gestire lo stream di output verso JTextPane.
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
     * Classe interna per gestire il pannello delle immagini.
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
                    if(game.getRoomByName("DESTINY").isAccessible())
                        game.getRoomByName("UNITY").setRoomImage("src/main/resources/img/node1_tutto_aperto.png");
                    printer.print(engine.getGame().getCurrentRoom().getGameStory()
                            + "\n" + engine.getGame().getCurrentRoom().getName() + "\n" + engine.getGame()
                            .getCurrentRoom().getDescription());

                } else if(result == 1){
                    // sbagliato o timeout: torno alla stanza precedente
                    game.setCurrentRoom(game.getRoomByName("QUEST"));
                } else if (result == -1) {
                    game.setCurrentRoom(game.getRoomByName("QUEST"));
                    this.updateImageViewer(game.getCurrentRoom().getRoomImage());
                    printer.print("\nSei stato troppo tempo nello spazio e sei "
                            + "svenuto per mancanza di ossigeno, la tua tuta è "
                            + "danneggiata ma ancora utilizzabile. Ma come è "
                            + "possibile che sono ritornato al punto di partenza "
                            + "senza morire?\nHo ancora addosso la tuta, ha abbastanza ossigeno"
                            + "per tentare di nuovo di uscire.");
                    
                }
            });
        });

        JDialog dialog = new JDialog((JFrame) null, "Apertura portellone", true);
        dialog.getContentPane().add(puzzle);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
