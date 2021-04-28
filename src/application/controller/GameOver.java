package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameOver {
    @FXML
    public Label riprendi;
    @FXML
    public Label home;



    public void homeClicked(MouseEvent mouseEvent) {
    }

    public void homeEntered(MouseEvent mouseEvent) {
        home.setTextFill(Color.web("#ffff00", 0.8));
        home.setFont(new Font(22.0));
    }

    public void homeExited(MouseEvent mouseEvent) {
        home.setTextFill(Color.web("#ffffff", 0.8));
        home.setFont(new Font(20.0));
    }

    public void riprendiClicked(MouseEvent mouseEvent) {

    }

    public void riprendiEntered(MouseEvent mouseEvent) {
        riprendi.setTextFill(Color.web("#ffff00", 0.8));
        riprendi.setFont(new Font(22.0));
    }

    public void riprendiExited(MouseEvent mouseEvent) {
        riprendi.setTextFill(Color.web("#ffffff", 0.8));
        riprendi.setFont(new Font(20.0));
    }

}
