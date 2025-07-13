/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import map.beforedeorbiting.parser.ParserOutput;
import map.beforedeorbiting.type.CommandType;
import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.type.Room;
import map.beforedeorbiting.util.ISSPositionREST;

/**
 * Questa classe rappresenta l'observer del comando 'OSSERVA', permette di
 * osservare l'ambiente circostante/l'oggetto desiderato. Per farlo, implmenta
 * l'interfaccia GameObserver.
 *
 * @author ronzu
 */
public class LookAtObserver implements GameObserver, Serializable {

    private final Map<Room, Function<GameDesc, String>> stateDescr = new HashMap<>();
    private String Continent;
    private final ISSPositionREST issService;

    /**
     *
     * @param game
     * @param issService
     */
    public LookAtObserver(GameDesc game, ISSPositionREST issService) {
        this.issService = issService;
        stateDescr.put(game.getRoomByName("ZVEZDA"), this::zvezdaDescr);
        stateDescr.put(game.getRoomByName("ZARYA"), this::zaryaDescr);
        stateDescr.put(game.getRoomByName("UNITY"), this::unityDescr);
        stateDescr.put(game.getRoomByName("DESTINY"), this::destinyDescr);
        stateDescr.put(game.getRoomByName("TRANQUILITY"), this::tranquilityDescr);
        stateDescr.put(game.getRoomByName("QUEST"), this::questDescr);
        stateDescr.put(game.getRoomByName("HARMONY"), this::harmonyDescr);
        stateDescr.put(game.getRoomByName("LEONARDO"), this::leonardoDescr);
        stateDescr.put(game.getRoomByName("KIBO"), this::kiboDescr);
    }

    /**
     * Aggiorna lo stato del gioco in base all'output del parser e restituisce
     * un messaggio di risposta.
     *
     * @param game l'oggetto GameDesc che rappresenta lo stato corrente del
     * gioco
     * @param parserOutput l'output del parser utile per conoscere l'input
     * dell'utente
     * @return il messaggio di risposta in base all'azione di 'osserva'.
     */
    @Override
    public String update(GameDesc game, ParserOutput parserOutput) {
        StringBuilder lookAtmsg = new StringBuilder();
        if (parserOutput.getCommand().getType() == CommandType.LOOK_AT) {
            if (parserOutput.getObject() != null && game.getCurrentRoom()
                    .getObjects().contains(parserOutput.getObject())) {
                if (parserOutput.getInvObject() != null) {
                    lookAtmsg.append("Provi a osservare due cose alla volta, ma il tuo cervello va in tilt."
                            + "Meglio uno alla volta, fidati. Intanto ti mostro solo il primo oggetto...");
                }
                lookAtmsg.append(parserOutput.getObject().getDescription());
            } else if (parserOutput.getInvObject() != null) {
                lookAtmsg.append(parserOutput.getInvObject().getDescription());
            } else {
                lookAtmsg.append(game.getCurrentRoom().getLook());
                if (!game.getCurrentRoom().getName().equals("SPAZIO")) {
                    if (game.getCurrentRoom().getName().equals("DESTINY")) {
                        lookAtmsg.append(stateDescr.get(game.getCurrentRoom()).apply(game));
                    } else {
                        lookAtmsg.append("\n").append(stateDescr.get(game.getCurrentRoom()).apply(game));
                    }
                }
            }
        }
        return lookAtmsg.toString();
    }

    public String zvezdaDescr(GameDesc game) {
        String msg;

        if (game.getCurrentRoom().getObject(0) != null) {
            msg = """
                  Tutto è in ordine, tranne per uno strano 
                  dettaglio… sembra, un pezzo di modellino (modellinorusso) 
                  che galleggia in aria?""";
        } else {
            msg = """
                  Tutto è in ordine.""";
        }

        return msg;
    }

    public String zaryaDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();

