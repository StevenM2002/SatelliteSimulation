package unsw.blackout.satellites;

import unsw.blackout.Communicable;
import unsw.blackout.devices.Device;
import unsw.utils.Angle;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StandardSatellite extends Satellite {
    //StandardSatellite
    //Moves at a linear speed of 2,500 kilometres (2,500,000 metres) per minute
    //Supports handhelds and laptops only (along with other satellites)
    //Maximum range of 150,000 kilometres (150,000,000 metres)
    //Can store up to either 3 files or 80 bytes (whichever is smallest for the current situation).
    //Can receive 1 byte per minute and can send 1 byte per minute meaning it can only transfer 1 file at a time.
    private static final String TYPE = "StandardSatellite";
    private static final int VELOCITY = 2500;
    private static final int MAXRANGE = 150000;

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, TYPE, height, position, MAXRANGE, VELOCITY);
    }

    /**
     * Sets the new position of the satellite after moving for 1 minute
     */
    @Override
    public void move() {
        setPosition(getNextMove());
    }

    @Override
    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
        SatellitesAndDevices communicable = Communicable.getAllCommunicableEntities(this, new SatellitesAndDevices(satellites, devices));
        communicable.devices =
                communicable.devices
                        .stream()
                        .filter(device -> !device.getType().equals("DesktopDevice"))
                        .collect(Collectors.toCollection(ArrayList::new));
        return communicable;
    }

    // So what I might want to do is find all devices and satellites that are visible to the satellite first,
    // then I may just want to find all devices and satellites I can see through the relays
    // and then I can find all the devices and satellites I can see through the relays which connect to other relays
    // and so on...
    // and then filter out all devices and satellites this one cannot connect to.

    //Supports handhelds and laptops only (along with other satellites)
    //Maximum range of 150,000 kilometres (150,000,000 metres)

//    public SatellitesAndDevices getAllCommunicableEntities(ArrayList<Satellite> satellites, ArrayList<Device> devices) {
//        // All devices and satellites that are visible first
//        var detectedDevices =
//                devices
//                        .stream()
//                        .filter(device -> isVisible(device, MAXRANGE))
//                        .collect(Collectors.toCollection(ArrayList::new));
//        var detectedSatellites =
//                satellites
//                        .stream()
//                        .filter(satellite -> isVisible(satellite, MAXRANGE))
//                        .collect(Collectors.toCollection(ArrayList::new));
//        // then I may just want to find all devices and satellites I can see through the relays
//        var relays =
//                getCommunicableSatellites()
//                        .stream()
//                        .filter(satellite -> satellite.getType().equals("RelaySatellite"))
//                        .collect(Collectors.toCollection(ArrayList::new));
//        if (relays.isEmpty()) return null;
////        var foundSatellites =
//        return null;
//    }
}
