package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    //LaptopDevice – laptop computers.
    //Laptops have a range of only 100,000 kilometres (100,000,000 metres)
    private static final String TYPE = "LaptopDevice";
    private static final int RANGE = 100000;
    public LaptopDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position);
    }
}
