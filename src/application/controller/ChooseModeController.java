package application.controller;

import application.GameAppManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ChooseModeController {

    @FXML
    public Label AI;
    @FXML
    public Label manual;
    @FXML
    public Label back;

    public void AIClicked(MouseEvent mouseEvent) {
        GameAppManager.getInstance().chooselvl();
    }

    public void AIEntered(MouseEvent mouseEvent) {
        AI.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void AIExited(MouseEvent mouseEvent) {
        AI.setTextFill(Color.web("#ffffff", 0.8));
    }

    public void ManualClicked(MouseEvent mouseEvent) {
        GameAppManager.getInstance().startGame();
    }

    public void ManualEntered(MouseEvent mouseEvent) {
        manual.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void ManualExited(MouseEvent mouseEvent) {
        manual.setTextFill(Color.web("#ffffff", 0.8));
    }

    public void backClicked(MouseEvent mouseEvent) {
        GameAppManager.getInstance().home();
    }

    public void backEntered(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffff00", 0.8));
    }

    public void backExited(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffffff", 0.8));
    }
}
