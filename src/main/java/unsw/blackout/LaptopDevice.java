package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;

public class LaptopDevice extends Device {
    private static final String TYPE = "LaptopDevice";
    private static final int MAX_RANGE = 100000;
    public LaptopDevice(String id, Angle position) {
        super(id, TYPE, position, MAX_RANGE);
    }
}
