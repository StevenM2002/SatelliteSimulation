package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {
    // data structure to store device or satellites or both?
    DataStorageStructure dataSS = new DataStorageStructure();
    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
            case ("DesktopDevice") : {
                dataSS.addDevice(new DesktopDevice(deviceId, position));
                break;
            }
            case ("LaptopDevice") : {
                dataSS.addDevice(new LaptopDevice(deviceId, position));
                break;
            }
            case ("HandheldDevice") : {
                dataSS.addDevice(new HandheldDevice(deviceId, position));
                break;
            }
        }
    }

    public void removeDevice(String deviceId) {
        dataSS.removeDeviceById(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
            case ("StandardSatellite") : {
                dataSS.addSatellite(new StandardSatellite(satelliteId, height, position));
                break;
            }
            case ("TeleportingSatellite") : {
                dataSS.addSatellite(new TeleportingSatellite(satelliteId, height, position));
                break;
            }
            case ("RelaySatellite") : {
                dataSS.addSatellite(new RelaySatellite(satelliteId, height, position));
                break;
            }
        }
    }

    public void removeSatellite(String satelliteId) {
        dataSS.removeSatelliteById(satelliteId);
    }

    public List<String> listDeviceIds() {
        return dataSS.getDevices().stream().map(device -> device.getDeviceId()).collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return dataSS.getSatellites().stream().map(satellite -> satellite.getSatelliteId()).collect(Collectors.toList());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        dataSS.addFileToDevice(deviceId, filename, content);
    }

    public EntityInfoResponse getInfo(String id) {
        Satellite sat = dataSS.getSatelliteById(id);
        Device dev = dataSS.getDeviceById(id);
        if (sat != null) {
            return new EntityInfoResponse(sat.getSatelliteId(),
                                            sat.getPosition(),
                                            sat.getHeight(),
                                            sat.getType());
        } else {
            Map<String, FileInfoResponse> files = new HashMap<>();
            dev.getFiles().stream().forEach(file -> files.put(file.getFilename(),
                                            new FileInfoResponse(file.getFilename(),
                                                                    file.getContent(),
                                                                    file.getFileSize(),
                                                                    true)));
            return new EntityInfoResponse(dev.getDeviceId(),
                                            dev.getPosition(),
                                            MathsHelper.RADIUS_OF_JUPITER,
                                            dev.getType(),
                                            files);
        }
    }

    public void simulate() {
        // TODO: Task 2a)
        //This should run the simulation for a single minute.
        // This will include moving satellites around and later on transferring files between satellites and devices.
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        return new ArrayList<>();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
