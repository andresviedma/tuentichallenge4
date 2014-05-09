package com.andresviedma.tuenticontest2014.challenge9;

public class Road {
    private int origin;
    private int target;
    
    private int speed;
    

    public Road() {
    }

    public Road(int origin, int target, int speed) {
        this.origin = origin;
        this.target = target;
        this.speed = speed;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
}
