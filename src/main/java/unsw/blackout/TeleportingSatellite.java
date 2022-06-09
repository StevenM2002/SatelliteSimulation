package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class TeleportingSatellite extends Satellite {
    //TeleportingSatellite
    //Moves at a linear velocity of 1,000 kilometres (1,000,000 metres) per minute
    //Supports all devices
    //Maximum range of 200,000 kilometres (200,000,000 metres)
    //Can receive 15 bytes per minute and can send 10 bytes per minute.
    //Can store up to 200 bytes and as many files as fits into that space.
    //When the position of the satellite reaches θ = 180, the satellite teleports to θ = 0 and changes direction.
    //If a file transfer is in progress when the satellite teleports, the rest of the file is instantly downloaded, however all "t" bytes are removed from the remaining bytes to be sent
    //Teleporting satellites start by moving anticlockwise.
    private static final String TYPE = "TeleportingSatellite";
    private static final int VELOCITY = 1000;
    private static final int MAXRANGE = 200000;
    private static final int DIRECTION = MathsHelper.ANTI_CLOCKWISE;
    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position);
    }
}
