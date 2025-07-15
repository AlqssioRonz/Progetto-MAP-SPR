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
 * Controller per la gestione della musica di sottofondo e degli effetti sonori.
 * Supporta play in loop, play singolo, pausa, ripresa, stop e regolazione del
 * volume.
 * <p>
 * Utilizza la libreria Java Sound API per caricare clip audio sia da risorse
 * interne al classpath (src/main/resources) che da file sul filesystem.
 * </p>
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class MusicController implements Runnable, Serializable {

    /**
     * Clip audio per la riproduzione della musica di sottofondo.
     */
    private transient Clip music;

    /**
     * Percorso della risorsa audio attualmente caricata.
     */
    private String resourcePath;

    /**
     * Flag che indica se la traccia deve essere riprodotta in loop continuo.
     */
    private boolean loop = true;

    /**
     * Volume in percentuale (0–100).
     */
    private int pendingVolumePercent = 100;

    /**
     * Soglia sotto la quale considerare il suono come rumore e mettere mute.
     */
    private static final float NOISE_GATE_DB = -40f;

    /**
     * Avvia la riproduzione in loop di una traccia presente nel classpath.
     *
     * @param resourcePath percorso della risorsa audio nel classpath, ad
     * esempio "/music/ZeldaMenu.wav"
     */
    public void playMusic(String resourcePath) {
        stopMusica();
        this.resourcePath = resourcePath;
        this.loop = true;
        new Thread(this, "MusicPlayer").start();
    }

    /**
     * Riproduce una traccia audio una sola volta, caricandola da file di
     * sistema.
     *
     * @param filePath percorso assoluto o relativo sul filesystem al file audio
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

    /**
     * Implementazione di {@link Runnable}: cerca la risorsa (classpath o file)
     * e la apre in un {@link Clip}.
     */
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

    /**
     * Apre lo stream audio e avvia il clip.
     *
     * @param ais lo {@link AudioInputStream} da aprire
     * @param loop se {@code true} ripete in loop continuo
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    private void openAndStart(AudioInputStream ais, boolean loop)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        music = AudioSystem.getClip();
        music.open(ais);
        applyVolumePercent(pendingVolumePercent);
        if (loop) {
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        music.start();
    }

    /**
     * Applica il volume in base alla percentuale impostata.
     *
     * @param percent volume da 0 a 100
     */
    private void applyVolumePercent(int percent) {
        if (music != null) {
            FloatControl ctrl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            float min = ctrl.getMinimum(), max = ctrl.getMaximum();
            float gainDb;
            if (percent <= 0) {
                gainDb = min;
            } else {
                gainDb = (float) (20.0 * Math.log10(percent / 100.0));
                if (gainDb < NOISE_GATE_DB) {
                    gainDb = min;
                }
            }
            gainDb = Math.max(min, Math.min(gainDb, max));
            ctrl.setValue(gainDb);
        }
    }

    /**
     * Ferma la riproduzione corrente e rilascia le risorse.
     */
    public void stopMusica() {
        if (music != null) {
            music.stop();
            music.close();
            music = null;
        }
    }

    /**
     * Mette in pausa la riproduzione corrente.
     */
    public void pausaMusica() {
        if (music != null && music.isRunning()) {
            music.stop();
        }
    }

    /**
     * Riprende la riproduzione se precedentemente in pausa.
     */
    public void riprendiMusica() {
        if (music != null) {
            music.start();
        }
    }

    /**
     * Controlla se un clip è attualmente in riproduzione.
     *
     * @return {@code true} se sta suonando, {@code false} altrimenti
     */
    public boolean isPlaying() {
        return music != null && music.isRunning();
    }

    /**
     * Imposta il volume desiderato come percentuale.
     *
     * @param percent valore compreso tra 0 (muto) e 100 (volume massimo)
     */
    public void setVolumePercent(int percent) {
        pendingVolumePercent = Math.max(0, Math.min(percent, 100));
        applyVolumePercent(pendingVolumePercent);
    }

    /**
     * Restituisce la percentuale di volume attualmente impostata.
     *
     * @return percentuale di volume (0–100)
     */
    public int getVolumePercent() {
        return pendingVolumePercent;
    }

    /**
     * Riproduce un breve effetto sonoro una tantum, caricato da file.
     *
     * @param filePath percorso del file audio sul filesystem
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
