package application.controller;

import application.AudioManager;
import application.GameAppManager;
import application.model.Direction;
import application.model.Game;
import application.model.GameMode;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyController implements EventHandler<KeyEvent> {

    private Game game;

    public KeyController(Game game) {
        this.game = game;
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            if(GameAppManager.getInstance().getMode().equals(GameMode.USER_MODE)) {
                if (event.getCode() == KeyCode.RIGHT) {
                    game.setNewPlayerDirection(Direction.RIGHT);
                } else if (event.getCode() == KeyCode.LEFT) {
                    game.setNewPlayerDirection(Direction.LEFT);
                } else if (event.getCode() == KeyCode.UP) {
                    game.setNewPlayerDirection(Direction.UP);
                } else if (event.getCode() == KeyCode.DOWN) {
                    game.setNewPlayerDirection(Direction.DOWN);
                } else if (event.getCode() == KeyCode.S) {
                    game.closePlayerWall();
                }
            }

            if(event.getCode() == KeyCode.ESCAPE) {
                GameAppManager.getInstance().pause();
            }
        }
    }
}