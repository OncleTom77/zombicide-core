package com.fouan.algorithm.pathfinding;

import com.fouan.old.board.Zone;
import com.fouan.old.board.Zones;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
public class ZombicidePathFinder {
    private static final Logger logger = Logger.getLogger(ZombicidePathFinder.class.getName());

    private final ZombicideShortestPath shortestPath;

    public ZombicidePathFinder(ZombicideShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Zone> findNextZoneOfAllShortestPaths(Zones zones, Zone from, Zone to) {
        Map<Zone, List<Zone>> routePerConnectedZones = shortestPath.findRoutes(zones, from.getConnectedZones(), to);
        logger.log(Level.FINE, routePerConnectedZones.size() + " routes found: " + routePerConnectedZones);

        Map<Integer, List<Zone>> connectedZonesPerRouteLength = routePerConnectedZones.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().size(),
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
        logger.log(Level.FINE, "Next zones per route length: " + connectedZonesPerRouteLength);

        return connectedZonesPerRouteLength.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                // TODO: handle case where no route has been found for all connected zones: rerun route finding, ignoring closed doors
                //  "In case there are no open paths to the noisiest Zone, Zombies move toward it as if all doors were open, though locked doors still stop them."
                .orElse(Collections.emptyList());
    }
}
