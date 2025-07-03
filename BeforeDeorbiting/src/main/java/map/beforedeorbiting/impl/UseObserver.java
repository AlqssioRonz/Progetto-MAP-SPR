/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    public UseObserver(GameDesc game){
        uses.put(game.getObjectByID(4), this::readSusanDiary);
        uses.put(game.getObjectByID(5), this::readLukeNote);
        uses.put(game.getObjectByID(6), this::createPrism);
        uses.put(game.getObjectByID(7), this::createPrism);
        uses.put(game.getObjectByID(8), this::usePrism);
        uses.put(game.getObjectByID(9), this::useComputer);
        uses.put(game.getObjectByID(10), this::wearSpaceSuit);
    }
    
    /*
    * Aggiorna lo stato del gioco in base all'output del parser e restituisce
    * un messaggio di risposta.
    * @param game l'oggetto GameDesc che rappresenta lo stato corrente del gioco
    * @param parserOutput l'output del parser utile per conoscere l'input dell'utente
    * @return il messaggio di risposta in base all'azione di 'usa'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder useMsg = new StringBuilder();
        if(parserOutput.getCommand().getType() == CommandType.USE){
            if(parserOutput.getObject() != null && parserOutput.getObject().isUsable()){
                if(game.getInventory().getList().contains(parserOutput.getObject())){
                    useMsg.append(uses.get(parserOutput.getObject()).apply(game));
                }else{
                    useMsg.append("Non possiedi questo oggetto al momento! Riprova, magari sarai più fortunato.");
                }
            }else{
                useMsg.append("Come pensi di poterlo usare?!");
            }
        }
        return useMsg.toString();
    }
    
    public String readSusanDiary(GameDesc game){
        StringBuilder readDiary = new StringBuilder();
        // AGGIUNGERE IL DIARIO DI SUSAN TRAMITE readDiary.append();
        return readDiary.toString();
    }
    
    public String readLukeNote(GameDesc game){
        StringBuilder readNote = new StringBuilder();
        // AGGIUNGERE IL BIGLIETTINO DI LUKE TRAMITE readDiary.append();
        return readNote.toString();
    }
    
    public String createPrism(GameDesc game){
        StringBuilder prismMsg = new StringBuilder();
        if(game.getInventory().count(game.getObjectByID(6)) >= 2){
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().remove(game.getObjectByID(6));
            game.getInventory().add(game.getObjectByID(7));
            prismMsg.append("Unendo due pezzi di vetro hai creato un mezzo prisma! Lo trovi nel tuo inventario.");
        }else{
            if(game.getInventory().getList().contains(game.getObjectByID(7)) && game.getInventory().count(game.getObjectByID(6)) == 1){
                game.getInventory().remove(game.getObjectByID(6));
                game.getInventory().remove(game.getObjectByID(7));
                game.getInventory().add(game.getObjectByID(8));
                prismMsg.append("Unendo il mezzo prisma e il pezzo di vetro "
                        + "hai creato un prisma! Lo trovi nel tuo inventario.");
            }else{
                prismMsg.append("Non hai abbastanza pezzi di vetro!");
            }
        }
        return prismMsg.toString();
    }
    
    public String usePrism(GameDesc game){
        StringBuilder usingPrismMsg = new StringBuilder();
        game.getInventory().remove(game.getObjectByID(8));
        System.out.println("Hai posizionato il prisma!");
        // METTERE IL MINIGAME
        usingPrismMsg.append("Hai completato l'enigma!");
        return usingPrismMsg.toString();
    }
    
    public String useComputer(GameDesc game){
        StringBuilder usingComputer = new StringBuilder();
        System.out.println("Hai acceso il computer!");
        game.getObjectByID(9).setInUse(true);
        // METTERE L'UTILIZZO DEL DATABASE
        game.getObjectByID(9).setInUse(false);
        usingComputer.append("Hai spento il pc!");
        return usingComputer.toString();
    }
    
    public String wearSpaceSuit(GameDesc game){
        StringBuilder wearSuit = new StringBuilder();
        wearSuit.append("Hai indossato la tuta di Luke!."
                + "Iniziano a riaffiorarti tutti i bei ricordi e le avventure passate insieme, "
                + "causandoti tanta nostalgia.\n"
                + "Nel frattempo, noti che la tuta è danneggiata.\n Ciò comporterà un'autonomia"
                + "limitata, rispetto al suo regolare funzionamento");
        game.getInventory().remove(game.getObjectByID(10));
        // quando entrerà in leonardo, dopo il minigame, scrivere che la tuta si è completamente rotta
        return wearSuit.toString();
    }

}
