package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class StandardSatellite extends Satellite {
    //StandardSatellite
    //Moves at a linear speed of 2,500 kilometres (2,500,000 metres) per minute
    //Supports handhelds and laptops only (along with other satellites)
    //Maximum range of 150,000 kilometres (150,000,000 metres)
    //Can store up to either 3 files or 80 bytes (whichever is smallest for the current situation).
    //Can receive 1 byte per minute and can send 1 byte per minute meaning it can only transfer 1 file at a time.
    private static final String TYPE = "StandardSatellite";
    private static final int VELOCITY = 2500;
    private static final int MAXRANGE = 150000;
    private static final int DIRECTION = MathsHelper.CLOCKWISE;
    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position);
    }
}
