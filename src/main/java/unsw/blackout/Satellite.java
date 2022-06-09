package unsw.blackout;

import unsw.utils.Angle;

import java.util.Objects;

public class Satellite {
    private String type;
    private String satelliteId;
    private double height;
    private Angle position;
    public Satellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
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


}
