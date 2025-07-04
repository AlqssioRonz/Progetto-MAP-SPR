/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.BDObject;
import map.beforedeorbiting.type.CommandType;

/**
 * Questa classe rappresenta l'observer del comando 'USE', permette al giocatore
 * di utilizzare gli oggetti in gioco e di visualizzare un messaggio di
 * risposta. Per farlo, implmenta l'interfaccia GameObserver.
 *
 * @author ronzu
 */
public class UseObserver implements GameObserver, Serializable {

    /*
     * Crea una HashMap che ci permette di utilizzare un determinato
     * oggetto, in base all'id scelto.
     */
    private final Map<BDObject, Function<GameDesc, String>> uses = new HashMap<>();

    private static final String FILE_PATH = "notebook.txt";

    public UseObserver(GameDesc game) {
        uses.put(game.getObjectByID(4), this::readSusanDiary);
        uses.put(game.getObjectByID(5), this::readLukeNote);
        uses.put(game.getObjectByID(6), this::createPrism);
        uses.put(game.getObjectByID(7), this::createPrism);
        uses.put(game.getObjectByID(8), this::usePrism);
        uses.put(game.getObjectByID(9), this::useComputer);
        uses.put(game.getObjectByID(10), this::wearSpaceSuit);
        uses.put(game.getObjectByID(11), this::useNotebook);
    }

