package com.fouan.algorithm.pathfinding;

import com.fouan.algorithm.pathfinding.old.Dijkstra;
import com.fouan.algorithm.pathfinding.old.ZombicidePathFinder;
import com.fouan.old.board.Zone;
import com.fouan.old.board.Zones;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

class ZombicidePathFinderTest {

    public static void main(String[] args) {
        Zones zones = Zones.emptyZones(9, 6);
        Zone from = zones.getZone(0, 0).orElseThrow();
        Zone to = zones.getZone(8, 5).orElseThrow();
//        List<Zone> connectedZones = from.getConnectedZones();
//        connectedZones.forEach(zone -> {
//            from.removeConnection(zone);
//            zone.removeConnection(from);
//        });

        setLogLevel(Level.FINE);

        List<Zone> result = new ZombicidePathFinder(new Dijkstra()).findNextZoneOfAllShortestPaths(zones, from, to);
        System.out.println("Simple algo: " + result);
    }

    private static void setLogLevel(Level logLevel) {
        Logger root = Logger.getLogger("");
        root.setLevel(logLevel);
        for (Handler handler : root.getHandlers()) {
            handler.setLevel(logLevel);
        }
    }
}
