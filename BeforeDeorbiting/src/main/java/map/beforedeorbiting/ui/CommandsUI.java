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
 * Finestra singleton che mostra all’utente la lista dei comandi di gioco
 * sovrapposta a un’immagine di background. Utilizza uno {@link ImageIcon} per
 * l’icona della finestra e un pannello personalizzato per disegnare lo sfondo.
 *
 * @author andre
 */
public class CommandsUI extends JFrame {

    /**
     * Istanza unica di {@code CommandsUI} (pattern singleton).
     */
    private static CommandsUI instance;

    /**
     * Immagine di background della finestra dei comandi.
     */
    private Image backgroundImage;

    /**
     * Restituisce l’unica istanza di {@code CommandsUI}, creandola se non
     * esiste ancora.
     *
     * @return l’istanza di {@code CommandsUI}
     */
    public static CommandsUI getInstance() {
        if (instance == null) {
            instance = new CommandsUI();
        }
        return instance;
    }

    /**
     * Costruttore privato che configura il frame:
     * <ul>
     * <li>Carica e disegna l’immagine di sfondo</li>
     * <li>Imposta titolo, dimensioni, icona e comportamento di chiusura</li>
     * <li>Aggiunge un {@link JTextArea} trasparente con la lista dei
     * comandi</li>
     * </ul>
     */
    private CommandsUI() {
        super("Comandi di gioco");

        // Caricamento dell’immagine di sfondo
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/img/SPAZIO1.jpg"));
        } catch (IOException e) {
            System.err.println("Errore nel caricamento dell'immagine di background: " + e.getMessage());
        }

        // Configurazione base del frame
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(650, 580);
        setResizable(false);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/main/resources/img/icona_pennello.jpg").getImage());

        // Pannello per disegnare lo sfondo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // Area di testo trasparente che mostra le istruzioni
        JTextArea textArea = new JTextArea(
                """
                        INVENTARIO: visualizza gli oggetti nell'inventario

                        FORWARD/NORD: per muoverti in avanti

                        AFT/SUD: per muoverti indietro

                        STARBOARD/EST: per muoverti a destra

                        PORT/OVEST: per muoverti a sinistra

                        OVERHEAD/SU: per andare sopra

                        DECK/GIU: per andare sotto

                        PRENDI: per prendere un oggetto

                        LASCIA: per lasciare un oggetto

                        USA: per usare un oggetto o più se concatenati

                        OSSERVA: per osservare l'ambiente circostante o un oggetto

                        SALVA: per salvare la partita

                        AIUTO: per visualizzare la lista dei comandi
                
                        STORIA: per conoscere la vera storia del moludo in cui ti trovi
                        """);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 16));
        textArea.setForeground(Color.WHITE);

        // Pannello trasparente per contenere il testo
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(textArea, BorderLayout.NORTH);
        backgroundPanel.add(textPanel, BorderLayout.CENTER);

        // Mostra la finestra al centro dello schermo
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
