package unsw.blackout.satellites;

import unsw.blackout.devices.Device;

import java.util.ArrayList;
import java.util.stream.Collectors;

// This is just a return class
public class SatellitesAndDevices {
    public ArrayList<Satellite> satellites;
    public ArrayList<Device> devices;

    public SatellitesAndDevices(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        this.satellites = satellites;
        this.devices = devices;
    }

    public SatellitesAndDevices() {
        this.satellites = new ArrayList<>();
        this.devices = new ArrayList<>();
    }

    @Override
    public String toString() {
        return satellites.stream().map(satellite -> satellite.getSatelliteId()).collect(Collectors.toList()).toString() +
                devices.stream().map(device -> device.getDeviceId()).collect(Collectors.toList()).toString();
    }
}
