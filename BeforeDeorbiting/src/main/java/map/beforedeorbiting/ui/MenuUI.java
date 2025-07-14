/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import map.beforedeorbiting.database.DBConfig;
import map.beforedeorbiting.util.MusicController;
import map.beforedeorbiting.util.RestServer;

/**
 * Classe che rappresenta il menu principale del gioco Before Deorbiting.
 * Contiene pulsanti per avviare, caricare e visualizzare comandi/crediti, più
 * un toggle per l'audio e un server REST per la classifica online.
 *
 * @author ronzu
 */
public class MenuUI extends JFrame {

    /**
     * Colore di sfondo principale del menu.
     */
    private static final Color BACKGROUND = new Color(13, 12, 33);

    /**
     * Colore del bordo dei bottoni e dei pannelli.
     */
    private static final Color BORDER = new Color(10, 9, 26);

    /**
     * Colore del testo dei bottoni.
     */
    private static final Color TEXT = new Color(182, 180, 209);

    /**
     * Controller per la musica di sottofondo.
     */
    private final MusicController music = new MusicController();

    /**
     * Server REST per gestire la classifica online.
     */
    private RestServer restServer;

    /**
     * Flag che indica se il server REST è già stato avviato.
     */
    private static boolean serverStarted;

    /**
     * Stato dell'audio (true = attivo, false = disattivato).
     */
    private boolean audioOn = true;

    /**
     * Pannello principale con sfondo personalizzato.
     */
    private JPanel background;

    /**
     * Bottone per iniziare una nuova partita.
     */
    private JButton start;

    /**
     * Bottone per caricare una partita salvata.
     */
    private JButton load;

    /**
     * Bottone per visualizzare i comandi di gioco.
     */
    private JButton commands;

    /**
     * Bottone per visualizzare crediti e classifica.
     */
    private JButton credits;

    /**
     * Bottone per attivare/disattivare l'audio.
     */
    private JButton audio;

    /**
     * File chooser per selezionare il salvataggio da caricare.
     */
    private JFileChooser fileChooser;

    /**
     * Costruttore che inizializza l'interfaccia del menu principale.
     */
    public MenuUI() {
        initComponents();
    }

