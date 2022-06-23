package unsw.blackout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static unsw.utils.MathsHelper.getDistance;
import static unsw.utils.MathsHelper.isVisible;

public final class CommunicationHelpers {
    public static ArrayList<Entity> getAllCommunicableEntitiesFromSourceRelay(RelaySatellite source, ArrayList<Entity> allEntities) {
        HashSet<RelaySatellite> visitedRelays = new HashSet<>();
        HashSet<Entity> visitedEntities = new HashSet<>();
        ArrayList<RelaySatellite> allRelays = allEntities
                .stream()
                .filter(entity -> entity.getType().equals("RelaySatellite"))
                .map(entity -> (RelaySatellite) entity)
                .collect(Collectors.toCollection(ArrayList::new));
        doRelayDfs(source, allRelays, visitedRelays, visitedEntities, allEntities);
        return new ArrayList<>(visitedEntities);
    }

    public static ArrayList<Entity> getAllPrimaryCommunicableEntities(Entity source, ArrayList<Entity> allEntities) {
        HashSet<Entity> entitySet = allEntities.stream()
                .filter(entity -> {
                    return isCommunicable(source, entity);
                })
                .collect(Collectors.toCollection(HashSet::new));
        return new ArrayList<>(entitySet);
    }

    private static void doRelayDfs(RelaySatellite sourceRelay, ArrayList<RelaySatellite> allRelays,
                                   HashSet<RelaySatellite> visitedRelays, HashSet<Entity> visitedEntities, ArrayList<Entity> allEntities) {
        if (sourceRelay == null) return;
        visitedRelays.add(sourceRelay);
        visitedEntities.addAll(getAllPrimaryCommunicableEntities((Entity) sourceRelay, allEntities));
        var nextRelay = getNextRelay(sourceRelay, allRelays, visitedRelays);
        doRelayDfs(nextRelay, allRelays, visitedRelays, visitedEntities, allEntities);
    }

    private static RelaySatellite getNextRelay(RelaySatellite source, ArrayList<RelaySatellite> allRelays, HashSet<RelaySatellite> visitedRelays) {
        return allRelays.stream()
                .filter(satellite -> isCommunicable(source, satellite) && !visitedRelays.contains(satellite))
                .findFirst()
                .orElse(null);
    }

    /**
     * Is communicable from Entity to Entity
     *
     * @param source
     * @param dest
     * @return if it is communicable
     */
    private static boolean isCommunicable(Entity source, Entity dest) {
        // If they are both devices then they cannot communicate with each other
        if (source instanceof Device && dest instanceof Device) return false;
        return getDistance(source.getHeight(), source.getPosition(), dest.getHeight(), dest.getPosition()) <= source.getMaxRange() &&
                isVisible(source instanceof Device ? source.getHeight() + 50 : source.getHeight(), source.getPosition(), dest instanceof Device ? dest.getHeight() + 50 : dest.getHeight(), dest.getPosition());
    }
}
