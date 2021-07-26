package application.view.jfxgraphic;

import application.AudioManager;
import application.GameAppManager;
import application.controller.KeyController;
import application.model.Game;
import application.model.NotifiableStates;
import application.model.Wall;
import application.view.GraphicSystem;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.List;

public class GraphicPanel extends Pane implements GraphicSystem {

    private Canvas canvas;
    private Game game;

    private CharacterView playerGraphic;
    private CharacterView stiffyGraphic;
    private CharacterView speedyGraphic;
    private CharacterView scaredyGraphic;
    private CharacterView sillyGraphic;

    public GraphicPanel(Game game) {
        this.game = game;
        canvas = new Canvas();

        playerGraphic = new CharacterView("player", CharacterView.PLAYER_VIEW);
        playerGraphic.move();
        stiffyGraphic = new CharacterView("stiffy", CharacterView.ENEMY_VIEW);
        stiffyGraphic.move();
        speedyGraphic = new CharacterView("speedy", CharacterView.ENEMY_VIEW);
        speedyGraphic.move();
        scaredyGraphic = new CharacterView("scaredy", CharacterView.ENEMY_VIEW);
        scaredyGraphic.move();
        sillyGraphic = new CharacterView("silly", CharacterView.ENEMY_VIEW);
        sillyGraphic.move();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(new KeyController(game));
        getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
    }

    private void draw() {
        boolean showPlayer = true;
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/emptyMap.png")), 0, 16, 240 * 2, 200 * 2);

        List<Integer> gameWallsIndexes = game.getActiveGameWallsIndexes();
        for(Integer i : gameWallsIndexes) {
            Wall w = game.getActiveWall(i);
            new GameWallGraphic().drawWall(canvas.getGraphicsContext2D(), w);
        }

        List<Integer> playerWallsIndexes = game.getActivePlayerWallsIndexes();
        for(Integer i : playerWallsIndexes) {
            Wall w = game.getActiveWall(i);
            new PlayerWallGraphic().drawWall(canvas.getGraphicsContext2D(), w);
        }

        List<Wall> perimeterWalls = game.getPerimeterWalls();
        for(Wall w : perimeterWalls)
            if(w.isActive())
                new FixedWallGraphic().drawWall(canvas.getGraphicsContext2D(), w);

        for(int i = 0; i < game.getRowsNum(); i++)
            for(int j = 0; j < game.getColsNum(); j++)
                if(game.isCoin(i, j)) {
                    canvas.getGraphicsContext2D().setFill(Color.YELLOW);
                    canvas.getGraphicsContext2D().fillRect((j * 8 + 3) * 2, (i * 8 + 3) * 2, 2 * 2, 2 * 2);
                }

        if(game.getBag().isActive()) {
            if(game.getBag().isPicked()) {
                int value = game.getBag().getValue() / 2;
                int offset = (value < 1000) ? 2 : 0;
                canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/points/" + value + ".png")), (game.getBag().getCoord().getCol() * 8 - 4 - offset) * 2, (game.getBag().getCoord().getRow() * 8) * 2, 16 * 2, 8 * 2);
                showPlayer = false;
            }
            else
                canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/items/bag.png")), (game.getBag().getCoord().getCol() * 8 - 4) * 2, (game.getBag().getCoord().getRow() * 8 - 4) * 2, 16 * 2, 16 * 2);
        }

        if(game.getBonusItem().isActive()) {
            if(game.getBonusItem().isPicked()) {
                int value = game.getBonusItem().getValue();
                int offset = (value < 1000) ? 2 : 0;
                canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/points/" + value + ".png")), (game.getBonusItem().getCoord().getCol() * 8 - 4 - offset) * 2, (game.getBonusItem().getCoord().getRow() * 8) * 2, 16 * 2, 8 * 2);
            }
            else {
                int level = game.getLevel();
                if(level > 9)
                    level = 9;
                canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/items/" + level + ".png")), (game.getBonusItem().getCoord().getCol() * 8 - 4) * 2, (game.getBonusItem().getCoord().getRow() * 8 - 4) * 2, 16 * 2, 16 * 2);
            }
        }

        int inc = 0;
        if(game.isFinished())
            if((int)game.getPlayer().getY() / 8 == 1)
                inc = -3;
            else if((int)game.getPlayer().getY() / 8 == 25)
                inc = 4;


        if(playerGraphic.getCurrentImage() == null)
            showPlayer = false;

        if(showPlayer)
            if(playerGraphic.getState().equals(CharacterView.CAR_PL))
                canvas.getGraphicsContext2D().drawImage(playerGraphic.getCurrentImage(), (game.getPlayer().getX() - 10) * 2, (game.getPlayer().getY() - 4) * 2, 32 * 2, 16 * 2);
            else
                canvas.getGraphicsContext2D().drawImage(playerGraphic.getCurrentImage(), (game.getPlayer().getX() - 4) * 2, (game.getPlayer().getY() - 4 + inc) * 2, playerGraphic.dim * 2, playerGraphic.dim * 2);

        if(!(game.isPlayerDead() && game.getPlayer().isActive())) {
            canvas.getGraphicsContext2D().drawImage(sillyGraphic.getCurrentImage(), (game.getSilly().getX() - 4) * 2, (game.getSilly().getY() - 4) * 2, sillyGraphic.dim * 2, sillyGraphic.dim * 2);
            canvas.getGraphicsContext2D().drawImage(scaredyGraphic.getCurrentImage(), (game.getScaredy().getX() - 4) * 2, (game.getScaredy().getY() - 4) * 2, scaredyGraphic.dim * 2, scaredyGraphic.dim * 2);
            canvas.getGraphicsContext2D().drawImage(speedyGraphic.getCurrentImage(), (game.getSpeedy().getX() - 4) * 2, (game.getSpeedy().getY() - 4) * 2, speedyGraphic.dim * 2, speedyGraphic.dim * 2);
            canvas.getGraphicsContext2D().drawImage(stiffyGraphic.getCurrentImage(), (game.getStiffy().getX() - 4) * 2, (game.getStiffy().getY() - 4) * 2, stiffyGraphic.dim * 2, stiffyGraphic.dim * 2);
        }

        canvas.getGraphicsContext2D().setFont(Font.font("Press Start 2P", 14));

        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillText("SCORE", 32, 31 * 16);
        canvas.getGraphicsContext2D().setFill(Color.YELLOW);
        canvas.getGraphicsContext2D().fillText("$", 34 + 6 * 14, 31 * 16);
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().fillText(game.getScore().toString(), 40 + 7 * 14, 31 * 16);

        if(game.getTrappedEnemiesEvent().isActive()) {
            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            canvas.getGraphicsContext2D().setFont(Font.font("Press Start 2P", 10));
            canvas.getGraphicsContext2D().fillText("+" + game.getTrappedEnemiesEvent().getPoints().toString(), 48 + ((7 + game.getScore().toString().length()) * 14), 31 * 16 - 2.5);
        }


        int lives = game.getLives();
        for(int i = 0; i < lives - 1; i++)
            canvas.getGraphicsContext2D().drawImage(new Image(getClass().getResourceAsStream("/application/resources/player/10.png")), 32 * i + 32, (27 * 8 - 4) * 2, playerGraphic.dim * 2, playerGraphic.dim * 2);

    }

