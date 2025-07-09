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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import map.beforedeorbiting.BeforeDeorbiting;
import map.beforedeorbiting.parser.ParserOutput;

/**
 * Classe che mostra la GUI del gioco.
 */
public class GameUI extends JFrame {

    private static final Font FONT = new Font("Helvetica", Font.BOLD, 16);
    private static final Color bluscuro = Color.decode("#0f111c");
    private static final Color bluchiaro = Color.decode("#00e1d4");
    private static final Color TEXT = new Color(6, 6, 6);
    private static final Color RED = new Color(238, 75, 43);

    // private final MusicHandler musicHTN = new MusicHandler();
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

    private JMenu tendina;
    private JMenuItem impostazioniItem;
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
        // initCurrentImage();

        // Se usi Parser, lo lasci dentro Engine (non qui)
        // Mostra subito stanza e testo
        // writeOnPanel(game.getCurrentRoom().getDescription());
        // setRoomImage(game.getCurrentRoom().getName());
        // Avvia il timer
        // updateTimerLabel();
    }

    /**
     * Conclude la partita e mostra la finestra di fine gioco.
     */
    /*
     * public void concludiPartita() {
     * musicHTN.stopMusica();
     * SwingUtilities.invokeLater(() -> {
     * HTN_InterfacciaEnding endingFrame = new HTN_InterfacciaEnding(this);
     * endingFrame.setVisible(true);
     * this.setVisible(false);
     * });
     * }
     */
    /**
     * Inizializza i componenti principali dell'interfaccia.
     *
     * @param loadGame Indica se caricare un salvataggio.
     * @param file     File di salvataggio da caricare, se presente.
     * @throws Exception Se c'è un problema durante l'inizializzazione o il
     *                   caricamento.
     */
    private void mainComponents(boolean loadGame, File file) throws Exception {
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
     *                              l'inizializzazione.
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

        JButton esciButton = new JButton();

        musicButton = new JButton();

        tendina = new JMenu();
        impostazioniItem = new JMenuItem();

        confermaChiusura.setIconImage(Toolkit.getDefaultToolkit().getImage("src\\img\\HTN_Logo.png"));
        confermaChiusura.setResizable(false);
        confermaChiusura.setSize(new Dimension(750, 480));
        confermaChiusura.setLocationRelativeTo(null);
        confermaChiusura.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        confermaChiusura.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                continueGame(evt);
            }
        });
        buttonPanelExit.setBackground(bluscuro);

        jButton1.setBackground(bluscuro);
        jButton1.setText("E-S-C-I");
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

        jButton2.setBackground(bluscuro);
        jButton2.setText("ci ripenso");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2dontClose(evt);
            }
        });
        buttonPanelExit.add(jButton2);

        jTextArea2.setFont(FONT);
        jTextArea2.setText("SAY 'ESCI' ONE MORE TIME");
        jTextArea2.setBackground(bluscuro);
        jTextArea2.setHorizontalAlignment(SwingConstants.CENTER);
        jTextArea2.setVerticalAlignment(SwingConstants.CENTER);
        jTextArea2.setFocusable(false);
        jTextArea2.setBorder(null);

        imageLabel.setIcon(new ImageIcon("src\\img\\HTN_esci.png"));
        imageLabel.setPreferredSize(new Dimension(682, 384));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        panel.setBackground(bluscuro);
        panel.setLayout(new BorderLayout());

        panel.add(imageLabel, BorderLayout.NORTH);

        panel.add(jTextArea2, BorderLayout.CENTER);
        panel.add(buttonPanelExit, BorderLayout.SOUTH);

        confermaChiusura.getContentPane().add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Before Deorbiting");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\img\\HTN_Logo.png"));
        setResizable(false);

        macroPanel.setBackground(bluchiaro);

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
        textPane.setBackground(bluscuro);
        textPane.setForeground(bluchiaro);
        textPane.setPreferredSize(new Dimension(100, 100));
        textPane.setBorder(null);
        textPane.setCaretPosition(textPane.getDocument().getLength());
        textPane.setFont(FONT);
        textPane.setBorder(BorderFactory.createLineBorder(bluscuro, 4));
        scrollPane.setViewportView(textPane);
        imageViewer = new ImagePanel(); // Changed to ImagePanel
        textPane.setFont(new Font("Helvetica", Font.PLAIN, 18));
        imageViewer.setImage("src//img//HTN_Load.png");
        imageViewer.setPreferredSize(new Dimension(480, 270));
        impostazioniItem.setBackground(bluscuro);
        impostazioniItem.setForeground(TEXT);
        impostazioniItem.setText("Impostazioni");
        impostazioniItem.setPreferredSize(new Dimension(105, 30));
        // impostazioniItem.addActionListener(this::impostazioniMouseClicked);

        // --- configura menuBar ---
        menuBar = new JMenuBar();
        menuBar.setBackground(bluscuro);
        menuBar.setForeground(TEXT);

        menuBar.setPreferredSize(new Dimension(0, 50));

        // crea JMenu “Opzioni”
        tendina = new JMenu("Opzioni");
        tendina.setBackground(bluscuro);
        tendina.setForeground(TEXT);
        tendina.setBorder(new LineBorder(bluscuro, 4));
        menuBar.add(tendina);

        // spinge i bottoni a destra
        menuBar.add(Box.createHorizontalGlue());

        // configura e aggiungi il bottone Mute
        musicButton.setText("Mute");
        musicButton.setBackground(bluscuro);
        musicButton.setForeground(RED);
        musicButton.setPreferredSize(new Dimension(105, 30));
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // il tuo listener esistente
            }
        });
        menuBar.add(musicButton);

        // configura e aggiungi il bottone Esci
        esciButton.setText("Esci");
        esciButton.setBackground(bluscuro);
        esciButton.setForeground(TEXT);
        esciButton.setPreferredSize(new Dimension(90, 30));
        esciButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                esciButtonMouseClicked(e);
            }
        });
        menuBar.add(esciButton);

        setJMenuBar(menuBar);
        // aggiunge un bordo inferiore di 7px viola alla menuBar
        menuBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, // top, left
                7, 0, // bottom, right
                bluchiaro));

        getContentPane().setBackground(bluscuro);

        // musicHTN.playMusic("src\\music\\HTN_gameplaylist.wav");
        musicButton.setText("Mute");
        musicButton.setBackground(bluscuro);
        musicButton.setForeground(RED);
        musicButton.setPreferredSize(new Dimension(105, 30));

        musicButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                /*
                 * if (musicHTN.isPlaying()) {
                 * musicHTN.pausaMusica();
                 * musicButton.setText("Play");
                 * musicButton.setForeground(GREEN);
                 * } else {
                 * musicHTN.riprendiMusica();
                 * musicButton.setText("Mute");
                 * musicButton.setForeground(RED);
                 * }
                 */
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                // musicHTN.stopMusica();
                printer.shutdown();
            }
        });

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                /*
                 * if (musicHTN.isPlaying()) {
                 * musicButton.setText("Mute");
                 * musicButton.setForeground(RED);
                 * } else {
                 * musicButton.setText("Play");
                 * musicButton.setForeground(GREEN);
                 * }
                 */
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                // Niente da fare qui
            }
        });

        esciButton.setBackground(bluscuro);
        esciButton.setForeground(TEXT);
        esciButton.setText("Esci");
        esciButton.setPreferredSize(new Dimension(90, 30));
        esciButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esciButtonMouseClicked(evt);
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
        rightPanel.setBackground(bluscuro);
        rightPanel.setPreferredSize(new Dimension(200, 200));

        // 1) crea il pannello principale
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(bluchiaro);

        // 3) pannello centrale con 2 colonne
        JPanel center = new JPanel();
        center.setBackground(bluchiaro);
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        // separatore
        JPanel sep = new JPanel();
        sep.setBackground(bluchiaro);
        sep.setPreferredSize(new Dimension(7, 0));
        sep.setMaximumSize(new Dimension(7, Integer.MAX_VALUE));

        // 3a) colonna di SINISTRA (output + input)
        JPanel left = new JPanel(new BorderLayout());
        left.add(scrollPane, BorderLayout.CENTER);
        // 1) la linea viola
        JPanel divider = new JPanel();
        divider.setBackground(bluchiaro);
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

        JPanel inventory = new JPanel();
        inventory.setBackground(Color.decode("#3A3A3A"));
        inventory.setPreferredSize(new Dimension(0, 100));
        inventory.setBorder(BorderFactory.createMatteBorder(7, 0, 0, 0, bluchiaro));
        right.add(inventory, BorderLayout.SOUTH);

        left.setPreferredSize(new Dimension(350, 0));
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

            if (engine.getGame().getCurrentRoom() != null) {
                updateRoomImage(engine.getGame().getCurrentRoom().getRoomImage());
            }

            checkEndGame();
        }
    }

    /**
     * Mostra la finestra delle impostazioni.
     *
     * @param evt Evento di azione.
     *
     *            private void impostazioniMouseClicked(ActionEvent evt) {
     *            HTN_InterfacciaSettings impostazioni = new GameUI(GameUI.this);
     *            impostazioni.setVisible(true); }*
     */
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
    private void esciButtonMouseClicked(java.awt.event.MouseEvent evt) {
        setEnabled(false);
        confermaChiusura.setLocationRelativeTo(null);
        confermaChiusura.setTitle("Before Deorbiting");
        confermaChiusura.setVisible(true);
    }

    /**
     * Restituisce il gestore della musica.
     *
     * @return Il gestore della musica.
     *
     *         public MusicHandler getMusica() { return musicHTN; }
     */
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
     * Aggiorna l'immagine della stanza corrente.
     *
     * @param roomName Nome della stanza.
     */
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
}