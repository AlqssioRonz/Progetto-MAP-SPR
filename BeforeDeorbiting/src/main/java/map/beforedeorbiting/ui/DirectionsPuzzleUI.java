/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * Pannello per il mini-gioco di inserimento di una sequenza di direzioni.
 * Mostra una griglia di pulsanti con frecce direzionali e pulsanti di azione
 * (CLEAR, ENTER), traccia l’input dell’utente, gestisce timeout e invoca una
 * callback di risultato.
 *
 * @author lorenzopeluso
 */
public class DirectionsPuzzleUI extends JPanel {

    /**
     * Colore di sfondo del pannello.
     */
    private static final Color BG_COLOR = Color.decode("#0A0C1E");

    /**
     * Colore di accento per elementi grafici.
     */
    private static final Color ACCENT_COLOR = Color.decode("#4DE0C7");

    /**
     * Font per i pulsanti direzionali.
     */
    private static final Font DIR_FONT = new Font("Arial", Font.BOLD, 24);

    /**
     * Font per i pulsanti di azione e testo.
     */
    private static final Font ACTION_FONT = new Font("Arial", Font.BOLD, 22);

    /**
     * Sequenza di frecce corretta da confrontare con l'input.
     */
    private final List<String> expectedSequence;

    /**
     * Sequenza di frecce inserita dall'utente.
     */
    private final List<String> inputSequence = new ArrayList<>();

    /**
     * Callback invocata al termine del puzzle con il risultato.
     */
    private final Consumer<Integer> resultCallback;

    /**
     * Label che mostra la sequenza di input corrente.
     */
    private final JLabel displayLabel;

    /**
     * Lista di tutti i pulsanti del pannello, usata per
     * abilitazione/disabilitazione.
     */
    private final List<JButton> allButtons = new ArrayList<>();

    /**
     * Timer che gestisce il timeout del puzzle.
     */
    private final Timer timeoutTimer;

