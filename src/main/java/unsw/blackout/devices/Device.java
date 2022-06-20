package unsw.blackout.devices;

import unsw.blackout.files.File;
import unsw.blackout.files.Transferring;
import unsw.blackout.satellites.Satellite;
import unsw.blackout.satellites.SatellitesAndDevices;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Device {

    private String deviceId;
    private String type;
    private Angle position;
    private int maxRange;
    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<File> untransferredFiles = new ArrayList<>();

    public Device(String deviceId, String type, Angle position, int maxRange) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.maxRange = maxRange;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getType() {
        return type;
    }

    public Angle getPosition() {
        return position;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public abstract SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices);

    /**
     * Adds a file to the storage
     *
     * @param file
     */
    public void addFile(File file) {
        files.add(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return deviceId.equals(device.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId);
    }

    @Override
    public String toString() {
        return "Device: " + "deviceId: " + deviceId + ", type: " + type + ", position: " + position;
    }
}
