package application.controller;

import application.AudioManager;
import application.GameAppManager;
import application.model.GameMode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

public class ChooseModeController {
    @FXML
    public Label AI;
    @FXML
    public Label manual;
    @FXML
    public Label back;

    final double MAX_FONT_SIZE = 21.0;

    public void AIClicked(MouseEvent mouseEvent) throws IOException {
        AudioManager.getInstance().secondaryPlay("beep", false);
        GameAppManager.getInstance().chooselvl();
    }

    public void AIEntered(MouseEvent mouseEvent) {
        AI.setTextFill(Color.web("#ffff00", 0.8));
        AI.setFont(new Font(MAX_FONT_SIZE));
    }

    public void AIExited(MouseEvent mouseEvent) {
        AI.setTextFill(Color.web("#ffffff"));
        AI.setFont(new Font(20.0));
    }

    public void ManualClicked(MouseEvent mouseEvent) {
        AudioManager.getInstance().secondaryPlay("beep", false);
        GameAppManager.getInstance().setMode(GameMode.USER_MODE);
        GameAppManager.getInstance().startGame();
    }

    public void ManualEntered(MouseEvent mouseEvent) {
        manual.setTextFill(Color.web("#ffff00", 0.8));
        manual.setFont(new Font(MAX_FONT_SIZE));
    }

    public void ManualExited(MouseEvent mouseEvent) {
        manual.setTextFill(Color.web("#ffffff"));
        manual.setFont(new Font(20.0));
    }

    public void backClicked(MouseEvent mouseEvent) throws IOException {
        GameAppManager.getInstance().home();
        AudioManager.getInstance().secondaryPlay("beep", false);
    }

    public void backEntered(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffff00", 0.8));
        back.setFont(new Font(20.0));
    }

    public void backExited(MouseEvent mouseEvent) {
        back.setTextFill(Color.web("#ffffff"));
        back.setFont(new Font(18.0));
    }
}
