package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DesktopDevice extends Device {
    private static final String TYPE = "DesktopDevice";
    private static final int MAX_RANGE = 200000;
    public DesktopDevice(String id, Angle position) {
        super(id, TYPE, position, MAX_RANGE);
    }
    @Override
    public ArrayList<Entity> getAllCommunicableEntities(ArrayList<Entity> allEntities) {
        ArrayList<Entity> communicableEntities = super.getAllCommunicableEntities(allEntities);
        communicableEntities.removeIf(entity -> entity.getType().equals("StandardSatellite"));
        return communicableEntities;
    }
}
