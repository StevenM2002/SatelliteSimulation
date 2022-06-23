package unsw.blackout;

import java.util.*;
import java.util.stream.Collectors;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {
    // data structure to store device or satellites or both?
//    DataStorageStructure dataSS = new DataStorageStructure();
    private ArrayList<Entity> allEntities = new ArrayList<>();
    private FileCentre fileCentre = new FileCentre();
    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
            case ("DesktopDevice"): {
                allEntities.add(new DesktopDevice(deviceId, position));
                break;
            }
            case ("LaptopDevice"): {
                allEntities.add(new LaptopDevice(deviceId, position));
                break;
            }
            case ("HandheldDevice"): {
                allEntities.add(new HandheldDevice(deviceId, position));
                break;
            }
        }
    }

    public void removeDevice(String deviceId) {
        allEntities.removeIf(entity -> entity.getId().equals(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
            case ("StandardSatellite"): {
                allEntities.add(new StandardSatellite(satelliteId, position, height));
                break;
            }
            case ("TeleportingSatellite"): {
                allEntities.add(new TeleportingSatellite(satelliteId, position, height));
                break;
            }
            case ("RelaySatellite"): {
                allEntities.add(new RelaySatellite(satelliteId, position, height));
                break;
            }
        }
    }

    public void removeSatellite(String satelliteId) {
        allEntities.removeIf(entity -> entity.getId().equals(satelliteId));
    }

    public List<String> listDeviceIds() {
        return allEntities.stream()
                .filter(entity -> entity instanceof Device)
                .map(entity -> entity.getId())
                .collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return allEntities.stream()
                .filter(entity -> entity instanceof Satellite)
                .map(entity -> entity.getId())
                .collect(Collectors.toList());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        for (Entity entity : allEntities) {
            if (entity.getId().equals(deviceId)) {
                fileCentre.addFileInstantly(entity, new File(filename, content));
            }
        };
    }

    public EntityInfoResponse getInfo(String id) {
        Entity entity = allEntities.stream()
                .filter(myEntity -> myEntity.getId().equals(id))
                .findFirst()
                .orElse(null);
        Map<String, FileInfoResponse> files = new HashMap<>();
        for (File file : fileCentre.getFiles(entity)) {
            files.put(file.getFileName(),
            new FileInfoResponse(file.getFileName(), file.getContent(), file.getSize(), file.isTransferred()));
        }
        return new EntityInfoResponse(entity.getId(), entity.getPosition(), entity.getHeight(), entity.getType(), files);
    }

    public void simulate() {
        // TODO: Task 2a)
        allEntities.stream().filter(entity -> entity instanceof Movement).forEach(entity -> ((Movement) entity).move());
        fileCentre.updateTransfer(allEntities);
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
        Entity entity = allEntities.stream().filter(myEntity -> myEntity.getId().equals(id)).findFirst().orElse(null);
        return entity.getAllCommunicableEntities(allEntities).stream().map(myEntity -> myEntity.getId()).collect(Collectors.toList());
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Entity fromEntity = allEntities.stream().filter(entity -> entity.getId().equals(fromId)).findFirst().orElse(null);
        Entity toEntity = allEntities.stream().filter(entity -> entity.getId().equals(toId)).findFirst().orElse(null);
        fileCentre.sendFile(fromEntity, toEntity, fileName);
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
