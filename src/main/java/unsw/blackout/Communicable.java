package unsw.blackout;

import unsw.blackout.devices.Device;
import unsw.blackout.satellites.Satellite;
import unsw.blackout.satellites.SatellitesAndDevices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static unsw.utils.MathsHelper.getDistance;
import static unsw.utils.MathsHelper.isVisible;

public class Communicable {
    /**
     * Gets all communicable entities ignoring any compatability issues and removing source entity from returned value
     *
     * @param source
     * @param allEntities
     * @param <T>         Either Device or Satellite
     * @return An object containing all communicable entities to source
     */
    public static <T> SatellitesAndDevices getAllCommunicableEntities(T source, SatellitesAndDevices allEntities) {

        SatellitesAndDevices primaryCommunicable = getAllPrimaryCommunicableEntities(source, allEntities);
        ArrayList<Satellite> allRelays = primaryCommunicable.satellites
                .stream()
                .filter(satellite -> satellite.getType().equals("RelaySatellite"))
                .collect(Collectors.toCollection(ArrayList::new));
        SatellitesAndDevices allCommunicable;
        if (!allRelays.isEmpty()) {
            HashSet<Satellite> setOfAllRelays = new HashSet<>();
            for (var relay : allRelays) {
                setOfAllRelays.addAll(getAllCommunicableRelays(relay, allEntities.satellites));
            }
            ArrayList<Satellite> chainOfAllRelays = new ArrayList<>(setOfAllRelays);
            allCommunicable = getSet(getAllCommunicableFromRelays(chainOfAllRelays, allEntities));
        } else {
            allCommunicable = getSet(primaryCommunicable);
        }
        allCommunicable.devices.remove(source);
        allCommunicable.satellites.remove(source);
        return allCommunicable;
    }

    /**
     * Removes any duplicates
     *
     * @param toSet
     * @return set with no duplicates
     */
    private static SatellitesAndDevices getSet(SatellitesAndDevices toSet) {
        HashSet<Satellite> satellites = new HashSet<>();
        HashSet<Device> devices = new HashSet<>();
        satellites.addAll(toSet.satellites);
        devices.addAll(toSet.devices);
        return new SatellitesAndDevices(new ArrayList<>(satellites), new ArrayList<>(devices));
    }

    /**
     * Gets all the primary communicable entities i.e. not through relays
     *
     * @param source
     * @param allEntities
     * @param <T>         can either be Device or Satellite
     * @return Communicable satellites and devices if source is Satellite, if source is Device, only communicable satellites
     */
    private static <T> SatellitesAndDevices getAllPrimaryCommunicableEntities(T source, SatellitesAndDevices allEntities) {
        // Satellite to satellite
        if (source instanceof Satellite) {
            var communicableSatellites =
                    allEntities.satellites
                            .stream()
                            .filter(satellite -> isCommunicable((Satellite) source, satellite))
                            .collect(Collectors.toCollection(ArrayList::new));
            // Satellite to device
            var communicableDevices =
                    allEntities.devices
                            .stream()
                            .filter(device -> isCommunicable((Satellite) source, device))
                            .collect(Collectors.toCollection(ArrayList::new));
            return new SatellitesAndDevices(communicableSatellites, communicableDevices);
        } else if (source instanceof Device) {
            // Only need device to satellite as devices cannot talk to other devices
            var communicableSatellites =
                    allEntities.satellites
                            .stream()
                            .filter(satellite -> isCommunicable((Device) source, satellite))
                            .collect(Collectors.toCollection(ArrayList::new));
            return new SatellitesAndDevices(communicableSatellites, new ArrayList<>());
        }
        return null;
    }

