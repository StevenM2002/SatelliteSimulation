package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;

public class HandheldDevice extends Device {
    private static final String TYPE = "HandheldDevice";
    private static final int MAX_RANGE = 50000;
    public HandheldDevice(String id, Angle position) {
        super(id, TYPE, position, MAX_RANGE);
    }

}
