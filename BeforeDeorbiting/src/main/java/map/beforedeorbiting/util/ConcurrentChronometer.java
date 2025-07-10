/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.util;

import java.time.Duration;
import java.time.Instant;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author lorenzopeluso
 */
public class ConcurrentChronometer extends Thread {

    private Instant start;
    private Instant end;
    
    private boolean runningThread = false;
    
    private JButton button;
    private JTextArea textarea;

    public ConcurrentChronometer() {
        this.start = null;
        this.end = null;
    }

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

    public String getTimeToString() {
        Duration duration = this.getTime();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void setButton(JButton button) {
        this.button = button;
    }
    
    public void setButton(JTextArea textarea) {
        this.textarea = textarea;
    }

    @Override
    public void run() {
        start = Instant.now();
        runningThread = true;
        while (runningThread) {
            try {
                
                SwingUtilities.invokeLater(() -> {
                    if(button != null)
                        button.setText(getTimeToString());
                    else if(textarea != null)
                        textarea.setText(getTimeToString());
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    
    
    public void stopTimer() {
        runningThread = false;
        end = Instant.now();      
    }
    
}

