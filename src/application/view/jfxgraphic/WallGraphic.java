package application.view.jfxgraphic;

import application.model.Wall;
import javafx.scene.canvas.GraphicsContext;

public interface WallGraphic {
    void drawWall(GraphicsContext graphicsContext, Wall wall);
}
