package unsw.blackout;

import unsw.utils.Angle;

import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static unsw.blackout.CommunicationHelpers.getAllCommunicableEntitiesFromSourceRelay;
import static unsw.blackout.CommunicationHelpers.getAllPrimaryCommunicableEntities;

public abstract class Entity {
    private String id;
    private String type;
    private Angle position;
    private int maxRange;
    private double height;
    private int maxOutgoing = Integer.MAX_VALUE;
    private int maxIncoming = Integer.MAX_VALUE;
    private int maxStorageFiles = Integer.MAX_VALUE;
    private int maxStorageBytes = Integer.MAX_VALUE;

    public Entity(String id, String type, Angle position, int maxRange, double height) {
        this.id = id;
        this.type = type;
        this.position = MyAngleHelper.normaliseAngle(position);
        this.maxRange = maxRange;
        this.height = height;
    }

    public void setTransferringProperties(int maxIncoming, int maxOutgoing, int maxStorageBytes, int maxStorageFiles) {
        this.maxIncoming = maxIncoming;
        this.maxOutgoing = maxOutgoing;
        this.maxStorageBytes = maxStorageBytes;
        this.maxStorageFiles = maxStorageFiles;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Angle getPosition() {
        return MyAngleHelper.normaliseAngle(position);
    }

    public int getMaxRange() {
        return maxRange;
    }

    public double getHeight() {
        return height;
    }

    public void setPosition(Angle position) {
        this.position = MyAngleHelper.normaliseAngle(position);
    }

    public int getMaxOutgoing() {
        return maxOutgoing;
    }

    public int getMaxIncoming() {
        return maxIncoming;
    }

    public int getMaxStorageFiles() {
        return maxStorageFiles;
    }

    public int getMaxStorageBytes() {
        return maxStorageBytes;
    }

    /**
     * Gets all communicable entities from source regardless of compatability
     *
     * @param allEntities
     * @return all communicable entities
     */
    public ArrayList<Entity> getAllCommunicableEntities(ArrayList<Entity> allEntities) {
        HashSet<Entity> communicableEntitySet = new HashSet<>();
        ArrayList<Entity> startingCommunicable = getAllPrimaryCommunicableEntities(this, allEntities);
        communicableEntitySet.addAll(startingCommunicable);
        startingCommunicable
                .stream()
                .filter(entity -> entity.getType().equals("RelaySatellite"))
                .forEach(relaySatellite ->
                        communicableEntitySet.addAll(
                                getAllCommunicableEntitiesFromSourceRelay((RelaySatellite) relaySatellite, allEntities)
                        )
                );
        communicableEntitySet.remove(this);
        return new ArrayList<>(communicableEntitySet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