    /*
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     * 
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
     * 
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * 
     * @return il messaggio di risposta in base all'azione di 'usa'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder useMsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.USE) {
            if (parserOutput.getObject() != null && parserOutput.getObject().isUsable()) {
                if (game.getInventory().getList().contains(parserOutput.getObject())) {
                    useMsg.append(uses.get(parserOutput.getObject()).apply(game));
                } else {
                    useMsg.append("Non possiedi questo oggetto al momento! Riprova, magari sarai più fortunato.");
                }
            } else {
                useMsg.append("Come pensi di poterlo usare?!");
            }
        }
        return useMsg.toString();
    }

    public String readSusanDiary(GameDesc game) {
        StringBuilder readDiary = new StringBuilder();
        readDiary.append("Le pagine sono poche, ordinate, scritte con una grafia decisa. \n"
                + "Non si perde in pensieri, ma qualcosa filtra tra le righe. \n"
                + "Si capisce che era entrata nel modulo Leonardo con uno scopo preciso. \n"
                + "Aveva notato discrepanze nei log di HAL, comportamenti anomali. \n"
                + "Scrive: “I dati non tornano. Ogni volta che controllo, c’è qualcosa che manca o è stato sovrascritto. "
                + "Nessuno sembra accorgersene. \n"
                + "Nell’ultima pagina, datata oggi, la scrittura è più rapida, quasi nervosa:\n"
                + "“È l’ultimo giorno. Prima di andarmene devo passare i dati principali manualmente da questo computer alla base sulla terra."
                + " È solo una precauzione… ma qualcosa nel modo in cui questa macchina ‘decide’ mi mette a disagio.”");
        return readDiary.toString();
    }

    public String readLukeNote(GameDesc game) {
        StringBuilder readNote = new StringBuilder();
        readNote.append("Nel bigliettino di Luke c'è una nota affrettata: \n");
        readNote.append("D4T4B4S3 -> 'Il dado è tratto. [4]' \n");
        readNote.append("Cosa vorrà mai dire...? \n");
        return readNote.toString();
    }

    public String createPrism(GameDesc game) {
        StringBuilder prismMsg = new StringBuilder();
        if (game.getInventory().count(game.getObjectByID(6)) >= 2) {
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().add(game.getObjectByID(7));
            prismMsg.append("Unendo due pezzi di vetro hai creato un mezzo prisma! Lo trovi nel tuo inventario.");
        } else {
            if (game.getInventory().getList().contains(game.getObjectByID(7))
                    && game.getInventory().count(game.getObjectByID(6)) == 1) {
                game.getInventory().remove(game.getObjectByID(6));
                game.getInventory().remove(game.getObjectByID(7));
                game.getInventory().add(game.getObjectByID(8));
                prismMsg.append("Unendo il mezzo prisma e il pezzo di vetro "
                        + "hai creato un prisma! Lo trovi nel tuo inventario.");
            } else {
                prismMsg.append("Non hai abbastanza pezzi di vetro!");
            }
        }
        return prismMsg.toString();
    }

    public String usePrism(GameDesc game) {
        StringBuilder usingPrismMsg = new StringBuilder();
        // servirebbe anche il controllo per capire se la luce dall'oblò arriva
        if (game.getCurrentRoom().getName().equalsIgnoreCase("destiny")) {
            game.getInventory().remove(game.getObjectByID(8));
            System.out.println("Hai posizionato il prisma!");
            // METTERE IL MINIGAME
            usingPrismMsg.append("Hai completato l'enigma!");
        } else {
            usingPrismMsg.append("Non puoi usare qui il prisma!");
        }
        return usingPrismMsg.toString();
    }

    public String useComputer(GameDesc game) {
        StringBuilder usingComputer = new StringBuilder();
        System.out.println("Hai acceso il computer!");
        game.getObjectByID(9).setInUse(true);
        // METTERE L'UTILIZZO DEL DATABASE
        game.getObjectByID(9).setInUse(false);
        usingComputer.append("Hai spento il pc!");
        return usingComputer.toString();
    }

    public String wearSpaceSuit(GameDesc game) {
        StringBuilder wearSuit = new StringBuilder();
        wearSuit.append("Hai indossato la tuta di Luke! \n"
                + "Iniziano a riaffiorare tutti i bei ricordi e le avventure passate insieme, "
                + "causandoti tanta nostalgia.\n");
        game.getInventory().remove(game.getObjectByID(10));
        // quando entrerà in leonardo, dopo il minigame, scrivere che la tuta si è
        // completamente rotta
        return wearSuit.toString();
    }

    public String useNotebook(GameDesc game) {
        System.out.println("Apri il taccuino!");
        mostraTaccuinoSwing();
        return "";
    }

    // Metodo principale che apre la finestra del taccuino
    private void mostraTaccuinoSwing() {
        SwingUtilities.invokeLater(() -> {
            // Crea la finestra
            JFrame frame = new JFrame("Taccuino di bordo");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // vogliamo gestire la chiusura manualmente

            // Area di testo dove scrivere e leggere
            JTextArea textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            frame.add(scrollPane, BorderLayout.CENTER); // aggiungiamo al centro della finestra

            File file = new File(FILE_PATH);
            boolean[] modificato = { false }; // flag per sapere se l'utente ha modificato il testo

            // Carica il contenuto del file nel text area
            caricaContenuto(file, textArea);
            modificato[0] = false; // dopo il caricamento, non ci sono modifiche

            // Listener per sapere quando il testo è stato modificato
            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    modificato[0] = true;
                }
            });

            // Crea il pulsante Salva
            JButton salvaButton = new JButton("Salva");
            salvaButton.addActionListener(e -> {
                salvaContenuto(file, textArea); // salva su file
                modificato[0] = false; // dopo il salvataggio, non ci sono modifiche
            });

            // Aggiungiamo il pulsante in basso
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(salvaButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Gestione della chiusura finestra (es. clic sulla X)
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (modificato[0]) {
                        Object[] opzioni = {"Sì", "No", "Annulla"};
                        int scelta = JOptionPane.showOptionDialog(frame,
                        "Hai modificato il taccuino. Vuoi salvare prima di uscire?",
                        "Salvataggio",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opzioni,
                        opzioni[0]);

                        if (scelta == JOptionPane.YES_OPTION) {
                            salvaContenuto(file, textArea);
                            JOptionPane.showMessageDialog(frame,
                                    "Hai chiuso il taccuino. Puoi consultarlo di nuovo in qualsiasi momento, finché resterà nel tuo inventario.",
                                    "Chiusura taccuino",
                                    JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose();
                        } else if (scelta == JOptionPane.NO_OPTION) {
                            JOptionPane.showMessageDialog(frame,
                                    "Hai chiuso il taccuino. Puoi consultarlo di nuovo in qualsiasi momento, finché resterà nel tuo inventario.",
                                    "Chiusura taccuino",
                                    JOptionPane.INFORMATION_MESSAGE);
                            frame.dispose();
                        }
                        // CANCEL: non chiudere
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Hai chiuso il taccuino. Puoi consultarlo di nuovo in qualsiasi momento, finché resterà nel tuo inventario.",
                                "Chiusura taccuino",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    }
                }
            });

            frame.setVisible(true); // mostra la finestra
        });
    }

    // Metodo per leggere il contenuto del file e inserirlo nel JTextArea
    private void caricaContenuto(File file, JTextArea textArea) {
        try {
            if (!file.exists()) {
                file.createNewFile(); // crea il file se non esiste
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n"); // aggiunge ogni riga al text area
            }
            reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nella lettura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo per salvare il contenuto del JTextArea nel file
    private void salvaContenuto(File file, JTextArea textArea) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(textArea.getText()); // scrive tutto il contenuto
            writer.close();
            JOptionPane.showMessageDialog(null, "Taccuino salvato con successo!", "Salvato",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Errore nel salvataggio del file.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}