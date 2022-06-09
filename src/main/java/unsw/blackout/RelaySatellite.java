package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class RelaySatellite extends Satellite {
    //RelaySatellite
    //Moves at a linear velocity of 1,500 kilometres (1,500,000 metres) per minute
    //Supports all devices
    //Max range of 300,000 kilometres (300,000,000 metres)
    //Cannot store any files and has no bandwidth limits
    //Devices/Satellites cannot transfer files directly to a relay but instead a relay can be automatically used by
    // satellites/devices to send to their real target.
    //For example if a HandheldDevice (range 50,000km) is 200,000km away from a StandardSatellite that it wishes to
    // communicate with, it is able to communicate to the satellite through the use of the relay if the relay is within 50,000km
    // of the device and the satellite is within 300,000km (the range of the relay) of the relay.
    //Files being transferred through a relay should not show up in the relay's list of files.
    //Only travels in the region between 140° and 190°
    //When it reaches one side of the region its direction reverses and it travels in the opposite direction.
    //This 'correction' will only apply on the next minute. This means that it can briefly exceed this boundary.
    // There is a unit test that details this behaviour quite well called testRelaySatelliteMovement in Task2ExampleTests.java
    //You can either do the radian maths here yourself or use the functions in src/unsw/utils/Angle.java to do comparisons.
    //In the case that the satellite doesn't start in the region [140°, 190°], it should choose whatever direction gets
    // it to the region [140°, 190°] in the shortest amount of time.
    //As a hint (and to prevent you having to do maths) this 'threshold' angle is 345°; if a relay satellite starts on
    // the threshold 345° it should take the positive direction.
    //Relay satellites don't allow you to ignore satellite requirements (other than visibility/range), for example
    // you can't send a file from a Desktop Device to a Standard Satellite due to the fact that a Standard Satellite
    // doesn't support Desktops. This should hold even if a Relay is used along the way.
    //HINT: because there are no bandwidth limits and you don't have to show any tracking of files that go through the relay.
    // Keep it simple! Don't over-engineer a solution for this one. You'll notice that the frontend when drawing
    // connections that utilise relays don't go through the relay as shown below.
    private static final String TYPE = "RelaySatellite";
    private static final int VELOCITY = 1500;
    private static final int MAXRANGE = 300000;
    private static final int DIRECTION = MathsHelper.CLOCKWISE;

    public RelaySatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position);
    }
}
