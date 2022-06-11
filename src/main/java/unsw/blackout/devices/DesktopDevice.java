package unsw.blackout.devices;

import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Communicable;
import unsw.blackout.satellites.Satellite;
import unsw.blackout.satellites.SatellitesAndDevices;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DesktopDevice extends Device {
    //DesktopDevice – desktop computers and servers.
    //Desktops have a range of only 200,000 kilometres (200,000,000 metres)
    private static final String TYPE = "DesktopDevice";
    private static final int RANGE = 200000;
    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, TYPE, position, RANGE);
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        communicable.satellites.removeIf(satellite -> satellite.getType().equals("StandardSatellite"));
        return communicable;
    }
}
