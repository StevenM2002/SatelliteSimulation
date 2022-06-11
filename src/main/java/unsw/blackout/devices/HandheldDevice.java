package unsw.blackout.devices;

import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Communicable;
import unsw.blackout.satellites.Satellite;
import unsw.blackout.satellites.SatellitesAndDevices;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HandheldDevice extends Device {
    //HandheldDevice – phones, GPS devices, tablets.
    //Handhelds have a range of only 50,000 kilometres (50,000,000 metres)
    private static final String TYPE = "HandheldDevice";
    private static final int RANGE = 50000;
    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position, RANGE);
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        return communicable;
    }
}
