package unsw.blackout.satellites;

import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Satellite;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position, MAXRANGE, VELOCITY);
        setDirection(ANTI_CLOCKWISE);
    }
    @Override
    public void move() {
        Angle nextPosition = getNextMove(VELOCITY);
        //When the position of the satellite reaches θ = 180, the satellite teleports to θ = 0 and changes direction.
        if ((getDirection() == ANTI_CLOCKWISE && nextPosition.compareTo(Angle.fromDegrees(180)) >= 0) ||
                (getDirection() == CLOCKWISE && nextPosition.compareTo(Angle.fromDegrees(180)) <= 0)) {
            setPosition(Angle.fromDegrees(0));
            flipDirection();
            //If a device is transferring to a satellite that teleports mid-transfer,
            // all 't' bytes will be deleted from the device and the transfer will be cancelled
            //If a teleporting satellite is transferring to a device and teleports mid-transfer,
            // the download is completed instantly and all 't' bytes are removed from the device but kept on the satellite
        } else {
            setPosition(nextPosition);
        }
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        return communicable;
    }

}