    @Override
    public void update() {
        game.update();

        if(game.isGameOver())
            GameAppManager.getInstance().gameOver();

        if(!game.getStiffy().isActive() && game.isWinFreeze())
            stiffyGraphic.setState(CharacterView.PL_WIN);
        else
            stiffyGraphic.setState(game.getStiffy().getDirection());

        stiffyGraphic.update();


        if(!game.getScaredy().isActive() && game.isWinFreeze())
            scaredyGraphic.setState(CharacterView.PL_WIN);
        else
            scaredyGraphic.setState(game.getScaredy().getDirection());

        scaredyGraphic.update();


        if(!game.getSilly().isActive() && game.isWinFreeze())
            sillyGraphic.setState(CharacterView.PL_WIN);
        else
            sillyGraphic.setState(game.getSilly().getDirection());

        sillyGraphic.update();


        if(!game.getSpeedy().isActive() && game.isWinFreeze())
            speedyGraphic.setState(CharacterView.PL_WIN);
        else
            speedyGraphic.setState(game.getSpeedy().getDirection());

        speedyGraphic.update();

        if(!game.getPlayer().isActive() && game.isWinFreeze())
            playerGraphic.setState(CharacterView.PL_WIN);
        else if(game.isBeginning() && !game.isPlayerAligned())
            playerGraphic.setState(CharacterView.CAR_PL);
        else if(game.getPlayer().isActive() && game.isPlayerDead())
            playerGraphic.setState(CharacterView.PL_DIE);
        else
            playerGraphic.setState(game.getPlayer().getDirection());

        playerGraphic.update();


        chooseAudio();

        draw();
    }

    private void chooseAudio() {
        NotifiableStates states = game.getStates();
        AudioManager audioManager = AudioManager.getInstance();

        if(states.getStateValue("win")) {
            audioManager.primaryPlay("win", false, null, true);
            states.processState("win");
        }
        else if(states.getStateValue("startGame")) {
            audioManager.primaryPlay("startGame", false, "run", false);
            states.processState("startGame");
        }
        else if(states.getStateValue("die")) {
            audioManager.primaryPlay("die", false, null, true);
            states.processState("die");
        }
        else if(states.getStateValue("newLvl")) {
            audioManager.primaryPlay("run", true, null, false);
            states.processState("newLvl");
        }


        if(states.getStateValue("money")) {
            audioManager.secondaryPlay("money", false);
            states.processState("money");
        }
        if(states.getStateValue("bagSpawn")) {
            audioManager.secondaryPlay("bagSpawn", true);
            states.processState("bagSpawn");
        }
        if(states.getStateValue("doorClosed")) {
            if(states.getStateValue("lockedEnemies")) {
                audioManager.secondaryPlay("lockedEnemies", false);
                states.processState("lockedEnemies");
            }
            else
                audioManager.secondaryPlay("doorClosed", false);

            states.processState("doorClosed");
        }
        if(states.getStateValue("pickBag")) {
            audioManager.secondaryPlay("pickBag", true);
            states.processState("pickBag");
        }
        if(states.getStateValue("pickLvlBonus")) {
            audioManager.secondaryPlay("pickLvlBonus", false);
            states.processState("pickLvlBonus");
        }
        if(states.getStateValue("bonusLife")) {
            audioManager.secondaryPlay("bonusLife", false);
            states.processState("bonusLife");
        }

    }
}