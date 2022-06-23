package unsw.blackout;

import unsw.utils.Angle;

import static unsw.utils.MathsHelper.CLOCKWISE;

public interface Movement {
    public void move();
    public default Angle getNextMove(Angle currPosition, double velocity, double height, int direction) {
        Angle offset = Angle.fromRadians((double) velocity / (double) height);
        Angle nextPosition;
        if (direction == CLOCKWISE) {
            nextPosition = currPosition.subtract(offset);
        } else {
            nextPosition = currPosition.add(offset);
        }
        return MyAngleHelper.normaliseAngle(nextPosition);
    }
}
