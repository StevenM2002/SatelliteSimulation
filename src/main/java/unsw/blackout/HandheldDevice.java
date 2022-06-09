package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    //HandheldDevice – phones, GPS devices, tablets.
    //Handhelds have a range of only 50,000 kilometres (50,000,000 metres)
    private static final String TYPE = "HandheldDevice";
    private static final int RANGE = 50000;
    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position);
    }
}
