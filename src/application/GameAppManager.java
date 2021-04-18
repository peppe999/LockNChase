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

    private GameAppManager() {
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
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/home.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseMode() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/chooseMode.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooselvl() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/jfxgraphic/chooseLvl.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        Game game = new Game();
        game.setMode(GameMode.USER_MODE);
        GraphicPanel gp = new GraphicPanel(game);
        Scene scene = new Scene(gp);
        stage.setScene(scene);
        GameLoop gameLoop = new GameLoop(gp);
        gameLoop.start();
        AudioSystem a = AudioSystem.getInstance();
        a.playStart();
    }
}
