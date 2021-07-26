package application.ai;

import application.model.*;
import it.unical.mat.embasp.base.*;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIPlayer {
    private Game game;
    private Integer mode;
    private DesktopService service;
    private Handler handler;
    private boolean finished;
    private HashMap<Coord, HashMap<Coord, Integer>> distances;

    public AIPlayer(Game game, Integer mode) {
        this.game = game;
        this.mode = mode;
        finished = true;
        service = new DLV2DesktopService("src/application/resources/lib/dlv2");
        handler = new DesktopHandler(service);
        try {
            ASPMapper.getInstance().registerClass(AIDirection.class);
            ASPMapper.getInstance().registerClass(AICloseWallCommand.class);
            ASPMapper.getInstance().registerClass(AIDistances.class);
            loadPrimaryFacts();
        } catch (ObjectNotValidException e) {
            e.printStackTrace();
        } catch (IllegalAnnotationException e) {
            e.printStackTrace();
        }
    }

    private void addToDistancesMap(Coord source, Coord destination, Integer distance) {
        if(!distances.containsKey(source))
            distances.put(source, new HashMap<>());

        distances.get(source).put(destination, distance);
    }

    private List<Coord> getNodeCoords(Integer node) {
        List<Coord> coords = new ArrayList<>();

        if(node == 1001)
            coords.add(new Coord(11, 30));
        else if(node == 1002) {
            coords.add(new Coord(11, -2));
            coords.add(new Coord(11, 31));
        }
        else if(node == 1003)
            coords.add(new Coord(11, -1));
        else
            coords.add(new Coord(node / 30, node % 30));

        return coords;
    }


    private Integer getCoordsNode(Coord coord) {
        if(coord.getRow() == 11) {
            if(coord.getCol() == 30)
                return 1001;
            else if(coord.getCol() == 31 || coord.getCol() == -2)
                return 1002;
            else if(coord.getCol() == -1)
                return 1003;
        }

        return coord.getRow() * 30 + coord.getCol();
    }

    private void loadPrimaryFacts() {
        distances = new HashMap<>();

        InputProgram program = new ASPInputProgram();
        program.addFilesPath("src/application/resources/lib/distances.txt");
        handler.addProgram(program);


        // ANSWER SETS ANALYSIS
        Output output = handler.startSync();
        AnswerSets answersets = (AnswerSets) output;

        try {
            for(Object obj : answersets.getAnswersets().get(0).getAtoms()) {
                if (!(obj instanceof AIDistances)) {
                    continue;
                }

                AIDistances distance = (AIDistances) obj;

                List<Coord> sources = getNodeCoords(distance.getSourceNode());
                List<Coord> destinations = getNodeCoords(distance.getDestinationNode());

                for(Coord s : sources)
                    for(Coord d : destinations)
                        addToDistancesMap(s, d, distance.getDistance());

            }
        }catch (Exception e) {}
    }

    private Coord updatedPlayerCoords(GameCharacter c) {
        double myY = c.getY(), myX = c.getX();

        switch (c.getDirection()) {
            /*case 0:
                myY -= 4;
                break;*/
            case 1:
                myX += 4;
                break;
            case 2:
                myY += 4;
                break;
            /*case 3:
                myX -= 4;
                break;*/
        }

        Coord newCoords;

        int myRow = (int)myY / 8, myCol = (int)myX / 8;

        if(myCol <= -3)
            myCol = 30;
        else if(myCol >= 32)
            myCol = -1;

        if(game.isValidPosition(myRow, myCol))
            newCoords = new Coord(myRow, myCol);
        else
            newCoords = new Coord((int)c.getY() / 8, (int)c.getX() / 8);

        return newCoords;
    }

    private String enemyNode(GameCharacter c, String name) {
        String fact = "enemy(" + getCoordsNode(new Coord((int)c.getY() / 8, (int)c.getX() / 8)) + ", " + name;

        fact += ", ";

        if(c.isActive())
            fact += "active";
        else if(c.isImminentRestart())
            fact += "restart";
        else
            fact += "locked";

        fact += "). ";

        return fact;
    }

    private String wallNode(Wall w, String type) {
        String fact = type + "(" + getCoordsNode(w.getCoord()) + "). ";
        if(type.equals("wall")) {
            if(w.getType().equals(Wall.HORIZONTAL_WALL)) {
                fact += "nearWall" + "(" + ((w.getCoord().getRow() - 1) * game.getColsNum() + w.getCoord().getCol()) + "). ";
                fact += "nearWall" + "(" + ((w.getCoord().getRow() + 1) * game.getColsNum() + w.getCoord().getCol()) + "). ";
            }
            else if(w.getType().equals(Wall.VERTICAL_WALL)) {
                fact += "nearWall" + "(" + (w.getCoord().getRow() * game.getColsNum() + w.getCoord().getCol() - 1) + "). ";
                fact += "nearWall" + "(" + (w.getCoord().getRow() * game.getColsNum() + w.getCoord().getCol() + 1) + "). ";
            }
        }


        return fact;
    }

    private String targetNode(Coord c, String name) {
        return name + "(" + getCoordsNode(c) + "). ";
    }

    private String distance(Coord x, Coord y) {
        String fact;

        if(distances.get(x) != null) {
            Integer d = distances.get(x).get(y);

            if(d != null)
                return "distanzeGenerate(" + getCoordsNode(x) + ", " + getCoordsNode(y) + ", " + d + "). ";

        }

        int d = Math.abs(x.getRow() - y.getRow()) + Math.abs(x.getCol() - y.getCol());
        return "distanzeCalcolate(" + getCoordsNode(x) + ", " + getCoordsNode(y) + ", " + d + "). ";
    }

    private String coordsToEnemiesDistances(Coord p, Integer dir) {
        String facts = "";
        facts += distance(p, new Coord((int)game.getStiffy().getY() / 8, (int)game.getStiffy().getX() / 8));
        facts += distance(p, new Coord((int)game.getSpeedy().getY() / 8, (int)game.getSpeedy().getX() / 8));
        facts += distance(p, new Coord((int)game.getScaredy().getY() / 8, (int)game.getScaredy().getX() / 8));
        facts += distance(p, new Coord((int)game.getSilly().getY() / 8, (int)game.getSilly().getX() / 8));

        return facts;
    }

    public void exec() {
        if(!finished || (game.isBeginning() || game.isFinished() || game.isWinFreeze() || game.isGameOver() || game.isPlayerDead()))
            return;

        finished = false;
        InputProgram program = new ASPInputProgram();
        program.addFilesPath("src/application/resources/lib/prova6.txt");
        String myFacts = "";
        handler.removeAll();
        OptionDescriptor optionDescriptor = new OptionDescriptor("-n 100 --no-facts");
        handler.addOption(optionDescriptor);


        // UPDATE PLAYER COORDS
        Coord playerCoords = updatedPlayerCoords(game.getPlayer());


        // CHARACTERS POSITION
        myFacts += "player(" + getCoordsNode(playerCoords) + "). ";

        myFacts += enemyNode(game.getStiffy(), "stiffy");

        myFacts += enemyNode(game.getSpeedy(), "speedy");

        myFacts += enemyNode(game.getScaredy(), "scaredy");

        myFacts += enemyNode(game.getSilly(), "silly");


        // ACTUAL PLAYER DIRECTION
        switch (game.getPlayer().getDirection()) {
            case 0:
                myFacts += "previousDirection(up). ";
                break;
            case 1:
                myFacts += "previousDirection(right). ";
                break;
            case 2:
                myFacts += "previousDirection(down). ";
                break;
            case 3:
                myFacts += "previousDirection(left). ";
                break;
        }


        // NUMBER OF WALLS CLOSED BY PLAYER
        myFacts += "closedWalls(" + game.getActivePlayerWallsIndexes().size() + "). ";


        // THE LAST WALL CROSSED BY THE PLAYER THAT CAN BE CLOSED
        if(game.getLastWall() != null)
            myFacts += wallNode(game.getActiveWall(game.getLastWall()), "candidateWall");


        // ACTIVE WALLS
        for(Integer i : game.getActiveGameWallsIndexes())
            if(game.getActiveWall(i).isClosed())
                myFacts += wallNode(game.getActiveWall(i), "wall");

        for(Integer i : game.getActivePlayerWallsIndexes())
            if(game.getActiveWall(i).isClosed())
                myFacts += wallNode(game.getActiveWall(i), "wall");

        for(Wall w : game.getPerimeterWalls())
            if(w.isClosed())
                myFacts += wallNode(w, "wall");


        // COINS
        for(int i = 0; i < game.getRowsNum(); i++)
            for(int j = 0; j < game.getColsNum(); j++)
               if(game.isCoin(i, j))
                   myFacts += targetNode(new Coord(i, j), "coin");


        // BAG
        Bag bag = game.getBag();
        if(bag.isActive())
            myFacts += targetNode(bag.getCoord(), "bag");


        // BONUS
        BonusItem bonus = game.getBonusItem();
        if(bonus.isActive())
            myFacts += targetNode(bonus.getCoord(), "bonus");


        // EXIT
        for(Wall w : game.getPerimeterWalls()) {
            if(!w.isClosed())
                myFacts += targetNode(w.getCoord(), "exit");
        }

        // CRITICAL POSITIONS
        for(Coord c : game.getCriticalPositions()) {
            myFacts += "criticalPosition(" + (c.getRow() * game.getColsNum() + c.getCol()) + "). ";
        }


        // DISTANCES

        for(int i = -1; i <= 1; i++) {
            Integer colDir = null, rowDir = null;
            if(i == -1) {
                colDir = Direction.LEFT;
                rowDir = Direction.UP;
            }
            else if(i == 1) {
                colDir = Direction.RIGHT;
                rowDir = Direction.DOWN;
            }

            if(i == 0)
                myFacts += coordsToEnemiesDistances(playerCoords, null);
            else {
                int myCol = playerCoords.getCol() + i;

                if(myCol <= -3)
                    myCol = 30;
                else if(myCol >= 32)
                    myCol = -1;

                if (game.isValidPosition(playerCoords.getRow() + i, playerCoords.getCol()))
                    myFacts += coordsToEnemiesDistances(new Coord(playerCoords.getRow() + i, playerCoords.getCol()), rowDir);
                if (game.isValidPosition(playerCoords.getRow(), myCol))
                    myFacts += coordsToEnemiesDistances(new Coord(playerCoords.getRow(), myCol), colDir);
            }
        }



        program.addProgram(myFacts);
        handler.addProgram(program);


        // ANSWER SETS ANALYSIS
        Output output = handler.startSync();
        AnswerSets answersets = (AnswerSets) output;

        System.out.println(answersets.getAnswerSetsString());

        try {
            AnswerSet a = answersets.getOptimalAnswerSets().get(0);

            for(Object obj : a.getAtoms()){
                if(! (obj instanceof AIDirection)) {
                    if(obj instanceof AICloseWallCommand)
                        game.closePlayerWall();
                    continue;
                }
                AIDirection dir = (AIDirection) obj;
                System.out.println(dir.getValue().getValue());
                switch (dir.getValue().getValue()) {
                    case "up":
                        game.setNewPlayerDirection(Direction.UP);
                        break;
                    case "right":
                        game.setNewPlayerDirection(Direction.RIGHT);
                        break;
                    case "down":
                        game.setNewPlayerDirection(Direction.DOWN);
                        break;
                    case "left":
                        game.setNewPlayerDirection(Direction.LEFT);
                        break;
                }
            }
        } catch (Exception e) {
            //game.setNewPlayerDirection(answersets.getAnswerSetsString().length() % 4);
        }

        finished = true;
        System.out.println();
        System.out.println();




        /*
        handler.startAsync(output -> {
            AnswerSets answersets = (AnswerSets) output;

            try {
                AnswerSet a = answersets.getOptimalAnswerSets().get(0);

                for(Object obj : a.getAtoms()){
                    if(! (obj instanceof AIDirection))
                        continue;
                    AIDirection dir = (AIDirection) obj;

                    switch (dir.getValue().getValue()) {
                        case "up":
                            game.setNewPlayerDirection(Direction.UP);
                            break;
                        case "right":
                            game.setNewPlayerDirection(Direction.RIGHT);
                            break;
                        case "down":
                            game.setNewPlayerDirection(Direction.DOWN);
                            break;
                        case "left":
                            game.setNewPlayerDirection(Direction.LEFT);
                            break;
                    }
                }
            } catch (Exception e) {
                //game.setNewPlayerDirection(answersets.getAnswerSetsString().length() % 4);
            }

            finished = true;
        });
*/

        /*for(AnswerSet a: answersets.getAnswersets()){
            try {
                for(Object obj : a.getAtoms()){
                    if(! (obj instanceof AIDirection))
                        continue;
                    AIDirection cell = (AIDirection) obj;
                    System.out.println(cell.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }
}
