package map.beforedeorbiting.util;

import java.time.Duration;
import java.time.Instant;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Un cronometro che gira in un thread separato e aggiorna periodicamente un
 * {@link JButton} o una {@link JTextArea} con il tempo trascorso dal suo avvio.
 * <p>
 * Utilizza {@link Instant} per misurare il tempo e SwingUtilities per
 * aggiornare la UI in modo thread-safe.
 * </p>
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class ConcurrentChronometer extends Thread {

    private Instant start;
    private Instant end;
    private boolean runningThread = false;

    private JButton button;
    private JTextArea textarea;

    /**
     * Costruisce un cronometro non avviato. {@link #start} e {@link #end}
     * saranno null finché non viene invocato {@link #run()}.
     */
    public ConcurrentChronometer() {
        this.start = null;
        this.end = null;
    }

    /**
     * Restituisce la durata corrente del cronometro. Se il cronometro non è
     * stato avviato, ritorna {@link Duration#ZERO}. Se è in esecuzione, calcola
     * la differenza tra l'istante di avvio e l'istante attuale. Se è stato
     * fermato, calcola la differenza tra l'istante di avvio e quello di
     * arresto.
     *
     * @return la {@link Duration} trascorsa
     */
    public Duration getTime() {
        if (start == null) {
            return Duration.ZERO;
        } else if (runningThread) {
            return Duration.between(start, Instant.now());
        } else if (end != null) {
            return Duration.between(start, end);
        }
        return Duration.ZERO;
    }

    /**
     * Converte il tempo corrente in una stringa formattata "HH:mm:ss".
     *
     * @return il tempo trascorso formattato
     */
    public String getTimeToString() {
        Duration duration = this.getTime();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Associa un {@link JButton} che verrà aggiornato ogni secondo con il tempo
     * trascorso.
     *
     * @param button il pulsante da aggiornare
     */
    public void setButton(JButton button) {
        this.button = button;
    }

    /**
     * Associa una {@link JTextArea} che verrà aggiornata ogni secondo con il
     * tempo trascorso.
     *
     * @param textarea l'area di testo da aggiornare
     */
    public void setButton(JTextArea textarea) {
        this.textarea = textarea;
    }

    /**
     * Avvia il cronometro nel thread corrente: salva l'istante di inizio,
     * imposta il flag di esecuzione e ogni secondo invoca SwingUtilities per
     * aggiornare la UI con {@link #getTimeToString()}.
     */
    @Override
    public void run() {
        start = Instant.now();
        runningThread = true;
        while (runningThread) {
            try {
                SwingUtilities.invokeLater(() -> {
                    String text = getTimeToString();
                    if (button != null) {
                        button.setText(text);
                    } else if (textarea != null) {
                        textarea.setText(text);
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Se il thread viene interrotto, esce dal ciclo
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Ferma il cronometro: cancella il flag di esecuzione e memorizza l'istante
     * di termine.
     */
    public void stopTimer() {
        runningThread = false;
        end = Instant.now();
    }
}