        //id = 10 spaceSuit
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(10))) {
            if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(5))) {
                msg.append("""
                      Luke è seduto contro il muro, immobile. La visiera riflette 
                      la luce, ma non si muove.
                      Era il mio migliore amico, è probabilmente morto per mancanza 
                      di ossigeno, come sarà mai potuto succedere… stringe un 
                      bigliettino tra le sue mani.
                           """);
            } else {
                msg.append("""
                      Luke è seduto contro il muro, immobile. La visiera riflette 
                      la luce, ma non si muove.
                      Era il mio migliore amico. è probabilmente morto per mancanza 
                      di ossigeno, come sarà mai potuto succedere… """);
            }
        } else {
            if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(5))) {
                msg.append("""
                      Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta 
                      mi rivoltava lo stomaco. Il suo bigliettino galleggia in aria.
                           """);
            } else {
                msg.append("""
                      Ho dovuto spostare il cadavere di Luke.. vederlo senza tutta 
                      mi rivoltava lo stomaco.
                           """);
            }
        }
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(6))) {
            msg.append("Nella stanza vedo anche un piccolo pezzo di vetro... a cosa potrà mai servire?");
        }

        return msg.toString();
    }

    public String unityDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();

        if (game.getRoomByName("DESTINY").isAccessible()) {
            msg.append("La porta davanti a me è aperta, posso procedere verso Destiny.\n");
        } else {
            msg.append("""
                       La porta davanti, quella che porta al laboratorio Destiny è 
                       chiusa con tastierino numerico: serve un codice per aprirla.
                       
                       """);
        }
        if (game.getRoomByName("LEONARDO").isAccessible()) {
            msg.append("""
                       La botola sotto i miei piedi che conduce nel modulo Leonardo 
                       è aperta, ma non vorrei rivedere il cadavere di Susan. 
                       """);
        } else {
            msg.append("La botola sotto i miei piedi che conduce nel modulo Leonardo è chiusa.\n");
        }
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(1))) {
            msg.append("Sul pavimento galleggia un pezzo del modellino (modellinoamericano).");
        }

        return msg.toString();
    }

    public String destinyDescr(GameDesc game) {
        StringBuilder msg = new StringBuilder();
        if (!game.getRoomByName("HARMONY").isAccessible()) {
            if (game.getCurrentRoom().isVisible()) {
                msg.append("Adesso riesco a vedere qualcosa... ma non abbastanza per orientarmi.\n"
                        + "Riesco però a vedere il modulo di connessione, devo, in qualche modo, far riflettere la luce lì.\n");
                if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(6))) {
                    msg.append("Nella stanza vedo anche un piccolo pezzo di vetro... a cosa potrà mai servire?\n");
                }
            } else {
                msg.append("""
                           Cos’è stato… non vedo nulla se non per un flebile fascio di
                           luce riflesso dalla luna che proviene dalla finestra di
                           osservazione, ma non è sufficiente per orientarmi nella stanza. 
                           In questo momento non posso fare nulla qui... dovrei ASPETTARE.
                           """);
            }
        } else {
            msg.append("Un laboratorio sporco e silenzioso. \n"
                    + "Monitor spenti, attrezzi fluttuanti, l’aria di qualcosa lasciato a metà.\n"
                    + "Dal pavimento, un oblò mostra la Terra.");
        }
        return msg.toString();
    }

    public String tranquilityDescr(GameDesc game) {
        String msg;
        //id 2 modellinodx
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(2))) {
            msg = """
                  In mezzo alla calma, qualcosa di minuscolo fluttua davanti 
                  all’oblò centrale: un pezzo rigido, rettangolare, troppo 
                  ordinato per essere solo un detrito.
                  Postrebbe essere un altro pezzo del modellino (modellinodestro)""";
        } else {
            msg = "";
        }

        Continent = issService.getContinentForCoordinates();
        if (Continent != null) {
            switch (Continent) {
                case "africa":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Africa.jpg");
                    break;
                case "europa":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Europa.png");
                    break;
                case "asia":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Asia.png");
                    break;
                case "nordamerica":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/NordAmerica.png");
                    break;
                case "sudamerica":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/SudAmerica.png");
                    break;
                case "oceania":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Oceania.png");
                    break;
                case "antartide":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Antartide.png");
                    break;
                case "oceano":
                    game.getCurrentRoom().setRoomImage("src/main/resources/img/Oceano.png");
                    break;
            }
        }
        return msg;
    }

    public String questDescr(GameDesc game) {
        String msg;
        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(3))) {
            msg = """
                  Uno strano e piccolo oggetto fluttua davanti alla botola.
                  Postrebbe essere un alrro pezzo del modellino (modellinosinistro)""";
        } else {
            msg = "";
        }
        return msg;
    }

    public String harmonyDescr(GameDesc game) {
        return "Sembra che non ci sia nulla di che qui";
    }

    public String leonardoDescr(GameDesc game) {
        String msg;

        if (game.getCurrentRoom().getObjects().contains(game.getObjectByID(4))) {
            msg = """
                  Il modulo Leonardo, rivestito da contenitori imbottiti e
                 cavi. Sul lato sinistro, il terminale principale di HAL
                 emette un tenue bagliore verde. La botola su si è aperta e porta
                 direttamente a Unity. Il corpo di Susan
                 fluttua a mezz’aria, immobile. C'è un diario che vicino al corpo 
                 di Susan""";
        } else {
            msg = """ 
                  Il modulo Leonardo, rivestito da contenitori imbottiti e
                  cavi. Sul lato sinistro, il terminale principale di HAL
                  emette un tenue bagliore verde. La botola su si è aperta e porta
                  direttamente a Unity. Il corpo di Susan
                  fluttua a mezz’aria, immobile.""";
        }

        return msg;
    }
    
    public String kiboDescr(GameDesc game){
        return "";
    }
    
}
