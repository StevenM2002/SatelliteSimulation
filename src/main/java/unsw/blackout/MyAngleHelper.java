package unsw.blackout;

import unsw.utils.Angle;

public class MyAngleHelper {
    public static Angle normaliseAngle(Angle givenAngle) {
        Angle myAngle = Angle.fromRadians(givenAngle.toRadians());
        while (myAngle.toRadians() < 0) {
            myAngle = myAngle.add(Angle.fromDegrees(360));
        }
        while (myAngle.toRadians() >= 2 * Math.PI) {
            myAngle = myAngle.subtract(Angle.fromDegrees(360));
        }
        return myAngle;
    }
}
