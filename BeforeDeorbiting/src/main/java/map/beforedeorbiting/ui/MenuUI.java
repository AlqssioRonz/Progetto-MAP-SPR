package map.beforedeorbiting.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import map.beforedeorbiting.database.DBConfig;

/**
 * Classe che rappresenta il menu principale del gioco Before Deorbiting.
 * Contiene pulsanti per avviare, caricare e visualizzare comandi/crediti, più
 * un toggle per l'audio.
 *
 * @author ronzu
 */
public class MenuUI extends JFrame {

    private static final Color BACKGROUND = new Color(13, 12, 33);
    private static final Color BORDER = new Color(10, 9, 26);
    private static final Color TEXT = new Color(182, 180, 209);

    private static boolean serverOn = false;
    private boolean audioOn = true;

    private JPanel background;
    private JButton start;
    private JButton load;
    private JButton commands;
    private JButton credits;
    private JButton audio;
    private JFileChooser fileChooser;

    public MenuUI() throws InterruptedException {
        if (!serverOn) {
            serverOn = true;
            Thread.sleep(1200);
        }
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Before Deorbiting");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\img\\HTN_Logo.png"));
        setPreferredSize(new Dimension(1100, 700));
        setResizable(false);

        // --- pannello con sfondo ---
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/intro.png"));
        Image bgImg = icon.getImage().getScaledInstance(1100, 700, Image.SCALE_SMOOTH);
        background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // --- bottoni di testo (custom paintComponent per mantenere α) ---
        start = createTextButton("Nuova Partita");
        load = createTextButton("Carica Partita");
        commands = createTextButton("Comandi di Gioco");
        credits = createTextButton("Crediti e Classifica");

        // --- pulsante audio come immagine ---
        audio = new JButton();
        audio.setPreferredSize(new Dimension(64, 64));
        audio.setMargin(new Insets(0, 0, 0, 0));
        audio.setBorder(BorderFactory.createLineBorder(BORDER, 5));
        audio.setContentAreaFilled(false);
        Icon ico = loadAudioIcon();
        audio.setIcon(ico);
        audio.setRolloverIcon(ico);
        audio.setPressedIcon(ico);

        // file chooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./saves"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("File JSON", "json"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        // azioni
        start.addActionListener(this::onStart);
        load.addActionListener(this::onLoad);
        commands.addActionListener(e -> {
            CommandsUI help = CommandsUI.getInstance();
            help.setLocationRelativeTo(this);
            help.setVisible(true);
        });
        credits.addActionListener(e -> System.exit(0));
        audio.addActionListener(e -> toggleAudio());

        // layout
        setGroupLayout();
        pack();
        setLocationRelativeTo(null);
    }

    private void onStart(ActionEvent e) {
        // 0) Tronco il file notebook.txt
        Path notebookFile = Paths.get("notebook.txt");
        try {
            Files.writeString(
                    notebookFile,
                    "", // contenuto vuoto
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(MenuUI.class.getName())
                    .log(Level.SEVERE, "Impossibile resettare il taccuino", ex);
        }

        // 1) Rimuovo tutti i componenti del menu
        getContentPane().removeAll();
        repaint();

        // 2) Creo e aggiungo LoadBarUI alla stessa finestra
        LoadBarUI loadBarPanel = new LoadBarUI();
        setResizable(false);
        setTitle("Caricamento in corso...");
        getContentPane().add(loadBarPanel);
        validate();

        // 3) Listener per isFinished
        loadBarPanel.addPropertyChangeListener(evt -> {
            if ("isFinished".equals(evt.getPropertyName())
                    && Boolean.TRUE.equals(evt.getNewValue())) {
                SwingUtilities.invokeLater(() -> {
                    GameUI gameUI = new GameUI();
                    gameUI.setVisible(true);
                    dispose();
                });
            }
        });

        // 4) Avvio l'animazione
        loadBarPanel.startLoadBar();
    }

    private void onLoad(ActionEvent e) {
        fileChooser.setDialogTitle("Seleziona salvataggio JSON");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path path = fileChooser.getSelectedFile().toPath();
            try {
                // avvia GameUI caricando direttamente dal JSON
                GameUI gioco = new GameUI(path);
                gioco.setVisible(true);
                this.dispose(); // chiude il menu principale
            } catch (IOException ex) {
                Logger.getLogger(MenuUI.class.getName())
                        .log(Level.SEVERE, "Errore nel caricamento del JSON", ex);
                JOptionPane.showMessageDialog(
                        this,
                        "Impossibile caricare il salvataggio:\n" + ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createTextButton(String text) {
        // Colore di fondo semitrasparente
        int alpha = (int) (255 * 0.75);
        Color bg = new Color(
                BACKGROUND.getRed(),
                BACKGROUND.getGreen(),
                BACKGROUND.getBlue(),
                alpha);
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // dipingi sempre lo sfondo con α desiderata
                g.setColor(bg);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

        };
        btn.setFocusPainted(false);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Consolas", Font.BOLD, 16));
        btn.setOpaque(false); // disabilita fill LAF
        btn.setContentAreaFilled(false); // disabilita fill LAF
        btn.setBorder(BorderFactory.createLineBorder(BORDER, 5));
        Dimension sz = new Dimension(200, 64);
        btn.setPreferredSize(sz);
        btn.setMinimumSize(sz);
        btn.setMaximumSize(sz);
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    private Icon loadAudioIcon() {
        String path = audioOn ? "/img/audio_on.png" : "/img/audio_off.png";
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            // Fallback: icona di sistema se il file non viene trovato
            return UIManager.getIcon("OptionPane.informationIcon");
        }
        ImageIcon ico = new ImageIcon(resource);
        Image img = ico.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void toggleAudio() {
        // Inverti lo stato
        audioOn = !audioOn;
        // Ricarica l'icona in base al nuovo stato
        Icon icon = loadAudioIcon();
        audio.setIcon(icon);
        audio.setRolloverIcon(icon);
        audio.setPressedIcon(icon);
        audio.repaint();
    }

    private void setGroupLayout() {
        GroupLayout gl = new GroupLayout(background);
        background.setLayout(gl);

        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl.createSequentialGroup()
                                .addGap(50)
                                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(start, 200, 200, 200)
                                        .addComponent(load, 200, 200, 200)
                                        .addComponent(commands, 200, 200, 200)
                                        .addComponent(credits, 200, 200, 200)))
                        .addGroup(gl.createSequentialGroup()
                                .addContainerGap(0, Short.MAX_VALUE) // “spinge” tutto a sinistra
                                .addComponent(audio,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(50) // 30px dal bordo destro
                        ));

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(200)
                        .addComponent(start, 64, 64, 64)
                        .addGap(32)
                        .addComponent(load, 64, 64, 64)
                        .addGap(32)
                        .addComponent(commands, 64, 64, 64)
                        .addGap(32)
                        .addComponent(credits, 64, 64, 64)
                        .addGap(30)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, Short.MAX_VALUE, Short.MAX_VALUE)
                        .addComponent(audio, 64, 64, 64)
                        .addGap(50));

        GroupLayout fl = new GroupLayout(getContentPane());
        getContentPane().setLayout(fl);
        fl.setHorizontalGroup(
                fl.createParallelGroup()
                        .addComponent(background,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));
        fl.setVerticalGroup(
                fl.createParallelGroup()
                        .addComponent(background,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

    }

    @SuppressWarnings("unused")
    private void startGame() throws InterruptedException {
        System.out.println("\nGIOCO CARICATO CORRETTAMENTE\n");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            DBConfig.populateDatabase();
            try {
                new MenuUI().setVisible(true);
            } catch (InterruptedException ex) {
                Logger.getLogger(MenuUI.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
    }
}
