package application.controller;

import application.GameAppManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ChooseLvlController  {

    @FXML
    public Label semplice;
    @FXML
    public Label difficile;
    @FXML
    public Label back;


    public void difficileClicked(MouseEvent mouseEvent) {
    }

    public void difficileEntered(MouseEvent mouseEvent) {
        difficile.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void difficileExited(MouseEvent mouseEvent) {
        difficile.setTextFill(Color.web("#ffffff", 0.8));
    }

    public void sempliceClicked(MouseEvent mouseEvent) {
    }

    public void sempliceEntered(MouseEvent mouseEvent) {
        semplice.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void sempliceExited(MouseEvent mouseEvent) {
        semplice.setTextFill(Color.web("#ffffff", 0.8));
    }

    public void backClicked(MouseEvent mouseEvent) {
        GameAppManager.getInstance().chooseMode();
    }

    public void backEntered(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void backExited(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffffff", 0.8));
    }
}
