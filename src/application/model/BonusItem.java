package application.model;

public class BonusItem {
    private Coord coord;
    private boolean active;
    private Integer tickState;
    private Integer level;
    private boolean picked;

    private Integer[] values = {200, 300, 500, 100, 800, 1000, 2000, 400, 3000, 5000};

    public BonusItem(Coord coord, Integer level) {
        this.coord = coord;
        active = false;
        picked = false;
        this.level = level;
        if(this.level > 9)
            this.level = 9;
        tickState = 0;
    }

    public void show() {
        active = true;
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

    public Integer takeBag() {
        if(!active || picked)
            return null;

        tickState = 196;
        picked = true;

        return values[level];
    }

    public Coord getCoord() {
        return coord;
    }

    public Integer getValue() {
        return values[level];
    }

    public void reset() {
        active = false;
        picked = false;
        tickState = 0;
    }
}
