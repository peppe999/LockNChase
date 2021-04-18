package application.view.jfxgraphic;

import application.model.Coord;
import application.model.Wall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FixedWallGraphic implements WallGraphic {
    @Override
    public void drawWall(GraphicsContext graphicsContext, Wall wall) {
        Coord coord = wall.getCoord();
        graphicsContext.setFill(Color.YELLOW);

        int inc;
        if(coord.getRow() == 2)
            inc = 2;
        else
            inc = 4;

        if(wall.isClosed())
            graphicsContext.fillRect((coord.getCol() * 8 - 4) * 2, (coord.getRow() * 8 + inc) * 2, 16 * 2, 2 * 2);
        else {
            graphicsContext.setFont(Font.font("Press Start 2P", 8));

            if(coord.getRow() != 2)
                inc = 10;

            graphicsContext.fillText("OUT", (coord.getCol() * 8 - 1.4) * 2, (coord.getRow() * 8 + inc) * 2);

        }
    }
}
