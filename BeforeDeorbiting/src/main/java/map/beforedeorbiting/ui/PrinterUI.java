package map.beforedeorbiting.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;

/**
 * Gestisce la "stampa" di testo in un {@link JTextPane} con effetto di
 * digitazione simulando una macchina da scrivere. Internamente utilizza un
 * {@link ExecutorService} per non bloccare il thread UI.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class PrinterUI {

    private final JTextPane textPane;
    private final ExecutorService executor;
    private final DefaultCaret caret;
    private int speed = 20;

    /**
     * Costruisce un PrinterUI associato a un dato JTextPane. Inizializza anche
     * l'ExecutorService per gestire la stampa asincrona.
     *
     * @param textPane il JTextPane su cui verrà stampato il testo con l'effetto
     * di digitazione
     */
    public PrinterUI(JTextPane textPane) {
        this.textPane = textPane;
        this.executor = Executors.newSingleThreadExecutor();
        this.caret = (DefaultCaret) textPane.getCaret();
    }

    /**
     * Restituisce la velocità di stampa (ritardo in millisecondi tra un
     * carattere e l'altro).
     *
     * @return il ritardo corrente in millisecondi tra la stampa di due
     * caratteri
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Imposta la velocità di stampa.
     *
     * @param speed il nuovo ritardo in millisecondi tra la stampa di due
     * caratteri
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Stampa il testo nel JTextPane con un effetto di digitazione. Ogni
     * carattere viene inserito con un ritardo pari a {@link #speed}. Il caret
     * viene temporaneamente disabilitato per evitare salti indesiderati durante
     * la digitazione, e ripristinato al termine.
     *
     * @param text la stringa da "digitare" nel JTextPane
     */
    public void print(String text) {
        executor.submit(() -> {
            try {
                // Disabilita l'aggiornamento automatico del caret
                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

                // Documento in cui inserire il testo
                StyledDocument doc = textPane.getStyledDocument();

                for (char c : text.toCharArray()) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            doc.insertString(doc.getLength(), String.valueOf(c), null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(speed);
                }

                // Riabilita l'aggiornamento automatico del caret e posizionalo alla fine
                SwingUtilities.invokeLater(() -> {
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                    textPane.setCaretPosition(doc.getLength());
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Termina immediatamente l'ExecutorService, interrompendo ogni compito in
     * corso e impedendo ulteriori stampe.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
}
