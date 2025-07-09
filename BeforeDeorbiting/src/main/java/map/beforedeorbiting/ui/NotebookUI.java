/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import map.beforedeorbiting.GameDesc;

/**
 *
 * @author ronzu
 */
public class NotebookUI {

    private static final String FILE_PATH = "notebook.txt";
    private static final Color bluscuro = Color.decode("#0f111c");
    private static final Color bluchiaro = Color.decode("#00e1d4");

    public static void show(GameDesc game) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setUndecorated(true);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.getRootPane().setBorder(BorderFactory.createLineBorder(bluchiaro, 8));
            ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(bluscuro);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

            // Barra in alto con titolo e pulsante X
            JPanel topBar = new JPanel(new BorderLayout());
            topBar.setBackground(bluscuro);

            JLabel titolo = new JLabel("Taccuino di bordo", SwingConstants.CENTER);
            titolo.setFont(new Font("Consolas", Font.BOLD, 20));
            titolo.setForeground(bluchiaro);
            titolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

            JButton closeButton = new JButton("X");
            closeButton.setForeground(bluchiaro);
            closeButton.setBackground(bluscuro);
            closeButton.setBorder(null);
            closeButton.setFocusPainted(false);
            closeButton.setFont(new Font("Consolas", Font.BOLD, 18));
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

