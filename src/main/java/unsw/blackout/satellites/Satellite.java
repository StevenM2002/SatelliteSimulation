package unsw.blackout.satellites;

import unsw.blackout.devices.Device;
import unsw.blackout.files.File;
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
    private ArrayList<Satellite> communicableSatellites = new ArrayList<>();
    private ArrayList<Device> communicableDevices = new ArrayList<>();
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
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
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

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public void addFile(File file) {
        files.add(file);
    }
    public void addCommunicableSatellite(Satellite satellite) {
        communicableSatellites.add(satellite);
    }
    public ArrayList<Satellite> getCommunicableSatellites() {
        return communicableSatellites;
    }

    public void setCommunicableSatellites(ArrayList<Satellite> communicableSatellites) {
        this.communicableSatellites = communicableSatellites;
    }

    public void addCommunicableDevice(Device device) {
        communicableDevices.add(device);
    }

    public ArrayList<Device> getCommunicableDevice() {
        return communicableDevices;
    }

    public void setCommunicableDevice(ArrayList<Device> communicableDevice) {
        this.communicableDevices = communicableDevice;
    }

    public abstract void move();

    /**
     * Move the satellite in the direction of it's current direction
     * @param velocity
     */
    public Angle getNextMove(int velocity) {
        Angle offset = Angle.fromRadians(velocity / getHeight());
        Angle nextPosition;
        if (getDirection() == CLOCKWISE) {
            nextPosition = position.subtract(offset);
        } else {
            nextPosition = position.add(offset);
        }
        return nextPosition;
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
