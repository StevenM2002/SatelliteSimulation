package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.Objects;

public class Device {

    private String deviceId;
    private String type;
    private Angle position;
    private ArrayList<File> files = new ArrayList<>();

    public Device(String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
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

    /**
     * Adds a file to the storage
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
