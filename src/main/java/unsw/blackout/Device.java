package unsw.blackout;

import unsw.utils.Angle;

import java.util.ArrayList;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public abstract class Device extends Entity {
    private static final double HEIGHT = RADIUS_OF_JUPITER;
    public Device(String id, String type, Angle position, int maxRange) {
        super(id, type, position, maxRange, HEIGHT);
    }
    @Override
    public ArrayList<Entity> getAllCommunicableEntities(ArrayList<Entity> allEntities) {
        ArrayList<Entity> communicableEntities = super.getAllCommunicableEntities(allEntities);
        communicableEntities.removeIf(entity -> entity instanceof Device);
        return communicableEntities;
    }
}
