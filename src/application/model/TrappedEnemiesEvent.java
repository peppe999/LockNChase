package application.model;

public class TrappedEnemiesEvent {
    private static final Integer[] trapEnemiesPoints = {100, 300, 1000, 2000};

    private boolean active;
    private Integer points;
    private Integer tickState;

    public TrappedEnemiesEvent() {
        reset();
    }

    public Integer startEvent(Integer enemies) {
        if(enemies == 0)
            return 0;

        active = true;
        points = trapEnemiesPoints[enemies - 1];
        return points;
    }

    public void update() {
        if(!active)
            return;

        tickState++;

        if(tickState > 84)
            reset();
    }

    public boolean isActive() {
        return active;
    }

    public Integer getPoints() {
        return points;
    }

    public void reset() {
        tickState = 0;
        active = false;
        points = 0;
    }
}