    /**
     * Inizializza e configura tutti i componenti grafici del menu: pannello di
     * sfondo, bottoni, file chooser e relative azioni.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Before Deorbiting");
        java.net.URL iconURL = getClass().getResource("/img/icon.png");
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        music.playMusic("/music/ZeldaMenu.wav");
        music.setVolumePercent(100);
        setPreferredSize(new Dimension(1100, 700));
        setResizable(false);

        // Pannello con sfondo custom
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/intro.png"));
        Image bgImg = icon.getImage().getScaledInstance(1100, 700, Image.SCALE_SMOOTH);
        background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Bottoni di testo trasparenti
        start = createTextButton("Nuova Partita");
        load = createTextButton("Carica Partita");
        commands = createTextButton("Comandi di Gioco");
        credits = createTextButton("Documentazione");

        // Pulsante audio con icona
        audio = new JButton();
        audio.setPreferredSize(new Dimension(64, 64));
        audio.setMargin(new Insets(0, 0, 0, 0));
        audio.setBorder(BorderFactory.createLineBorder(BORDER, 5));
        audio.setContentAreaFilled(false);
        Icon ico = loadAudioIcon();
        audio.setIcon(ico);
        audio.setRolloverIcon(ico);
        audio.setPressedIcon(ico);

        // File chooser per caricamenti
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./saves"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("File JSON", "json"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Azioni dei bottoni
        start.addActionListener(this::onStart);
        load.addActionListener(this::onLoad);
        commands.addActionListener(e -> {
            CommandsUI help = CommandsUI.getInstance();
            help.setLocationRelativeTo(this);
            help.setVisible(true);
            if (iconURL != null) {
                help.setIconImage(new ImageIcon(iconURL).getImage());
            }
        });
        credits.addActionListener(this::showWebsite);
        audio.addActionListener(e -> toggleAudio());

        // Layout dei componenti
        setGroupLayout();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Listener per il pulsante "Nuova Partita". Elimina il file di taccuino,
     * mostra la LoadBar e infine apre GameUI.
     *
     * @param e evento di ActionEvent dalla UI
     */
    private void onStart(ActionEvent e) {
        // 1) Reset del taccuino
        Path notebookFile = Paths.get("notebook.txt");
        try {
            Files.writeString(
                    notebookFile,
                    "",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(MenuUI.class.getName())
                    .log(Level.SEVERE, "Impossibile resettare il taccuino", ex);
        }

        // 2) Rimuove i componenti del menu
        getContentPane().removeAll();
        repaint();

        // 3) Crea e mostra LoadBarUI
        LoadBarUI loadBarPanel = new LoadBarUI();
        setTitle("Caricamento in corso...");
        getContentPane().add(loadBarPanel);
        validate();

        // 4) Alla fine del caricamento, apri GameUI
        loadBarPanel.addPropertyChangeListener(evt -> {
            if ("isFinished".equals(evt.getPropertyName())
                    && Boolean.TRUE.equals(evt.getNewValue())) {
                SwingUtilities.invokeLater(() -> {
                    music.stopMusica();
                    new GameUI().setVisible(true);
                    dispose();
                });
            }
        });

        loadBarPanel.startLoadBar();
    }

    /**
     * Listener per il pulsante "Carica Partita". Apre un file chooser e, se
     * selezionato un JSON valido, carica GameUI.
     *
     * @param e evento di ActionEvent dalla UI
     */
    private void onLoad(ActionEvent e) {
        fileChooser.setDialogTitle("Seleziona salvataggio JSON");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path path = fileChooser.getSelectedFile().toPath();
            try {
                music.stopMusica();
                new GameUI(path).setVisible(true);
                dispose();
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

    /**
     * Crea un JButton di testo con sfondo semitrasparente e bordo custom.
     *
     * @param text il testo da visualizzare nel bottone
     * @return il JButton configurato
     */
    private JButton createTextButton(String text) {
        int alpha = (int) (255 * 0.75);
        Color bg = new Color(
                BACKGROUND.getRed(),
                BACKGROUND.getGreen(),
                BACKGROUND.getBlue(),
                alpha);

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(bg);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setFocusPainted(false);
        btn.setForeground(TEXT);
        btn.setFont(new Font("Consolas", Font.BOLD, 16));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(BORDER, 5));
        Dimension sz = new Dimension(200, 64);
        btn.setPreferredSize(sz);
        btn.setMinimumSize(sz);
        btn.setMaximumSize(sz);
        btn.setMargin(new Insets(0, 0, 0, 0));
        return btn;
    }

    /**
     * Carica l'icona per il toggle audio, basata sullo stato {@code audioOn}.
     *
     * @return Icona corretta, o un'icona di default se non trovata
     */
    private Icon loadAudioIcon() {
        String path = audioOn ? "/img/audio_on.png" : "/img/audio_off.png";
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            return UIManager.getIcon("OptionPane.informationIcon");
        }
        ImageIcon ico = new ImageIcon(resource);
        Image img = ico.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Avvia (o riavvia) il server REST per la classifica e apre il browser
     * sulla root locale.
     *
     * @param e evento di ActionEvent dalla UI
     */
    private void showWebsite(ActionEvent e) {
        if (!serverStarted) {
            try {
                restServer = new RestServer();
                restServer.start();
                serverStarted = true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Errore avviando il server:\n" + ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Non posso aprire il browser:\n" + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Attiva o disattiva la musica e aggiorna l'icona del pulsante audio.
     */
    private void toggleAudio() {
        audioOn = !audioOn;
        if (audioOn) {
            music.riprendiMusica();
        } else {
            music.pausaMusica();
        }
        Icon icon = loadAudioIcon();
        audio.setIcon(icon);
        audio.setRolloverIcon(icon);
        audio.setPressedIcon(icon);
        audio.repaint();
    }

    /**
     * Configura il {@link GroupLayout} per posizionare i bottoni sullo sfondo.
     */
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
                                .addContainerGap(0, Short.MAX_VALUE)
                                .addComponent(audio)
                                .addGap(50)));

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
                        .addComponent(background, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        fl.setVerticalGroup(
                fl.createParallelGroup()
                        .addComponent(background, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }

    /**
     * Metodo principale per avviare il menu come applicazione stand-alone.
     *
     * @param args argomenti da linea di comando (non usati)
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            DBConfig.populateDatabase();
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | IllegalAccessException
                    | InstantiationException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new MenuUI().setVisible(true);
        });
    }
}
