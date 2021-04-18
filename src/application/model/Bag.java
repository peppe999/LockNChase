package application.model;

public class Bag {
    private Coord coord;
    private boolean active;
    private Integer tickState;
    private Integer value;
    private boolean picked;

    public Bag(Coord coord) {
        this.coord = coord;
        active = false;
        picked = false;
        value = 500;
        tickState = 0;
    }

    public void show() {
        active = true;
        tickState = 0;
        picked = false;
    }

    public Coord getCoord() {
        return coord;
    }

    public void update() {
        if(!active && !picked)
            return;

        tickState++;

        if(tickState >= 252) {
            tickState = 0;
            active = false;
            picked = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPicked() {
        return picked;
    }

    public Integer getValue() {
        return value;
    }

    public Integer takeBag() {
        if(!active || picked)
            return null;

        Integer actualValue = value;

        value *= 2;
        tickState = 224;
        picked = true;

        return actualValue;
    }

    public void reset() {
        active = false;
        picked = false;
        tickState = 0;
    }
}