    /**
     * Gets all communicable devices and satellites from a chain of relays
     *
     * @param relays      chain of relays which can all communicate with each other
     * @param allEntities
     * @return All communicable devices and satellites from the chain of relays including the relays themselves
     */
    private static SatellitesAndDevices getAllCommunicableFromRelays(ArrayList<Satellite> relays, SatellitesAndDevices allEntities) {
        SatellitesAndDevices communicableEntities = new SatellitesAndDevices();
        HashSet<Satellite> uniqueSatellites = new HashSet<>();
        HashSet<Device> uniqueDevices = new HashSet<>();
        for (var relay : relays) {
            var communicableSatellites = getAllCommunicableSatellites(relay, allEntities.satellites);
            var communicableDevices = getAllCommunicableDevices(relay, allEntities.devices);
            uniqueDevices.addAll(communicableDevices);
            uniqueSatellites.addAll(communicableSatellites);
        }
        communicableEntities.devices.addAll(uniqueDevices);
        communicableEntities.satellites.addAll(uniqueSatellites);
        communicableEntities.satellites.addAll(relays);
        return communicableEntities;
    }

    /**
     * Gets all satellites that the source satellite can communicate with regardless of compatability
     *
     * @param source
     * @param allSatellites
     * @return satellites that are communicable from source
     */
    private static ArrayList<Satellite> getAllCommunicableSatellites(Satellite source, ArrayList<Satellite> allSatellites) {
        return allSatellites
                .stream()
                .filter(satellite -> isCommunicable(source, satellite))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all devices that the source satellite can communicate to regardless of compatability
     *
     * @param source
     * @param allDevices
     * @return Devices that are communicable from source
     */
    private static ArrayList<Device> getAllCommunicableDevices(Satellite source, ArrayList<Device> allDevices) {
        return allDevices
                .stream()
                .filter(device -> isCommunicable(source, device))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all the relays that are communicable to the source
     *
     * @param source        relay
     * @param allSatellites
     * @return all relays that are communicable with the source relay
     */
    private static HashSet<Satellite> getAllCommunicableRelays(Satellite source, ArrayList<Satellite> allSatellites) {
        HashSet<Satellite> visitedRelays = new HashSet<>();
        var allRelays = allSatellites
                .stream()
                .filter(satellite -> satellite.getType().equals("RelaySatellite"))
                .collect(Collectors.toCollection(ArrayList::new));
        doRelayDfs(source, allRelays, visitedRelays);
        return visitedRelays;
    }

    private static void doRelayDfs(Satellite sourceRelay, ArrayList<Satellite> allRelays,
                                   HashSet<Satellite> visitedRelays) {
        if (sourceRelay == null) return;
        visitedRelays.add(sourceRelay);
        var nextRelay = getNextRelay(sourceRelay, allRelays, visitedRelays);
        doRelayDfs(nextRelay, allRelays, visitedRelays);
    }

    /**
     * Gets the next relay for dfs
     *
     * @param source
     * @param allRelays
     * @param visitedRelays
     * @return the next relay to dfs else if none found, returns null
     */
    private static Satellite getNextRelay(Satellite source, ArrayList<Satellite> allRelays, HashSet<Satellite> visitedRelays) {
        for (var relay : allRelays) {
            if (isCommunicable(source, relay) && !visitedRelays.contains(relay)) {
                return relay;
            }
        }
        return null;
    }

    /**
     * Is communicable from satellite to satellite
     *
     * @param source
     * @param dest
     * @return if it is communicable
     */
    private static boolean isCommunicable(Satellite source, Satellite dest) {
        return getDistance(source.getHeight(), source.getPosition(), dest.getHeight(), dest.getPosition()) <= source.getMaxRange() &&
                isVisible(source.getHeight(), source.getPosition(), dest.getHeight(), dest.getPosition());
    }

    /**
     * Is communicable from satellite to device
     *
     * @param source
     * @param dest
     * @return if it is communicable
     */
    private static boolean isCommunicable(Satellite source, Device dest) {
        return getDistance(source.getHeight(), source.getPosition(), dest.getPosition()) <= source.getMaxRange() &&
                isVisible(source.getHeight(), source.getPosition(), dest.getPosition());
    }

    /**
     * Is communicable from device to satellite
     *
     * @param source
     * @param dest
     * @return if it is communicable
     */
    private static boolean isCommunicable(Device source, Satellite dest) {
        return getDistance(dest.getHeight(), dest.getPosition(), source.getPosition()) <= source.getMaxRange() &&
                isVisible(dest.getHeight(), dest.getPosition(), source.getPosition());
    }

}
