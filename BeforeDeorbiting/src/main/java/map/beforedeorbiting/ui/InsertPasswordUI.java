/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Pannello per l'inserimento e la verifica di una password tramite dialog
 * modale. Mostra un campo di testo per l’inserimento e un pulsante "Enter",
 * restituendo se la password corrisponde a quella attesa.
 *
 * @author lorenzopeluso
 */
public class InsertPasswordUI extends JPanel {

    /**
     * Colore di sfondo del pannello.
     */
    private final Color BG_COLOR = Color.decode("#0f111c");

    /**
     * Colore di accento per testi e bordi.
     */
    private final Color ACCENT_COLOR = Color.decode("#00e1d4");

    /**
     * Colore di sfondo del campo di testo.
     */
    private final Color TEXTFIELD_BG = Color.decode("#101233");

    /**
     * Colore di sfondo dei pulsanti.
     */
    private final Color BUTTON_BG = Color.decode("#00e1d4");

    /**
     * Colore del testo dei pulsanti.
     */
    private final Color BUTTON_TEXT = Color.decode("#0A0C1E");

    /**
     * Font per etichette direzionali.
     */
    private final Font DIR_FONT = new Font("Arial", Font.BOLD, 24);

    /**
     * Font per azioni e pulsanti.
     */
    private final Font ACTION_FONT = new Font("Arial", Font.BOLD, 22);

    /**
     * Password corretta da confrontare con l’input utente.
     */
    private final String password;

    /**
     * Flag che indica se l’ultimo inserimento era corretto.
     */
    private boolean passwordCorrect = false;

    /**
     * Costruisce il pannello per l’inserimento della password.
     *
     * @param password la password corretta da confrontare con l’input utente
     */
    public InsertPasswordUI(String password) {
        this.password = password;
        setupUI();
    }

    /**
     * Configura e dispone i componenti grafici del pannello: etichetta, campo
     * di testo e pulsante di conferma.
     */
    private void setupUI() {
        this.setBackground(BG_COLOR);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Inserisci la password:");
        label.setForeground(ACCENT_COLOR);
        label.setFont(DIR_FONT);
        label.setAlignmentX(CENTER_ALIGNMENT);

        JTextField passwordField = new JTextField(15);
        passwordField.setFont(ACTION_FONT);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(CENTER_ALIGNMENT);
        passwordField.setBackground(TEXTFIELD_BG);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(new LineBorder(BUTTON_BG, 5, true));

        JButton enterButton = new JButton("Enter");
        enterButton.setFont(ACTION_FONT);
        enterButton.setBackground(BUTTON_BG);
        enterButton.setForeground(BUTTON_TEXT);
        enterButton.setBorder(new LineBorder(Color.WHITE, 1, true));
        enterButton.setFocusPainted(false);
        enterButton.setAlignmentX(CENTER_ALIGNMENT);

        enterButton.addActionListener((ActionEvent e) -> {
            String enteredPassword = passwordField.getText();
            passwordCorrect = enteredPassword.equals(password);
            SwingUtilities.getWindowAncestor(InsertPasswordUI.this).dispose();
        });

        this.add(Box.createVerticalStrut(40));
        this.add(label);
        this.add(Box.createVerticalStrut(20));
        this.add(passwordField);
        this.add(Box.createVerticalStrut(20));
        this.add(enterButton);
    }

    /**
     * Indica se l’ultimo inserimento corrispondeva alla password corretta.
     *
     * @return {@code true} se la password inserita era corretta, {@code false}
     * altrimenti
     */
    public boolean isPasswordCorrect() {
        return passwordCorrect;
    }

    /**
     * Visualizza un dialog modale per l’inserimento della password, bloccando
     * finché l’utente non conferma o chiude.
     *
     * @param correctPassword la password attesa
     * @return {@code true} se l’utente ha inserito la password corretta,
     * {@code false} altrimenti
     */
    public static boolean showPasswordDialog(String correctPassword) {
        InsertPasswordUI panel = new InsertPasswordUI(correctPassword);
        JDialog dialog = new JDialog((JFrame) null, "Inserimento Password", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return panel.isPasswordCorrect();
    }
}
