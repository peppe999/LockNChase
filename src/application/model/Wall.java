package application.model;

import java.lang.reflect.Field;

public class Wall {
    public static final Integer HORIZONTAL_WALL = 0;
    public static final Integer VERTICAL_WALL = 1;
    public static final Integer FIXED_WALL = 2;

    public static final Integer GAME_OWNER = 0;
    public static final Integer PLAYER_OWNER = 1;

    private Integer type;
    private Integer owner;
    private Integer tickState;
    private Coord coord;
    private boolean active;
    private boolean closed;

    public Wall(Integer type, Coord coord) {
        this.type = HORIZONTAL_WALL;
        this.coord = coord;

        if(type >= 0 && type <= 2)
            this.type = type;

        owner = GAME_OWNER;
        active = false;
        closed = false;
        tickState = 0;
    }

    public void closeWall(Integer owner) {
        if(owner < 0 || owner > 1)
            return;

        this.owner = owner;
        active = true;

        if(owner.equals(PLAYER_OWNER) || type.equals(FIXED_WALL))
            closed = true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isClosed() {
        return closed;
    }

    public Coord getCoord() {
        return coord;
    }

    public Integer getTickState() {
        return tickState;
    }

    public Integer getType() {
        return type;
    }

    public void updateWallState() {
        if(!active)
            return;

        if(type.equals(FIXED_WALL))
            return;

        tickState++;

        if(owner.equals(GAME_OWNER)) {
            if(tickState >= 56 && tickState < 252 && !closed) {
                closed = true;
                return;
            }
            else if(tickState >= 252 && closed) {
                closed = false;
                return;
            }
        }

        if(tickState >= 336) {
            tickState = 0;
            active = false;

            if(owner.equals(PLAYER_OWNER)) {
                closed = false;
            }
        }

    }

    public void openFixedWall() {
        if(type.equals(FIXED_WALL))
            closed = false;
    }

    public void resetWall() {
        closed = false;
        active = false;
        tickState = 0;
    }
}
