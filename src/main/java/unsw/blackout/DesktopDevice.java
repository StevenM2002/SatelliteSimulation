package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    //DesktopDevice – desktop computers and servers.
    //Desktops have a range of only 200,000 kilometres (200,000,000 metres)
    private static final String TYPE = "DesktopDevice";
    private static final int RANGE = 200000;
    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position);
    }
}
