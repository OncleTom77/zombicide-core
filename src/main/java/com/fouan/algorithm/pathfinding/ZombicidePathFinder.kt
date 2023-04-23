package com.fouan.algorithm.pathfinding

import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Named

@Named
class ZombicidePathFinder(
    private val shortestPath: Dijkstra,
    private val zonesQueries: ZonesQueries
) {
    private val logger = Logger.getLogger(ZombicidePathFinder::class.java.name)

    fun findNextZoneOfAllShortestPaths(from: Zone, to: Zone): List<Zone> {
        val routePerConnectedZones = shortestPath.findRoutes(
            zonesQueries.findAll(),
            zonesQueries.findAllConnections(),
            zonesQueries.findConnectedZones(from),
            to
        )
        logger.log(Level.FINE, routePerConnectedZones.size.toString() + " routes found: " + routePerConnectedZones)

        val connectedZonesPerRouteLength = routePerConnectedZones.entries
            .groupBy({ it.value.size }, { it.key })

        logger.log(Level.FINE, "Next zones per route length: $connectedZonesPerRouteLength")
        return connectedZonesPerRouteLength.entries
            .minByOrNull { it.key }
            ?.value
        // TODO: handle case where no route has been found for all connected zones: rerun route finding, ignoring closed doors
        //  "In case there are no open paths to the noisiest Zone, Zombies move toward it as if all doors were open, though locked doors still stop them."
            ?: emptyList()
    }
}
