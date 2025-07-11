/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.util;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import map.beforedeorbiting.GameDesc;

/**
 *
 * @author lorenzopeluso
 */
public class ConcurrentCountdown extends Thread {

    private Instant start;
    private Duration countdownDuration;
    private boolean runningThread = false;
    private boolean startThread = false;
    private boolean countdownExpired = false;
    private GameDesc game;

    private JButton button;
    private JTextArea textarea;
    
    private ConcurrentChronometer prey;
    
    private Consumer<String> onFinish;

    public ConcurrentCountdown(Duration countdownDuration, GameDesc game) {
        this.countdownDuration = countdownDuration;
        this.game = game;
    }

    public Duration getRemainingTime() {
        if (start == null) {
            return countdownDuration;
        }
        Duration elapsed = Duration.between(start, Instant.now());
        Duration remaining = countdownDuration.minus(elapsed);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public String getTimeToString() {
        Duration duration = getRemainingTime();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public void setTextarea(JTextArea textarea) {
        this.textarea = textarea;
    }

    @Override
    public void run() {

        while (runningThread) {
            if(startThread) {
                startThread = false;
                start = Instant.now();
                runningThread = true;
                this.predatePrey(); //avvia la predazione
            }
            Duration remaining = getRemainingTime();
            if (remaining.isZero()) {
                countdownExpired = true;
                this.stopCountdown();
                this.releasePrey(); //ferma la predazione
                this.game.setCurrentRoom(game.getRoomByName("QUEST"));
                System.out.println("Hai finito il tuo tempo nello spazio e ti risvegli "
                    + "al punto di partenza. Come è mai potuto succedere?");
            }

            SwingUtilities.invokeLater(() -> {
                if (button != null)
                    //button.setText(getTimeToString());
                    button.setText("qui");
                else if (textarea != null)
                    textarea.setText(getTimeToString());
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        
        if (onFinish != null && countdownExpired) {
            onFinish.accept("Hai finito il tuo tempo nello spazio e ti risvegli "
                    + "al punto di partenza. Come è mai potuto succedere?");
        }
    }

    public void stopCountdown() {
        runningThread = false;
    }
    
    public void startCountdown() {
        startThread = true;
        countdownExpired = false;
        runningThread = true;
        if (!this.isAlive()) {
            this.start();
        }
    }
    
    public void setConcurrentChronometerPrey(ConcurrentChronometer prey) {
        this.prey = prey;
    }
    
    public void predatePrey() {
        this.prey.setHideDisplay(true);
    }
    
    public void releasePrey() {
        this.prey.setHideDisplay(false);
    }
    
    public void setOnFinish(Consumer<String> onFinish) {
        this.game.setCurrentRoom(game.getRoomByName("QUEST"));
        this.onFinish = onFinish;
    }
    
    
}

