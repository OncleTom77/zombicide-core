package com.fouan.algorithm.pathfinding;

import com.fouan.board.Zone;
import com.fouan.board.Zones;

import java.util.List;
import java.util.Map;

public interface ZombicideShortestPath {

    List<Zone> findRoute(Zones zones, Zone from, Zone to);

    Map<Zone, List<Zone>> findRoutes(Zones zones, List<Zone> from, Zone to);
}
