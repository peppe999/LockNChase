package application.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import application.Main;
import application.GameAppManager;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        GameAppManager.getInstance().chooseMode();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
