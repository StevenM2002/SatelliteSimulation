package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;

import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;
import static unsw.utils.MathsHelper.CLOCKWISE;

public class TeleportingSatellite extends Satellite {
    private static final int VELOCITY = 1000;
    private static final int MAX_RANGE = 200000;
    private static final String TYPE = "TeleportingSatellite";

    private static final int MAX_OUTGOING_BANDWIDTH = 10;
    private static final int MAX_INCOMING_BANDWIDTH = 15;
    private static final int MAX_STORAGE_BYTES = 200;
    private boolean hasCrossedOverTo0And180Once = false;
    public TeleportingSatellite(String id, Angle position, double height) {
        super(id, TYPE, position, MAX_RANGE, height, VELOCITY);
        setDirection(ANTI_CLOCKWISE);
        setTransferringProperties(MAX_INCOMING_BANDWIDTH, MAX_OUTGOING_BANDWIDTH, MAX_STORAGE_BYTES, Integer.MAX_VALUE);
    }

    private boolean isBetween0And180(Angle angle) {
        return angle.compareTo(Angle.fromDegrees(0)) >= 0 && angle.compareTo(Angle.fromDegrees(180)) <= 0;
    }
    @Override
    public void move() {
        Angle nextMove = getNextMove(getPosition(), VELOCITY, getHeight(), getDirection());
        // Need to fix this, doesnt move straight away after teleporting and instead re teleports
        if (isBetween0And180(getPosition())) hasCrossedOverTo0And180Once = true;
        if (getDirection() == CLOCKWISE) {
            // 0 < x <= 180
            if (nextMove.compareTo(Angle.fromDegrees(180)) <= 0 && getPosition().compareTo(Angle.fromDegrees(0)) > 0) {
                setPosition(Angle.fromDegrees(0));
                // Flip direction
                setDirection(getDirection() * -1);
            }
            else {
                setPosition(nextMove);
            }
        } else {
            // 180 <= x < 360
            if (hasCrossedOverTo0And180Once && nextMove.compareTo(Angle.fromDegrees(180)) >= 0 && getPosition().compareTo(Angle.fromDegrees(360)) < 0) {
                setPosition(Angle.fromDegrees(0));
                setDirection(getDirection() * -1);
            } else {
                setPosition(nextMove);
            }
        }
    }
}
