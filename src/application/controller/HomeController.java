package application.controller;

import application.AudioManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import application.GameAppManager;

import java.io.IOException;

public class HomeController extends Application  {

    @FXML
    public ImageView logo;
    @FXML
    Labeled exit;
    @FXML
    Labeled play;

    @FXML
    private ImageView idid;

    public void mouseEnteredExit(MouseEvent mouseEvent) {
        exit.setTextFill(Color.web("#ffff00", 0.8));

    }

    public void mouseExitedExit(MouseEvent mouseEvent) {
        exit.setTextFill(Color.web("#ffffff"));
    }

    public void mouseEnteredPlay(MouseEvent mouseEvent) {
        play.setTextFill(Color.web("#ffff00", 0.8));
    }


    public void mouseExitedPlay(MouseEvent mouseEvent) {
        play.setTextFill(Color.web("#ffffff"));
    }

    public void mouseEntered(DragEvent dragEvent) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void mouseClickedPlay(MouseEvent mouseEvent) throws IOException {
        AudioManager.getInstance().secondaryPlay("beep", false);
        GameAppManager.getInstance().chooseMode();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
