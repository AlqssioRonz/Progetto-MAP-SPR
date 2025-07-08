/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author andre
 */
public class CommandsUI extends JFrame {


    /**
     * Istanza della
     */
    private static CommandsUI instance;
    /**
     * Immagine di background della finestra dei comandi.
     */
    private Image backgroundImage;

    /**
     * Metodo getter per ottenere la singola istanza della classe. Se la classe
     * non è stata ancora istanziata, la istanzia.
     *
     * @return istanza di CommandsGUI.
     */
    public static CommandsUI getInstance() {
        if (instance == null) {
            instance = new CommandsUI();
        }
        return instance;
    }

    /**
     * Costruttore privato per l'impostazione del frame
     */
    private CommandsUI() {
        // Imposta il titolo della finestra
        super("Comandi di gioco");

        // Carica l'immagine di sfondo
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/img/SPAZIO1.jpg"));
        } catch (IOException e) {
         System.err.print("Errore nel caricamento dell'immagine di background");
        }

        // Configura la finestra
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(650, 560);
        setResizable(false);
        setLayout(new BorderLayout());
        ImageIcon img = new ImageIcon("src/main/resources/img/icona_pennello.jpg");
        setIconImage(img.getImage());

        // Pannello personalizzato per disegnare lo sfondo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        //JTextArea per il testo sopra l'immagine
        JTextArea textArea = new JTextArea("""
                                           \s
        INVENTARIO: visualizza gli oggetti nell'inventario
                                           \s
        AVANTI: per muoverti in avanti
                                           \s
        INDIETRO: per muoverti indietro
                                           \s
        DESTRA: per muoverti a destra
                                           \s
        SINISTRA: per muoverti a sinistra
                                           \s
        SOPRA: per andare sopra
                                           \s
        SOTTO: per andare sotto
                                           \s
        PRENDI: per prendere un oggetto
                                           \s
        LASCIA: per lasciare un oggetto
                                           \s
        USA: per usare un oggetto o più se concatenati
                                           \s
        OSSERVA: per osservare l'ambiente circostante o un oggetto
                                           \s
        SALVA: per salvare la partita
                                           \s
        AIUTO: per visualizzare la lista dei comandi
                                           """);

        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 16));
        textArea.setForeground(Color.WHITE);

        // Pannello con layout trasparente per il testo
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(textArea, BorderLayout.NORTH);

        backgroundPanel.add(textPanel, BorderLayout.CENTER);

        // Rendi la finestra visibile
        setLocationRelativeTo(null);
        setVisible(true);
    }
}