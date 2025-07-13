/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import map.beforedeorbiting.database.AstronautsDAO;
import map.beforedeorbiting.database.Astronaut;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.JTableHeader;
import map.beforedeorbiting.GameDesc;

import map.beforedeorbiting.database.DBConfig;

public class HALterminal extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final GameDesc game;

    private JTextArea roboticsStatusArea;

    private static final Color BG_COLOR = Color.decode("#0f111c");
    private static final Color PANEL_COLOR = Color.decode("#0f111c");
    private static final Color BUTTON_BG = Color.decode("#0f111c");
    private static final Color BUTTON_FG = Color.decode("#00e1d4");
    private static final Font BUTTON_FONT = new Font("Consolas", Font.BOLD, 25);

    public HALterminal(GameDesc game) {
        this.game = game;
        setTitle("Computer centrale della Stazione");
        java.net.URL iconURL = getClass().getResource("/img/icon.png");
        if (iconURL != null) {
            ImageIcon frameIcon = new ImageIcon(iconURL);
            setIconImage(frameIcon.getImage());
        } else {
            System.err.println("Warning: icona non trovata in /img/icon.png");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG_COLOR);

        mainPanel.add(createModulePanel(), "MODULE_PANEL");
        mainPanel.add(createControlPanel(), "CONTROL_PANEL");
        mainPanel.add(createCrewPanel(), "CREW_PANEL");
        mainPanel.add(createRoboticsPanel(), "ROBOTICS_PANEL");

        add(mainPanel);
        cardLayout.show(mainPanel, "MODULE_PANEL");

    }

    private JPanel createModulePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] modules = {"Robotica", "Supporto Vitale", "Navigazione", "Crew"};
        for (String name : modules) {
            JButton btn = new JButton(name);
            styleSciFiButton(btn);
            if (null == name) {
                btn.addActionListener(e -> cardLayout.show(mainPanel, "CONTROL_PANEL"));
            } else {
                switch (name) {
                    case "Crew" ->
                        btn.addActionListener(l -> cardLayout.show(mainPanel, "CREW_PANEL"));
                    case "Robotica" ->
                        btn.addActionListener(l -> cardLayout.show(mainPanel, "ROBOTICS_PANEL"));
                    default ->
                        btn.addActionListener(e -> cardLayout.show(mainPanel, "CONTROL_PANEL"));
                }
            }
            panel.add(btn);
        }

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Pannello di controllo ISS", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 26));
        title.setForeground(BUTTON_FG);
        panel.add(title, BorderLayout.NORTH);

        JTextArea statusArea = new JTextArea(getStatusText(true));
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        statusArea.setBackground(BG_COLOR);
        statusArea.setForeground(BUTTON_FG);
        statusArea.setBorder(new LineBorder(BUTTON_BG, 2));
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setBackground(PANEL_COLOR);

        JToggleButton aiToggle = new JToggleButton("Disattiva Controllo AI");
        styleSciFiButton(aiToggle);
        aiToggle.addActionListener(e -> {
            game.setAiActive(!game.isAiActive());
            aiToggle.setText(game.isAiActive() ? "Disattiva Controllo AI" : "Attiva Controllo AI");
            statusArea.setText(getStatusText(game.isAiActive()));
        });

        JButton backBtn = new JButton("Indietro");
        styleSciFiButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MODULE_PANEL"));

        controls.add(aiToggle);
        controls.add(backBtn);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRoboticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Pannello di controllo ISS – Robotica", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 26));
        title.setForeground(BUTTON_FG);
        panel.add(title, BorderLayout.NORTH);

        roboticsStatusArea = new JTextArea(getStatusText(true));
        roboticsStatusArea.setEditable(false);
        roboticsStatusArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        roboticsStatusArea.setBackground(BG_COLOR);
        roboticsStatusArea.setForeground(BUTTON_FG);
        roboticsStatusArea.setBorder(new LineBorder(BUTTON_BG, 2));
        panel.add(new JScrollPane(roboticsStatusArea), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setBackground(PANEL_COLOR);

        JToggleButton aiToggle = new JToggleButton("Disattiva Controllo AI");
        styleSciFiButton(aiToggle);
        aiToggle.addActionListener(e -> {
            game.setAiActive(!game.isAiActive());
            aiToggle.setText(game.isAiActive() ? "Disattiva Controllo AI" : "Attiva Controllo AI");
            roboticsStatusArea.setText(getStatusText(game.isAiActive()));
        });

        JButton maneuversBtn = new JButton("Manovre CanadArm2");
        styleSciFiButton(maneuversBtn);
        maneuversBtn.addActionListener(e -> showManeuverCodesInTextArea());

        JButton backBtn = new JButton("Indietro");
        styleSciFiButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MODULE_PANEL"));

        controls.add(aiToggle);
        controls.add(maneuversBtn);
        controls.add(backBtn);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCrewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Crew ISS", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 26));
        title.setForeground(BUTTON_FG);
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = {
            "Nome", "Cognome", "Nascita", "Luogo di nascita", "Ore ISS"
        };

        Object[][] data;
        try (Connection conn = DBConfig.getConnection()) {
            AstronautsDAO dao = new AstronautsDAO(conn);
            java.util.List<Astronaut> list = dao.getAll();

            data = new Object[list.size()][5];
            for (int i = 0; i < list.size(); i++) {
                Astronaut a = list.get(i);
                data[i][0] = a.getName();
                data[i][1] = a.getSurname();
                data[i][2] = a.getDateOfBirth();
                data[i][3] = a.getBirthplace();
                data[i][4] = a.getHoursOnISS();
            }
        } catch (SQLException e) {
            System.out.println("Errore: " + e);
            data = new Object[0][5];
        }

        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setRowHeight(24);

        table.setBackground(BG_COLOR);
        table.setForeground(BUTTON_FG);
        table.setGridColor(BUTTON_BG);
        table.setShowGrid(true);
        table.setSelectionBackground(BUTTON_FG.darker());
        table.setSelectionForeground(BG_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Consolas", Font.BOLD, 14));
        header.setBackground(BG_COLOR);
        header.setForeground(BUTTON_FG);
        header.setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BUTTON_BG, 2));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setBackground(PANEL_COLOR);
        JButton backBtn = new JButton("Indietro");
        styleSciFiButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MODULE_PANEL"));
        controls.add(backBtn);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    private void showManeuverCodesInTextArea() {
        StringBuilder codes = new StringBuilder();
        codes.append("""
                    RBM-EXT-001: Estensione braccio  
                        0  1  0  
                        0  1  1  
                        0  2  1    
                     
                    RBM-RET-002: Retrarre braccio  
                        0 -1  0  
                        0 -1 -1  
                        0 -2 -1  
                     
                    RBM-ROT-003: Rotazione pinza a 90°  
                        0  0  1  
                        1  0  1  
                        1  0  0  
                     
                    RBM-ALN-004: Allineamento al modulo Kibo  
                        1  0  0  
                        1  1  0  
                        2  1  0  
                     
                    RBM-DOCK-005: Manovra di ormeggio  
                        0  -1 -1  
                       -1  0  -1  
                       -1  0  -2  
                     
                    RBM-INS-006: Ispezione pannelli solari  
                        0  2  0  
                        0  2  1  
                        1  2  1  
                     
                    RBM-HLD-007: Mantenimento posizione  
                        0  0  0  
                        0  0  0  
                        0  0  0  
                     
                    RBM-RST-008: Rientro posizione standby  
                        -1 -1  0  
                        -1 -2  0  
                        -2 -2 -1  """);

        roboticsStatusArea.setText(String.join("\n", codes));
        roboticsStatusArea.setCaretPosition(0);
    }

    private void styleSciFiButton(AbstractButton btn) {
        btn.setFont(BUTTON_FONT);
        btn.setBackground(BUTTON_BG);
        btn.setForeground(BUTTON_FG);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BUTTON_FG, 2));
    }

    private String getStatusText(boolean aiActive) {
        String prefix = aiActive ? "AI Status: Active" : "AI Status: Deactivated";
        return prefix + "\n"
                + "Life Support: Nominal\n"
                + "Navigation: Online\n"
                + "Communications: Online\n";
    }

    /*public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            }   
        
        try (Connection conn = DBConfig.getConnection()) {
            DBConfig.populateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            HALterminal terminal = new HALterminal();
            terminal.setVisible(true);
        });
    }*/
}
