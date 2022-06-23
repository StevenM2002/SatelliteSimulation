package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;

import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;
import static unsw.utils.MathsHelper.CLOCKWISE;


public class RelaySatellite extends Satellite {
    private static final int VELOCITY = 1500;
    private static final int MAX_RANGE = 300000;
    private static final String TYPE = "RelaySatellite";
    public RelaySatellite(String id, Angle position, double height) {
        super(id, TYPE, position, MAX_RANGE, height, VELOCITY);
        //In the case that the satellite doesn't start in the region [140°, 190°], it should choose whatever direction gets
        // it to the region [140°, 190°] in the shortest amount of time.
        //As a hint (and to prevent you having to do maths) this 'threshold' angle is 345°; if a relay satellite starts on
        // the threshold 345° it should take the positive direction.
        if (!isInRegion(position)) {
            if (position.compareTo(Angle.fromDegrees(140)) < 0 && position.compareTo(Angle.fromDegrees(0)) >= 0) {
                setDirection(ANTI_CLOCKWISE);
            } else if (position.compareTo(Angle.fromDegrees(345)) >= 0 && position.compareTo(Angle.fromDegrees(360)) < 0) {
                setDirection(ANTI_CLOCKWISE);
            } else {
                setDirection(CLOCKWISE);
            }
        }
    }
    private boolean isInRegion(Angle angle) {
        return (angle.compareTo(Angle.fromDegrees(140)) >= 0 &&
                angle.compareTo(Angle.fromDegrees(190)) <= 0);
    }

    @Override
    public void move() {
        Angle nextPosition = getNextMove(getPosition(), VELOCITY, getHeight(), getDirection());
        // Move normally but if it extends out of region on next move then flip direction
        if (isInRegion(getPosition()) && !isInRegion(nextPosition)) {
            setPosition(nextPosition);
            // Effectively flips direction
            setDirection(getDirection() * -1);
        } else {
            setPosition(nextPosition);
        }
    }
}
