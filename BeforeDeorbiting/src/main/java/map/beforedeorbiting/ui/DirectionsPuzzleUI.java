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
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 *
 * @author lorenzopeluso
 */
public class DirectionsPuzzleUI extends JPanel {
    private static final Color BG_COLOR = Color.decode("#0A0C1E");
    private static final Color ACCENT_COLOR = Color.decode("#4DE0C7");
    private static final Font DIR_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font ACTION_FONT = new Font("Arial", Font.BOLD, 22);

    private final List<String> expectedSequence;
    private final List<String> inputSequence = new ArrayList<>();
    private final Consumer<Integer> resultCallback;
    private final JLabel displayLabel;
    private final List<JButton> allButtons = new ArrayList<>();
    public final Timer timeoutTimer;

    public DirectionsPuzzleUI(List<String> expectedSequence, Consumer<Integer> resultCallback) {
        this.expectedSequence = expectedSequence;
        this.resultCallback = resultCallback;
        setLayout(new BorderLayout(10,10));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 5));

       
        displayLabel = new JLabel("", SwingConstants.CENTER);
        displayLabel.setForeground(ACCENT_COLOR);
        displayLabel.setBackground(BG_COLOR);
        displayLabel.setOpaque(true);
        displayLabel.setFont(ACTION_FONT);
        displayLabel.setPreferredSize(new Dimension(0, 40));
        add(displayLabel, BorderLayout.NORTH);

        
        JPanel grid = new JPanel(new GridLayout(3,3,8,8));
        grid.setBackground(BG_COLOR);
        grid.setPreferredSize(new Dimension(240,240));
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

        // Bottom actions
        JPanel bottom = new JPanel(new GridLayout(1,2,10,10));
        bottom.setBackground(BG_COLOR);
        bottom.setPreferredSize(new Dimension(0, 60));

        JButton clearBtn = createActionButton("CLEAR");
        clearBtn.addActionListener(e -> resetInput());
        bottom.add(clearBtn);

        JButton enterBtn = createActionButton("ENTER");
        enterBtn.addActionListener(e -> submitInput());
        bottom.add(enterBtn);

        add(bottom, BorderLayout.SOUTH);

        // Timeout
        timeoutTimer = new Timer(30_000, e -> onTimeout());
        timeoutTimer.setRepeats(false);
        timeoutTimer.start();
    }

    private JPanel createSpacer() {
        JPanel spacer = new JPanel();
        spacer.setBackground(BG_COLOR);
        return spacer;
    }

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

    private void updateDisplay() {
        SwingUtilities.invokeLater(() -> displayLabel.setText(String.join(" ", inputSequence)));
    }

    private void resetInput() {
        inputSequence.clear();
        updateDisplay();
    }

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

    private void onTimeout() {
        showPopup("Il tuo ossigeno è finito!");
        allButtons.forEach(b -> b.setEnabled(false));
        this.closeWindow();
        resultCallback.accept(-1);
    }

    private void showPopup(String message) {
        // Stile popup
        UIManager.put("OptionPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("OptionPane.messageForeground", ACCENT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", BG_COLOR);
        UIManager.put("Button.font", ACTION_FONT);

        JOptionPane.showMessageDialog(
            this,
            new JLabel(message, SwingConstants.CENTER) {{
                setForeground(ACCENT_COLOR);
                setBackground(BG_COLOR);
                setFont(ACTION_FONT);
            }},
            "Risultato",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    /*public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            List<String> pattern = Arrays.asList("▲", "▼", "▲", "►", "▲", "◄");
            DirectionsPuzzleUI panel = new DirectionsPuzzleUI(pattern, code -> {});
            JFrame frame = new JFrame("Tastierino direzionale: apertura di emegenza");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }*/
}
