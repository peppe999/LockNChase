package application.view.jfxgraphic;

import application.model.Coord;
import application.model.Wall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PlayerWallGraphic implements WallGraphic {

    @Override
    public void drawWall(GraphicsContext graphicsContext, Wall wall) {
        Coord coord = wall.getCoord();
        int x = 0, y = 0;
        if(wall.getType().equals(Wall.HORIZONTAL_WALL)) {
            x = coord.getCol() * 8 - 4;
            y = coord.getRow() * 8 + 3;
        }
        else if(wall.getType().equals(Wall.VERTICAL_WALL)) {
            x = coord.getCol() * 8 + 3;
            y = coord.getRow() * 8 - 4;
        }


        int tick = wall.getTickState() / 28;
        if(tick <= 2 || (tick >= 6 && tick <= 8))
            stateOne(graphicsContext, x, y, wall.getType());
        else if(tick <= 5)
            stateTwo(graphicsContext, x, y, wall.getType());
        else if(tick <= 11) {
            if((wall.getTickState() / 7) % 2 == 0)
                stateOne(graphicsContext, x, y, wall.getType());
            else
                stateTwo(graphicsContext, x, y, wall.getType());
        }

    }

    private void stateTwo(GraphicsContext graphicsContext, int x, int y, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.RED);

            for(int i = 0; i < 5; i++) {
                graphicsContext.fillRect((x + i + 1 + (i * 2)) * 2, y * 2, 2 * 2, 2 * 2);
            }
        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.RED);

            for(int i = 0; i < 5; i++) {
                graphicsContext.fillRect(x * 2, (y + i + 1 + (i * 2)) * 2, 2 * 2, 2 * 2);
            }
        }
    }

    private void stateOne(GraphicsContext graphicsContext, int x, int y, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.RED);

            for(int i = 0; i < 15; i += 2) {
                graphicsContext.fillRect((x + i) * 2, y * 2, 1 * 2, 1 * 2);
                graphicsContext.fillRect((x + i + 1) * 2, (y + 1) * 2, 1 * 2, 1 * 2);
            }
        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.RED);

            for(int i = 0; i < 15; i += 2) {
                graphicsContext.fillRect(x * 2, (y + i) * 2, 1 * 2, 1 * 2);
                graphicsContext.fillRect((x + 1) * 2, (y + i + 1) * 2, 1 * 2, 1 * 2);
            }
        }
    }
}
