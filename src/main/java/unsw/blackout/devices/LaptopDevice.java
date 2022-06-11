package unsw.blackout.devices;

import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Communicable;
import unsw.blackout.satellites.Satellite;
import unsw.blackout.satellites.SatellitesAndDevices;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LaptopDevice extends Device {
    //LaptopDevice – laptop computers.
    //Laptops have a range of only 100,000 kilometres (100,000,000 metres)
    private static final String TYPE = "LaptopDevice";
    private static final int RANGE = 100000;
    public LaptopDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position, RANGE);
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        return communicable;
    }
}
