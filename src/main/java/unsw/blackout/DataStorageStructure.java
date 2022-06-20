package unsw.blackout;

import unsw.blackout.devices.Device;
import unsw.blackout.files.File;
import unsw.blackout.satellites.Satellite;

import java.util.ArrayList;

public class DataStorageStructure {
    private ArrayList<Device> devices = new ArrayList<>();
    private ArrayList<Satellite> satellites = new ArrayList<>();
    private ArrayList<Device> removedDevices = new ArrayList<>();
    private ArrayList<Satellite> removedSatellites = new ArrayList<>();

    /**
     * Gets a satellite by its satelliteId
     *
     * @param satelliteId
     * @return Satellite if found else null
     */
    public Satellite getSatelliteById(String satelliteId) {
        for (var satellite : satellites) {
            if (satellite.getSatelliteId().equals(satelliteId)) {
                return satellite;
            }
        }
        return null;
    }

    /**
     * Removes the first satellite found by satelliteId
     *
     * @param satelliteId
     */
    public void removeSatelliteById(String satelliteId) {
        for (int i = 0; i < satellites.size(); i++) {
            if (satellites.get(i).getSatelliteId().equals(satelliteId)) {
                removedSatellites.add(satellites.get(i));
                satellites.remove(i);
                break;
            }
        }
    }

    /**
     * Removes the first device found by deviceId
     *
     * @param deviceId
     */
    public void removeDeviceById(String deviceId) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getDeviceId().equals(deviceId)) {
                removedDevices.add(devices.get(i));
                devices.remove(i);
                break;
            }
        }
    }

    /**
     * Adds a file to device
     *
     * @param deviceId
     * @param filename
     * @param content
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        Device device = this.getDeviceById(deviceId);
        if (device == null) return;
        device.addFile(new File(filename, content, true));
    }

    /**
     * Gets a device by its deviceId
     *
     * @param deviceId
     * @return Device if found else null
     */
    public Device getDeviceById(String deviceId) {
        for (var device : devices) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Adds a device to storage
     *
     * @param device
     */
    public void addDevice(Device device) {
        devices.add(device);
    }

    /**
     * Adds a satellite to storage
     *
     * @param satellite
     */
    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public ArrayList<Satellite> getSatellites() {
        return satellites;
    }

}
