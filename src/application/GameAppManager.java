package application;

import application.model.Game;
import application.model.GameMode;
import application.view.jfxgraphic.GraphicPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameAppManager {
    private static GameAppManager instance = null;
    private Stage stage;
    private AudioManager audioManager;

    private Scene gameScene;
    private GameLoop gameLoop;

    private GameAppManager(){
        this.audioManager = AudioManager.getInstance();
        gameScene = null;
        gameLoop = null;
    }

    public void setStage(Stage s) {
        stage = s;
    }

    public static GameAppManager getInstance(){
        if(instance == null){
            instance = new GameAppManager();
        }
        return instance;
    }

    public void home(){
        gameScene = null;
        gameLoop = null;

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/home.fxml"));
            stage.setScene(new Scene(root));
            audioManager.homeMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseMode() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/chooseMode.fxml"));
            stage.setScene(new Scene(root));
         //   audioManager.homeMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooselvl() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/chooseLvl.fxml"));
            stage.setScene(new Scene(root));
           // audioManager.homeMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        audioManager.pauseAll();
        if(gameLoop != null)
            gameLoop.stop();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/pause.fxml"));
            stage.setScene(new Scene(root));
            //audioManager.homeMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        audioManager.resumeAll();
        if(gameScene != null) {
            stage.setScene(gameScene);
            gameLoop.start();
        }
    }

    public void startGame() {
        Game game = new Game();
        game.setMode(GameMode.USER_MODE);
        GraphicPanel gp = new GraphicPanel(game);
        gameScene = new Scene(gp);
        stage.setScene(gameScene);
        gameLoop = new GameLoop(gp);
        gameLoop.start();
        audioManager.runMedia();
    }

    public void gameOver() {
        if(gameLoop != null)
            gameLoop.stop();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/GameOver.fxml"));
            stage.setScene(new Scene(root));
            //audioManager.homeMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
