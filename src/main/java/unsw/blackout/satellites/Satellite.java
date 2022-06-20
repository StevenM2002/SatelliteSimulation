package unsw.blackout.satellites;

import unsw.blackout.devices.Device;
import unsw.blackout.files.File;
import unsw.blackout.files.Transferring;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import java.util.ArrayList;
import java.util.Objects;

import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;
import static unsw.utils.MathsHelper.CLOCKWISE;

public abstract class Satellite {
    private String type;
    private int velocity;
    private int maxRange;
    private String satelliteId;
    private double height;
    private Angle position;
    private int direction = CLOCKWISE;
    private ArrayList<File> files = new ArrayList<>();

    public Satellite(String satelliteId, String type, double height, Angle position, int maxRange, int velocity) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
        this.maxRange = maxRange;
        this.velocity = velocity;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return Angle.normaliseAngle(position);
    }

    public void setPosition(Angle position) {
        this.position = Angle.normaliseAngle(position);
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public abstract void move();

    /**
     * Move the satellite in the direction of it's current direction
     *
     * @param
     */
    public Angle getNextMove() {
        Angle offset = Angle.fromRadians((double) velocity / getHeight());
        Angle nextPosition;
        if (getDirection() == CLOCKWISE) {
            nextPosition = position.subtract(offset);
        } else {
            nextPosition = position.add(offset);
        }
        return Angle.normaliseAngle(nextPosition);
    }

    public void flipDirection() {
        if (direction == CLOCKWISE) {
            direction = ANTI_CLOCKWISE;
        } else {
            direction = CLOCKWISE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() != this.getClass()) return false;
        Satellite satellite = (Satellite) o;
        return satelliteId.equals(satellite.satelliteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(satelliteId);
    }

    @Override
    public String toString() {
        return "Satellite: " + "type: " + type + ", satelliteId: " + satelliteId + ", height: " + height + ", position: " + position;
    }

    public abstract SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices);

}
