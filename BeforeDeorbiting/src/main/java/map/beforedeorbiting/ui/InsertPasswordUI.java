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
 *
 * @author lorenzopeluso
 */
public class InsertPasswordUI extends JPanel {

    private final Color BG_COLOR = Color.decode("#0f111c");
    private final Color ACCENT_COLOR = Color.decode("#00e1d4");
    private final Color TEXTFIELD_BG = Color.decode("#101233");
    private final Color BUTTON_BG = Color.decode("#00e1d4");
    private final Color BUTTON_TEXT = Color.decode("#0A0C1E");
    private final Font DIR_FONT = new Font("Arial", Font.BOLD, 24);
    private final Font ACTION_FONT = new Font("Arial", Font.BOLD, 22);

    private final String password;
    private boolean passwordCorrect = false;

    public InsertPasswordUI(String password) {
        this.password = password;
        setupUI();
    }

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
            if (enteredPassword.equals(password)) {
                passwordCorrect = true;
            } else {
                passwordCorrect = false;
            }
            SwingUtilities.getWindowAncestor(InsertPasswordUI.this).dispose();
        });

        this.add(Box.createVerticalStrut(40));
        this.add(label);
        this.add(Box.createVerticalStrut(20));
        this.add(passwordField);
        this.add(Box.createVerticalStrut(20));
        this.add(enterButton);
    }

    public boolean isPasswordCorrect() {
        return passwordCorrect;
    }

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

    /*
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            boolean success = InsertPasswordUI.showPasswordDialog("segreta123");
            if (success) {
                System.out.println("Password corretta!");
            } else {
                System.out.println("Password errata.");
            }
            
        });
    }
    */
}
