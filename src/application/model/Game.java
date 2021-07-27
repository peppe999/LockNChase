package application.model;

import java.io.*;
import java.util.*;

public class Game {
    private Integer level;
    private Integer points;
    private Integer score;
    private Integer lives;
    private boolean beginning;
    private boolean winFreeze;
    private boolean dieFreeze;
    private boolean playerAligned;
    private boolean finished;
    private boolean playerDead;
    private boolean gameOver;
    private boolean bonusLife;

    private TrappedEnemiesEvent trappedEnemiesEvent;

    private Integer startTimeout;
    private Integer endTimeout;

    private boolean[][] allowedCells;
    private boolean[][] coins;

    private Bag bag;
    private BonusItem bonusItem;

    private List<Wall> perimeterWalls;
    private List<Wall> allowedWalls;
    private List<Integer> centralWallsIndexes;
    private Map<Coord, Integer> wallsIndexesMapping;
    private List<Integer> activeGameWallsIndexes;
    private List<Integer> activePlayerWallsIndexes;
    private List<Integer> candidateLockingWalls;
    private List<Coord> criticalPositions;

    private GameCharacter player;
    private Integer newPlayerDirection;
    private Integer lastWallTemp;
    private Integer lastWall;

    private GameCharacter stiffy;
    private GameCharacter speedy;
    private GameCharacter silly;
    private GameCharacter scaredy;

    private NotifiableStates states;

    public Game() {
        level = 0;
        points = 0;
        score = 0;
        lives = 5;
        startTimeout = 0;
        endTimeout = 0;
        lastWallTemp = null;
        lastWall = null;
        beginning = true;
        winFreeze = false;
        dieFreeze = false;
        playerDead = false;
        gameOver = false;
        playerAligned = false;
        finished = false;
        bonusLife = false;

        states = new NotifiableStates();
        states.notifyState("startGame");
        initializeMap();
    }

