/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 *
 * @author andre
 */
public class LoadBarUI extends JPanel {
    

    private JProgressBar progressBar;
    private JLabel progressBarLabel;
    private JPanel background;
    private final PropertyChangeSupport support;
    private int counter;
    private JPanel imgPanel;
    private int xPosition;
    private int yPosition;

    public LoadBarUI() {
        this.setSize(1100, 700);
        support = new PropertyChangeSupport(this);

        // Background panel con immagine
        background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon img = new ImageIcon("src/main/resources/img/SPAZIO2.jpg");
                g.drawImage(img.getImage(), 0, 0, 1100, 700, this);
            }
        };
        background.setLayout(null);

        // Load bar
        int border=5;
        progressBar = new JProgressBar();
        progressBar.setForeground(new Color(0, 200, 170));
        progressBar.setBackground(Color.BLACK);
        progressBar.setOpaque(true);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, border));
        progressBar.setLayout(new BorderLayout());
        progressBar.setBounds(100, 615+border, 900, 50-2*border);

        // Etichetta sulla barra
        progressBarLabel = new JLabel();
        progressBarLabel.setForeground(Color.BLACK);
        progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressBar.add(progressBarLabel, BorderLayout.CENTER);

        // Immagine da muovere
        ImageIcon img = new ImageIcon("src/main/resources/img/ISS.png");
        xPosition = 0; // Partenza visibile

        // Calcolo posizione iniziale su arco
        int arcWidth = 800;
        int arcHeight = 200;
        int startX = 100;
        int centerX = startX + arcWidth / 2;
        int centerY = 550;

        
        // Pannello per disegnare il pennello
        imgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image i = img.getImage();
                g.drawImage(i, xPosition, yPosition, 200, 200, this); // dimensione fissa per chiarezza
            }
        };
        imgPanel.setOpaque(false);
        imgPanel.setLayout(null);
        imgPanel.setBounds(0, 0, 1100, 700);

        // Aggiunta componenti
        background.add(imgPanel);
        background.add(progressBar);
        this.setLayout(null);
        this.add(background);
        background.setBounds(0, 0, 1100, 700);
    }

    public void setFinished(boolean isFinished) {
        support.firePropertyChange("isFinished", null, isFinished);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void startLoadBar() {
        counter = 0;
        Timer timer = new Timer();
        TimerTask taskProgressBar = new TimerTask() {
            @Override
            public void run() {
                if (counter < 100) {
                   
                    progressBar.setValue(counter);
                    progressBarLabel.setText("Lancio della spazione... " + counter + "%");

                    // Percorso semisferico
                    int arcWidth = 850;
                    int startX = 20;
                    int centerX = startX + arcWidth / 2;
                    int centerY = 550;

                    double t = counter / 100.0;
                    xPosition = (int) (startX + t * arcWidth);
                    double dx = xPosition - centerX;
                    yPosition = (int) (centerY - Math.sqrt(Math.max(0, (arcWidth / 2.0) * (arcWidth / 2.0) - dx * dx)))-35;
                    repaint(); // Ridisegna tutto il pannello
                    counter++;
                } else {
                    progressBarLabel.setText("Pronto al lancio!");
                    Timer timerlw = new Timer();
                    TimerTask taskProgressBarLastWord = new TimerTask() {
                        @Override
                        public void run() {
                            setFinished(true);
                            timerlw.cancel();
                        }
                    };
                    timerlw.schedule(taskProgressBarLastWord, 500);
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(taskProgressBar, 0, 20);         
    }
}

