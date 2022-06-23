package unsw.blackout;

import unsw.blackout.Entity;
import unsw.blackout.Movement;
import unsw.utils.Angle;

import static unsw.utils.MathsHelper.CLOCKWISE;

public abstract class Satellite extends Entity implements Movement {
    private int direction = CLOCKWISE;
    private int velocity;
    public Satellite(String id, String type, Angle position, int maxRange, double height, int velocity) {
        super(id, type, position, maxRange, height);
        this.velocity = velocity;
    }
    public int getDirection() {return direction;}
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
