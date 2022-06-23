package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StandardSatellite extends Satellite {

    private static final int VELOCITY = 2500;
    private static final int MAX_RANGE = 150000;
    private static final String TYPE = "StandardSatellite";

    private static final int MAX_OUTGOING_BANDWIDTH = 1;
    private static final int MAX_INCOMING_BANDWIDTH = 1;
    private static final int MAX_STORAGE_BYTES = 80;
    private static final int MAX_STORAGE_FILES = 3;

    public StandardSatellite(String id, Angle position, double height) {
        super(id, TYPE, position, MAX_RANGE, height, VELOCITY);
        setTransferringProperties(MAX_INCOMING_BANDWIDTH, MAX_OUTGOING_BANDWIDTH, MAX_STORAGE_BYTES, MAX_STORAGE_FILES);
    }


    @Override
    public ArrayList<Entity> getAllCommunicableEntities(ArrayList<Entity> allEntities) {
        ArrayList<Entity> communicable = super.getAllCommunicableEntities(allEntities);
        communicable.removeIf(entity -> entity.getType().equals("DesktopDevice"));
        return communicable;
    }
    @Override
    public void move() {
        setPosition(getNextMove(getPosition(), VELOCITY, getHeight(), getDirection()));
    }
}
