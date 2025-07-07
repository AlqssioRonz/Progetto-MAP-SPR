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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.table.JTableHeader;
import map.beforedeorbiting.database.DBConfig;

public class HALterminal extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private static final Color BG_COLOR = Color.decode("#0f111c");
    private static final Color PANEL_COLOR = Color.decode("#0f111c");
    private static final Color BUTTON_BG = Color.decode("#00e1d4");
    private static final Color BUTTON_FG = Color.decode("#00e1d4");
    private static final Font BUTTON_FONT = new Font("Consolas", Font.BOLD, 25);

    public HALterminal() {
        setTitle("Computer centrale della Stazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG_COLOR);

        mainPanel.add(createModulePanel(), "MODULE_PANEL");
        mainPanel.add(createControlPanel(), "CONTROL_PANEL");
        mainPanel.add(createCrewPanel(), "CREW_PANEL");

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
            if (name == "Crew"){
                btn.addActionListener(l -> cardLayout.show(mainPanel, "CREW_PANEL"));
            } else {
                btn.addActionListener(e -> cardLayout.show(mainPanel, "CONTROL_PANEL"));
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
        title.setForeground(BUTTON_BG);
        panel.add(title, BorderLayout.NORTH);

        JTextArea statusArea = new JTextArea(getStatusText(true));
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        statusArea.setBackground(BG_COLOR);
        statusArea.setForeground(BUTTON_BG);
        statusArea.setBorder(new LineBorder(BUTTON_BG, 2));
        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setBackground(PANEL_COLOR);

        JToggleButton aiToggle = new JToggleButton("Disattiva Controllo AI");
        styleSciFiButton(aiToggle);
        aiToggle.addActionListener(new ActionListener() {
            private boolean aiActive = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                aiActive = !aiActive;
                aiToggle.setText(aiActive ? "Disattiva Controllo AI" : "Attiva Controllo AI");
                statusArea.setText(getStatusText(aiActive));
            }
        });

        JButton backBtn = new JButton("Indietro");
        styleSciFiButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MODULE_PANEL"));

        controls.add(aiToggle);
        controls.add(backBtn);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel createCrewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel title = new JLabel("Crew ISS", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 26));
        title.setForeground(BUTTON_BG);
        panel.add(title, BorderLayout.NORTH);

        // 1) DEFINISCO I NOMI DELLE COLONNE
        String[] columnNames = {
            "Nome", "Cognome", "Nascita", "Luogo di nascita", "Ore ISS"
        };

        // 2) RECUPERO I DATI DAL DB IN UNA LISTA
        Object[][] data;
        try (Connection conn = DBConfig.getConnection()) {
            DBConfig.populateDatabase();
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
            e.printStackTrace();
            data = new Object[0][5];
        }

        // 3) CREO LA JTABLE
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Consolas", Font.PLAIN, 14));
        table.setRowHeight(24);

        // 4) STYLING “SCI-FI”
        table.setBackground(BG_COLOR);
        table.setForeground(BUTTON_BG);
        table.setGridColor(BUTTON_BG);
        table.setShowGrid(true);
        table.setSelectionBackground(BUTTON_BG.darker());
        table.setSelectionForeground(BG_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Consolas", Font.BOLD, 14));
        header.setBackground(BG_COLOR);
        header.setForeground(BUTTON_BG);
        header.setReorderingAllowed(false);

        // 5) METTO TUTTO NEL PANEL
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BUTTON_BG, 2));
        panel.add(scroll, BorderLayout.CENTER);

        // Pulsante Indietro
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setBackground(PANEL_COLOR);
        JButton backBtn = new JButton("Indietro");
        styleSciFiButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "MODULE_PANEL"));
        controls.add(backBtn);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }


    private void styleSciFiButton(AbstractButton btn) {
        btn.setFont(BUTTON_FONT);
        btn.setBackground(BUTTON_BG);
        btn.setForeground(BUTTON_FG);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BUTTON_BG, 2));
    }

    private String getStatusText(boolean aiActive) {
        String prefix = aiActive ? "AI Status: Active" : "AI Status: Deactivated";
        return prefix + "\n"
                + "Life Support: Nominal\n"
                + "Navigation: Online\n"
                + "Communications: Online\n";
    }
    
    public static void main(String[] args) {
    // 1) Una tantum, prima di aprire la UI
    try (Connection conn = DBConfig.getConnection()) {
        DBConfig.populateDatabase();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // 2) Avvio la GUI
    SwingUtilities.invokeLater(() -> {
        HALterminal terminal = new HALterminal();
        terminal.setVisible(true);
    });
}
}
