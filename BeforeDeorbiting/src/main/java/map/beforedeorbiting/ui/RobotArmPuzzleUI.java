package map.beforedeorbiting.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Pannello per la risoluzione del puzzle di configurazione del braccio robotico
 * CanadArm2. Mostra una griglia 3x3 di campi di testo in cui l’utente inserisce
 * numeri, quindi verifica se la combinazione inserita corrisponde a quella
 * corretta. Se l’utente preme ENTER, il dialogo si chiude e lo stato di
 * risoluzione viene salvato.
 *
 * @author lorenzopeluso
 */
public class RobotArmPuzzleUI extends JPanel {

    /**
     * Colore di sfondo del pannello.
     */
    private final Color BG_COLOR = Color.decode("#0f111c");

    /**
     * Colore di accento per bordi e testi principali.
     */
    private final Color ACCENT_COLOR = Color.decode("#00e0c7");

    /**
     * Colore di sfondo dei campi di testo.
     */
    private final Color TEXTFIELD_BG = Color.decode("#101233");

    /**
     * Colore di sfondo del pulsante di conferma.
     */
    private final Color BUTTON_BG = Color.decode("#00e0c7");

    /**
     * Colore del testo del pulsante ENTER.
     */
    private final Color BUTTON_TEXT = Color.decode("#0A0C1E");

    /**
     * Font per il titolo del puzzle.
     */
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    /**
     * Font per i valori all'interno delle celle.
     */
    private final Font CELL_FONT = new Font("Arial", Font.BOLD, 22);

    /**
     * Font per il pulsante di conferma.
     */
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 23);

    /**
     * Matrice 3x3 di JTextField in cui l'utente inserisce la combinazione.
     */
    private final JTextField[][] cells = new JTextField[3][3];

    /**
     * Matrice 3x3 che rappresenta la combinazione corretta da verificare.
     */
    private final int[][] correctCombination;

    /**
     * Flag che indica se il puzzle è stato risolto con successo.
     */
    private boolean puzzleSolved = false;

    /**
     * Costruisce il pannello del puzzle impostando la combinazione corretta.
     *
     * @param correctCombination matrice 3x3 di numeri attesi per risolvere il
     * puzzle
     */
    public RobotArmPuzzleUI(int[][] correctCombination) {
        this.correctCombination = correctCombination;
        setupUI();
    }

    /**
     * Configura tutti i componenti grafici del pannello: titolo, griglia di
     * JTextField e pulsante ENTER.
     */
    private void setupUI() {
        setBackground(BG_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Configurare l'ormeggio:");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gridPanel.setBackground(BG_COLOR);
        gridPanel.setMaximumSize(new Dimension(320, 320));
        gridPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Crea e aggiunge i 9 campi di testo
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(CELL_FONT);
                cell.setBackground(TEXTFIELD_BG);
                cell.setForeground(Color.WHITE);
                cell.setCaretColor(Color.WHITE);
                cell.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
                cells[row][col] = cell;
                gridPanel.add(cell);
            }
        }

        JButton checkButton = new JButton("ENTER");
        checkButton.setFont(BUTTON_FONT);
        checkButton.setBackground(BUTTON_BG);
        checkButton.setForeground(BUTTON_TEXT);
        checkButton.setBorder(new LineBorder(Color.WHITE, 1, true));
        checkButton.setFocusPainted(false);
        checkButton.setAlignmentX(CENTER_ALIGNMENT);
        checkButton.setPreferredSize(new Dimension(120, 40));
        checkButton.addActionListener((ActionEvent e) -> {
            puzzleSolved = checkSolution();
            SwingUtilities.getWindowAncestor(RobotArmPuzzleUI.this).dispose();
        });

        add(Box.createVerticalStrut(30));
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(gridPanel);
        add(Box.createVerticalStrut(20));
        add(checkButton);
        add(Box.createVerticalStrut(30));
    }

    /**
     * Controlla se i valori inseriti corrispondono alla combinazione corretta.
     *
     * @return {@code true} se ogni cella contiene il numero previsto,
     * {@code false} altrimenti (inclusi formati non numerici)
     */
    private boolean checkSolution() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                try {
                    int value = Integer.parseInt(cells[row][col].getText().trim());
                    if (value != correctCombination[row][col]) {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Indica se il puzzle è stato risolto correttamente dall'utente.
     *
     * @return {@code true} se la combinazione inserita corrisponde a quella
     * attesa
     */
    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    /**
     * Mostra un dialogo modale contenente il puzzle. Al termine (premendo ENTER
     * o chiudendo), restituisce se è stato risolto.
     *
     * @param correctCombination la combinazione 3x3 da far indovinare
     * @return {@code true} se l'utente ha inserito la combinazione corretta
     */
    public static boolean showPuzzleDialog(int[][] correctCombination) {
        RobotArmPuzzleUI panel = new RobotArmPuzzleUI(correctCombination);
        JDialog dialog = new JDialog((JFrame) null, "Controllo braccio CanadArm2", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setSize(400, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return panel.isPuzzleSolved();
    }
}
