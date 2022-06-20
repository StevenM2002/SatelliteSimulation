package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.blackout.satellites.StandardSatellite;
import unsw.blackout.satellites.TeleportingSatellite;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.NEGATIVE_DIRECTION;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.ArrayList;
import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testAllDeviceTypesToAllSatelliteTypesCommunicable() {
        var controller = new BlackoutController();
        controller.createSatellite("S1", "StandardSatellite", 73787, Angle.fromDegrees(118));
        controller.createSatellite("S2", "RelaySatellite", 75635, Angle.fromDegrees(88));
        controller.createSatellite("S3", "TeleportingSatellite", 74404, Angle.fromDegrees(69));
        controller.createSatellite("S4", "TeleportingSatellite", 76051, Angle.fromDegrees(103));
        controller.createDevice("D1", "HandheldDevice", Angle.fromDegrees(108));
        controller.createDevice("D2", "LaptopDevice", Angle.fromDegrees(87));
        controller.createDevice("D3", "DesktopDevice", Angle.fromDegrees(64));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3", "S4", "D1", "D2"), controller.communicableEntitiesInRange("S1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S3", "S4", "D1", "D2", "D3"), controller.communicableEntitiesInRange("S2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2", "S4", "D1", "D2", "D3"), controller.communicableEntitiesInRange("S3"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2", "S3", "D1", "D2", "D3"), controller.communicableEntitiesInRange("S4"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2", "S3", "S4"), controller.communicableEntitiesInRange("D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2", "S3", "S4"), controller.communicableEntitiesInRange("D2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3", "S4"), controller.communicableEntitiesInRange("D3"));
        // Testing lone relay satellite
        controller.createSatellite("S5", "RelaySatellite", 104532, Angle.fromDegrees(359));
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.communicableEntitiesInRange("S5"));
    }

    @Test
    public void testDesktopDeviceToAllSatelliteTypesCommunicable() {
        var controller = new BlackoutController();
        controller.createSatellite("S1", "StandardSatellite", 75186, Angle.fromDegrees(91));
        controller.createSatellite("S2", "TeleportingSatellite", 75415, Angle.fromDegrees(82));
        controller.createSatellite("S3", "RelaySatellite", 74560, Angle.fromDegrees(102));
        controller.createDevice("D1", "DesktopDevice", Angle.fromDegrees(92));
        controller.createDevice("D2", "DesktopDevice", Angle.fromDegrees(102));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3"), controller.communicableEntitiesInRange("D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3"), controller.communicableEntitiesInRange("D2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3"), controller.communicableEntitiesInRange("S1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("D1", "D2", "S1", "S2"), controller.communicableEntitiesInRange("S3"));
        assertListAreEqualIgnoringOrder(Arrays.asList("D1", "D2", "S1", "S3"), controller.communicableEntitiesInRange("S2"));
    }

    @Test
    public void testRelayEntitiesInRange() {
        var controller = new BlackoutController();
        controller.createSatellite("S1", "RelaySatellite", 74664, Angle.fromDegrees(167));
        controller.createSatellite("S2", "RelaySatellite", 75486, Angle.fromDegrees(149));
        controller.createSatellite("S3", "RelaySatellite", 79306, Angle.fromDegrees(116));
        controller.createSatellite("S4", "RelaySatellite", 80755, Angle.fromDegrees(84));
        controller.createSatellite("S5", "RelaySatellite", 81691, Angle.fromDegrees(52));
        controller.createSatellite("S6", "RelaySatellite", 77086, Angle.fromDegrees(25));
        controller.createSatellite("S7", "RelaySatellite", 74029, Angle.fromDegrees(8));
        // Satellite 1 will not communicate with satellite 8 but will with all other satellites
        controller.createSatellite("S8", "RelaySatellite", 73366, Angle.fromDegrees(270));
        controller.createDevice("D1", "HandheldDevice", Angle.fromDegrees(0));
        // Following is not communicable device except to S8
        controller.createDevice("D2", "HandheldDevice", Angle.fromDegrees(260));
        assertListAreEqualIgnoringOrder(Arrays.asList("S2", "S3", "S4", "S5", "S6", "S7", "D1"), controller.communicableEntitiesInRange("S1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2", "S3", "S4", "S5", "S6", "S7"), controller.communicableEntitiesInRange("D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S8"), controller.communicableEntitiesInRange("D2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("D2"), controller.communicableEntitiesInRange("S8"));
    }

    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite2"), controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"), controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }

    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(180));

        // moves in negative direction
        assertEquals(
                new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"),
                controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(24);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.87), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingMovement() {
        // Test for expected teleportation movement behaviour
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));

        // Satellite position should increase if going anticlockwise (except from 360 -> 0)
        // Verify that Satellite1 is going in a anticlockwise direction (default)
        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 250 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        // Verify that Satellite1 is now at theta=0
        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
    }

    private double getPositionDegreesWrapper(BlackoutController controller, String satId) {
        var ans = controller.getInfo(satId).getPosition().toDegrees();
        return round2DP(ans);
    }

    private double getPositionRadiansWrapper(BlackoutController controller, String satId) {
        var ans = (Math.toRadians(controller.getInfo(satId).getPosition().toDegrees()));
        if (Math.toDegrees(ans) < 0) {
            while (Math.toDegrees(ans) < 0) {
                ans = 2 * Math.PI + ans;
            }
        }
        return ans;
    }

    private double round2DP(double num) {
        return Math.round(num * 100.0) / 100.0;
    }
    private double normaliseAngle(double degrees) {
        return Angle.normaliseAngle(Angle.fromDegrees(degrees)).toDegrees();
    }

    @Test
    public void simpleStandardSatelliteMovement() {
        var controller = new BlackoutController();
        controller.createSatellite("S1", "StandardSatellite", 400 + RADIUS_OF_JUPITER, Angle.fromDegrees(358));
        controller.createSatellite("S2", "StandardSatellite", 400 + RADIUS_OF_JUPITER, Angle.fromDegrees(182));
        controller.createSatellite("S3", "StandardSatellite", 400 + RADIUS_OF_JUPITER, Angle.fromDegrees(247));
        controller.createSatellite("S4", "StandardSatellite", 400 + RADIUS_OF_JUPITER, Angle.fromDegrees(67));
        double increase = 2.0372267324132185;
        controller.simulate(1);
        assertEquals(round2DP(358 - increase), getPositionDegreesWrapper(controller, "S1"));
        assertEquals(round2DP(182 - increase), getPositionDegreesWrapper(controller, "S2"));
        assertEquals(round2DP(247 - increase), getPositionDegreesWrapper(controller, "S3"));
        assertEquals(round2DP(67 - increase), getPositionDegreesWrapper(controller, "S4"));
        controller.simulate(120);
        assertEquals(round2DP(358 - increase * 121), getPositionDegreesWrapper(controller, "S1"));
        assertEquals(round2DP(182 - increase * 121 + 360), getPositionDegreesWrapper(controller, "S2"));
        assertEquals(round2DP(247 - increase * 121), getPositionDegreesWrapper(controller, "S3"));
        assertEquals(round2DP(67 - increase * 121 + 360), getPositionDegreesWrapper(controller, "S4"));
        controller.createSatellite("S5", "StandardSatellite", 150 + RADIUS_OF_JUPITER, Angle.fromRadians(0.2617993877991494));
        controller.simulate(1);
        assertEquals(0.22611619743646547, getPositionRadiansWrapper(controller, "S5"));
        controller.simulate(200);
        assertEquals(5.6558487392588805, getPositionRadiansWrapper(controller, "S5"));
    }

    @Test
    public void simpleTeleportingSatelliteMovement() {
        var con = new BlackoutController();
        con.createSatellite("S0", "TeleportingSatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(136));
        con.simulate(54);
        assertEquals(0, getPositionDegreesWrapper(con, "S0"));

        //70031
        con.createSatellite("S1", "TeleportingSatellite", 120 + RADIUS_OF_JUPITER, Angle.fromDegrees(35));
        con.createSatellite("S2", "TeleportingSatellite", 120 + RADIUS_OF_JUPITER, Angle.fromDegrees(136));
        con.createSatellite("S3", "TeleportingSatellite", 120 + RADIUS_OF_JUPITER, Angle.fromDegrees(222));
        con.createSatellite("S4", "TeleportingSatellite", 120 + RADIUS_OF_JUPITER, Angle.fromDegrees(331));
        double increase = 0.8181488128554829;
        con.simulate(1);
        assertEquals(round2DP(35 + increase), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(331 + increase), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 26), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase * 26), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 26), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(331 + increase * 26), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 51), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase * 51), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 51), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 51)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(24);
        assertEquals(round2DP(35 + increase * 75), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 21), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 75), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 75)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 100), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 46), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 100), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 100)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 125), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 71), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 125), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 125)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 150), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 96), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 + increase * 150), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 150)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 175), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 121), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(normaliseAngle(222 + increase * 175)), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 175)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(3);
        assertEquals(round2DP(0), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(360 - increase * 124), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(normaliseAngle(222 + increase * 178)), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(331 + increase * 178)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(97);
        assertEquals(round2DP(360 - increase * 97), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(0), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(normaliseAngle(222 + increase * 275)), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(360 - increase * 19)), getPositionDegreesWrapper(con, "S4"));
        con.simulate(1);
        assertEquals(round2DP(360 - increase * 98), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(0 + increase), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(normaliseAngle(222 + increase * 276)), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(normaliseAngle(360 - increase * 20)), getPositionDegreesWrapper(con, "S4"));
    }

    @Test
    public void simpleRelaySatelliteMovement() {
        var con = new BlackoutController();
        con.createSatellite("S1", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(35));
        con.createSatellite("S2", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(136));
        con.createSatellite("S3", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(222));
        con.createSatellite("S4", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(331));
        double increase = Angle.fromRadians(1500 / (150 + RADIUS_OF_JUPITER)).toDegrees();
        con.simulate(1);
        assertEquals(round2DP(35 + increase), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 - increase), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(331 - increase), getPositionDegreesWrapper(con, "S4"));
        con.simulate(25);
        assertEquals(round2DP(35 + increase * 26), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase * 26), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 - increase * 26), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(331 - increase * 26), getPositionDegreesWrapper(con, "S4"));
        con.simulate(20);
        assertEquals(round2DP(35 + increase * 46), getPositionDegreesWrapper(con, "S1"));
        assertEquals(round2DP(136 + increase * 46 - increase * 2), getPositionDegreesWrapper(con, "S2"));
        assertEquals(round2DP(222 - increase * 46), getPositionDegreesWrapper(con, "S3"));
        assertEquals(round2DP(331 - increase * 46), getPositionDegreesWrapper(con, "S4"));
    }
    @Test
    public void testBordersRegionRelaySatellite() {
        var con = new BlackoutController();
        con.createSatellite("1", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(139));
        con.createSatellite("2", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(140));
        con.createSatellite("3", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(191));
        con.createSatellite("4", "RelaySatellite", 150 + RADIUS_OF_JUPITER, Angle.fromDegrees(190));
        double increase = Angle.fromRadians(1500 / (150 + RADIUS_OF_JUPITER)).toDegrees();
        con.simulate(1);
        assertEquals(round2DP(139 + increase), getPositionDegreesWrapper(con, "1"));
        assertEquals(round2DP(140 - increase), getPositionDegreesWrapper(con, "2"));
        assertEquals(round2DP(191 - increase), getPositionDegreesWrapper(con, "3"));
        assertEquals(round2DP(190 - increase), getPositionDegreesWrapper(con, "4"));
        con.simulate(1);
        assertEquals(round2DP(139 + increase * 2), getPositionDegreesWrapper(con, "1"));
        assertEquals(round2DP(140), getPositionDegreesWrapper(con, "2"));
        assertEquals(round2DP(191 - increase * 2), getPositionDegreesWrapper(con, "3"));
        assertEquals(round2DP(190 - increase * 2), getPositionDegreesWrapper(con, "4"));
        con.simulate(41);
        assertEquals(round2DP(139 + increase * 43 - increase * 2), getPositionDegreesWrapper(con, "1"));
        assertEquals(round2DP(140 + increase * 41), getPositionDegreesWrapper(con, "2"));
        assertEquals(round2DP(191 - increase * 42 + 1 * increase), getPositionDegreesWrapper(con, "3"));
        assertEquals(round2DP(190 - increase * 41 + increase * 2), getPositionDegreesWrapper(con, "4"));
    }
}