    private void initializeMap() {
        BufferedReader mapFile = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/application/resources/map.txt")));
        allowedCells = new boolean[27][30];
        coins = new boolean[27][30];
        perimeterWalls = new ArrayList<>();
        allowedWalls = new ArrayList<>();
        activeGameWallsIndexes = new ArrayList<>();
        activePlayerWallsIndexes = new ArrayList<>();
        wallsIndexesMapping = new HashMap<>();
        centralWallsIndexes = new ArrayList<>();
        candidateLockingWalls = new ArrayList<>();
        trappedEnemiesEvent = new TrappedEnemiesEvent();
        criticalPositions = new ArrayList<>();

        try {
            int rIndex = 0;
            while (mapFile.ready()) {
                String row = mapFile.readLine();
                String[] cols = row.split(" ");
                for(int i = 0; i < cols.length; i++) {
                    char value = cols[i].charAt(0);

                    allowedCells[rIndex][i] = value != 'N';


                    coins[rIndex][i] = (value == 'C' || value == 'c');

                    if(value == 'e' || value == 'c' || value == 'v' || value == 'h' || value == 'x' || value == '3' || value == '4')
                        criticalPositions.add(new Coord(rIndex, i));

                    double inc = (level < 4) ? 0.025 * level : 0.1;
                    int dec = (level < 4) ? 28 * level : 28 * 4;

                    if(value == '1') {
                        stiffy = new GameCharacter();
                        stiffy.setInitialY(rIndex * 8);
                        stiffy.setInitialX(i * 8);
                        stiffy.setSpeed(0.8 + inc);
                        stiffy.setDirection(Direction.RIGHT);
                    }
                    else if(value == '2') {
                        speedy = new GameCharacter();
                        speedy.setInitialY(rIndex * 8);
                        speedy.setInitialX(i * 8);
                        speedy.setSpeed(0.7 + inc);
                        speedy.setDirection(Direction.LEFT);
                        speedy.setFreezeTime(168 - dec);
                    }
                    else if(value == '3') {
                        scaredy = new GameCharacter();
                        scaredy.setInitialY(rIndex * 8);
                        scaredy.setInitialX(i * 8);
                        scaredy.setSpeed(0.65 + inc);
                        scaredy.setDirection(Direction.RIGHT);
                        scaredy.setFreezeTime(196 - dec);
                    }
                    else if(value == '4') {
                        silly = new GameCharacter();
                        silly.setInitialY(rIndex * 8);
                        silly.setInitialX(i * 8);
                        silly.setSpeed(0.6 + inc);
                        silly.setDirection(Direction.LEFT);
                        silly.setFreezeTime(224 - dec);
                    }
                    else if(value == 'W') {
                        Wall w = new Wall(Wall.FIXED_WALL, new Coord(rIndex, i));
                        perimeterWalls.add(w);
                    }
                    else if(value == 'H' || value == 'F' || value == 'h') {
                        Wall w = new Wall(Wall.HORIZONTAL_WALL, new Coord(rIndex, i));
                        allowedWalls.add(w);
                        wallsIndexesMapping.put(new Coord(rIndex, i), allowedWalls.size() - 1);

                        if(value == 'F')
                            centralWallsIndexes.add(allowedWalls.size() - 1);
                        else if(value == 'h')
                            candidateLockingWalls.add(allowedWalls.size() - 1);
                    }
                    else if(value == 'V' || value == 'v' || value == 'x') {
                        Wall w = new Wall(Wall.VERTICAL_WALL, new Coord(rIndex, i));
                        allowedWalls.add(w);
                        wallsIndexesMapping.put(new Coord(rIndex, i), allowedWalls.size() - 1);

                        if(value == 'v')
                            candidateLockingWalls.add(allowedWalls.size() - 1);

                    }
                    else if(value == 'B') {
                        bag = new Bag(new Coord(rIndex, i));
                    }
                    else if(value == 'L') {
                        bonusItem = new BonusItem(new Coord(rIndex, i), level);
                    }
                }
                rIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(allowedWalls.size());
        calcPlayerPosition();
    }

    private void calcPlayerPosition() {
        player = new GameCharacter();
        player.setInitialY(27 * 8);
        player.setInitialX(16 * (lives - 1) + 28);
        player.setSpeed(0.9);
        player.setDirection(Direction.UP);
        newPlayerDirection = player.getDirection();
    }

    private void beginningMovement() {
        startTimeout++;
        if(startTimeout < 84)
            return;

        if(!playerAligned) {
            player.setX(player.getX() + 0.8);

            if(perimeterWalls.get(1).getCoord().getCol() == (int) player.getX() / 8)
                playerAligned = true;
        }
        else {
            player.setY(player.getY() - 0.8);
            afterPlayerMovementChecks();
            if(perimeterWalls.get(1).getCoord().getRow() - 2 == (int) player.getY() / 8) {
                beginning = false;
                startTimeout = 0;
                for(Wall w : perimeterWalls) {
                    w.closeWall(Wall.GAME_OWNER);
                    Coord coord = w.getCoord();
                    allowedCells[coord.getRow()][coord.getCol()] = false;
                }

                states.notifyState("doorClosed");
            }
        }
    }

    private void resetVariables() {
        startTimeout = 0;
        endTimeout = 0;
        lastWallTemp = null;
        lastWall = null;
        beginning = true;
        winFreeze = false;
        dieFreeze = false;
        playerDead = false;
        finished = false;
    }

    private void changeLevel() {
        level++;
        points = 0;
        resetVariables();

        initializeMap();
        playerAligned = true;
        player.setX(perimeterWalls.get(1).getCoord().getCol() * 8);

        states.notifyState("newLvl");
    }

    private void levelFinishedBehaviour() {
        stiffy.updateFreezeTime();
        speedy.updateFreezeTime();
        scaredy.updateFreezeTime();
        silly.updateFreezeTime();
        player.updateFreezeTime();

        winFreeze = !player.isActive();

        if(!winFreeze)
            changeLevel();
    }

    private void playerDeadBehaviour() {
        if(dieFreeze) {
            stiffy.updateFreezeTime();
            speedy.updateFreezeTime();
            scaredy.updateFreezeTime();
            silly.updateFreezeTime();
            player.updateFreezeTime();

            dieFreeze = !player.isActive();
        }
        else {
            endTimeout++;
            if(endTimeout < 168)
                return;

            checkRestart();
        }
    }

    private void checkRestart() {
        lives--;

        if(lives == 0) {
            gameOver = true;
            return;
        }

        states.notifyState("startGame");
        resetVariables();
        resetElements();

        playerAligned = false;

        calcPlayerPosition();

        int dec = (level < 4) ? 28 * level : 28 * 4;

        stiffy.setX(stiffy.getInitialX());
        stiffy.setY(stiffy.getInitialY());
        stiffy.setDirection(Direction.RIGHT);

        speedy.setX(speedy.getInitialX());
        speedy.setY(speedy.getInitialY());
        speedy.setDirection(Direction.LEFT);
        speedy.setFreezeTime(168 - dec);

        scaredy.setX(scaredy.getInitialX());
        scaredy.setY(scaredy.getInitialY());
        scaredy.setDirection(Direction.RIGHT);
        scaredy.setFreezeTime(196 - dec);

        silly.setX(silly.getInitialX());
        silly.setY(silly.getInitialY());
        silly.setDirection(Direction.LEFT);
        silly.setFreezeTime(224 - dec);
    }

    public void update() {
        if(gameOver)
            return;

        if(beginning) {
            beginningMovement();
            return;
        }

        if(finished) {
            levelFinishedBehaviour();
            return;
        }

        if(playerDead) {
            playerDeadBehaviour();
            return;
        }

        if(winFreeze)
            winFreeze = !stiffy.isActive() || !speedy.isActive() || !scaredy.isActive() || !silly.isActive();

        if(!bonusLife && score >= 15000) {
            lives++;
            bonusLife = true;
            states.notifyState("bonusLife");
        }

        checkPlayerDeadState();
        checkWinState();

        if(!playerDead) {
            if(!finished && points >= 137 * 20) {
                for(Wall w : perimeterWalls) {
                    w.openFixedWall();
                }
            }

            checkBonus();
            checkPlayerWalls();
            checkGameWalls();

            moveStiffy();
            moveSpeedy();
            moveScaredy();
            moveSilly();
            movePlayer();
        }

    }

    public List<Integer> getActiveGameWallsIndexes() {
        return activeGameWallsIndexes;
    }

    public List<Integer> getActivePlayerWallsIndexes() {
        return activePlayerWallsIndexes;
    }

    public Wall getActiveWall(Integer i) {
        return allowedWalls.get(i);
    }

    public List<Wall> getPerimeterWalls() {
        return perimeterWalls;
    }

    private void showBonus() {
        if(points == 800 || points == 1400 || points == 2000 || points == 2600) {
            bag.show();
            states.notifyState("bagSpawn");
        }
        else if(points == 1100 || points == 1700)
            bonusItem.show();
    }

    private void checkBonus() {
        if(bag.isActive())
            bag.update();

        if(bonusItem.isActive())
            bonusItem.update();
    }

    private void checkPlayerWalls() {
        if(trappedEnemiesEvent.isActive())
            trappedEnemiesEvent.update();

        List<Integer> uselessWalls = new ArrayList<>();
        for(Integer i : activePlayerWallsIndexes) {
            Wall w = allowedWalls.get(i);
            if(w.isActive()) {
                w.updateWallState();
                Coord coord = w.getCoord();
                allowedCells[coord.getRow()][coord.getCol()] = !w.isClosed();
                if(!w.isClosed())
                    uselessWalls.add(i);
            }
        }

        activePlayerWallsIndexes.removeAll(uselessWalls);
    }

    private int generateRandomWallIndex() {
        int maxIndex = allowedWalls.size() - 1;
        int rangeInc = allowedWalls.size() / 5;

        Random random = new Random();
        int selectRange = random.nextInt(10);
        int value;

        if(selectRange < 5) {
            value = random.nextInt(maxIndex + 1 - (rangeInc + 1) * 4);
            value += (rangeInc + 1) * 2;
        }
        else if(selectRange < 9) {
            value = random.nextInt((rangeInc + 1) * 2);
            maxIndex -= (rangeInc + 1);
            if(value > rangeInc)
                value = maxIndex - (value - rangeInc - 1);
            else
                value += (rangeInc + 1);
        }
        else {
            value = random.nextInt((rangeInc + 1) * 2);
            if(value > rangeInc)
                value = maxIndex - (value - rangeInc - 1);
        }

        return value;
    }

    private void checkGameWalls() {
        if(activeGameWallsIndexes.size() > 0) {
            boolean ok = true;
            for(Integer i : activeGameWallsIndexes) {
                Wall w = allowedWalls.get(i);
                if(w.isActive()) {
                    w.updateWallState();
                    Coord coord = w.getCoord();
                    allowedCells[coord.getRow()][coord.getCol()] = !w.isClosed();

                }
                else
                    ok = false;
            }
            if(!ok)
                activeGameWallsIndexes.clear();
        }
        else {
            int wallsNum = 4;
            for(Integer i : centralWallsIndexes) {
                if(!activePlayerWallsIndexes.contains(i)) {
                    allowedWalls.get(i).closeWall(Wall.GAME_OWNER);
                    activeGameWallsIndexes.add(i);
                    wallsNum--;
                }
            }
            for(int i = 0; i < wallsNum; i++) {
                int index = generateRandomWallIndex();
                if(!activeGameWallsIndexes.contains(index) && !activePlayerWallsIndexes.contains(index)) {
                    allowedWalls.get(index).closeWall(Wall.GAME_OWNER);
                    activeGameWallsIndexes.add(index);
                }
                else
                    i--;
            }
        }
    }

    public void setNewPlayerDirection(Integer dir) {
        newPlayerDirection = dir;
    }

    public boolean isCoin(int row, int col) {
        if((row < 0 || row >= coins.length) || (col < 0 || row >= coins[0].length))
            return false;

        return coins[row][col];
    }

    public int getRowsNum() {
        return allowedCells.length;
    }

    public int getColsNum() {
        return allowedCells[0].length;
    }

    private double checkDirection() {
        boolean tunnel = ((int)player.getY() / 8 == 11 && ((int)(player.getX() / 8) >= allowedCells[0].length - 1 || (int)(player.getX() / 8) <= 0));
        if(!player.getDirection().equals(newPlayerDirection)) {
            if(player.getDirection() % 2 == newPlayerDirection % 2) {
                player.setDirection(newPlayerDirection);
                lastWallTemp = null;
                return 0;
            }

            /*if((int)player.getX() % 8 != 0 || ((int) player.getY() % 8 == 4)) {
                return 0;
            }*/

            if(newPlayerDirection.equals(Direction.LEFT)) {
                int rowModuloOffset = (int) player.getY() % 8;
                if(player.getDirection().equals(Direction.UP) && (rowModuloOffset >= 7)) {
                    if(tunnel || allowedCells[(int)(player.getY() / 8) + 1][(int)(player.getX() / 8) - 1]) {
                        double offset = (((int)(player.getY() / 8) + 1) * 8) - player.getY();
                        player.setY(((int)(player.getY() / 8) + 1) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(player.getDirection().equals(Direction.DOWN) && (rowModuloOffset <= 1)) {
                    if(tunnel || allowedCells[(int)(player.getY() / 8)][(int)(player.getX() / 8) - 1]) {
                        double offset = player.getY() - (((int)(player.getY() / 8)) * 8);
                        player.setY(((int)(player.getY() / 8)) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(rowModuloOffset == 0 && (tunnel || allowedCells[(int)player.getY() / 8][(int)(player.getX() / 8) - 1])) {
                    player.setDirection(newPlayerDirection);
                    lastWallTemp = null;
                }
            }
            else if(newPlayerDirection.equals(Direction.RIGHT)) {
                int rowModuloOffset = (int) player.getY() % 8;
                if(player.getDirection().equals(Direction.UP) && (rowModuloOffset >= 7)) {
                    if(tunnel || allowedCells[(int)(player.getY() / 8) + 1][(int)(player.getX() / 8) + 1]) {
                        double offset = (((int)(player.getY() / 8) + 1) * 8) - player.getY();
                        player.setY(((int)(player.getY() / 8) + 1) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(player.getDirection().equals(Direction.DOWN) && (rowModuloOffset <= 1)) {
                    if(tunnel || allowedCells[(int)(player.getY() / 8)][(int)(player.getX() / 8) + 1]) {
                        double offset = player.getY() - (((int)(player.getY() / 8)) * 8);
                        player.setY(((int)(player.getY() / 8)) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(rowModuloOffset == 0 && (tunnel || allowedCells[(int)player.getY() / 8][(int)(player.getX() / 8) + 1])) {
                    player.setDirection(newPlayerDirection);
                    lastWallTemp = null;
                }
            }
            else if(newPlayerDirection.equals(Direction.DOWN)) {
                int colModuloOffset = (int) player.getX() % 8;
                if(player.getDirection().equals(Direction.LEFT) && (colModuloOffset >= 7)) {
                    if(!tunnel && (allowedCells[(int)(player.getY() / 8) + 1][(int)(player.getX() / 8) + 1] || (!perimeterWalls.get(1).isClosed() && new Coord((int)(player.getY() / 8) + 1, (int)(player.getX() / 8) + 1).equals(perimeterWalls.get(1).getCoord())))) {
                        double offset = (((int)(player.getX() / 8) + 1) * 8) - player.getX();
                        player.setX(((int)(player.getX() / 8) + 1) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(player.getDirection().equals(Direction.RIGHT) && (colModuloOffset <= 1)) {
                    if(!tunnel && (allowedCells[(int)(player.getY() / 8) + 1][(int)player.getX() / 8] || (!perimeterWalls.get(1).isClosed() && new Coord((int)(player.getY() / 8) + 1, (int)player.getX() / 8).equals(perimeterWalls.get(1).getCoord())))) {
                        double offset = player.getX() - (((int)(player.getX() / 8)) * 8);
                        player.setX(((int)(player.getX() / 8)) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(colModuloOffset == 0 && !tunnel && (allowedCells[(int)(player.getY() / 8) + 1][(int)player.getX() / 8] || (!perimeterWalls.get(1).isClosed() && new Coord((int)(player.getY() / 8) + 1, (int)player.getX() / 8).equals(perimeterWalls.get(1).getCoord())))) {
                    player.setDirection(newPlayerDirection);
                    lastWallTemp = null;
                }
            }
            else if(newPlayerDirection.equals(Direction.UP)) {
                int colModuloOffset = (int) player.getX() % 8;
                if(player.getDirection().equals(Direction.LEFT) && (colModuloOffset >= 7)) {
                    if(!tunnel && (allowedCells[(int)(player.getY() / 8) - 1][(int)(player.getX() / 8) + 1] || (!perimeterWalls.get(1).isClosed() && new Coord((int)(player.getY() / 8) - 1, (int)(player.getX() / 8) + 1).equals(perimeterWalls.get(1).getCoord())))) {
                        double offset = (((int)(player.getX() / 8) + 1) * 8) - player.getX();
                        player.setX(((int)(player.getX() / 8) + 1) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(player.getDirection().equals(Direction.RIGHT) && (colModuloOffset <= 1)) {
                    if(!tunnel && (allowedCells[(int)(player.getY() / 8) - 1][(int)player.getX() / 8] || (!perimeterWalls.get(1).isClosed() && new Coord((int)(player.getY() / 8) - 1, (int)player.getX() / 8).equals(perimeterWalls.get(1).getCoord())))) {
                        double offset = player.getX() - (((int)(player.getX() / 8)) * 8);
                        player.setX(((int)(player.getX() / 8)) * 8);
                        player.setDirection(newPlayerDirection);
                        lastWallTemp = null;

                        return offset;
                    }
                }
                else if(colModuloOffset == 0 && !tunnel && (allowedCells[(int)(player.getY() / 8) - 1][(int)player.getX() / 8] || (!perimeterWalls.get(0).isClosed() && new Coord((int)(player.getY() / 8) - 1, (int)player.getX() / 8).equals(perimeterWalls.get(0).getCoord())))) {
                    player.setDirection(newPlayerDirection);
                    lastWallTemp = null;
                }
            }

        }
        return 0;
    }

    public double moveCharacter(GameCharacter character, double speed) {
        boolean tunnel = ((int)character.getY() / 8 == 11 && ((int)(character.getX() / 8) >= allowedCells[0].length - 1 || (int)(character.getX() / 8) <= 0));
        if(character.getDirection().equals(Direction.LEFT)) {
            if(character.getX() % 8 == 0 && !tunnel && !(allowedCells[(int)character.getY() / 8][(int)(character.getX() / 8) - 1]))
                return -1;

            int intValue = (int)character.getX();
            double newValue = Math.round((character.getX() - speed) * 100.0) / 100.0;

            if(newValue <= -8) {
                double offset = 16 + newValue;
                newValue = (allowedCells[0].length + 1) * 8 + offset;
                character.setX(newValue);
                return -1;
            }

            if(character.getX() > intValue && newValue < intValue && intValue % 8 == 0) {
                character.setX(intValue);
                return intValue - newValue;
            }

            character.setX(newValue);
        }
        else if(character.getDirection().equals(Direction.RIGHT)) {
            if(character.getX() % 8 == 0 && !tunnel && !(allowedCells[(int)character.getY() / 8][(int)(character.getX() / 8) + 1]))
                return -1;

            int intValue = (int)character.getX() + 1;
            double newValue = Math.round((character.getX() + speed) * 100.0) / 100.0;

            if(newValue >= (allowedCells[0].length + 1) * 8) {
                double offset = newValue - (allowedCells[0].length + 1) * 8;
                newValue = -16 + offset;
                character.setX(newValue);
                return -1;
            }

            if(character.getX() < intValue && newValue > intValue && intValue % 8 == 0) {
                character.setX(intValue);
                return newValue - intValue;
            }

            character.setX(newValue);
        }
        else if(character.getDirection().equals(Direction.DOWN)) {
            if(character.getY() % 8 == 0 && (tunnel || !(allowedCells[(int)(character.getY() / 8) + 1][(int)character.getX() / 8])))
                if(!(character == player && !perimeterWalls.get(1).isClosed() && new Coord((int)(character.getY() / 8) + 1, (int)character.getX() / 8).equals(perimeterWalls.get(1).getCoord())))
                    return -1;

            int intValue = (int)character.getY() + 1;
            double newValue = Math.round((character.getY() + speed) * 100.0) / 100.0;

            if(character.getY() < intValue && newValue > intValue && intValue % 8 == 0) {
                character.setY(intValue);
                return newValue - intValue;
            }

            character.setY(newValue);
        }
        else if(character.getDirection().equals(Direction.UP)) {
            if(character.getY() % 8 == 0 && (tunnel || !(allowedCells[(int)(character.getY() / 8) - 1][(int)character.getX() / 8])))
                if(!(character == player && !perimeterWalls.get(0).isClosed() && new Coord((int)(character.getY() / 8) - 1, (int)character.getX() / 8).equals(perimeterWalls.get(0).getCoord())))
                    return -1;

            int intValue = (int)character.getY();
            double newValue = Math.round((character.getY() - speed) * 100.0) / 100.0;

            if(character.getY() > intValue && newValue < intValue && intValue % 8 == 0) {
                character.setY(intValue);
                return intValue - newValue;
            }

            character.setY(newValue);
        }

        return -1;
    }

    private void checkPlayerTrapsEnemies() {
        if(activePlayerWallsIndexes.size() < 2)
            return;

        if(!(candidateLockingWalls.contains(activePlayerWallsIndexes.get(0)) && candidateLockingWalls.contains(activePlayerWallsIndexes.get(1))))
            return;

        Coord firstWallCoords = allowedWalls.get(activePlayerWallsIndexes.get(0)).getCoord();
        Coord secondWallCoords = allowedWalls.get(activePlayerWallsIndexes.get(1)).getCoord();

        if(((firstWallCoords.getRow() < getRowsNum() / 2) == (secondWallCoords.getRow() < getRowsNum() / 2)) && ((firstWallCoords.getCol() < getColsNum() / 2) == (secondWallCoords.getCol() < getColsNum() / 2))) {
            System.out.println("I'm in");
            int minRow = firstWallCoords.getRow();
            int minCol = firstWallCoords.getCol();
            int maxRow = secondWallCoords.getRow();
            int maxCol = secondWallCoords.getCol();

            if(minRow > maxRow) {
                int z = minRow;
                minRow = maxRow;
                maxRow = z;
            }

            if(minCol > maxCol) {
                int z = minCol;
                minCol = maxCol;
                maxCol = z;
            }
            else if(minCol == maxCol) {
                if(minCol < getColsNum() / 2) {
                    minCol = 0;
                }
                else
                    maxCol = getColsNum() - 1;
            }

            int trappedEnemies = 0;

            System.out.println(minRow + " " + maxRow);
            System.out.println(minCol + " " + maxCol);

            for(; minRow <= maxRow; minRow++) {
                for (int j = minCol; j <= maxCol; j++) {
                    if ((minRow == ((int) stiffy.getY() / 8)) && (j == ((int) stiffy.getX() / 8)))
                        trappedEnemies++;
                    if ((minRow == ((int) speedy.getY() / 8)) && (j == ((int) speedy.getX() / 8)))
                        trappedEnemies++;
                    if ((minRow == ((int) scaredy.getY() / 8)) && (j == ((int) scaredy.getX() / 8)))
                        trappedEnemies++;
                    if ((minRow == ((int) silly.getY() / 8)) && (j == ((int) silly.getX() / 8)))
                        trappedEnemies++;
                }
            }

            System.out.println(trappedEnemies);

            if(trappedEnemies > 0) {
                score += trappedEnemiesEvent.startEvent(trappedEnemies);
                states.notifyState("lockedEnemies");
            }
        }

    }

    private boolean isCharacterOnWall(GameCharacter character, Wall w) {
        return w.getCoord().getRow() == (int) character.getY() / 8 && w.getCoord().getCol() == (int) character.getX() / 8;
    }

    public void closePlayerWall() {
        if(dieFreeze || playerDead || beginning || winFreeze)
            return;

        if(lastWall != null && activePlayerWallsIndexes.size() < 2 && !activePlayerWallsIndexes.contains(lastWall) && !activeGameWallsIndexes.contains(lastWall)) {
            Wall w = allowedWalls.get(lastWall);
            boolean isStiffyPos = isCharacterOnWall(stiffy, w);
            boolean isSpeedyPos = isCharacterOnWall(speedy, w);
            boolean isScaredyPos = isCharacterOnWall(scaredy, w);
            boolean isSillyPos = isCharacterOnWall(silly, w);

            if(!isStiffyPos && !isSpeedyPos && !isScaredyPos && !isSillyPos) {
                activePlayerWallsIndexes.add(lastWall);
                allowedWalls.get(lastWall).closeWall(Wall.PLAYER_OWNER);
                lastWall = null;
                checkPlayerTrapsEnemies();
                states.notifyState("doorClosed");
            }
        }
    }

    private void selectNewWall() {
        if((int) player.getX() % 8 == 0 && (int) player.getY() % 8 == 0) {
            Coord coord = null;

            if(player.getDirection().equals(Direction.RIGHT))
                coord = new Coord((int) player.getY() / 8, (int) player.getX() / 8 + 1);
            else if(player.getDirection().equals(Direction.LEFT))
                coord = new Coord((int) player.getY() / 8, (int) player.getX() / 8 - 1);
            else if(player.getDirection().equals(Direction.UP))
                coord = new Coord((int) player.getY() / 8 - 1, (int) player.getX() / 8);
            else if(player.getDirection().equals(Direction.DOWN))
                coord = new Coord((int) player.getY() / 8 + 1, (int) player.getX() / 8);

            if(wallsIndexesMapping.containsKey(coord))
                lastWallTemp = wallsIndexesMapping.get(coord);
            else if(lastWallTemp != null) {
                if(allowedWalls.get(lastWallTemp).getType().equals(Wall.VERTICAL_WALL) && Math.abs((int) player.getX() / 8 - allowedWalls.get(lastWallTemp).getCoord().getCol()) == 1 && (int) player.getY() / 8 == allowedWalls.get(lastWallTemp).getCoord().getRow()) {
                    lastWall = lastWallTemp;
                    lastWallTemp = null;
                }
                else if(allowedWalls.get(lastWallTemp).getType().equals(Wall.HORIZONTAL_WALL) && Math.abs((int) player.getY() / 8 - allowedWalls.get(lastWallTemp).getCoord().getRow()) == 1 && (int) player.getX() / 8 == allowedWalls.get(lastWallTemp).getCoord().getCol()) {
                    lastWall = lastWallTemp;
                    lastWallTemp = null;
                }
            }
        }
    }

    private void resetElements() {
        for(Integer i : activeGameWallsIndexes) {
            Coord coord = allowedWalls.get(i).getCoord();
            allowedCells[coord.getRow()][coord.getCol()] = true;
            allowedWalls.get(i).resetWall();
        }

        for(Integer i : activePlayerWallsIndexes) {
            Coord coord = allowedWalls.get(i).getCoord();
            allowedCells[coord.getRow()][coord.getCol()] = true;
            allowedWalls.get(i).resetWall();
        }

        for(Wall w : perimeterWalls)
            w.resetWall();

        activePlayerWallsIndexes.clear();
        activeGameWallsIndexes.clear();

        bag.reset();
        bonusItem.reset();
        trappedEnemiesEvent.reset();
    }

    private void checkWinState() {
        int pRow = (int) player.getY() / 8;
        int pCol = (int) player.getX() / 8;

        Coord supWallCoords = perimeterWalls.get(0).getCoord();
        Coord infWallCoords = perimeterWalls.get(1).getCoord();

        if(points >= 137 * 20 && ((pCol == supWallCoords.getCol() && pRow == 1) || (pCol == infWallCoords.getCol() && pRow == 25))) {
            startLevelWinFreeze();
            player.setY(pRow * 8);
            player.setX(pCol * 8);
        }
    }

    private boolean checkCollision(GameCharacter c1, GameCharacter c2) {
        return Math.abs(c1.getX() - c2.getX()) < 8 && Math.abs(c1.getY() - c2.getY()) < 8;
    }

    private void checkPlayerDeadState() {
        /*Coord playerCoord = new Coord((int) player.getY() / 8, (int) player.getX() / 8);

        Coord stiffyCoord = new Coord((int) stiffy.getY() / 8, (int) stiffy.getX() / 8);
        Coord speedyCoord = new Coord((int) speedy.getY() / 8, (int) speedy.getX() / 8);
        Coord scaredyCoord = new Coord((int) scaredy.getY() / 8, (int) scaredy.getX() / 8);
        Coord sillyCoord = new Coord((int) silly.getY() / 8, (int) silly.getX() / 8);

        if((playerCoord.equals(stiffyCoord) && stiffy.isActive()) || (playerCoord.equals(speedyCoord) && speedy.isActive()) || (playerCoord.equals(scaredyCoord) && scaredy.isActive()) || (playerCoord.equals(sillyCoord) && silly.isActive())) {
            startEndFreeze();
        }*/

        if((checkCollision(player, stiffy) && stiffy.isActive()) || (checkCollision(player, speedy) && speedy.isActive()) || (checkCollision(player, scaredy) && scaredy.isActive()) || (checkCollision(player, silly) && silly.isActive()))
            startEndFreeze();
    }

    private void startEndFreeze() {
        dieFreeze = true;
        states.notifyState("die");
        player.setFreezeTime(56);
        stiffy.setFreezeTime(56);
        speedy.setFreezeTime(56);
        scaredy.setFreezeTime(56);
        silly.setFreezeTime(56);
        playerDead = true;
    }

    private void startLevelWinFreeze() {
        winFreeze = true;
        player.setFreezeTime(112);
        stiffy.setFreezeTime(112);
        speedy.setFreezeTime(112);
        scaredy.setFreezeTime(112);
        silly.setFreezeTime(112);
        finished = true;
        states.notifyState("win");

        for(Wall w : perimeterWalls)
            w.closeWall(Wall.GAME_OWNER);
    }

    private void afterPlayerMovementChecks() {
        selectNewWall();
        int pRow = (int) player.getY() / 8;
        int pCol = (int) player.getX() / 8;
        if(pRow > 0 && pRow < getRowsNum() && pCol > 0 && pCol < getColsNum()) {
            if(isCoin(pRow, pCol)) {
                coins[pRow][pCol] = false;
                points += 20;
                score += 20;
                states.notifyState("money");
                showBonus();
            }

            if(bag.isActive() && !bag.isPicked() && bag.getCoord().equals(new Coord(pRow, pCol))) {
                score += bag.takeBag();
                states.notifyState("pickBag");

                winFreeze = true;
                player.setFreezeTime(112);
                stiffy.setFreezeTime(168);
                speedy.setFreezeTime(168);
                scaredy.setFreezeTime(168);
                silly.setFreezeTime(168);
            }

            if(bonusItem.isActive() && !bonusItem.isPicked() && bonusItem.getCoord().equals(new Coord(pRow, pCol))) {
                score += bonusItem.takeBag();
                states.notifyState("pickLvlBonus");
            }
        }
    }

    public void movePlayer() {
        if(!player.isActive()) {
            player.updateFreezeTime();
            return;
        }

        double offset = moveCharacter(player, player.getSpeed());
        double dirOffset = checkDirection();

        if(dirOffset > 0) {
            offset = dirOffset;
            System.out.println(offset);
        }

        afterPlayerMovementChecks();

        if(offset > 0) {
            moveCharacter(player, offset);
            afterPlayerMovementChecks();
        }
    }

    private double coordsDistance(Coord first, Coord second) {
        Coord newCoords = new Coord(second.getRow() - first.getRow(), second.getCol() - first.getCol());
        return Math.sqrt(Math.pow(newCoords.getRow(), 2) + Math.pow(newCoords.getCol(), 2));
    }

    private Integer choiceDirection(Integer actualDirection, Coord targetCoord, Coord actualCoord) {
        double[] distances = new double[4];
        int cont = 0;
        boolean tunnel = (actualCoord.getRow() == 11 && (actualCoord.getCol() >= allowedCells[0].length - 1 || actualCoord.getCol() <= 0));
        if(!tunnel && allowedCells[actualCoord.getRow() - 1][actualCoord.getCol()]) {
            distances[Direction.UP] = coordsDistance(new Coord(actualCoord.getRow() - 1, actualCoord.getCol()), targetCoord);
            cont++;
        }
        else
            distances[Direction.UP] = -1;

        if(tunnel || allowedCells[actualCoord.getRow()][actualCoord.getCol() + 1]) {
            distances[Direction.RIGHT] = coordsDistance(new Coord(actualCoord.getRow(), actualCoord.getCol() + 1), targetCoord);
            cont++;
        }
        else
            distances[Direction.RIGHT] = -1;

        if(!tunnel && allowedCells[actualCoord.getRow() + 1][actualCoord.getCol()]) {
            distances[Direction.DOWN] = coordsDistance(new Coord(actualCoord.getRow() + 1, actualCoord.getCol()), targetCoord);
            cont++;
        }
        else
            distances[Direction.DOWN] = -1;

        if(tunnel || allowedCells[actualCoord.getRow()][actualCoord.getCol() - 1]) {
            distances[Direction.LEFT] = coordsDistance(new Coord(actualCoord.getRow(), actualCoord.getCol() - 1), targetCoord);
            cont++;
        }
        else
            distances[Direction.LEFT] = -1;

        Integer newDirection = null;
        double bestDistance = 0;

        for(int i = 0; i < 4; i++) {
            if(distances[i] != -1) {
                if(cont == 1) {
                    return i;
                }

                if(i % 2 != actualDirection % 2 || i == actualDirection) {
                    if(newDirection == null || distances[i] < bestDistance) {
                        newDirection = i;
                        bestDistance = distances[i];
                    }
                }

            }
        }

        return newDirection;
    }

    private void moveStiffy() {
        if(!stiffy.isActive()) {
            stiffy.updateFreezeTime();
            return;
        }

        double offset = moveCharacter(stiffy, stiffy.getSpeed());
        double x = stiffy.getX();
        double y = stiffy.getY();
        if((x - (int)x == 0 && (int)x % 8 == 0) && (y - (int)y == 0 && (int)y % 8 == 0)) {
            int characterCol = (int) x / 8;
            int characterRow = (int) y / 8;

            int targetCol = (int) player.getX() / 8;
            int targetRow = (int) player.getY() / 8;

            stiffy.setDirection(choiceDirection(stiffy.getDirection(), new Coord(targetRow, targetCol), new Coord(characterRow, characterCol)));

            if(offset > 0)
                moveCharacter(stiffy, offset);
        }

    }

    private void moveSpeedy() {
        if(!speedy.isActive()) {
            speedy.updateFreezeTime();
            return;
        }

        double offset = moveCharacter(speedy, speedy.getSpeed());
        double x = speedy.getX();
        double y = speedy.getY();
        if((x - (int)x == 0 && (int)x % 8 == 0) && (y - (int)y == 0 && (int)y % 8 == 0)) {
            int characterCol = (int) x / 8;
            int characterRow = (int) y / 8;

            int targetCol = (int) player.getX() / 8;
            int targetRow = (int) player.getY() / 8;

            double vectDistance = coordsDistance(new Coord(characterRow, characterCol), new Coord(targetRow, targetCol));

            if(vectDistance > 2) {
                if (player.getDirection().equals(Direction.UP))
                    targetRow -= 4;
                else if (player.getDirection().equals(Direction.RIGHT))
                    targetCol += 4;
                else if (player.getDirection().equals(Direction.DOWN))
                    targetRow += 4;
                else if (player.getDirection().equals(Direction.LEFT))
                    targetCol -= 4;
            }

            speedy.setDirection(choiceDirection(speedy.getDirection(), new Coord(targetRow, targetCol), new Coord(characterRow, characterCol)));

            if(offset > 0)
                moveCharacter(speedy, offset);
        }

    }

    private void moveScaredy() {
        if(!scaredy.isActive()) {
            scaredy.updateFreezeTime();
            return;
        }

        double offset = moveCharacter(scaredy, scaredy.getSpeed());
        double x = scaredy.getX();
        double y = scaredy.getY();
        if((x - (int)x == 0 && (int)x % 8 == 0) && (y - (int)y == 0 && (int)y % 8 == 0)) {
            int characterCol = (int) x / 8;
            int characterRow = (int) y / 8;

            int targetCol = (int) player.getX() / 8;
            int targetRow = (int) player.getY() / 8;

            double vectDistance = coordsDistance(new Coord(characterRow, characterCol), new Coord(targetRow, targetCol));

            if(vectDistance > 2) {
                if (player.getDirection().equals(Direction.UP))
                    targetRow -= 2;
                else if (player.getDirection().equals(Direction.RIGHT))
                    targetCol += 2;
                else if (player.getDirection().equals(Direction.DOWN))
                    targetRow += 2;
                else if (player.getDirection().equals(Direction.LEFT))
                    targetCol -= 2;

                int stiffyCol = (int) stiffy.getX() / 8;
                int stiffyRow = (int) stiffy.getY() / 8;

                targetCol = stiffyCol + (targetCol - stiffyCol) * 2;
                targetRow = stiffyRow + (targetRow - stiffyRow) * 2;
            }

            scaredy.setDirection(choiceDirection(scaredy.getDirection(), new Coord(targetRow, targetCol), new Coord(characterRow, characterCol)));

            if(offset > 0)
                moveCharacter(scaredy, offset);
        }

    }

    private void moveSilly() {
        if(!silly.isActive()) {
            silly.updateFreezeTime();
            return;
        }

        double offset = moveCharacter(silly, silly.getSpeed());
        double x = silly.getX();
        double y = silly.getY();
        if((x - (int)x == 0 && (int)x % 8 == 0) && (y - (int)y == 0 && (int)y % 8 == 0)) {
            int characterCol = (int) x / 8;
            int characterRow = (int) y / 8;

            int targetCol = (int) player.getX() / 8;
            int targetRow = (int) player.getY() / 8;

            double vectDistance = coordsDistance(new Coord(characterRow, characterCol), new Coord(targetRow, targetCol));

            if(vectDistance <= 8) {
                Random generator = new Random();
                if(generator.nextBoolean()) {
                    targetCol = 0;
                    targetRow = 24;
                }
            }

            silly.setDirection(choiceDirection(silly.getDirection(), new Coord(targetRow, targetCol), new Coord(characterRow, characterCol)));

            if(offset > 0)
                moveCharacter(silly, offset);
        }


    }

    public GameCharacter getStiffy() {
        return stiffy;
    }

    public GameCharacter getScaredy() {
        return scaredy;
    }

    public GameCharacter getSpeedy() {
        return speedy;
    }

    public GameCharacter getSilly() {
        return silly;
    }

    public GameCharacter getPlayer() {
        return player;
    }

    public Bag getBag() {
        return bag;
    }

    public BonusItem getBonusItem() {
        return bonusItem;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getLevel() {
        return level;
    }

    public boolean isBeginning() {
        return beginning;
    }

    public boolean isWinFreeze() {
        return winFreeze;
    }

    public Integer getLives() {
        return lives;
    }

    public boolean isPlayerAligned() {
        return playerAligned;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isPlayerDead() {
        return playerDead;
    }

    public TrappedEnemiesEvent getTrappedEnemiesEvent() {
        return trappedEnemiesEvent;
    }

    public NotifiableStates getStates() {
        return states;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Integer getLastWall() {
        return lastWall;
    }

    public boolean isValidPosition(int r, int c) {
        return (r >= 0 && r < getRowsNum() && c >= 0 && c < getColsNum() && allowedCells[r][c]) || (r == 11 && (c == -2 || c == -1 || c == 30 || c == 31));
    }

    public List<Coord> getCriticalPositions() {
        return criticalPositions;
    }
}
