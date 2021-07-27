package application;

import application.ai.AIPlayer;
import application.view.GraphicSystem;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

    private long previousTime;
    private GraphicSystem graphicSystem;
    private AIPlayer aiPlayer;
    private long frequency = 32 * 1000000;

    public GameLoop(GraphicSystem graphicSystem, AIPlayer aiPlayer) {
        this.graphicSystem = graphicSystem;
        this.aiPlayer = aiPlayer;
        previousTime = 0;
    }

    @Override
    public void handle(long currentNanoTime) {

        if(currentNanoTime - previousTime >= frequency/2 && aiPlayer != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    aiPlayer.exec();
                }
            }).start();
        }
        if (currentNanoTime - previousTime >= frequency) {
            /*if(aiPlayer != null)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        aiPlayer.exec();
                    }
                }).start();*/

            graphicSystem.update();
            previousTime = currentNanoTime;

        }
    }
}
