package unsw.blackout.satellites;

import unsw.blackout.Communicable;
import unsw.blackout.devices.Device;
import unsw.utils.Angle;

import java.util.ArrayList;

import static unsw.utils.MathsHelper.*;

public class TeleportingSatellite extends Satellite {
    //TeleportingSatellite
    //Moves at a linear velocity of 1,000 kilometres (1,000,000 metres) per minute
    //Supports all devices
    //Maximum range of 200,000 kilometres (200,000,000 metres)
    //Can receive 15 bytes per minute and can send 10 bytes per minute.
    //Can store up to 200 bytes and as many files as fits into that space.
    //When the position of the satellite reaches θ = 180, the satellite teleports to θ = 0 and changes direction.
    //If a file transfer is in progress when the satellite teleports, the rest of the file is instantly downloaded,
    // however all "t" bytes are removed from the remaining bytes to be sent
    //Teleporting satellites start by moving anticlockwise.
    private static final String TYPE = "TeleportingSatellite";
    private static final int VELOCITY = 1000;
    private static final int MAXRANGE = 200000;
    private boolean hasCrossedOverTo0And180Once = false;

    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position, MAXRANGE, VELOCITY);
        setDirection(ANTI_CLOCKWISE);
    }

    @Override
    public void move() {
        Angle nextMove = getNextMove();
        // Need to fix this, doesnt move straight away after teleporting and instead re teleports
        if (isBetween0And180(getPosition())) hasCrossedOverTo0And180Once = true;
        if (getDirection() == CLOCKWISE) {
            // 0 < x <= 180
            if (nextMove.compareTo(Angle.fromDegrees(180)) <= 0 && getPosition().compareTo(Angle.fromDegrees(0)) > 0) {
                setPosition(Angle.fromDegrees(0));
                flipDirection();
            }
            else {
                setPosition(nextMove);
            }
        } else {
            // 180 <= x < 360
            if (hasCrossedOverTo0And180Once && nextMove.compareTo(Angle.fromDegrees(180)) >= 0 && getPosition().compareTo(Angle.fromDegrees(360)) < 0) {
                setPosition(Angle.fromDegrees(0));
                flipDirection();
            } else {
                setPosition(nextMove);
            }
        }
//        if (isBetween0And180(this.getPosition())) {
//            System.out.println("AA");
//            hasCrossedOverTo0And180Once = true;
//            // If it is between 0 and 180 and it crosses over to 180 and 360 then teleport
//            if (isBetween180And360(nextMove)) {
//                setPosition(Angle.fromDegrees(360));
//                flipDirection();
//            }
//            // If it doesn't cross into 180 and 360 in the next position then move normally
//            else {
//                setPosition(nextMove);
//            }
//        } else if (isBetween180And360(this.getPosition())) {
//            System.out.println("BB");
//            // If it has already crossed over once, and it should flip then flip, else don't flip
//            if (hasCrossedOverTo0And180Once && isBetween0And180(nextMove)) {
//                setPosition(Angle.fromDegrees(0));
//                flipDirection();
//            }
//            // If it doesn't cross into 0 and 180 in the next position then move normally
//            else {
//                setPosition(nextMove);
//            }
//        }
    }

    private boolean isBetween180And360(Angle angle) {
        return angle.compareTo(Angle.fromDegrees(180)) >= 0 && angle.compareTo(Angle.fromDegrees(360)) <= 0;
    }

    private boolean isBetween0And180(Angle angle) {
        return angle.compareTo(Angle.fromDegrees(0)) >= 0 && angle.compareTo(Angle.fromDegrees(180)) <= 0;
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        return communicable;
    }

}
