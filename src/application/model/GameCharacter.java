package application.model;

public class GameCharacter {
    private double x;
    private double y;
    private double speed;
    private Integer direction;

    private double initialX;
    private double initialY;

    private Integer freezeTime;
    private boolean active;
    private boolean imminentRestart;

    public GameCharacter() {
        active = true;
        imminentRestart = false;
        freezeTime = 0;
    }

    public double getInitialX() {
        return initialX;
    }

    public void setInitialX(double initialX) {
        setX(initialX);
        this.initialX = initialX;
    }

    public double getInitialY() {
        return initialY;
    }

    public void setInitialY(double initialY) {
        setY(initialY);
        this.initialY = initialY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        if(direction < 0 || direction > 3)
            return;

        this.direction = direction;
    }

    public boolean isActive() {
        return active;
    }

    public void setFreezeTime(Integer time) {
        freezeTime = time;
        imminentRestart = false;
        active = false;
    }

    public void updateFreezeTime() {
        if(active)
            return;

        freezeTime--;

        if(freezeTime <= 60)
            imminentRestart = true;

        if(freezeTime <= 0) {
            freezeTime = 0;
            active = true;
            imminentRestart = false;
        }
    }

    public boolean isImminentRestart() {
        return imminentRestart;
    }
}
