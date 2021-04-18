package application;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Font.loadFont(
                getClass().getResource("/application/resources/font/PressStart2P-vaV7.ttf").toExternalForm(),
                18);

        primaryStage.setWidth(485);
        primaryStage.setHeight(543);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Lock&Chase");

        GameAppManager.getInstance().setStage(primaryStage);
        GameAppManager.getInstance().home();

        primaryStage.show();
    }
}