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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * Pannello grafico che mostra una barra di caricamento animata con un'immagine
 * che si muove lungo un percorso semisferico indicante il progresso del
 * caricamento.
 * <p>
 * Utilizza un {@link PropertyChangeSupport} per notificare ai listener
 * l'avvenuto completamento del caricamento.
 * </p>
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class LoadBarUI extends JPanel {

    /**
     * Barra di progresso grafica che indica il caricamento.
     */
    private JProgressBar progressBar;

    /**
     * Etichetta centrale sovrapposta alla barra di progresso.
     */
    private JLabel progressBarLabel;

    /**
     * Pannello di sfondo su cui viene disegnata l'immagine di fondo.
     */
    private JPanel background;

    /**
     * Gestore di eventi per notificare la proprietà "isFinished".
     */
    private final PropertyChangeSupport support;

    /**
     * Contatore percentuale di avanzamento del caricamento.
     */
    private int counter;

    /**
     * Pannello trasparente in cui viene disegnata l'icona animata.
     */
    private JPanel imgPanel;

    /**
     * Coordinata orizzontale corrente dell'immagine animata.
     */
    private int xPosition;

    /**
     * Coordinata verticale corrente dell'immagine animata.
     */
    private int yPosition;

    /**
     * Costruisce il pannello di caricamento, inizializzando:
     * <ul>
     * <li>lo sfondo con immagine</li>
     * <li>la {@link JProgressBar} personalizzata</li>
     * <li>l'immagine animata da muovere</li>
     * </ul>
     */
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
        int border = 5;
        progressBar = new JProgressBar();
        progressBar.setForeground(Color.decode("#00e1d4"));
        progressBar.setBackground(Color.BLACK);
        progressBar.setOpaque(true);
        progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, border));

        // Override UI per disegnare la fill a tutta larghezza percentuale
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                double progress = progressBar.getPercentComplete();
                int larghezza = (int) (c.getWidth() * progress);
                int altezza = c.getHeight();
                g.setColor(progressBar.getForeground());
                g.fillRect(0, 0, larghezza, altezza);
                // disegno del bordo esterno
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
            }
        });

        progressBar.setLayout(new BorderLayout());
        progressBar.setBounds(100, 615 + border, 900, 50 - 2 * border);

        // Etichetta centrale sulla barra di progresso
        progressBarLabel = new JLabel();
        progressBarLabel.setForeground(Color.BLACK);
        progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressBar.add(progressBarLabel, BorderLayout.CENTER);

        // Immagine da muovere
        ImageIcon img = new ImageIcon("src/main/resources/img/ISS.png");
        xPosition = 0; // posizione iniziale
        imgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image i = img.getImage();
                g.drawImage(i, xPosition, yPosition, 200, 200, this);
            }
        };
        imgPanel.setOpaque(false);
        imgPanel.setLayout(null);
        imgPanel.setBounds(0, 0, 1100, 700);

        // Aggiunta di componenti al pannello principale
        background.add(imgPanel);
        background.add(progressBar);
        this.setLayout(null);
        this.add(background);
        background.setBounds(0, 0, 1100, 700);
    }

    /**
     * Registra un listener per essere notificato quando il caricamento è
     * terminato (property "isFinished" cambia a {@code true}).
     *
     * @param listener il {@link PropertyChangeListener} da aggiungere
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Imposta lo stato di completamento e notifica i listener registrati.
     *
     * @param isFinished {@code true} se il caricamento è terminato
     */
    public void setFinished(boolean isFinished) {
        support.firePropertyChange("isFinished", null, isFinished);
    }

    /**
     * Avvia l'animazione della barra di caricamento:
     * <ul>
     * <li>incrementa regolarmente il valore di {@code progressBar}</li>
     * <li>aggiorna la {@code progressBarLabel} con la percentuale</li>
     * <li>muove l'immagine secondo un percorso semisferico</li>
     * <li>al termine notifica i listener con {@link #setFinished(boolean)}</li>
     * </ul>
     */
    public void startLoadBar() {
        counter = 0;
        Timer timer = new Timer();
        TimerTask taskProgressBar = new TimerTask() {
            @Override
            public void run() {
                if (counter < 100) {
                    progressBar.setValue(counter);
                    progressBarLabel.setText("Lancio della spazione... " + counter + "%");

                    // Calcolo percorso semisferico
                    int arcWidth = 850;
                    int startX = 20;
                    int centerX = startX + arcWidth / 2;
                    int centerY = 550;
                    double t = counter / 100.0;
                    xPosition = (int) (startX + t * arcWidth);
                    double dx = xPosition - centerX;
                    yPosition = (int) (centerY
                            - Math.sqrt(Math.max(0,
                                    (arcWidth / 2.0) * (arcWidth / 2.0) - dx * dx)))
                            - 35;
                    repaint();
                    counter++;
                } else {
                    progressBarLabel.setText("Pronto al lancio!");
                    Timer timerlw = new Timer();
                    TimerTask taskLast = new TimerTask() {
                        @Override
                        public void run() {
                            setFinished(true);
                            timerlw.cancel();
                        }
                    };
                    timerlw.schedule(taskLast, 500);
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(taskProgressBar, 0, 20);
    }
}
