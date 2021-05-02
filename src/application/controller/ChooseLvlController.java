package application.controller;

import application.AudioManager;
import application.GameAppManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;

public class ChooseLvlController  {

    @FXML
    public Label semplice;
    @FXML
    public Label difficile;
    @FXML
    public Label back;


    public void difficileClicked(MouseEvent mouseEvent) {
        AudioManager.getInstance().secondaryPlay("beep", false);
    }

    public void difficileEntered(MouseEvent mouseEvent) {
        difficile.setTextFill(Color.web("#ffff00", 0.8));
        difficile.setFont(new Font(22.0));
    }

    public void difficileExited(MouseEvent mouseEvent) {
        difficile.setTextFill(Color.web("#ffffff"));
        difficile.setFont(new Font(20.0));
    }

    public void sempliceClicked(MouseEvent mouseEvent) {
        AudioManager.getInstance().secondaryPlay("beep", false);
        //GameAppManager.getInstance().pause();
    }

    public void sempliceEntered(MouseEvent mouseEvent) {
        semplice.setTextFill(Color.web("#ffff00", 0.8));
        semplice.setFont(new Font(22.0));
    }

    public void sempliceExited(MouseEvent mouseEvent) {
        semplice.setTextFill(Color.web("#ffffff"));
        semplice.setFont(new Font(20.0));
    }


    public void backClicked(MouseEvent mouseEvent) throws IOException {
        AudioManager.getInstance().secondaryPlay("beep", false);
        GameAppManager.getInstance().chooseMode();
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
