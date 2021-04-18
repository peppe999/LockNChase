package application.view.jfxgraphic;

import application.model.Coord;
import application.model.Wall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameWallGraphic implements WallGraphic {

    @Override
    public void drawWall(GraphicsContext graphicsContext, Wall wall) {
        Coord coord = wall.getCoord();
        int x = 0, y = 0, width = 0, height = 0;
        if(wall.getType().equals(Wall.HORIZONTAL_WALL)) {
            x = coord.getCol() * 8 - 8;
            y = coord.getRow() * 8 + 3;
            width = 24;
            height = 2;
        }
        else if(wall.getType().equals(Wall.VERTICAL_WALL)) {
            x = coord.getCol() * 8 + 3;
            y = coord.getRow() * 8 - 8;
            width = 2;
            height = 24;
        }


        int tick = wall.getTickState() / 28;
        if(tick == 0 || tick == 11)
            stateOne(graphicsContext, x, y, width, height, wall.getType());
        else if(tick == 1 || tick == 10)
            stateTwo(graphicsContext, x, y, width, height, wall.getType());
        else if(tick == 2 || tick == 9)
            stateThree(graphicsContext, x, y, width, height, wall.getType());
        else if(tick == 3 || tick == 8)
            stateFour(graphicsContext, x, y, width, height, wall.getType());
        else if(tick == 4 || tick == 7)
            stateFive(graphicsContext, x, y, width, height, wall.getType());
        else if(tick == 5 || tick == 6)
            stateSix(graphicsContext, x, y, width, height, wall.getType());
    }

    private void stateSix(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        stateFour(graphicsContext, x, y, width, height, type);
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect((x + 8) * 2, y * 2, 3 * 2, height * 2);
            graphicsContext.fillRect((x + 13) * 2, y * 2, 3 * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 11) * 2, y * 2, 2 * 2, height * 2);

        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect(x * 2, (y + 8) * 2, width * 2, 3 * 2);
            graphicsContext.fillRect(x * 2, (y + 13) * 2, width * 2, 3 * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 11) * 2, width * 2, 2 * 2);
        }
    }

    private void stateFive(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        stateFour(graphicsContext, x, y, width, height, type);
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect((x + 8) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 15) * 2, y * 2, 1 * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 9) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 14) * 2, y * 2, 1 * 2, height * 2);

        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect(x * 2, (y + 8) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 15) * 2, width * 2, 1 * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 9) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 14) * 2, width * 2, 1 * 2);
        }
    }

    private void stateFour(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect((x + 1) * 2, y * 2, 6 * 2, height * 2);
            graphicsContext.fillRect((x + 17) * 2, y * 2, 6 * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 7) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 16) * 2, y * 2, 1 * 2, height * 2);

        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect(x * 2, (y + 1) * 2, width * 2, 6 * 2);
            graphicsContext.fillRect(x * 2, (y + 17) * 2, width * 2, 6 * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 7) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 16) * 2, width * 2, 1 * 2);
        }
    }

    private void stateThree(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect((x + 1) * 2, y * 2, 4 * 2, height * 2);
            graphicsContext.fillRect((x + 19) * 2, y * 2, 4 * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 5) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 18) * 2, y * 2, 1 * 2, height * 2);

        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect(x * 2, (y + 1) * 2, width * 2, 4 * 2);
            graphicsContext.fillRect(x * 2, (y + 19) * 2, width * 2, 4 * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 5) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 18) * 2, width * 2, 1 * 2);
        }
    }

    private void stateTwo(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect((x + 1) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 22) * 2, y * 2, 1 * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 2) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 21) * 2, y * 2, 1 * 2, height * 2);

        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.rgb(0, 255, 0));
            graphicsContext.fillRect(x * 2, (y + 1) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 22) * 2, width * 2, 1 * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 2) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 21) * 2, width * 2, 1 * 2);
        }
    }

    private void stateOne(GraphicsContext graphicsContext, int x, int y, int width, int height, Integer type) {
        if(type.equals(Wall.HORIZONTAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect((x + 1) * 2, y * 2, 1 * 2, height * 2);
            graphicsContext.fillRect((x + 22) * 2, y * 2, 1 * 2, height * 2);
        }
        else if(type.equals(Wall.VERTICAL_WALL)) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(x * 2, y * 2, width * 2, height * 2);

            graphicsContext.setFill(Color.RED);
            graphicsContext.fillRect(x * 2, (y + 1) * 2, width * 2, 1 * 2);
            graphicsContext.fillRect(x * 2, (y + 22) * 2, width * 2, 1 * 2);
        }
    }
}
