/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author lorenzopeluso
 */
public class RobotArmPuzzleUI extends JPanel {

    private final Color BG_COLOR = Color.decode("#0f111c");
    private final Color ACCENT_COLOR = Color.decode("#00e1d4");
    private final Color TEXTFIELD_BG = Color.decode("#101233");
    private final Color BUTTON_BG = Color.decode("#00e1d4");
    private final Color BUTTON_TEXT = Color.decode("#0A0C1E");
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font CELL_FONT = new Font("Arial", Font.BOLD, 22);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 23);

    private final JTextField[][] cells = new JTextField[3][3];
    private final int[][] correctCombination;
    private boolean puzzleSolved = false;

    public RobotArmPuzzleUI(int[][] correctCombination) {
        this.correctCombination = correctCombination;
        setupUI();
    }

    private void setupUI() {
        this.setBackground(BG_COLOR);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        

        JLabel titleLabel = new JLabel("Configurare l'ormeggio:");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gridPanel.setBackground(BG_COLOR);
        gridPanel.setMaximumSize(new Dimension(320, 320));
        gridPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Crea la griglia 3x3
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
            if (checkSolution()) {
                puzzleSolved = true;
            } else {
                puzzleSolved = false;
            }
            SwingUtilities.getWindowAncestor(RobotArmPuzzleUI.this).dispose();
        });

        this.add(Box.createVerticalStrut(30));
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(gridPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(checkButton);
        this.add(Box.createVerticalStrut(30));
    }

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

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public static boolean showPuzzleDialog(int[][] correctCombination) {
        RobotArmPuzzleUI panel = new RobotArmPuzzleUI(correctCombination);
        JDialog dialog = new JDialog((JFrame) null, "Controllo braccio CanadArm,", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);

        dialog.setSize(400, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return panel.isPuzzleSolved();
    }

    /*
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int[][] correct = {
            {0, -1, -1},
            {-1, 0, -1},
            {-1, 0, -2}
        };

        SwingUtilities.invokeLater(() -> {
            boolean solved = RobotArmPuzzleUI.showPuzzleDialog(correct);
            if (solved) {
                System.out.println("Puzzle risolto!");
            } else {
                System.out.println("Combinazione errata.");
            }
        });
    }*/
}