            topBar.add(titolo, BorderLayout.CENTER);
            JPanel closeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            closeWrapper.setOpaque(false);
            closeWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 20));
            closeWrapper.add(closeButton);
            topBar.add(closeWrapper, BorderLayout.EAST);
            mainPanel.add(topBar, BorderLayout.NORTH);

            // Area di testo
            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            textArea.setBackground(new Color(15, 15, 30));
            textArea.setForeground(new Color(0, 255, 204));
            textArea.setCaretColor(Color.WHITE);
            textArea.setMargin(new Insets(10, 10, 10, 10));
            textArea.setBorder(null);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createLineBorder(bluchiaro, 5));
            scrollPane.setBackground(bluscuro);

            mainPanel.add(scrollPane, BorderLayout.CENTER);

            File file = new File(FILE_PATH);
            boolean[] modificato = {false};

            caricaContenuto(file, textArea);

            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }
            });

            JButton salvaButton = new JButton("SALVA");
            salvaButton.setBackground(bluchiaro);
            salvaButton.setFont(new Font("Consolas", Font.BOLD, 18));
            salvaButton.setFocusPainted(false);
            salvaButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
            salvaButton.addActionListener(e -> {
                salvaContenuto(file, textArea, frame);
                modificato[0] = false;
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(bluscuro);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            buttonPanel.add(salvaButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            frame.add(mainPanel);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (modificato[0]) {
                        Object[] opzioni = {"Sì", "No", "Annulla"};
                        int scelta = mostraMessaggioChiusuraSalvataggio(frame);

                        if (scelta == JOptionPane.YES_OPTION) {
                            salvaContenuto(file, textArea, frame);
                            chiudi(frame);
                        } else if (scelta == JOptionPane.NO_OPTION) {
                            chiudi(frame);
                        }

                    } else {
                        chiudi(frame);
                    }
                }

                private void chiudi(JFrame frame) {
                    mostraMessaggioChiusura(frame);
                    frame.dispose();
                }
            });

            final Point[] clickOffset = {new Point()};

            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    clickOffset[0] = e.getPoint();
                }
            });

            frame.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point newLoc = e.getLocationOnScreen();
                    newLoc.translate(-clickOffset[0].x, -clickOffset[0].y);
                    frame.setLocation(newLoc);
                }
            });

            frame.setVisible(true);
        });
    }

    // Metodo per leggere il contenuto del file e inserirlo nel JTextArea
    private static void caricaContenuto(File file, JTextArea textArea) {
        try {
            if (!file.exists()) {
                file.createNewFile(); // crea il file se non esiste
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n"); // aggiunge ogni riga al text area
            }
            reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nella lettura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per salvare il contenuto del JTextArea nel file
    private static void salvaContenuto(File file, JTextArea textArea, JFrame parentFrame) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(textArea.getText());
            writer.close();
            mostraMesaggioSalvataggio(parentFrame);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nel salvataggio del file.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostraMessaggioChiusura(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Messaggio", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(bluchiaro, 5));
        dialog.getContentPane().setBackground(bluscuro);
        dialog.setLayout(new BorderLayout());

        // Titolo
        JLabel titolo = new JLabel("Taccuino chiuso", SwingConstants.CENTER);
        titolo.setForeground(bluchiaro);
        titolo.setFont(new Font("Consolas", Font.BOLD, 20));
        titolo.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Messaggio
        JLabel messaggio = new JLabel("<html><center>Hai chiuso il taccuino.<br>Puoi consultarlo di nuovo in qualsiasi momento.</center></html>", SwingConstants.CENTER);
        messaggio.setForeground(Color.WHITE);
        messaggio.setFont(new Font("Consolas", Font.PLAIN, 18));

        // Pulsante OK
        JButton okButton = new JButton("OK");
        okButton.setBackground(bluchiaro);
        okButton.setForeground(bluscuro);
        okButton.setFont(new Font("Consolas", Font.BOLD, 14));
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bluscuro);
        bottomPanel.add(okButton);

        dialog.add(titolo, BorderLayout.NORTH);
        dialog.add(messaggio, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static void mostraMesaggioSalvataggio(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Salvato", true);
        dialog.setUndecorated(true);
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(bluchiaro, 5));
        dialog.getContentPane().setBackground(bluscuro);
        dialog.setLayout(new BorderLayout());

        JLabel messaggio = new JLabel("<html><center> Taccuino salvato con successo! <html><center>", SwingConstants.CENTER);
        messaggio.setForeground(Color.WHITE);
        messaggio.setFont(new Font("Consolas", Font.BOLD, 18));
        messaggio.setBorder(new EmptyBorder(20, 10, 10, 10));
        dialog.add(messaggio, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setBackground(bluchiaro);
        okButton.setForeground(new Color(15, 15, 30));
        okButton.setFont(new Font("Consolas", Font.BOLD, 15));
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bluscuro);
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private static int mostraMessaggioChiusuraSalvataggio(JFrame frame) {
        final int[] scelta = {-1}; // 0 = Sì, 1 = No, 2 = Annulla

        JDialog dialog = new JDialog(frame, "Salvataggio", true);
        dialog.setUndecorated(true);
        dialog.setSize(420, 160);
        dialog.setLocationRelativeTo(frame);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(bluchiaro, 3));
        dialog.getContentPane().setBackground(bluscuro);
        dialog.setLayout(new BorderLayout());

        JLabel messaggio = new JLabel("<html><center> Hai modificato il taccuino. Vuoi salvare prima di uscire?<html><center>", SwingConstants.CENTER);
        messaggio.setForeground(Color.WHITE);
        messaggio.setFont(new Font("Consolas", Font.BOLD, 20));
        messaggio.setBorder(new EmptyBorder(20, 10, 10, 10));
        dialog.add(messaggio, BorderLayout.CENTER);

        JButton siButton = new JButton("Sì");
        JButton noButton = new JButton("No");
        JButton annullaButton = new JButton("Annulla");

        for (JButton btn : new JButton[]{siButton, noButton, annullaButton}) {
            btn.setBackground(bluchiaro);
            btn.setForeground(new Color(15, 15, 30));
            btn.setFont(new Font("Consolas", Font.BOLD, 15));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        }

        siButton.addActionListener(e -> {
            scelta[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });

        noButton.addActionListener(e -> {
            scelta[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });

        annullaButton.addActionListener(e -> {
            scelta[0] = JOptionPane.CANCEL_OPTION;
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(bluscuro);
        buttonPanel.add(siButton);
        buttonPanel.add(noButton);
        buttonPanel.add(annullaButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return scelta[0];
    }
}