    /**
     * Costruisce il pannello del puzzle direzionale. Avvia un timer di 30
     * secondi, configura layout, pulsanti direzionali e pulsanti di azione, e
     * imposta lo stile grafico.
     *
     * @param expectedSequence sequenza corretta di frecce da confrontare
     * @param resultCallback callback invocata con
     * 0=successo,1=errore,-1=timeout
     */
    public DirectionsPuzzleUI(List<String> expectedSequence,
            Consumer<Integer> resultCallback) {
        this.expectedSequence = expectedSequence;
        this.resultCallback = resultCallback;

        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 5));

        displayLabel = new JLabel("", SwingConstants.CENTER);
        displayLabel.setOpaque(true);
        displayLabel.setBackground(BG_COLOR);
        displayLabel.setForeground(ACCENT_COLOR);
        displayLabel.setFont(ACTION_FONT);
        displayLabel.setPreferredSize(new Dimension(0, 40));
        add(displayLabel, BorderLayout.NORTH);

        // Crea la griglia di frecce
        JPanel grid = new JPanel(new GridLayout(3, 3, 8, 8));
        grid.setBackground(BG_COLOR);
        grid.setPreferredSize(new Dimension(240, 240));
        grid.add(createSpacer());
        grid.add(createDirectionButton("▲"));
        grid.add(createSpacer());
        grid.add(createDirectionButton("◄"));
        grid.add(createSpacer());
        grid.add(createDirectionButton("►"));
        grid.add(createSpacer());
        grid.add(createDirectionButton("▼"));
        grid.add(createSpacer());
        add(grid, BorderLayout.CENTER);

        // Pulsanti CLEAR e ENTER
        JPanel bottom = new JPanel(new GridLayout(1, 2, 10, 10));
        bottom.setBackground(BG_COLOR);
        bottom.setPreferredSize(new Dimension(0, 60));

        JButton clearBtn = createActionButton("CLEAR");
        clearBtn.addActionListener(e -> resetInput());
        bottom.add(clearBtn);

        JButton enterBtn = createActionButton("ENTER");
        enterBtn.addActionListener(e -> submitInput());
        bottom.add(enterBtn);

        add(bottom, BorderLayout.SOUTH);

        // Timer di timeout (30 secondi)
        timeoutTimer = new Timer(30_000, e -> onTimeout());
        timeoutTimer.setRepeats(false);
        timeoutTimer.start();
    }

    /**
     * Crea un pannello vuoto usato come spacer nella griglia.
     */
    private JPanel createSpacer() {
        JPanel spacer = new JPanel();
        spacer.setBackground(BG_COLOR);
        return spacer;
    }

    /**
     * Crea un pulsante direzionale con la freccia specificata. Aggiunge il
     * simbolo alla sequenza di input e aggiorna il display.
     *
     * @param arrow simbolo freccia ("▲","▼","◄","►")
     * @return il pulsante configurato
     */
    private JButton createDirectionButton(String arrow) {
        JButton btn = new JButton(arrow);
        btn.setFont(DIR_FONT);
        btn.setFocusPainted(false);
        btn.setBackground(BG_COLOR);
        btn.setForeground(ACCENT_COLOR);
        btn.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        btn.setPreferredSize(new Dimension(60, 60));
        btn.addActionListener(e -> {
            inputSequence.add(arrow);
            updateDisplay();
        });
        allButtons.add(btn);
        return btn;
    }

    /**
     * Crea un pulsante di azione (CLEAR o ENTER).
     *
     * @param text etichetta del pulsante
     * @return il pulsante configurato
     */
    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(ACTION_FONT);
        btn.setFocusPainted(false);
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(BG_COLOR);
        btn.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 2));
        btn.setPreferredSize(new Dimension(0, 50));
        allButtons.add(btn);
        return btn;
    }

    /**
     * Aggiorna il JLabel con la sequenza di input correntemente inserita.
     */
    private void updateDisplay() {
        SwingUtilities.invokeLater(() -> displayLabel.setText(String.join(" ", inputSequence)));
    }

    /**
     * Pulisce la sequenza di input e resetta il display.
     */
    private void resetInput() {
        inputSequence.clear();
        updateDisplay();
    }

    /**
     * Confronta la sequenza inserita con quella attesa. Se corretta, ferma il
     * timer, mostra successo e chiude il pannello; altrimenti mostra errore,
     * resetta l’input e richiama la callback con 1.
     */
    private void submitInput() {
        if (inputSequence.equals(expectedSequence)) {
            timeoutTimer.stop();
            showPopup("Sequenza CORRETTA!");
            closeWindow();
            resultCallback.accept(0);
        } else {
            showPopup("Sequenza SBAGLIATA!");
            resetInput();
            resultCallback.accept(1);
        }
    }

    /**
     * Gestisce il timeout di inserimento. Mostra un messaggio di fine ossigeno,
     * disabilita i bottoni, chiude il pannello e invoca callback con -1.
     */
    private void onTimeout() {
        showPopup("Il tuo ossigeno è finito!");
        allButtons.forEach(b -> b.setEnabled(false));
        closeWindow();
        resultCallback.accept(-1);
    }

    /**
     * Mostra un JOptionPane stilizzato con il messaggio indicato.
     *
     * @param message testo da visualizzare nella finestra di dialogo
     */
    private void showPopup(String message) {
        UIManager.put("OptionPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("OptionPane.messageForeground", ACCENT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", BG_COLOR);
        UIManager.put("Button.font", ACTION_FONT);

        JOptionPane.showMessageDialog(
                this,
                new JLabel(message, SwingConstants.CENTER) {
            {
                setForeground(ACCENT_COLOR);
                setBackground(BG_COLOR);
                setFont(ACTION_FONT);
            }
        },
                "Risultato",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Ricava la finestra padre e ne invoca dispose() per chiuderla.
     */
    private void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
