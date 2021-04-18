package application;

import application.view.GraphicSystem;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

    private long previousTime;
    private GraphicSystem graphicSystem;
    private long frequency = 16 * 1000000;

    public GameLoop(GraphicSystem graphicSystem) {
        this.graphicSystem = graphicSystem;
        previousTime = 0;
    }

    @Override
    public void handle(long currentNanoTime) {
        if (currentNanoTime - previousTime >= frequency) {
            graphicSystem.update();
            previousTime = currentNanoTime;
        }
    }
}
