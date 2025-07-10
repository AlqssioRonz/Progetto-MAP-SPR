package map.beforedeorbiting.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Controller per la musica di sottofondo,
 * con supporto a play/stop/pause/resume e volume%.
 */
public class MusicController implements Runnable, Serializable {

    private transient Clip music;
    private String resourcePath;
    private boolean loop = true;

    // Percentuale di volume predefinita (0–100)
    private int pendingVolumePercent = 100;

    /**
     * Riproduce in loop la traccia presente nel classpath (resources).
     * @param resourcePath percorso dentro src/main/resources, es. "/music/ZeldaMenu.wav"
     */
    public void playMusic(String resourcePath) {
        stopMusica();
        this.resourcePath = resourcePath;
        this.loop = true;
        new Thread(this, "MusicPlayer").start();
    }

    /**
     * Riproduce una traccia una sola volta direttamente da file system.
     * @param filePath percorso assoluto o relativo sul disco
     */
    public void playMusicFromFile(String filePath) {
        stopMusica();
        this.resourcePath = filePath;
        this.loop = false;
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath));
                openAndStart(ais, false);
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }, "MusicPlayer").start();
    }

    @Override
    public void run() {
        try {
            AudioInputStream ais = null;
            if (resourcePath != null) {
                InputStream is = getClass().getResourceAsStream(resourcePath);
                if (is != null) {
                    ais = AudioSystem.getAudioInputStream(is);
                }
            }
            if (ais == null) {
                File f = new File(resourcePath);
                if (f.exists()) {
                    ais = AudioSystem.getAudioInputStream(f);
                } else {
                    System.err.println("Risorsa audio non trovata: " + resourcePath);
                    return;
                }
            }
            openAndStart(ais, loop);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void openAndStart(AudioInputStream ais, boolean loop)
            throws LineUnavailableException, IOException {
        music = AudioSystem.getClip();
        music.open(ais);
        // Applica volume predefinito o impostato
        applyVolumePercent(pendingVolumePercent);
        if (loop) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        music.start();
    }

    private void applyVolumePercent(int percent) {
        if (music != null) {
            FloatControl c = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            float min = c.getMinimum();
            float max = c.getMaximum();
            float db = min + (max - min) * (percent / 100f);
            c.setValue(db);
        }
    }

    /**
     * Ferma la riproduzione e rilascia le risorse.
     */
    public void stopMusica() {
        if (music != null) {
            music.stop();
            music.close();
            music = null;
        }
    }

    /**
     * Mette in pausa la riproduzione.
     */
    public void pausaMusica() {
        if (music != null && music.isRunning()) {
            music.stop();
        }
    }

    /**
     * Riprende la riproduzione se in pausa.
     */
    public void riprendiMusica() {
        if (music != null) {
            music.start();
        }
    }

    /**
     * Verifica se la traccia è in riproduzione.
     */
    public boolean isPlaying() {
        return music != null && music.isRunning();
    }

    /**
     * Imposta volume come percentuale [0–100].
     */
    public void setVolumePercent(int percent) {
        pendingVolumePercent = Math.max(0, Math.min(percent, 100));
        applyVolumePercent(pendingVolumePercent);
    }

    /**
     * Ottiene il volume corrente percentuale.
     */
    public int getVolumePercent() {
        return pendingVolumePercent;
    }

    /**
     * Riproduce una breve clip una tantum da filesystem.
     */
    public void riproduciClip(String filePath) {
        new Thread(() -> {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath))) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }, "SFXPlayer").start();
    }
}
