package com.fouan.algorithm.pathfinding;

import com.fouan.board.Zone;
import com.fouan.board.Zones;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named("dijkstra")
public class Dijkstra implements ZombicideShortestPath {

    @Override
    public List<Zone> findRoute(Zones zones, Zone from, Zone to) {
        SimpleGraph<Zone, DefaultEdge> graph = initializeGraph(zones);

        GraphPath<Zone, DefaultEdge> path = new DijkstraShortestPath<>(graph).getPath(from, to);

        return Optional.ofNullable(path)
                .map(GraphPath::getVertexList)
                .orElse(null);
    }

    @Override
    public Map<Zone, List<Zone>> findRoutes(Zones zones, List<Zone> from, Zone to) {
        SimpleGraph<Zone, DefaultEdge> graph = initializeGraph(zones);

        return from.stream()
                .map(zone -> new DijkstraShortestPath<>(graph).getPath(zone, to))
                .filter(Objects::nonNull)
                .map(graphPath -> new AbstractMap.SimpleEntry<>(graphPath.getStartVertex(), graphPath.getVertexList()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    private static SimpleGraph<Zone, DefaultEdge> initializeGraph(Zones zones) {
        SimpleGraph<Zone, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        zones.getZones().forEach(graph::addVertex);
        zones.getZones().forEach(zone -> zone.getConnectedZones().forEach(connectedZone -> graph.addEdge(zone, connectedZone)));
        return graph;
    }
}
