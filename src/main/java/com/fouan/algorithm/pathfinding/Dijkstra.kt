package com.fouan.algorithm.pathfinding

import com.fouan.zones.Zone
import com.fouan.zones.view.Connection
import org.jgrapht.GraphPath
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.inject.Named

@Named
class Dijkstra {
    fun findRoutes(zones: List<Zone>, connections: Set<Connection>, from: List<Zone>, to: Zone): Map<Zone, List<Zone>> {
        val graph = initializeGraph(zones, connections)
        return from.mapNotNull { DijkstraShortestPath(graph).getPath(it, to) }
            .map { Pair<Zone, List<Zone>>(it.startVertex, it.vertexList) }
            .groupBy( { it.first }, { it.second })
            .mapValues { it.value.flatten() }
    }

    private fun initializeGraph(zones: List<Zone>, connections: Set<Connection>): SimpleGraph<Zone, DefaultEdge> {
        val graph = SimpleGraph<Zone, DefaultEdge>(DefaultEdge::class.java)
        zones.forEach { graph.addVertex(it) }
        connections.forEach { graph.addEdge(it.start, it.end) }
        return graph
    }
}
